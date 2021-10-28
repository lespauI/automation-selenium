package com.ta.utils;

import com.ta.driver.Driver;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class Log {

  public static final Logger logger = LoggerFactory.getLogger(Log.class);

  public static String formatLogMessage(String message, WebDriver driver) {
    StringBuilder stringBuilder = new StringBuilder();
    if (driver != null) {
      stringBuilder.append("Session ID: ")
          .append((driver instanceof RemoteWebDriver)
              ? ((RemoteWebDriver) driver).getSessionId()
              : ((RemoteWebDriver) ((Driver) driver).getWrappedDriver()).getSessionId());
    }
    stringBuilder.append("\n")
        .append(message);

    return stringBuilder.toString();
  }

  public static String formatLogMessage(String message) {
    return formatLogMessage(message, null);
  }


}
