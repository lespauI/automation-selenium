package com.ta;

import com.ta.pages.signup.SignUpPage;
import com.ta.pages.signup.SocialNetworkPopUp;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@Feature("Sign up with social networks")
public class SignUpSocialNetworksTests extends BaseTestClass {

  SignUpPage signUpPage;
  SocialNetworkPopUp socialNetworkPopUp;

  @BeforeEach
  public void openPage() {
    signUpPage = new SignUpPage(driver);
    signUpPage = (SignUpPage) signUpPage.openPage(driver);
  }

  @ParameterizedTest(name = "Validate sign up with social network redirect to {0}")
  @ValueSource(strings = {"google", "slack", "office365", "facebook"})
  @Tag("Positive")
  public void checkSignUpWithSocialMedia(String socialNetwork) {
    signUpPage.getElementByCssSelector("button[data-soc='" + socialNetwork + "']").click();
    socialNetworkPopUp = new SocialNetworkPopUp(driver);

    driver = socialNetworkPopUp.signUpWithSocialNetwork(true);
    Assertions.assertTrue(driver.getCurrentUrl().contains(socialNetwork),
        "Redirect to " + socialNetwork + "page has failed");
  }

  @Test()
  @DisplayName("Validate sign up with apple redirect to apple page")
  @Tag("Positive")
  public void checkSignUpWithApple() {
    signUpPage.getElementByCssSelector("#apple-auth").click();
    socialNetworkPopUp = new SocialNetworkPopUp(driver);

    driver = socialNetworkPopUp.signUpWithSocialNetwork(true);
    Assertions.assertTrue(driver.getCurrentUrl().contains("apple"),
        "Redirect to apple page has failed");
  }

  @Test()
  @DisplayName("Validate sign up with social network checkbox validation")
  @Tag("Negative")
  public void checkSignUpWithOutCheckbox() {
    signUpPage.getElementByCssSelector("#apple-auth").click();
    socialNetworkPopUp = new SocialNetworkPopUp(driver);

    socialNetworkPopUp.signUpWithSocialNetwork(false);
    Assertions.assertEquals("Please agree with the Terms of Service and Privacy Policy",
        socialNetworkPopUp.termsError.getText());
  }

}
