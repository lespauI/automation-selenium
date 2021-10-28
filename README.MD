## Test automation Signup page

#### Table of Contents
1. [List of required tools](#requrement)
2. [Installation instruction](#installation)
3. [Tools used](#tools)
3. [Solution overview](#overview)

## List of required tools <a name="requrement"></a>

* Java 15 (code should work for any version above 10, just change `<maven.compiler.source> and <maven.target.source>` to required version)
* Maven
* Chrome browser

## Installation instruction <a name="installation"></a>

- To execute tests from maven use `mvn clean test`, by default tests are executed with Chrome.
- To execute with another browser using `mvn clean test -Dbrowser=Fierfox` currently implemented chrome and firefox
- Allure reports generated in 'allure-results' folder, to generate and check allure report after test execution use `mvn allure:serve`
- Also, you can execute tests from IDE as it is classic Junit5 tests

## Tools used <a name="tools"></a>

- Spring boot starter test as executor
- Junit5 as test engine 
- Selenium webdriver
- <a href="https://github.com/bonigarcia/webdrivermanager">Webdriver manager</a> to download compatible webdriver before execution
- log4j as logger  
- Allure as a reporting tool
- Maven as dependency manager

## Solution overview <a name="overview"></a>

`main/java/com/ta/driver` package contains all required driver management methods like create, quit, setup and, etc. 
For now, I was implemented only required for the test task methods, but this package could be extended with other browsers for example.
<br>
`main/java/com/ta/utils` Logger and TestWatcher implementation to track test status and attach logs after execution
<br>
`main/java/com/ta/waits` simple safe waiter and waits configuration
<br>
`main/java/com/ta/pages` package for page objects, I decide to use PO pattern as we have a Base Page and subpages like Conformation, Social networks and, etc.
All pages extend BasePage to share common methods, base page is abstract.
<br>
<br>
`test/java/com/ta` tests itself, I decide to split tests into 2 features, the first is base registration where we check ta registration functions, like mandatory fields, validation checks, etc, and the second is Social Network registration, for now I check only redirect to different social networks, but not the registration process.   
Tests designed with junit5 and in test case design i follow kinda cucumber approach with Given/When/Then structure for example `checkRegistrationFormReturnConformationPage()`
<br>

- Given  part is prepare random username and fill the regform
````java
String name = RandomStringUtils.randomAlphabetic(15);
signUpPage.fillRegistration(name, name + emailSuffix, RandomStringUtils.randomAlphabetic(9), true);
````
- When user click signUp button
````java
emailConfirmPage = (EmailConfirmPage) signUpPage.signUpClick();
````
- Then he received correct conformation page   
````java
emailConfirmPage.assertConformationPageIsCorrect(name + emailSuffix); 
````

The assertions itself is in EmailConfirmPage class as I plan to reuse this asserts across multiple tests
````java
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
````
Junit5 assertAll provide multiple assertions feature, all asserts will be executed without depends on previous asserts.

Test method `public void failedTest()` specifically broken to show logging and screenshot mechanism after failed test

`test/resources` contains spring config (just for resolving beans), log4j config, and test.properties file with default props.

For test execution reporting I choose Allure as most common reporting tool across the industry, and I also include maven allure plugin to download and serv allure reports via mvn command.