package com.ta;

import com.ta.pages.EmailConfirmPage;
import com.ta.pages.SignInPage;
import com.ta.pages.signup.SignUpPage;
import io.qameta.allure.Feature;
import java.util.Optional;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Value;

@Feature("Sign up with account")
public class SignUpTests extends BaseTestClass {

  SignUpPage signUpPage;
  EmailConfirmPage emailConfirmPage;

  @Value("${email.suffix}")
  private String emailSuffix;

  @Value("${valid.password}")
  private String validPassword;

  @Value("${valid.name}")
  private String validName;

  @BeforeEach
  public void openPage() {
    signUpPage = new SignUpPage(driver);
    signUpPage = (SignUpPage) signUpPage.openPage(driver);
  }

  @Test
  @DisplayName("Check SignUp page return ConformationPage if registration is success")
  @Tag("Positive")
  public void checkRegistrationFormReturnConformationPage() {
    String name = RandomStringUtils.randomAlphabetic(15);
    signUpPage.fillRegistration(
        name, name + emailSuffix, RandomStringUtils.randomAlphabetic(9), true);
    emailConfirmPage = (EmailConfirmPage) signUpPage.signUpClick();

    emailConfirmPage.assertConformationPageIsCorrect(name + emailSuffix);
  }

  @Test
  @DisplayName("Check all validation messages")
  @Tag("Negative")
  public void checkRegistrationFormValidationAll() {
    signUpPage.fillRegistration("", "", "", false);
    signUpPage = (SignUpPage) signUpPage.signUpClick();

    Assertions.assertAll(
        () -> Assertions.assertEquals(signUpPage.nameError.getText(), "Please enter your name."),
        () -> Assertions.assertEquals(signUpPage.emailError.getText(), "Please enter your email address."),
        () -> Assertions.assertEquals(signUpPage.termsError.getText(), "Please agree with the Terms to sign up.")
    );
  }

  @ParameterizedTest(name = "Check error message {4}")
  @CsvSource({
      " , qwerty@testmail.com, QwErTy123, true, #nameError",
      "qwerty, , QwErTy123, true, #emailError",
      "qwerty, qwerty@testmail.com, , true, #passwordError",
      "qwerty, qwerty@testmail.com, QwErTy123, false, #termsError",
  })
  @Tag("Negative")
  public void checkRegistrationFormValidation(
      String name, String email, String password, boolean checkbox, String validationCss) {

    signUpPage.fillRegistration(
        Optional.ofNullable(name).orElse(""),
        Optional.ofNullable(email).orElse(""),
        Optional.ofNullable(password).orElse(""),
        checkbox);
    signUpPage = (SignUpPage) signUpPage.signUpClick();

    switch (validationCss) {
      case "#nameError" -> Assertions.assertEquals("Please enter your name.", signUpPage.nameError.getText());
      case "#emailError" -> Assertions.assertEquals("Please enter your email address.", signUpPage.emailError.getText());
      case "#passwordError" -> Assertions.assertEquals(signUpPage.getEmptyPasswordAlert().getText(),
          "Please enter your password.");
      case "#termsError" -> Assertions.assertEquals("Please agree with the Terms to sign up.", signUpPage.termsError.getText());
      default -> Assertions.assertTrue(signUpPage.isPageDisplayed());
    }

  }

  @ParameterizedTest(name = "Check password validation hint \"{1}\"")
  @CsvSource({
      "qwerty12, So-so password",
      "Qs!2Sed#eq, Good password",
      "Qs!2Sed#eqQs!2Sed#eq, Great password",
  })
  @Tag("Positive")
  public void checkRegistrationFormValidationPassword(String pass, String message) {
    signUpPage.fillRegistration(validName, validName + emailSuffix, pass, true);
    Assertions.assertEquals(message, signUpPage.passwordHint.getText());
  }

  @Test
  @DisplayName("Check validation failed if password is too short")
  @Tag("Negative")
  public void checkRegistrationFormPasswordValidation() {
    signUpPage.fillRegistration(validName, validName + emailSuffix, "1234567", true);
    signUpPage = (SignUpPage) signUpPage.signUpClick();

    waiters.waitSafe(signUpPage.signupHint, driver);

    Assertions.assertEquals(
        "Please use 8+ characters for secure password", signUpPage.signupHint.getText());
  }

  @ParameterizedTest(name = "Check email validation {0}")
  @ValueSource(strings = {"qwerty", "qwerty@.qw", "@qweq.com"})
  @Tag("Negative")
  public void checkRegistrationFormEmailValidation(String email) {
    signUpPage.fillRegistration(validName, email, validPassword, true);
    signUpPage = (SignUpPage) signUpPage.signUpClick();

    waiters.waitSafe(signUpPage.emailError, driver);

    Assertions.assertEquals(
        "The email you entered is incorrect.", signUpPage.emailError.getText());
  }

  @Test
  @DisplayName("Check email validation with wrong email")
  @Tag("Negative")
  public void checkRegistrationFormEmailValidationWithVrongEmail() {
    signUpPage.fillRegistration(validName, "qwe@qwe.qwe", validPassword, true);
    signUpPage = (SignUpPage) signUpPage.signUpClick();

    waiters.waitSafe(signUpPage.emailError, driver);

    Assertions.assertEquals(
        "This doesn’t look like an email address. Please check it for typos and try again.",
        signUpPage.emailError.getText());
  }

  @Test
  @DisplayName("Check login and password validation when same")
  @Tag("Negative")
  public void checkRegistrationFormWithSameLoginAndPassword() {
    signUpPage.fillRegistration(validName, validName + emailSuffix, validName + emailSuffix, true);
    signUpPage = (SignUpPage) signUpPage.signUpClick();

    waiters.waitSafe(signUpPage.passwordError, driver);

    Assertions.assertEquals(
        "Sorry, login and password cannot be the same",
        signUpPage.passwordError.getText());
  }

  @Test
  @DisplayName("Validate user is not able to register twice")
  @Tag("Negative")
  public void checkRegistrationFormForDoubleRegistration() throws Exception {
    String name = RandomStringUtils.randomAlphabetic(15);

    signUpPage.fillRegistration(name, name + emailSuffix, validPassword, true);
    emailConfirmPage = (EmailConfirmPage) signUpPage.signUpClick();
    if (emailConfirmPage.isPageDisplayed()) {

      signUpPage.openPage(driver);

      signUpPage.fillRegistration(name, name + emailSuffix, validPassword, true);
      signUpPage.signUpClick();
      waiters.waitSafe(signUpPage.emailError, driver);
      Assertions.assertAll(
          () -> Assertions.assertFalse(emailConfirmPage.isPageDisplayed()),
          () -> Assertions.assertEquals(
              "Sorry, this email is already registered",
              signUpPage.emailError.getText())
      );

    } else throw new Exception("First registration failed");
  }

  @Test
  @DisplayName("Check forward from signUp page to signIn")
  @Tag("Positive")
  public void checkSignInButton() {
    String email = RandomStringUtils.randomAlphabetic(15) + emailSuffix;
    signUpPage.fillRegistration(validName, email, validPassword, true);

    SignInPage signInPage = (SignInPage) signUpPage.signInClick();
    Assertions.assertTrue(signInPage.isPageDisplayed());
  }

  @Test
  @DisplayName("This is specifically broken test to show error logging mechanism")
  @Tag("Broken")
  @Disabled
  public void failedTest() {
    signUpPage.fillRegistration(validName, validName + emailSuffix, validPassword, true);
    Assertions.assertEquals("wrong message", signUpPage.passwordHint.getText());
  }
}
