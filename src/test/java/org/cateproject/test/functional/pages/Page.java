 package org.cateproject.test.functional.pages;

import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;

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
		    inputElement.clear();
		    inputElement.sendKeys(data.get(name));
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
}
