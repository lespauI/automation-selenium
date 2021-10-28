package com.ta;

import com.ta.driver.Driver;
import com.ta.driver.DriverManager;
import com.ta.utils.TestWatcherImpl;
import com.ta.waits.Waiters;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@TestPropertySource("classpath:test.properties")
@ContextConfiguration(locations = "classpath:spring.xml")
@ExtendWith({SpringExtension.class, TestWatcherImpl.class})
public class BaseTestClass {

  @Autowired
  public DriverManager driverManager;
  @Autowired
  public Waiters waiters;

  public Driver driver;

  @BeforeEach
  public void setup() {
    this.driver = driverManager.getCurrentDriver();
  }

}
