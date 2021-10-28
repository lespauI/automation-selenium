package com.ta.driver;

import com.ta.utils.Log;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.service.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DriverManager {

  @Autowired
  DriverParams driverParams;

  @Autowired
  DriverFactory driverFactory;

  private final List<DriverService> allDriverServices =
      Collections.synchronizedList(new ArrayList<>());
  private final List<Driver> allDrivers = Collections.synchronizedList(new ArrayList<>());
  private final ThreadLocal<Map<String, DriverService>> driverServices =
      ThreadLocal.withInitial(HashMap::new);
  private final ThreadLocal<Map<String, Driver>> drivers = ThreadLocal.withInitial(HashMap::new);
  private final ThreadLocal<String> currentDriverName = ThreadLocal.withInitial(() -> "MAIN");

  private final Thread closeThread = new Thread(() -> {
    allDrivers.forEach(driver -> {
      Log.logger.info("Closing browser completely!");
      driver.getWrappedDriver().quit();
    });

    allDriverServices.forEach(driverService -> {
      Log.logger.info("Stopping driver service!");
      driverService.stop();
    });
  });

  {
    Log.logger.info("Adding shutdown hook for closing browsers!");
    Runtime.getRuntime().addShutdownHook(closeThread);
  }

  protected void quit(String name) {
    Driver webDriver = drivers.get().get(name);

    Log.logger.info("Closing browser completely!");
    webDriver.getWrappedDriver().quit();

    drivers.get().remove(name);
    allDrivers.remove(webDriver);
  }

  private DriverService createDriverService(String name, DriverParams driverParams) {
    DriverService driverService = driverServices.get().get(name);
    if (driverService == null) {
      driverService = driverFactory.createAndStartDriverService(driverParams);
      driverServices.get().put(name, driverService);
      allDriverServices.add(driverService);
    }
    return driverService;
  }

  private Driver createDriver(String name, DriverParams driverParams) {
    DriverService driverService = createDriverService(name, driverParams);
    WebDriver webDriver = driverFactory.createDriver(driverService, driverParams);
    Driver driver = new Driver(webDriver);
    driver.setDriverName(name);
    drivers.get().put(name, driver);
    allDrivers.add(driver);
    return driver;
  }

  public Driver getOrCreateDriver() {
    return drivers.get().containsKey(currentDriverName.get()) ? drivers.get().get(currentDriverName.get())
        : createDriver(currentDriverName.get(), driverParams);
  }

  public Driver getOrCreateDriver(DriverParams driverParams) {
    return drivers.get().containsKey(currentDriverName.get()) ? drivers.get().get(currentDriverName.get())
        : createDriver(currentDriverName.get(), driverParams);
  }

  public void switchCurrentDriver(String name) {
    currentDriverName.set(name);
  }

  public String getCurrentDriverName() {
    return currentDriverName.get();
  }

  public DriverService getCurrentDriverService() {
    return driverServices.get().get(currentDriverName.get());
  }

  public Driver getCurrentDriver() {
    return getOrCreateDriver();
  }

  public Driver getCurrentDriver(DriverParams driverParams) {
    return getOrCreateDriver(driverParams);
  }

  public String getDriverLogs() {
    return driverFactory.getEnvironmentInfo();
  }

}
