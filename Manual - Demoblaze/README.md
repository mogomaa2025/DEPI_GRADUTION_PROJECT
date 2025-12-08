# DemoBlaze UI Test Automation Framework

## Overview

This repository contains a comprehensive test automation framework for the DemoBlaze e-commerce application (https://www.demoblaze.com/). The framework supports both **Java + Selenium WebDriver** and **TypeScript + Playwright** with **Cucumber BDD** for behavior-driven testing.

---

## Table of Contents

1. [Project Structure](#project-structure)
2. [Prerequisites](#prerequisites)
3. [Installation](#installation)
4. [Framework Architecture](#framework-architecture)
5. [Test Execution](#test-execution)
6. [Feature Files](#feature-files)
7. [Reporting](#reporting)
8. [CI/CD Integration](#cicd-integration)
9. [Best Practices](#best-practices)
10. [Contributing](#contributing)

---

## Project Structure

```
demoblaze-automation/
├── features/                          # Cucumber feature files (Gherkin scenarios)
│   ├── 01_signup.feature
│   ├── 02_login.feature
│   ├── 03_logout.feature
│   ├── 04_products.feature
│   ├── 05_product_details.feature
│   ├── 06_cart.feature
│   ├── 07_checkout.feature
│   ├── 08_contact.feature
│   ├── 09_navigation.feature
│   ├── 10_e2e_journeys.feature
│   ├── 11_about_us.feature
│   ├── 12_crossbrowser.feature
│   ├── 13_visual_regression.feature
│   ├── 14_performance.feature
│   ├── 15_accessibility.feature
│   ├── 16_security.feature
│   ├── 17_ui_validation.feature
│   └── 18_smoke_tests.feature
│
├── java-selenium/                     # Java + Selenium implementation
│   ├── src/
│   │   ├── main/
│   │   │   └── java/
│   │   │       ├── pages/             # Page Object classes
│   │   │       │   ├── BasePage.java
│   │   │       │   ├── HomePage.java
│   │   │       │   ├── SignUpPage.java
│   │   │       │   ├── LoginPage.java
│   │   │       │   ├── ProductListPage.java
│   │   │       │   ├── ProductDetailPage.java
│   │   │       │   ├── CartPage.java
│   │   │       │   ├── CheckoutPage.java
│   │   │       │   └── ContactPage.java
│   │   │       ├── utils/             # Utility classes
│   │   │       │   ├── DriverManager.java
│   │   │       │   ├── ConfigReader.java
│   │   │       │   ├── WaitHelper.java
│   │   │       │   ├── ScreenshotHelper.java
│   │   │       │   └── TestDataGenerator.java
│   │   │       └── config/            # Configuration files
│   │   │           └── config.properties
│   │   └── test/
│   │       └── java/
│   │           ├── stepdefinitions/   # Cucumber step definitions
│   │           │   ├── SignUpSteps.java
│   │           │   ├── LoginSteps.java
│   │           │   ├── ProductSteps.java
│   │           │   ├── CartSteps.java
│   │           │   └── CheckoutSteps.java
│   │           ├── runners/           # Test runners
│   │           │   ├── SmokeTestRunner.java
│   │           │   ├── RegressionRunner.java
│   │           │   └── E2ERunner.java
│   │           └── hooks/             # Before/After hooks
│   │               └── Hooks.java
│   ├── pom.xml                        # Maven dependencies
│   └── testng.xml                     # TestNG configuration
│
├── playwright-typescript/             # TypeScript + Playwright implementation
│   ├── src/
│   │   ├── pages/                     # Page Object classes
│   │   │   ├── BasePage.ts
│   │   │   ├── HomePage.ts
│   │   │   ├── SignUpPage.ts
│   │   │   ├── LoginPage.ts
│   │   │   ├── ProductListPage.ts
│   │   │   ├── ProductDetailPage.ts
│   │   │   ├── CartPage.ts
│   │   │   ├── CheckoutPage.ts
│   │   │   └── ContactPage.ts
│   │   ├── utils/                     # Utility functions
│   │   │   ├── browserManager.ts
│   │   │   ├── configReader.ts
│   │   │   ├── waitHelper.ts
│   │   │   ├── screenshotHelper.ts
│   │   │   └── testDataGenerator.ts
│   │   ├── fixtures/                  # Test fixtures
│   │   │   └── testFixtures.ts
│   │   └── config/                    # Configuration files
│   │       └── config.json
│   ├── tests/
│   │   ├── step-definitions/          # Cucumber step definitions
│   │   │   ├── signupSteps.ts
│   │   │   ├── loginSteps.ts
│   │   │   ├── productSteps.ts
│   │   │   ├── cartSteps.ts
│   │   │   └── checkoutSteps.ts
│   │   └── hooks/                     # Before/After hooks
│   │       └── hooks.ts
│   ├── package.json                   # npm dependencies
│   ├── tsconfig.json                  # TypeScript configuration
│   ├── playwright.config.ts           # Playwright configuration
│   └── cucumber.js                    # Cucumber configuration
│
├── test-data/                         # Test data files
│   ├── users.json
│   ├── products.json
│   └── test-credentials.csv
│
├── reports/                           # Test execution reports
│   ├── allure-results/
│   ├── cucumber-reports/
│   └── screenshots/
│
├── .github/                           # GitHub Actions CI/CD
│   └── workflows/
│       ├── smoke-tests.yml
│       └── regression-tests.yml
│
├── UI-TestAutomation-TestPlan.md      # Comprehensive test plan
├── UI-TestAutomation-TestCases.md     # Detailed test case documentation
└── README.md                          # This file
```

---

## Prerequisites

### For Java + Selenium

- **Java JDK**: 11 or higher
- **Maven**: 3.6+ or **Gradle**: 7.0+
- **IDE**: IntelliJ IDEA / Eclipse / VS Code
- **Browsers**: Chrome, Firefox, Edge (latest versions)

### For TypeScript + Playwright

- **Node.js**: 18+ (LTS version recommended)
- **npm**: 8+ or **yarn**: 1.22+
- **TypeScript**: 5.0+
- **IDE**: VS Code (recommended) / WebStorm

---

## Installation

### Java + Selenium Setup

```bash
cd java-selenium

# Using Maven
mvn clean install

# Using Gradle
gradle clean build

# Install WebDriver Manager (handles driver binaries automatically)
# Already included in pom.xml/build.gradle
```

### TypeScript + Playwright Setup

```bash
cd playwright-typescript

# Install dependencies
npm install

# Install Playwright browsers
npx playwright install

# Or install specific browsers
npx playwright install chromium firefox webkit
```

---

## Framework Architecture

### Design Patterns

1. **Page Object Model (POM)**
   - Encapsulates page elements and actions
   - Improves maintainability
   - Reduces code duplication

2. **Singleton Pattern**
   - WebDriver/Browser instance management
   - Configuration management

3. **Factory Pattern**
   - Browser initialization
   - Test data creation

4. **Fluent Interface**
   - Method chaining for readable code

### Layer Architecture

```
┌─────────────────────────────────────────┐
│  Feature Files (Gherkin - BDD)          │  ← Business readable scenarios
├─────────────────────────────────────────┤
│  Step Definitions (Glue Code)           │  ← Maps Gherkin to code
├─────────────────────────────────────────┤
│  Page Objects (UI Abstraction)          │  ← Encapsulates page elements
├─────────────────────────────────────────┤
│  Utilities (Helpers & Config)           │  ← Reusable functions
├─────────────────────────────────────────┤
│  WebDriver/Playwright (Browser Layer)   │  ← Browser automation
└─────────────────────────────────────────┘
```

---

## Test Execution

### Java + Selenium

#### Run All Tests
```bash
mvn clean test
```

#### Run Specific Tags
```bash
# Smoke tests only
mvn test -Dcucumber.filter.tags="@smoke"

# Regression tests
mvn test -Dcucumber.filter.tags="@regression"

# Critical tests
mvn test -Dcucumber.filter.tags="@critical"

# Run specific feature
mvn test -Dcucumber.features="src/test/resources/features/01_signup.feature"
```

#### Parallel Execution
```bash
mvn test -Dparallel=methods -DthreadCount=4
```

#### Run with TestNG
```bash
mvn test -DsuiteXmlFile=testng.xml
```

### TypeScript + Playwright

#### Run All Tests
```bash
npm test
```

#### Run Specific Tags
```bash
# Smoke tests
npm test -- --tags "@smoke"

# Regression tests
npm test -- --tags "@regression"

# Critical tests
npm test -- --tags "@critical"

# Multiple tags
npm test -- --tags "@smoke and @critical"
```

#### Run Specific Feature
```bash
npm test features/01_signup.feature
```

#### Parallel Execution
```bash
npm test -- --parallel 4
```

#### Headed Mode (visible browser)
```bash
npm test -- --headed
```

#### Debug Mode
```bash
npm test -- --debug
```

#### Specific Browser
```bash
npm test -- --browser=chromium
npm test -- --browser=firefox
npm test -- --browser=webkit
```

---

## Feature Files

### Test Coverage Summary

| Module | Feature File | Test Cases | Priority |
|--------|--------------|------------|----------|
| Sign Up | 01_signup.feature | 8 | P0 |
| Login | 02_login.feature | 9 | P0 |
| Logout | 03_logout.feature | 2 | P0 |
| Products | 04_products.feature | 10 | P0 |
| Product Details | 05_product_details.feature | 8 | P0 |
| Cart | 06_cart.feature | 9 | P0 |
| Checkout | 07_checkout.feature | 11 | P0 |
| Contact | 08_contact.feature | 8 | P1 |
| Navigation | 09_navigation.feature | 8 | P1 |
| E2E Journeys | 10_e2e_journeys.feature | 8 | P0 |
| About Us | 11_about_us.feature | 3 | P2 |
| Cross-Browser | 12_crossbrowser.feature | 4 | P1 |
| Visual | 13_visual_regression.feature | 5 | P2 |
| Performance | 14_performance.feature | 5 | P2 |
| Accessibility | 15_accessibility.feature | 5 | P2 |
| Security | 16_security.feature | 7 | P1 |
| UI Validation | 17_ui_validation.feature | 10 | P2 |
| Smoke Tests | 18_smoke_tests.feature | 7 | P0 |

**Total: 120+ test scenarios**

### Tag Strategy

- `@smoke` - Critical smoke tests (run on every commit)
- `@regression` - Full regression suite (nightly)
- `@critical` - High priority tests
- `@e2e` - End-to-end user journeys
- `@negative` - Negative test scenarios
- `@datadriven` - Data-driven tests
- `@crossbrowser` - Cross-browser tests
- `@playwright` - Playwright-specific tests
- `@visual` - Visual regression tests
- `@performance` - Performance tests
- `@accessibility` - Accessibility tests

---

## Reporting

### Allure Reports

```bash
# Generate Allure report (Java)
mvn allure:report

# Open report
mvn allure:serve

# Playwright
npm run report:allure
```

### Cucumber HTML Reports

Reports are automatically generated after test execution:
- **Java**: `target/cucumber-reports/cucumber.html`
- **Playwright**: `test-results/cucumber-report.html`

### Screenshots

Screenshots are automatically captured on test failure:
- **Location**: `reports/screenshots/`
- **Naming**: `{test-name}_{timestamp}.png`

### Video Recording (Playwright)

```typescript
// Enabled in playwright.config.ts
video: 'on-first-retry'  // Records video on failure
```

---

## CI/CD Integration

### GitHub Actions

```yaml
# .github/workflows/smoke-tests.yml
name: Smoke Tests

on:
  push:
    branches: [ main, develop ]
  pull_request:
    branches: [ main ]

jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      - name: Setup Node.js
        uses: actions/setup-node@v3
        with:
          node-version: '18'
      - name: Install dependencies
        run: npm ci
      - name: Run smoke tests
        run: npm test -- --tags "@smoke"
      - name: Upload test results
        uses: actions/upload-artifact@v3
        if: always()
        with:
          name: test-results
          path: test-results/
```

### Jenkins Pipeline

```groovy
pipeline {
    agent any
    
    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        
        stage('Install Dependencies') {
            steps {
                sh 'npm ci'
            }
        }
        
        stage('Run Tests') {
            steps {
                sh 'npm test -- --tags "@smoke"'
            }
        }
        
        stage('Generate Report') {
            steps {
                allure includeProperties: false,
                       jdk: '',
                       results: [[path: 'allure-results']]
            }
        }
    }
    
    post {
        always {
            publishHTML(target: [
                reportDir: 'test-results',
                reportFiles: 'index.html',
                reportName: 'Test Report'
            ])
        }
    }
}
```

---

## Best Practices

### 1. Writing Step Definitions

```typescript
// Good: Reusable and maintainable
Given('I am on the {string} page', async (pageName: string) => {
  await pages.navigate(pageName);
});

// Avoid: Hard-coded and not reusable
Given('I am on the signup page', async () => {
  await page.goto('https://www.demoblaze.com/index.html');
  await page.click('#signin2');
});
```

### 2. Page Objects

```typescript
// Good: Methods return page objects or meaningful values
class HomePage extends BasePage {
  async clickProduct(productName: string): Promise<ProductDetailPage> {
    await this.page.click(`text=${productName}`);
    return new ProductDetailPage(this.page);
  }
}

// Chain methods for fluent interface
await homePage
  .clickCategory('Phones')
  .clickProduct('Samsung galaxy s6')
  .addToCart();
```

### 3. Waits

```typescript
// Good: Explicit waits
await page.waitForSelector('#productList', { state: 'visible' });

// Avoid: Hard-coded sleep
await page.waitForTimeout(5000);
```

### 4. Test Data

```typescript
// Good: Dynamic test data
const username = `testuser_${Date.now()}`;

// Avoid: Hard-coded data that might cause conflicts
const username = 'testuser123';
```

### 5. Assertions

```typescript
// Good: Clear and specific assertions
expect(await cartPage.getProductCount()).toBe(2);
expect(await cartPage.getTotal()).toBe('$1180');

// Avoid: Vague assertions
expect(await cartPage.isDisplayed()).toBeTruthy();
```

---

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/new-test`)
3. Write tests following BDD principles
4. Ensure all tests pass
5. Create Pull Request

### Commit Message Convention

```
feat: Add product comparison test scenarios
fix: Resolve cart total calculation issue
test: Add accessibility tests for homepage
docs: Update README with new test execution commands
```

---

## Troubleshooting

### Common Issues

**Issue**: Browser not found
```bash
# Solution: Install browsers
npx playwright install
```

**Issue**: Maven dependencies not resolving
```bash
# Solution: Clear cache and reinstall
mvn clean install -U
```

**Issue**: Tests failing due to timing
```bash
# Solution: Increase timeout in configuration
# playwright.config.ts
timeout: 60000  // 60 seconds
```

---

## Support

For issues or questions:
- Create an issue in GitHub
- Contact: qa-team@example.com
- Slack: #test-automation

---

## License

MIT License - See LICENSE file for details

---

**Happy Testing! 🚀**
