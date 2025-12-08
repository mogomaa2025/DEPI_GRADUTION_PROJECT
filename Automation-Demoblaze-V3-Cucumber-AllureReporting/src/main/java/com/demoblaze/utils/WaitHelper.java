package com.demoblaze.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * WaitHelper - Provides various wait utilities for WebDriver
 */
public class WaitHelper {
    private static final Logger logger = LogManager.getLogger(WaitHelper.class);
    private static final ConfigReader config = ConfigReader.getInstance();
    private final WebDriver driver;
    private final WebDriverWait wait;

    public WaitHelper(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(config.getExplicitWait()));
    }

    /**
     * Wait for element to be visible
     */
    public WebElement waitForElementVisible(By locator) {
        logger.debug("Waiting for element to be visible: " + locator);
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        } catch (TimeoutException e) {
            logger.error("Element not visible within timeout: " + locator);
            throw e;
        }
    }

    /**
     * Wait for element to be clickable
     */
    public WebElement waitForElementClickable(By locator) {
        logger.debug("Waiting for element to be clickable: " + locator);
        try {
            return wait.until(ExpectedConditions.elementToBeClickable(locator));
        } catch (TimeoutException e) {
            logger.error("Element not clickable within timeout: " + locator);
            throw e;
        }
    }

    /**
     * Wait for element to be present in DOM
     */
    public WebElement waitForElementPresent(By locator) {
        logger.debug("Waiting for element to be present: " + locator);
        try {
            return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        } catch (TimeoutException e) {
            logger.error("Element not present within timeout: " + locator);
            throw e;
        }
    }

    /**
     * Wait for element to be invisible
     */
    public boolean waitForElementInvisible(By locator) {
        logger.debug("Waiting for element to be invisible: " + locator);
        try {
            return wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
        } catch (TimeoutException e) {
            logger.error("Element still visible after timeout: " + locator);
            return false;
        }
    }

    /**
     * Wait for alert to be present
     */
    public Alert waitForAlert() {
        logger.debug("Waiting for alert to be present");
        try {
            return wait.until(ExpectedConditions.alertIsPresent());
        } catch (TimeoutException e) {
            logger.error("Alert not present within timeout");
            throw e;
        }
    }

    /**
     * Wait for page title to contain text
     */
    public boolean waitForTitleContains(String title) {
        logger.debug("Waiting for title to contain: " + title);
        try {
            return wait.until(ExpectedConditions.titleContains(title));
        } catch (TimeoutException e) {
            logger.error("Title does not contain '" + title + "' within timeout");
            return false;
        }
    }

    /**
     * Wait for URL to contain text
     */
    public boolean waitForUrlContains(String urlPart) {
        logger.debug("Waiting for URL to contain: " + urlPart);
        try {
            return wait.until(ExpectedConditions.urlContains(urlPart));
        } catch (TimeoutException e) {
            logger.error("URL does not contain '" + urlPart + "' within timeout");
            return false;
        }
    }

    /**
     * Wait for element to have specific text
     */
    public boolean waitForTextToBePresentInElement(By locator, String text) {
        logger.debug("Waiting for text '" + text + "' in element: " + locator);
        try {
            return wait.until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
        } catch (TimeoutException e) {
            logger.error("Text '" + text + "' not present in element within timeout: " + locator);
            return false;
        }
    }

    /**
     * Wait for element attribute to contain value
     */
    public boolean waitForAttributeContains(By locator, String attribute, String value) {
        logger.debug("Waiting for attribute '" + attribute + "' to contain '" + value + "'");
        try {
            return wait.until(ExpectedConditions.attributeContains(locator, attribute, value));
        } catch (TimeoutException e) {
            logger.error("Attribute '" + attribute + "' does not contain '" + value + "' within timeout");
            return false;
        }
    }

    /**
     * Wait for number of elements to be
     */
    public boolean waitForNumberOfElementsToBe(By locator, int count) {
        logger.debug("Waiting for " + count + " elements: " + locator);
        try {
            wait.until(ExpectedConditions.numberOfElementsToBe(locator, count));
            return true;
        } catch (TimeoutException e) {
            logger.error("Number of elements is not " + count + " within timeout: " + locator);
            return false;
        }
    }

    /**
     * Fluent wait for element
     */
    public WebElement fluentWaitForElement(By locator, int timeoutSeconds, int pollingSeconds) {
        logger.debug("Fluent wait for element: " + locator);
        
        FluentWait<WebDriver> fluentWait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(timeoutSeconds))
                .pollingEvery(Duration.ofSeconds(pollingSeconds))
                .ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class);

        try {
            return fluentWait.until(driver -> driver.findElement(locator));
        } catch (TimeoutException e) {
            logger.error("Element not found with fluent wait: " + locator);
            throw e;
        }
    }

    /**
     * Wait for JavaScript to complete
     */
    public void waitForPageLoad() {
        logger.debug("Waiting for page to load completely");
        wait.until(driver -> 
            ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete")
        );
    }

    /**
     * Wait for jQuery to complete (if jQuery is present)
     */
    public void waitForJQueryToLoad() {
        logger.debug("Waiting for jQuery to complete");
        try {
            wait.until(driver -> {
                Boolean jQueryDefined = (Boolean) ((JavascriptExecutor) driver)
                        .executeScript("return typeof jQuery != 'undefined'");
                if (jQueryDefined) {
                    return (Boolean) ((JavascriptExecutor) driver)
                            .executeScript("return jQuery.active == 0");
                }
                return true;
            });
        } catch (Exception e) {
            logger.debug("jQuery not present or error checking jQuery status");
        }
    }

    /**
     * Wait for Angular to complete (if Angular is present)
     */
    public void waitForAngularToLoad() {
        logger.debug("Waiting for Angular to complete");
        try {
            wait.until(driver -> (Boolean) ((JavascriptExecutor) driver)
                    .executeScript("return window.getAllAngularTestabilities().findIndex(x=>!x.isStable()) === -1"));
        } catch (Exception e) {
            logger.debug("Angular not present or error checking Angular status");
        }
    }

    /**
     * Hard wait (use sparingly)
     */
    public void hardWait(int seconds) {
        logger.warn("Using hard wait for " + seconds + " seconds (not recommended)");
        try {
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException e) {
            logger.error("Hard wait interrupted: " + e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Wait for element to be stale (useful after page refresh)
     */
    public boolean waitForElementToBeStale(WebElement element) {
        logger.debug("Waiting for element to become stale");
        try {
            return wait.until(ExpectedConditions.stalenessOf(element));
        } catch (TimeoutException e) {
            logger.error("Element did not become stale within timeout");
            return false;
        }
    }

    /**
     * Custom wait with custom condition
     */
    public <T> T waitForCondition(java.util.function.Function<WebDriver, T> condition, int timeoutSeconds) {
        logger.debug("Waiting for custom condition");
        WebDriverWait customWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        return customWait.until(condition);
    }
}
