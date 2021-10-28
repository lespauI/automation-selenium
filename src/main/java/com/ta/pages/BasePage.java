package com.ta.pages;

import com.ta.driver.Driver;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

public abstract class BasePage<S extends BasePage> {
  public Driver driver;

  public String pageUrl = "https://google.com";

  public BasePage(Driver driver) {
    this.driver = driver;
    PageFactory.initElements(driver, this);
  }

  public abstract boolean isPageDisplayed();

  public Object openPage(Driver driver) {
    driver.get(pageUrl);
    Assertions.assertTrue(isPageDisplayed());
    return this;
  }

  public boolean isElementExist(WebElement webElement) {
    try {
      webElement.isDisplayed();
      return true;
    } catch (NoSuchElementException e) {
      return false;
    }
  }

  public WebElement getElementByAutotestId(String type, String id) {
    return driver.findElement(By.cssSelector(type + String.format("[data-autotest-id='%1$s']", id)));
  }

  public WebElement getElementByCssSelector(String css) {
    return driver.findElement(By.cssSelector(css));

  }

  public void checkTextForElement(WebElement element, String text) {
    Assertions.assertEquals(text, element.getText());
  }
}
