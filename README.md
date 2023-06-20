# WebDriver-Sizzle-Injector

The WebDriver Sizzle Injector enables more robust CSS Selectors by injecting the SizzleJS library onto the DOM of the
application under test. By using Sizzle CSS Selectors, Automation Testers are able to more precisely locate
UI elements without having to rely on complex XPath expressions, and more effectively parameterize element locators,
resulting in a lowered maintenance overhead for UI modeling.

# Getting started

On the `pom.xml`, add a new entry to the `<dependencies>` section:

```xml
<dependency>
    <groupId>dev.slifer</groupId>
    <artifactId>webdriver-sizzle-injector</artifactId>
    <version>4.10.0-R1</version>
</dependency>
```

# Usage

Invoking a Sizzle CSS Selector is as simple as slightly altering the standard WebDriver call to initialize a WebElement.

```java
WebElement signInButton = driver.findElement(BySizzle.css("#loginForm .button:contains('sign in')"));
```

The `BySizzle` class extends `By` to allow calls to the Sizzle components. This is compatible with anywhere else
within the Selenium API where a `By` is passed as an argument, including the `Actions` API and then `ExpectedConditions`
methods alongside the `WebDriverWait`.

The `SizzleScript` class simply loads the script from the `Webjars` dependency. Since the injection logic is executed
repeatedly throughout the course of a test, and injection itself occurs after every page transition, the
`SizzleScript` class has been implemented as a singleton in order to eliminate the repetition of loading and reading
the SizzleJS library.

# Sizzle CSS Selectors

The [Rosetta Stone](https://www.red-gate.com/simple-talk/dotnet/.net-framework/xpath,-css,-dom-and-selenium-the-rosetta-stone/)
wall chart by [Michael Sorens](https://github.com/msorens) is a fantastic reference for the many various structures
available to build both XPath (if that's your cup of tea) and CSS Selectors. This is far more thorough and easy to
read than anything I can add here. What I found especially handy is how the author has labeled the CSS Selectors that
are not natively supported by Selenium, but are available via Sizzle.

# Versions

This project is version-sync'd with Selenium so that dependencies can remain consistent. A revision indicator is
appended to the version number to accommodate potentially multiple releases of this library that support a specific
version of Selenium.
