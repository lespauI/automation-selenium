package com.ta.driver;

import com.ta.utils.Log;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.support.events.EventFiringWebDriver;

public class Driver extends EventFiringWebDriver {

  public String driverName;

  public Driver(WebDriver driver) {
    super(driver);
  }

  public String getDriverName() {
    return driverName;
  }

  public void setDriverName(String driverName) {
    this.driverName = driverName;
  }

  public byte[] getScreenshotAsRawBytes() {
    try {
      byte[] screenshot = getScreenshotAs(OutputType.BYTES);
      Log.logger.info(Log.formatLogMessage("Screenshot is created.", this));
      return screenshot;
    } catch (WebDriverException e) {
      Log.logger.error(Log.formatLogMessage(
          "Screenshot creation failed with error\n" + e.getMessage(), this), e);
      return null;
    }
  }
}
