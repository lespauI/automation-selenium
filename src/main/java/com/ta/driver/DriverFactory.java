package com.ta.driver;

import com.ta.utils.Log;
import com.ta.waits.WaitParams;
import io.github.bonigarcia.wdm.WebDriverManager;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.URL;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.HasCapabilities;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.GeckoDriverService;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.service.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DriverFactory {

  @Autowired
  WaitParams waitParams;

  private String environmentInfo;

  private DriverFactory() {

  }

  public DriverService createAndStartDriverService(
      DriverParams driverParams) {

    Log.logger.info(Log.formatLogMessage(
        "Resolving and setting local WebDriver path according to browser and OS"));
    Drivers.valueOf(driverParams.browser.toUpperCase()).resolveLocalWebDriverPath();

    Log.logger.info(Log.formatLogMessage("Creating driver service instance according to browser"));
    DriverService driverService = Drivers.valueOf(driverParams.browser.toUpperCase()).newDriverService();

    try {
      Log.logger.info(Log.formatLogMessage("Starting driver service instance"));
      driverService.start();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    return driverService;
  }

  public WebDriver createDriver(DriverService driverService, DriverParams driverParams) {
    WebDriver driver;

    Log.logger.info(Log.formatLogMessage(
        "Setting capabilities according to browser"));
    MutableCapabilities capabilities =
        Drivers.valueOf(driverParams.browser.toUpperCase()).newCapabilities(driverParams);

    URL url = (driverService == null) ? null : driverService.getUrl();

    if (url != null) {
      Log.logger.info(Log.formatLogMessage(
          "Creating RemoteWebDriver instance using capabilities and URL"));
      driver = new RemoteWebDriver(url, capabilities);
    } else {
      Log.logger.info(Log.formatLogMessage(
          "Creating local WebDriver instance using capabilities"));
      driver = Drivers.valueOf(driverParams.browser.toUpperCase()).newDriver(capabilities);
    }

    Log.logger.info(Log.formatLogMessage("Configuring browser", driver));
    driver.manage().timeouts().pageLoadTimeout(waitParams.pageLoadTimeoutSec, TimeUnit.SECONDS);
    driver.manage().timeouts().implicitlyWait(waitParams.implicitWaitTimeoutSec, TimeUnit.SECONDS);

    if (driverParams.maximize) {
      driver.manage().window().maximize();
    }
    setEnvInfo(driver, driverParams);

    Log.logger.info(Log.formatLogMessage(
        "Browser is started in thread '{}' with id  in JVM '{}'", driver),
        getThreadName(), getThreadId(), getProcessName());

    return driver;
  }

  public String getEnvironmentInfo() {
    return this.environmentInfo;
  }

  private void setEnvInfo(WebDriver webDriver, DriverParams driverParams) {
    environmentInfo = Log.formatLogMessage(""
        + "\nOS name: " + System.getProperty("os.name")
        + "\nOS architecture: " + System.getProperty("os.arch")
        + "\nOS version: " + System.getProperty("os.version")
        + "\nJava version: " + System.getProperty("java.version")
        + "\nEnvironment: " + System.getProperty("env")
        + "\nTarget: " + driverParams.target
        + "\nBrowser name: " + ((HasCapabilities) webDriver).getCapabilities().getBrowserName()
        + "\nBrowser version: " + ((HasCapabilities) webDriver).getCapabilities().getVersion()
        + "\nBrowser window size: " + webDriver.manage().window().getSize().width + "x" + webDriver.manage().window().getSize().height
        + "\nOS (Platform) name: " + ((HasCapabilities) webDriver).getCapabilities().getPlatform().toString()
        + "\nOS (Platform) version: " + ((HasCapabilities) webDriver).getCapabilities().getCapability("platformVersion")
        + "\nThreads Count: " + driverParams.threads
        + "\nAll Capabilities:\n" + ((HasCapabilities) webDriver).getCapabilities().asMap());
    Log.logger.debug(environmentInfo);
  }

  private String getProcessName() {
    return ManagementFactory.getRuntimeMXBean().getName();
  }

  private String getThreadName() {
    return Thread.currentThread().getName();
  }

  private long getThreadId() {
    return Thread.currentThread().getId();
  }

  private static ChromeOptions getChromeOptions(DriverParams driverParams) {
    ChromeOptions options = new ChromeOptions();

    if (driverParams.incognito) {
      options.addArguments("incognito");
    }

    options.setHeadless(driverParams.headless);
    return options;
  }

  private static FirefoxOptions getFirefoxOptions(DriverParams config) {
    FirefoxOptions options = new FirefoxOptions();

    if (config.incognito) {
      options.addArguments("-private");
    }

    options.setHeadless(config.headless);

    return options;
  }

  private enum Drivers {

    FIREFOX {
      @Override
      public void resolveLocalWebDriverPath() {
        WebDriverManager.firefoxdriver().setup();
      }

      @Override
      public MutableCapabilities newCapabilities(DriverParams config) {
        return getFirefoxOptions(config);
      }

      @Override
      public WebDriver newDriver(MutableCapabilities capabilities) {
        return new FirefoxDriver((FirefoxOptions) capabilities);
      }

      @Override
      public DriverService newDriverService() {
        return GeckoDriverService.createDefaultService();
      }
    }, CHROME {
      @Override
      public void resolveLocalWebDriverPath() {
        WebDriverManager.chromedriver().setup();
      }

      @Override
      public MutableCapabilities newCapabilities(DriverParams config) {
        return getChromeOptions(config);
      }

      @Override
      public WebDriver newDriver(MutableCapabilities capabilities) {
        return new ChromeDriver((ChromeOptions) capabilities);
      }

      @Override
      public DriverService newDriverService() {
        return ChromeDriverService.createDefaultService();
      }
    };

    public abstract void resolveLocalWebDriverPath();

    public abstract MutableCapabilities newCapabilities(DriverParams config);

    public abstract WebDriver newDriver(MutableCapabilities capabilities);

    public abstract DriverService newDriverService();
  }

}
