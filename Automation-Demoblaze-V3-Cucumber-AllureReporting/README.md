# DemoBlaze Java Selenium Test Automation

This is the Java + Selenium WebDriver implementation of the DemoBlaze test automation framework using Cucumber BDD.

---

## 📋 Table of Contents

- [Project Structure](#project-structure)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Running Tests](#running-tests)
- [Enhanced Features](#enhanced-features)
- [Generated Reports](#generated-reports)
- [Configuration](#configuration)
- [Reports](#reports)
- [Page Objects](#page-objects)
- [Step Definitions](#step-definitions)
- [Best Practices](#best-practices)

---

## 🗂️ Project Structure

```
java-selenium/
├── src/
│   ├── main/java/com/demoblaze/
│   │   ├── pages/                     # Page Object classes
│   │   │   ├── BasePage.java
│   │   │   ├── HomePage.java
│   │   │   ├── SignUpPage.java
│   │   │   ├── LoginPage.java
│   │   │   ├── ProductDetailPage.java
│   │   │   ├── CartPage.java
│   │   │   ├── CheckoutPage.java
│   │   │   └── ContactPage.java
│   │   └── utils/                     # Utility classes
│   │       ├── ConfigReader.java
│   │       ├── DriverManager.java
│   │       ├── WaitHelper.java
│   │       ├── ElementHelper.java
│   │       ├── ScreenshotHelper.java
│   │       └── TestDataGenerator.java
│   └── test/
│       ├── java/com/demoblaze/
│       │   ├── stepdefinitions/       # Cucumber step definitions
│       │   │   ├── CommonSteps.java
│       │   │   ├── SignUpSteps.java
│       │   │   └── LoginSteps.java
│       │   ├── runners/               # TestNG test runners
│       │   │   ├── SmokeTestRunner.java
│       │   │   ├── RegressionTestRunner.java
│       │   │   ├── CriticalTestRunner.java
│       │   │   └── E2ETestRunner.java
│       │   └── hooks/                 # Cucumber hooks
│       │       └── Hooks.java
│       └── resources/
│           ├── features/              # Cucumber feature files (18 files)
│           ├── config.properties      # Configuration
│           └── log4j2.xml            # Logging configuration
├── pom.xml                            # Maven dependencies
├── testng.xml                         # TestNG configuration
└── README.md                          # This file
```

---

## ✅ Prerequisites

Before you begin, ensure you have the following installed:

- **Java JDK**: 11 or higher
  ```bash
  java -version
  ```

- **Maven**: 3.6 or higher
  ```bash
  mvn -version
  ```

- **IDE**: IntelliJ IDEA, Eclipse, or VS Code with Java extensions

- **Git**: For version control

---

## 🚀 Installation

### 1. Clone the repository

```bash
git clone <repository-url>
cd java-selenium
```

### 2. Install dependencies

```bash
mvn clean install
```

This will:
- Download all required dependencies
- Compile the project
- Run any existing tests
- Generate target directory

### 3. Verify installation

```bash
mvn clean compile
```

---

## 🧪 Running Tests

### Quick Test Scripts
```bash
# Test the enhanced setup (compilation + basic test)
./test-allure-setup.bat

# Run tests with enhanced Allure reporting and video
./run-tests-with-allure.bat

# Run only SignUp tests
./run-signup-only.bat

# Test logout functionality (with enhanced login handling)
./test-logout-fix.bat

# Clean up any orphaned browser processes
./cleanup-browsers.bat
```

### Maven Test Commands

#### By Test Suite
```bash
# Smoke tests (critical functionality)
mvn test -Dtest=SmokeTestRunner

# Regression tests (comprehensive testing)
mvn test -Dtest=RegressionTestRunner

# End-to-end tests (complete user journeys)
mvn test -Dtest=E2ETestRunner

# Critical tests (high priority scenarios)
mvn test -Dtest=CriticalTestRunner

# Run all tests
mvn clean test
```

#### By Feature Tags
```bash
# SignUp functionality tests
mvn test -Dtest=SmokeTestRunner "-Dcucumber.filter.tags=@signup"

# Login functionality tests
mvn test -Dtest=SmokeTestRunner "-Dcucumber.filter.tags=@login"

# Logout functionality tests  
mvn test -Dtest=SmokeTestRunner "-Dcucumber.filter.tags=@logout"

# Cart functionality tests
mvn test -Dtest=SmokeTestRunner "-Dcucumber.filter.tags=@cart"

# Checkout functionality tests
mvn test -Dtest=SmokeTestRunner "-Dcucumber.filter.tags=@checkout"

# Product functionality tests
mvn test -Dtest=SmokeTestRunner "-Dcucumber.filter.tags=@products"

# Contact functionality tests
mvn test -Dtest=SmokeTestRunner "-Dcucumber.filter.tags=@contact"

# Navigation tests
mvn test -Dtest=SmokeTestRunner "-Dcucumber.filter.tags=@navigation"

# End-to-end journey tests
mvn test -Dtest=SmokeTestRunner "-Dcucumber.filter.tags=@e2e"
```

#### By Test Type
```bash
# Positive test scenarios
mvn test -Dtest=SmokeTestRunner "-Dcucumber.filter.tags=@positive"

# Negative test scenarios (validation testing)
mvn test -Dtest=SmokeTestRunner "-Dcucumber.filter.tags=@negative"

# Validation tests
mvn test -Dtest=SmokeTestRunner "-Dcucumber.filter.tags=@validation"

# UI tests
mvn test -Dtest=SmokeTestRunner "-Dcucumber.filter.tags=@ui"

# Critical priority tests
mvn test -Dtest=SmokeTestRunner "-Dcucumber.filter.tags=@critical"

# Smoke tests only
mvn test -Dtest=SmokeTestRunner "-Dcucumber.filter.tags=@smoke"

# Regression tests
mvn clean test -Dcucumber.filter.tags="@regression"
```

#### Specific Test Scenarios
```bash
# Specific test case by ID
mvn test -Dtest=SmokeTestRunner "-Dcucumber.filter.tags=@TC_SIGNUP_001"
mvn test -Dtest=SmokeTestRunner "-Dcucumber.filter.tags=@TC_LOGIN_001"
mvn test -Dtest=SmokeTestRunner "-Dcucumber.filter.tags=@TC_LOGOUT_001"

# Multiple specific tests
mvn test -Dtest=SmokeTestRunner "-Dcucumber.filter.tags=@TC_SIGNUP_001 or @TC_LOGIN_001"

# Combination tags
mvn test -Dtest=SmokeTestRunner "-Dcucumber.filter.tags=@signup and @validation"
mvn test -Dtest=SmokeTestRunner "-Dcucumber.filter.tags=@critical and @smoke"
```

#### Browser-Specific Execution
```bash
# Run with Chrome (default)
mvn test -Dtest=SmokeTestRunner -Dbrowser=chrome

# Run with Firefox
mvn test -Dtest=SmokeTestRunner -Dbrowser=firefox

# Run with Edge
mvn test -Dtest=SmokeTestRunner -Dbrowser=edge

# Run in headless mode
mvn test -Dtest=SmokeTestRunner -Dheadless=true

# Run with specific browser in headless mode
mvn test -Dtest=SmokeTestRunner -Dbrowser=chrome -Dheadless=true
```

#### Enhanced Reporting Commands
```bash
# Generate Allure report (after test execution)
mvn allure:report

# Serve interactive Allure report (recommended)
mvn allure:serve

# Run tests and automatically serve Allure report
./run-tests-with-allure.bat -suite smoke -serve

# Run with video recording enabled
mvn test -Dtest=SmokeTestRunner -Dvideo.recording.enabled=true

# Run with video recording only for failures
mvn test -Dtest=SmokeTestRunner -Dvideo.recording.enabled=true -Dvideo.onFailureOnly=true
```

## 🎬 Enhanced Features

### Video Recording
- **Automatic Recording**: Captures test execution as frame-based "videos"
- **Failure Focus**: Option to record only failed tests
- **Allure Integration**: Videos automatically attached to test reports
- **Configurable**: Adjustable frame rates and cleanup policies

### Enhanced Allure Reporting
- **Rich HTML Reports**: Interactive dashboards with charts and graphs
- **Multiple Attachments**: Screenshots, page source, console logs, network logs
- **Test Categorization**: Automatic failure categorization (UI, network, browser issues)
- **Environment Details**: Complete test environment information
- **Historical Trends**: Track test execution over time

### Robust Driver Management
- **No Orphaned Browsers**: Advanced cleanup prevents stuck browser processes
- **Exception Handling**: Graceful handling of browser failures
- **Multi-Level Cleanup**: Normal, force, and emergency cleanup strategies
- **Process Monitoring**: Real-time tracking of active driver instances

### Enhanced Error Handling
- **Smart Login**: Automatic user creation and robust login handling
- **Fail-Fast**: Quick failure with clear error messages instead of hanging
- **Alert Management**: Comprehensive alert detection and handling
- **Timeout Management**: Proper timeout handling prevents infinite waits

## 📊 Generated Reports

After test execution, you'll find:
- **Allure HTML Report**: `target/allure-report/index.html`
- **Video Recordings**: `target/videos/*.png` (frame summaries)
- **Screenshots**: `target/screenshots/*.png`
- **Cucumber Reports**: `target/cucumber-reports/*.html`
- **TestNG Reports**: `target/surefire-reports/index.html`

### Run E2E Tests

```bash
mvn clean test -Dcucumber.filter.tags="@e2e"
```

### Run Specific Feature

```bash
mvn clean test -Dcucumber.features="src/test/resources/features/01_signup.feature"
```

### Run with Specific Runner

```bash
# Smoke tests
mvn test -Dtest=SmokeTestRunner

# Regression tests
mvn test -Dtest=RegressionTestRunner

# Critical tests
mvn test -Dtest=CriticalTestRunner

# E2E tests
mvn test -Dtest=E2ETestRunner
```

### Run with TestNG XML

```bash
mvn test -DsuiteXmlFile=testng.xml
```

### Run in Headless Mode

Update `config.properties`:
```properties
headless=true
```

Then run tests normally:
```bash
mvn clean test
```

### Parallel Execution

```bash
mvn clean test -Dparallel=methods -DthreadCount=4
```

---

## ⚙️ Configuration

### config.properties

Located at: `src/test/resources/config.properties`

```properties
# Application Configuration
app.url=https://www.demoblaze.com/index.html
app.title=STORE

# Browser Configuration
browser=chrome          # chrome, firefox, edge, safari
headless=false         # true for headless mode
implicit.wait=10       # seconds
explicit.wait=20       # seconds
page.load.timeout=30   # seconds

# Screenshot Configuration
screenshot.onFailure=true
screenshot.directory=target/screenshots

# Test Data
default.password=Test@123
test.user.prefix=testuser_

# Logging
log.level=INFO
```

### Change Browser

Edit `config.properties`:
```properties
browser=firefox  # or edge, safari
```

Or pass as system property:
```bash
mvn test -Dbrowser=firefox
```

---

## 📊 Reports

### Cucumber HTML Report

After test execution, open:
```
target/cucumber-reports/cucumber.html
```

### Allure Report

Generate and view:
```bash
mvn allure:serve
```

Or generate only:
```bash
mvn allure:report
```

Then open:
```
target/site/allure-maven-plugin/index.html
```

### Screenshots

Failed test screenshots are saved to:
```
target/screenshots/
```

### Logs

Test execution logs:
```
target/logs/test-execution.log
```

---

## 📄 Page Objects

### BasePage.java

Base class with common methods:
- `navigateTo(url)` - Navigate to URL
- `click(locator)` - Click element
- `type(locator, text)` - Type text
- `getText(locator)` - Get element text
- `acceptAlert()` - Handle alerts
- `waitForVisible(locator)` - Wait for visibility
- `captureScreenshot(name)` - Take screenshot

### HomePage.java

Methods:
- `navigateToHomePage()` - Go to home
- `clickLogin()` - Open login modal
- `clickSignUp()` - Open signup modal
- `clickCart()` - Go to cart
- `clickLogout()` - Logout user
- `filterByCategory(category)` - Filter products
- `clickProductByName(name)` - Select product
- `isUserLoggedIn()` - Check login status

### SignUpPage.java

Methods:
- `enterUsername(username)` - Enter username
- `enterPassword(password)` - Enter password
- `clickSignUpButton()` - Submit signup
- `signUp(username, password)` - Complete signup
- `getAlertTextAndAccept()` - Handle alert

### LoginPage.java

Methods:
- `enterUsername(username)` - Enter username
- `enterPassword(password)` - Enter password
- `clickLoginButton()` - Submit login
- `login(username, password)` - Complete login

### CartPage.java

Methods:
- `getCartItemCount()` - Get item count
- `isProductInCart(name)` - Check if product exists
- `getTotalPriceValue()` - Get total price
- `deleteProduct(name)` - Remove product
- `clickPlaceOrder()` - Go to checkout

### CheckoutPage.java

Methods:
- `fillOrderForm(...)` - Fill all fields
- `clickPurchase()` - Submit order
- `getConfirmationText()` - Get order details
- `getOrderAmount()` - Get order amount
- `clickConfirmationOk()` - Close confirmation

---

## 📝 Step Definitions

### CommonSteps.java

Common steps used across features:
- `I am on the DemoBlaze homepage`
- `I click on the {string} link`
- `I should see an alert with message {string}`
- `the {string} link should be visible`

### SignUpSteps.java

Sign up related steps:
- `I enter username {string}`
- `I enter password {string}`
- `I click the {string} button`
- `I leave the username field empty`

### LoginSteps.java

Login related steps:
- `I enter login username {string}`
- `I enter login password {string}`
- `I should be logged in successfully`
- `I login with username {string} and password {string}`

---

## 🛠️ Utilities

### ConfigReader

Reads configuration from `config.properties`:
```java
ConfigReader config = ConfigReader.getInstance();
String url = config.getAppUrl();
String browser = config.getBrowser();
```

### DriverManager

Manages WebDriver instances:
```java
DriverManager.initializeDriver();
WebDriver driver = DriverManager.getDriver();
DriverManager.quitDriver();
```

### WaitHelper

Provides wait utilities:
```java
WaitHelper wait = new WaitHelper(driver);
wait.waitForElementVisible(locator);
wait.waitForElementClickable(locator);
wait.waitForAlert();
```

### ElementHelper

Element interaction helpers:
```java
ElementHelper element = new ElementHelper(driver);
element.click(locator);
element.type(locator, text);
element.getText(locator);
```

### TestDataGenerator

Generates test data:
```java
String username = TestDataGenerator.generateUsername();
String email = TestDataGenerator.generateEmail();
String password = TestDataGenerator.generateStrongPassword();
String cardNumber = TestDataGenerator.generateCreditCardNumber();
```

### ScreenshotHelper

Captures screenshots:
```java
ScreenshotHelper.captureScreenshot(driver, "test-name");
ScreenshotHelper.captureScreenshotOnFailure(driver, "failed-test");
```

---

## ✨ Best Practices

### 1. Page Object Model

```java
// Good: Use Page Object methods
homePage.clickLogin();
loginPage.login(username, password);

// Avoid: Direct driver calls in tests
driver.findElement(By.id("login")).click();
```

### 2. Explicit Waits

```java
// Good: Use explicit waits
waitHelper.waitForElementVisible(locator);

// Avoid: Hard waits
Thread.sleep(5000);
```

### 3. Dynamic Test Data

```java
// Good: Generate unique data
String username = TestDataGenerator.generateUsername();

// Avoid: Hard-coded data
String username = "testuser123"; // May cause conflicts
```

### 4. Assertions

```java
// Good: Clear assertions
Assert.assertTrue(homePage.isUserLoggedIn(), "User should be logged in");

// Avoid: Vague assertions
Assert.assertTrue(true);
```

### 5. Logging

```java
// Good: Log important actions
logger.info("Logging in with username: " + username);

// Helps with debugging
```

---

## 🐛 Troubleshooting

### Issue: WebDriver not found

**Solution:**
```bash
mvn clean install -U
```

### Issue: Tests failing with element not found

**Solution:**
- Increase wait times in `config.properties`
- Check element locators
- Ensure page is loaded before interaction

### Issue: Browser doesn't open

**Solution:**
- Check browser is installed
- Update WebDriverManager version
- Try different browser in config

### Issue: Maven dependencies not downloading

**Solution:**
```bash
mvn dependency:purge-local-repository
mvn clean install
```

---