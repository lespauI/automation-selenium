package com.ta.pages.signup;

import com.ta.driver.Driver;
import com.ta.pages.BasePage;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

public class SocialNetworkPopUp extends BasePage<SocialNetworkPopUp> {

  public SocialNetworkPopUp(Driver driver) {
    super(driver);
  }

  /*
   * Social networks block
   */
  @FindBy(how = How.CSS, using = ".socialtos__wrap")
  private WebElement poupUp;
  @FindBy(how = How.CSS, using = ".mr-checkbox-1__icon--medium")
  private WebElement checkBox;
  @FindBy(how = How.CSS, using = "button[data-autotest-id='mr-form-gdpr-btn-signin-1']")
  private WebElement socialSignUpButton;
  @FindBy(how = How.CSS, using = "#tos-signup-terms-error")
  public WebElement termsError;

  @Override
  public boolean isPageDisplayed() {
    return this.isElementExist(poupUp) && poupUp.isDisplayed();
  }

  public Driver signUpWithSocialNetwork(boolean checkbox) {
    if (checkbox) {
      checkBox.click();
    }
    socialSignUpButton.click();
    return this.driver;
  }

}
