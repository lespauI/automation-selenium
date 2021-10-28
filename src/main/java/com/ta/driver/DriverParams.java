package com.ta.driver;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DriverParams {

  @Value("${selenium.target:local}")
  public String target;

  @Value("${selenium.browser}")
  public String browser;

  @Value("${selenium.browser.headless:false}")
  public Boolean headless;

  @Value("${selenium.browser.incognito:false}")
  public Boolean incognito;

  @Value("${selenium.browser.maximize:false}")
  public Boolean maximize;

  @Value("${selenium.browser.width:0}")
  public Integer width;

  @Value("${selenium.browser.height:0}")
  public Integer height;

  @Value("${threads:1}")
  public Integer threads;

}
