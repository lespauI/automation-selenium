package com.ta.pages.signup;

import com.ta.driver.Driver;
import com.ta.pages.BasePage;
import com.ta.pages.EmailConfirmPage;
import com.ta.pages.SignInPage;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

public class SignUpPage extends BasePage<SignUpPage> {

  public SignUpPage(Driver driver) {
    super(driver);
    pageUrl = super.pageUrl + "/signup";
  }

  /*
   * Sign Up block
   */
  @FindBy(how = How.CSS, using = ".signup")
  private WebElement singUpBlock;
  @FindBy(how = How.CSS, using = "#name")
  private WebElement nameField;
  @FindBy(how = How.CSS, using = "#email")
  private WebElement emailField;
  @FindBy(how = How.CSS, using = "#password")
  private WebElement passwordField;
  @FindBy(how = How.CSS, using = ".mr-checkbox-1__check")
  private WebElement signupTermsCheckBox;
  @FindBy(how = How.CSS, using = "button[data-autotest-id='mr-form-signup-btn-start-1']")
  private WebElement signupButton;
  @FindBy(how = How.CSS, using = "a[data-autotest-id='mr-link-signin-1']")
  private WebElement signInButton;

  /*
   * Error messages block
   */
  @FindBy(how = How.CSS, using = "#nameError")
  public WebElement nameError;
  @FindBy(how = How.CSS, using = "#emailError")
  public WebElement emailError;
  @FindBy(how = How.CSS, using = "#passwordError")
  public WebElement passwordError;
  @FindBy(how = How.CSS, using = "#password-hint")
  public WebElement passwordHint;
  @FindBy(how = How.CSS, using = "#termsError")
  public WebElement termsError;
  @FindBy(how = How.CSS, using = ".signup__input-hint-text")
  public WebElement signupHint;

  @Override
  public boolean isPageDisplayed() {
    return this.isElementExist(nameField) && nameField.isDisplayed();
  }

  public void fillRegistration(String name, String email, String password, boolean checkbox) {
    nameField.sendKeys(name);
    emailField.sendKeys(email);
    passwordField.sendKeys(password);
    if (checkbox) {
      signupTermsCheckBox.click();
    }
  }

  public Object signUpClick() {
    signupButton.click();
    return isElementExist(nameField) ? new SignUpPage(driver) : new EmailConfirmPage(driver);
  }

  public Object signInClick() {
    signInButton.click();
    return new SignInPage(driver);
  }

}
