package com.ta.waits;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class WaitParams {

  @Value("${selenium.timeout.pageload.s:20}")
  public long pageLoadTimeoutSec;

  @Value("${selenium.timeout.wait.implicit.s:0}")
  public long implicitWaitTimeoutSec;

  @Value("${selenium.timeout.wait.explicit.s:5}")
  public long explicitWaitTimeoutSec;

  @Value("${selenium.time.sleep.ms:1000}")
  public long sleepTimeMs;

}
