package org.cateproject.test.functional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import javax.servlet.Filter;

import org.cateproject.Application;
import org.cateproject.multitenant.MultitenantContextHolder;
import org.cateproject.multitenant.MultitenantManager;
import org.cateproject.multitenant.MultitenantRepository;
import org.cateproject.multitenant.MultitenantStatus;
import org.cateproject.multitenant.domain.Multitenant;
import org.cateproject.repository.jpa.admin.TenantRepository;
import org.cateproject.test.functional.mockmvc.MockMvcHtmlUnitDriver;
import org.cateproject.test.functional.mockmvc.MockMvcWebConnection;
import org.cateproject.test.functional.pages.IndexPage;
import org.cateproject.test.functional.pages.Page;
import org.cateproject.web.multitenant.MultitenantFilter;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationContextLoader;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.gargoylesoftware.htmlunit.WebClient;

import cucumber.api.DataTable;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

@ContextConfiguration(classes = Application.class, loader = SpringApplicationContextLoader.class)
@WebIntegrationTest
public class Stepdefs {

	private static Logger logger = LoggerFactory.getLogger(Stepdefs.class);

	@Autowired
	private WebApplicationContext context;

	@Autowired
	private MultitenantManager multitenantManager;

	@Autowired
	private MultitenantStatus multitenantStatus;

	@Autowired
	private MultitenantRepository multitenantRepository;

	@Autowired
	private TenantRepository tenantRepository;
	
	@Autowired
	private Filter springSecurityFilterChain;

	@Autowired
	private MultitenantFilter multitenantFilter;

	private HtmlUnitDriver webDriver;

	private Page current;

	@Before
	public void setup() throws Exception {

		final MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(context).addFilters(multitenantFilter, springSecurityFilterChain).build();
		webDriver = new MockMvcHtmlUnitDriver(mockMvc, true) {

			@Override
			protected WebClient modifyWebClient(WebClient client) {
				// TODO Auto-generated method stub
				final WebClient webClient = super.modifyWebClient(client);
				webClient.setWebConnection(new MockMvcWebConnection(mockMvc, ""));
				webClient.addRequestHeader("Host", "localhost");
				webClient.getOptions().setThrowExceptionOnScriptError(false);
				webClient.getOptions().setRedirectEnabled(true);
				return webClient;
			}
		};
		
		SecurityContextHolder.clearContext();
	}

	@After
	public void tearDown() {

		if (webDriver != null) {
			webDriver.close();
		}
	}

	@Given("^there is one tenant \"([^\"]*)\"$")
	public void there_is_one_tenant(String tenant) {
		
		MultitenantContextHolder.getContext().setTenantId("localhost");
		if(tenantRepository.count() == 0 && multitenantRepository.count() == 0) {
		    assertEquals(tenantRepository.count(), 0);
		    assertEquals(multitenantRepository.count(), 0);

		    Multitenant multitenant = new Multitenant();
		    multitenant.setTitle("CATE");
		    multitenant.setIdentifier(tenant);
		    multitenant.setAdminEmail("admin@example.com");
		    multitenant.setAdminPassword("admin");
		    multitenant.setOwnerEmail("owner");
		    multitenant.setOwnerPassword("owner@example.com	");
		    multitenant.setDatabasePassword("");
		    multitenant.setDatabaseUrl("jdbc:h2:mem:test");
		    multitenant.setDriverClassName("org.h2.Driver");
		    multitenant.setDatabaseUsername("sa");

		    multitenantManager.save(multitenant, true);
		}
	}
	
	@Given("^I am logged in to CATE as ([^\"]*) with password ([^\"]*)$")
	public void i_am_logged_in_to_CATE_as_with_password(String username, String password) {
		if(this.current == null) {
			i_go_to_the_index_page();
		}
		if(this.current.loggedIn()) {
			if(!this.current.loggedIn(username)) {
			    this.current.logout();
			    this.current.logIn(username, password);
			}		
		} else {
			this.current.logIn(username, password);
		}
		
	}

	@When("^I go to the index page$")
	public void i_go_to_the_index_page() {
		this.current = Page.getIndexPage("http://localhost:8080/", webDriver);
	}

	@When("^the CATE application is uninitialized$")
	public void the_CATE_application_is_uninitialized() {
		if (multitenantStatus.isInitialized()) {
			for (Multitenant multitenant : multitenantRepository.findAll()) {
				multitenantManager.delete(multitenant);
			}
		}
		multitenantStatus.setInitialized(false);
	}
	@When("^I select the (.*) link from the (.*?) menu$")
	public void	i_select_the_link_from_the_menu(String link, String menu) {
		current.selectMenuLink(menu, link);
	}

	@When("^I enter the following information in the (.*) form$")
	public void i_enter_the_following_information_in_the_form(String formId, DataTable data) {
		current.enterFormData(formId, data.asMap(String.class, String.class));
	}
	
	@When("^I click the (.*?) button$")
    public void i_click_the_button(String button) throws Throwable {
        current.clickButton(button);
    }

	
	@When("^I am not logged in to CATE$")
	public void i_am_not_logged_in_to_CATE() {
		current.clearAuthentication();
	}
	

	@Then("^the page should say \"(.*)\"$")
	public void the_page_should_say(String message) {
		assertEquals(current.getBody(), message);
	}

	@Then("^an info alert should say \"(.*)\"$")
	public void an_info_alert_should_say(String message) {
		assertEquals(message, current.getInfoAlert());
	}

	@Then("^the hero title should be \"(.*)\"$")
	public void the_hero_title_should_say(String message) {
		assertEquals(((IndexPage) current).getHeroTitle(), message);
	}

	@When("^I submit the (.*) form$")
	public void i_submit_the_form(String formId) throws Throwable {
		current.submitForm(formId);
	}

	@Then("^the page title should be \"(.*)\"$")
	public void the_page_title_should_be(String title) throws Throwable {
		assertEquals(current.getTitle(), title);
	}

	@Then("^a success alert should say \"(.*?)\"$")
	public void a_success_alert_should_say(String message) throws Throwable {
		assertEquals(message, current.getSuccessAlert());
	}
	
	@Then("^there should be (\\d+) result[s]? in the (.*?) table$")
	public void there_should_be_results_in_the_table(int results, String table) {
		assertEquals(results, current.countRowsInTable(table));
	}
	
	@Then("^the following data about the (.*?) should be displayed$")
	public void the_following_data_about_the_should_be_displayed(String prefix, DataTable expected) {
		DataTable actual = current.getFields(prefix, expected.asMap(String.class, String.class).keySet());
		expected.diff(actual);
	}
	
	@Then("^the (.*?) dropdown is visible$")
	public void the_dropdown_is_visible(String menu) {
		assertTrue(current.isDropdownVisible(menu));
	}

}