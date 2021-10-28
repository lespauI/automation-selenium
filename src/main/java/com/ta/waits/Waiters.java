package com.ta.waits;

import static org.assertj.core.api.Fail.fail;

import com.ta.utils.Log;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Waiters {

  @Autowired
  WaitParams waitParams;

  public void waitSafe(WebElement element, WebDriver driver) {
    WebDriverWait wait = new WebDriverWait(driver, waitParams.sleepTimeMs);
    try {
      wait.until(
          ExpectedConditions.visibilityOf(element));
    } catch (TimeoutException e) {
      Log.logger.error(e.getMessage());
      fail(e.getMessage(), e);
    }
  }

}
