package com.demoblaze.pages;

import com.demoblaze.utils.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * BasePage - Base class for all Page Objects
 * Contains common methods and utilities used across all pages
 */
public abstract class BasePage {
    protected static final Logger logger = LogManager.getLogger(BasePage.class);
    protected WebDriver driver;
    protected WaitHelper waitHelper;
    protected ElementHelper elementHelper;
    protected ConfigReader config;
    protected WebDriverWait wait;

    /**
     * Constructor
     */
    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.waitHelper = new WaitHelper(driver);
        this.elementHelper = new ElementHelper(driver);
        this.config = ConfigReader.getInstance();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(config.getExplicitWait()));
        PageFactory.initElements(driver, this);
        logger.debug("Initialized " + this.getClass().getSimpleName());
    }

    /**
     * Navigate to URL
     */
    protected void navigateTo(String url) {
        logger.info("Navigating to: " + url);
        driver.get(url);
        waitHelper.waitForPageLoad();
    }

    /**
     * Get current URL
     */
    protected String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    /**
     * Get page title
     */
    protected String getPageTitle() {
        return driver.getTitle();
    }

    /**
     * Refresh page
     */
    protected void refreshPage() {
        logger.info("Refreshing page");
        driver.navigate().refresh();
        waitHelper.waitForPageLoad();
    }

    /**
     * Navigate back
     */
    protected void navigateBack() {
        logger.info("Navigating back");
        driver.navigate().back();
        waitHelper.waitForPageLoad();
    }

    /**
     * Navigate forward
     */
    protected void navigateForward() {
        logger.info("Navigating forward");
        driver.navigate().forward();
        waitHelper.waitForPageLoad();
    }

    /**
     * Switch to alert and accept
     */
    public void acceptAlert() {
        try {
            Alert alert = waitHelper.waitForAlert();
            logger.info("Alert present with text: " + alert.getText());
            alert.accept();
            logger.debug("Alert accepted");
        } catch (Exception e) {
            logger.error("Failed to accept alert: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Switch to alert and dismiss
     */
    protected void dismissAlert() {
        try {
            Alert alert = waitHelper.waitForAlert();
            logger.info("Alert present with text: " + alert.getText());
            alert.dismiss();
            logger.debug("Alert dismissed");
        } catch (Exception e) {
            logger.error("Failed to dismiss alert: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Get alert text
     */
    public String getAlertText() {
        try {
            Alert alert = waitHelper.waitForAlert();
            String text = alert.getText();
            logger.debug("Alert text: " + text);
            return text;
        } catch (Exception e) {
            logger.error("Failed to get alert text: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Check if alert is present
     */
    public boolean isAlertPresent() {
        try {
            driver.switchTo().alert();
            return true;
        } catch (NoAlertPresentException e) {
            return false;
        }
    }

    /**
     * Type text into alert
     */
    protected void typeInAlert(String text) {
        try {
            Alert alert = waitHelper.waitForAlert();
            alert.sendKeys(text);
            logger.debug("Typed text in alert: " + text);
        } catch (Exception e) {
            logger.error("Failed to type in alert: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Execute JavaScript
     */
    protected Object executeJavaScript(String script, Object... args) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        return js.executeScript(script, args);
    }

    /**
     * Scroll to top of page
     */
    protected void scrollToTop() {
        logger.debug("Scrolling to top");
        executeJavaScript("window.scrollTo(0, 0)");
    }

    /**
     * Scroll to bottom of page
     */
    protected void scrollToBottom() {
        logger.debug("Scrolling to bottom");
        executeJavaScript("window.scrollTo(0, document.body.scrollHeight)");
    }

    /**
     * Take screenshot
     */
    protected String captureScreenshot(String screenshotName) {
        return ScreenshotHelper.captureScreenshot(driver, screenshotName);
    }

    /**
     * Wait for element to be visible
     */
    protected WebElement waitForVisible(By locator) {
        return waitHelper.waitForElementVisible(locator);
    }

    /**
     * Wait for element to be clickable
     */
    protected WebElement waitForClickable(By locator) {
        return waitHelper.waitForElementClickable(locator);
    }

    /**
     * Wait for element to be present
     */
    protected WebElement waitForPresent(By locator) {
        return waitHelper.waitForElementPresent(locator);
    }

    /**
     * Click element
     */
    protected void click(By locator) {
        elementHelper.click(locator);
    }

    /**
     * Type text
     */
    protected void type(By locator, String text) {
        elementHelper.type(locator, text);
    }

    /**
     * Get text
     */
    protected String getText(By locator) {
        return elementHelper.getText(locator);
    }

    /**
     * Check if element is displayed
     */
    protected boolean isElementDisplayed(By locator) {
        return elementHelper.isDisplayed(locator);
    }

    /**
     * Check if element is enabled
     */
    protected boolean isElementEnabled(By locator) {
        return elementHelper.isEnabled(locator);
    }

    /**
     * Get page source
     */
    protected String getPageSource() {
        return driver.getPageSource();
    }

    /**
     * Switch to frame by index
     */
    protected void switchToFrame(int index) {
        driver.switchTo().frame(index);
        logger.debug("Switched to frame: " + index);
    }

    /**
     * Switch to frame by name or ID
     */
    protected void switchToFrame(String nameOrId) {
        driver.switchTo().frame(nameOrId);
        logger.debug("Switched to frame: " + nameOrId);
    }

    /**
     * Switch to frame by WebElement
     */
    protected void switchToFrame(WebElement element) {
        driver.switchTo().frame(element);
        logger.debug("Switched to frame element");
    }

    /**
     * Switch to default content
     */
    protected void switchToDefaultContent() {
        driver.switchTo().defaultContent();
        logger.debug("Switched to default content");
    }

    /**
     * Switch to parent frame
     */
    protected void switchToParentFrame() {
        driver.switchTo().parentFrame();
        logger.debug("Switched to parent frame");
    }

    /**
     * Get window handles
     */
    protected java.util.Set<String> getWindowHandles() {
        return driver.getWindowHandles();
    }

    /**
     * Switch to window by title
     */
    protected void switchToWindowByTitle(String title) {
        for (String handle : getWindowHandles()) {
            driver.switchTo().window(handle);
            if (driver.getTitle().equals(title)) {
                logger.debug("Switched to window: " + title);
                return;
            }
        }
        throw new RuntimeException("Window not found with title: " + title);
    }

    /**
     * Close current window
     */
    protected void closeCurrentWindow() {
        driver.close();
        logger.debug("Closed current window");
    }

    /**
     * Verify page is loaded (to be implemented by child classes)
     */
    public abstract boolean isPageLoaded();

    /**
     * Wait for page to load
     */
    public void waitForPageToLoad() {
        waitHelper.waitForPageLoad();
        if (!isPageLoaded()) {
            throw new RuntimeException(this.getClass().getSimpleName() + " did not load properly");
        }
    }
}
