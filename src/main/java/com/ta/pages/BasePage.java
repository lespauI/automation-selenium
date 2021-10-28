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

  public Object openPage(Driver driver) {
    driver.get(pageUrl);
    Assertions.assertTrue(isPageDisplayed());
    return this;
  }

  protected boolean isElementExist(WebElement webElement) {
    try {
      webElement.isDisplayed();
      return true;
    } catch (NoSuchElementException e) {
      return false;
    }
  }

  protected WebElement getElementByAutotestId(String type, String id) {
    return driver.findElement(By.cssSelector(type + String.format("[data-autotest-id='%1$s']", id)));
  }

  protected WebElement getElementByCssSelector(String css) {
    return driver.findElement(By.cssSelector(css));

  }

  protected abstract boolean isPageDisplayed();

}
