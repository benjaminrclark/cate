package org.cateproject.test.functional.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

public class IndexPage extends Page {
	
	@FindBy(how = How.CLASS_NAME, using = "hero-unit")
    private WebElement hero;
	
	public String getHeroTitle() {
		WebElement heroTitle = hero.findElement(By.xpath("div[@class = 'container']/div[@class = 'row']/h1"));
		return heroTitle.getText();
	}

}
