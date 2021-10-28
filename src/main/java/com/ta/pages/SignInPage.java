package com.ta.pages;

import com.ta.driver.Driver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;


public class SignInPage extends BasePage<SignInPage> {

  public SignInPage(Driver driver) {
    super(driver);
    pageUrl = super.pageUrl + "/login";
  }

  @FindBy(how = How.CSS, using = ".signup")
  private WebElement singInBlock;

  @FindBy(how = How.CSS, using = "#email")
  private WebElement emailField;

  @FindBy(how = How.CSS, using = "#password")
  private WebElement passwordField;

  @FindBy(how = How.CSS, using = "button[data-autotest-id='mr-form-login-btn-signin-1']")
  private WebElement signInButton;

  @Override
  public boolean isPageDisplayed() {
    return this.isElementExist(signInButton) && signInButton.isDisplayed();
  }

}
