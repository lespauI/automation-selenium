package com.ta.pages;

import com.ta.driver.Driver;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

public class EmailConfirmPage extends BasePage<EmailConfirmPage> {

  public EmailConfirmPage(Driver driver) {
    super(driver);
    pageUrl = super.pageUrl + "/email-confirm";
  }

  @FindBy(how = How.CSS, using = ".signup")
  private WebElement singUpBlock;

  @FindBy(how = How.CSS, using = ".signup__title-form")
  private WebElement singUpTitle;

  @FindBy(how = How.CSS, using = ".signup__subtitle-form")
  private WebElement singUpSubtitle;

  @FindBy(how = How.CSS, using = "#code")
  private WebElement singUpCode;

  @FindBy(how = How.CSS, using = "a[href='/get-new-code/']")
  private WebElement sendCodeAgainButton;

  @Override
  public boolean isPageDisplayed() {
    return this.isElementExist(singUpCode) && singUpCode.isDisplayed();
  }

  public void assertConformationPageIsCorrect(String email) {
    Assertions.assertAll(
        () -> singUpBlock.isDisplayed(),
        () -> Assertions.assertEquals("Check your email", singUpTitle.getText()),
        () -> Assertions.assertTrue(singUpSubtitle.getText().contains(email)),
        () -> Assertions.assertEquals("Enter 6-digit code", singUpCode.getAttribute("placeholder")),
        () -> Assertions.assertEquals("6", singUpCode.getAttribute("maxlength")),
        () -> Assertions.assertTrue(sendCodeAgainButton.isDisplayed())
    );
  }

}
