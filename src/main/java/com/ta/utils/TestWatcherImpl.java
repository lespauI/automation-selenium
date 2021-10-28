package com.ta.utils;

import com.ta.driver.DriverManager;
import io.qameta.allure.Allure;
import java.io.ByteArrayInputStream;
import java.lang.reflect.Field;
import java.util.Arrays;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;

public class TestWatcherImpl implements TestWatcher {

  @Override
  public void testFailed(ExtensionContext extensionContext, Throwable throwable) {

    DriverManager driverManager = null;
    Field field;

    Object testInstance = extensionContext.getRequiredTestInstance();

    Class<?> testInstanceClass = testInstance.getClass();
    try {
      field = testInstanceClass.getField("driverManager");
      driverManager = (DriverManager) field.get(testInstance);

      Log.logger.info(driverManager.getDriverLogs());
      Allure.addAttachment("Console Log: ", driverManager.getDriverLogs());
      Allure.addAttachment("Screenshot",
          new ByteArrayInputStream(driverManager.getCurrentDriver().getScreenshotAsRawBytes()));
    } catch (NoSuchFieldException | IllegalAccessException e) {
      Log.logger.error(Arrays.toString(e.getStackTrace()));
    }
  }
}
