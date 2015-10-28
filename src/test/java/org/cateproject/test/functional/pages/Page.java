 package org.cateproject.test.functional.pages;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TreeSet;
import java.util.Map;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

import cucumber.api.DataTable;

public class Page {
	
	private WebDriver webDriver;
	
	private String baseUri;
	
	@FindBy(how = How.TAG_NAME, using = "body")
        private WebElement body;

	public void setWebDriver(WebDriver webDriver) {
		this.webDriver = webDriver;
	}

	public void setBaseUri(String baseUri) {
		this.baseUri = baseUri;
	}
	
	public WebDriver getWebDriver() {
		return webDriver;
	}
	
	public String getBaseUri() {
		return baseUri;
	}
	
	public static IndexPage getIndexPage(String baseUri, WebDriver webDriver) {
		Page page = new Page();
		page.setBaseUri(baseUri);
		page.setWebDriver(webDriver);
		return Page.openAs(page, baseUri, IndexPage.class);
	}

	protected static <T extends Page> T openAs(Page page, String address, Class<T> pageClass) {
		open(page.getWebDriver(), address);
		T t = init(page.getWebDriver(), pageClass);
		t.setBaseUri(page.getBaseUri());
		t.setWebDriver(page.getWebDriver());
		return t;
	}
	
	private static void open(WebDriver webDriver, String address) {
        webDriver.navigate().to(address);
    }
	
	private static <T extends Page> T init(WebDriver webDriver, Class<T> pageClass) {
        return PageFactory.initElements(webDriver, pageClass);
    }

	public String getBody() {
		return body.getText();
	}

	public String getInfoAlert() {
		WebElement alert = webDriver.findElement(By.className("alert-info"));
		return alert.findElement(By.tagName("p")).getText();
	}
	
	public String getSuccessAlert() {
		WebElement alert = webDriver.findElement(By.className("alert-success"));
		return alert.findElement(By.tagName("p")).getText();
	}

	public void enterFormData(String form, Map<String, String> data) {
		WebElement formElement = webDriver.findElement(By.name(form));
		for(String name : data.keySet()) {
			WebElement inputElement = formElement.findElement(By.name(name));
			if(inputElement.getTagName().equals("select")) {
				Select select = new Select(inputElement);
				select.selectByValue(data.get(name));
			} else {
		        inputElement.clear();
		        inputElement.sendKeys(data.get(name));
			}
		}
	}

	public void submitForm(String form) {
		WebElement formElement = webDriver.findElement(By.name(form));
		formElement.submit();
	}

	public String getTitle() {
		return webDriver.getTitle();
	}

	public void clearAuthentication() {
		Cookie cookie = webDriver.manage().getCookieNamed("jsessionid");
        if (cookie != null) {
            webDriver.manage().deleteCookie(cookie);
        }
	}

	public boolean loggedIn() {
		return !webDriver.findElements(By.id("user_menu")).isEmpty();
	}
	
	public boolean loggedIn(String username) {
		return webDriver.findElement(By.id("user_menu")).findElement(By.tagName("a")).getText().contains(username);
	}

	public void logout() {
		webDriver.findElement(By.id("user_menu")).findElement(By.xpath("ul/li/a[text() = 'Logout']")).click();;
	}

	public void logIn(String username, String password) {
		webDriver.findElement(By.id("signin_menu")).findElement(By.tagName("a")).click();
		Map<String, String> data = new HashMap<String, String>();
		data.put("username", username);
		data.put("password", password);
		enterFormData("login", data);
		submitForm("login");
		
	}

	public void selectMenuLink(String menu, String linkText) {
                //throw new RuntimeException("''" + webDriver.getPageSource()+ "''");
		webDriver.findElement(By.id(menu)).findElement(By.xpath("..")).findElement(By.partialLinkText(linkText)).click();
	}
	
	public void clickButton(String button) {
		webDriver.findElement(By.name(button)).submit();
	}

	public int countRowsInTable(String table) {
		return webDriver.findElement(By.id(table)).findElements(By.xpath("tbody/tr")).size();
	}
	
	public DataTable getFields(String prefix, Set<String> fields) {
		List<List<String>> rows = new ArrayList<List<String>>();
		for(String field : new TreeSet<String>(fields)) {
			List<String> row = new ArrayList<String>();
			row.add(field);
			row.add(webDriver.findElement(By.id(prefix.toLowerCase() + "_" + field.toLowerCase() + "_id")).getText());
			rows.add(row);
		}
		DataTable actual = DataTable.create(rows);
		return actual;
	}

	public boolean isDropdownVisible(String menu) {
		return webDriver.findElement(By.id(menu)).getAttribute("class").contains("open");
	}
}
