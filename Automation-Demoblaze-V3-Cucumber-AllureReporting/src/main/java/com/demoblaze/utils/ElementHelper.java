package com.demoblaze.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

/**
 * ElementHelper - Provides helper methods for interacting with web elements
 */
public class ElementHelper {
    private static final Logger logger = LogManager.getLogger(ElementHelper.class);
    private final WebDriver driver;
    private final WaitHelper waitHelper;
    private final Actions actions;

    public ElementHelper(WebDriver driver) {
        this.driver = driver;
        this.waitHelper = new WaitHelper(driver);
        this.actions = new Actions(driver);
    }

    /**
     * Click element with wait
     */
    public void click(By locator) {
        try {
            WebElement element = waitHelper.waitForElementClickable(locator);
            element.click();
            logger.debug("Clicked element: " + locator);
        } catch (Exception e) {
            logger.error("Failed to click element: " + locator);
            throw e;
        }
    }

    /**
     * Click element using JavaScript
     */
    public void clickUsingJs(By locator) {
        try {
            WebElement element = waitHelper.waitForElementPresent(locator);
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].click();", element);
            logger.debug("Clicked element using JS: " + locator);
        } catch (Exception e) {
            logger.error("Failed to click element using JS: " + locator);
            throw e;
        }
    }

    /**
     * Type text into element
     */
    public void type(By locator, String text) {
        try {
            WebElement element = waitHelper.waitForElementVisible(locator);
            element.clear();
            element.sendKeys(text);
            logger.debug("Typed text into element: " + locator);
        } catch (Exception e) {
            logger.error("Failed to type into element: " + locator);
            throw e;
        }
    }

    /**
     * Type text slowly (character by character)
     */
    public void typeSlowly(By locator, String text, int delayMillis) throws InterruptedException {
        try {
            WebElement element = waitHelper.waitForElementVisible(locator);
            element.clear();
            
            for (char c : text.toCharArray()) {
                element.sendKeys(String.valueOf(c));
                Thread.sleep(delayMillis);
            }
            logger.debug("Typed text slowly into element: " + locator);
        } catch (Exception e) {
            logger.error("Failed to type slowly into element: " + locator);
            throw e;
        }
    }

    /**
     * Clear element
     */
    public void clear(By locator) {
        try {
            WebElement element = waitHelper.waitForElementVisible(locator);
            element.clear();
            logger.debug("Cleared element: " + locator);
        } catch (Exception e) {
            logger.error("Failed to clear element: " + locator);
            throw e;
        }
    }

    /**
     * Get text from element
     */
    public String getText(By locator) {
        try {
            WebElement element = waitHelper.waitForElementVisible(locator);
            String text = element.getText();
            logger.debug("Got text from element: " + locator + " -> " + text);
            return text;
        } catch (Exception e) {
            logger.error("Failed to get text from element: " + locator);
            throw e;
        }
    }

    /**
     * Get attribute value
     */
    public String getAttribute(By locator, String attribute) {
        try {
            WebElement element = waitHelper.waitForElementVisible(locator);
            String value = element.getAttribute(attribute);
            logger.debug("Got attribute '" + attribute + "' from element: " + locator + " -> " + value);
            return value;
        } catch (Exception e) {
            logger.error("Failed to get attribute from element: " + locator);
            throw e;
        }
    }

    /**
     * Check if element is displayed
     */
    public boolean isDisplayed(By locator) {
        try {
            WebElement element = driver.findElement(locator);
            boolean displayed = element.isDisplayed();
            logger.debug("Element displayed: " + locator + " -> " + displayed);
            return displayed;
        } catch (NoSuchElementException e) {
            logger.debug("Element not found: " + locator);
            return false;
        }
    }

    /**
     * Check if element is enabled
     */
    public boolean isEnabled(By locator) {
        try {
            WebElement element = driver.findElement(locator);
            boolean enabled = element.isEnabled();
            logger.debug("Element enabled: " + locator + " -> " + enabled);
            return enabled;
        } catch (NoSuchElementException e) {
            logger.debug("Element not found: " + locator);
            return false;
        }
    }

    /**
     * Check if element is selected
     */
    public boolean isSelected(By locator) {
        try {
            WebElement element = driver.findElement(locator);
            boolean selected = element.isSelected();
            logger.debug("Element selected: " + locator + " -> " + selected);
            return selected;
        } catch (NoSuchElementException e) {
            logger.debug("Element not found: " + locator);
            return false;
        }
    }

    /**
     * Select from dropdown by visible text
     */
    public void selectByVisibleText(By locator, String text) {
        try {
            WebElement element = waitHelper.waitForElementVisible(locator);
            Select select = new Select(element);
            select.selectByVisibleText(text);
            logger.debug("Selected '" + text + "' from dropdown: " + locator);
        } catch (Exception e) {
            logger.error("Failed to select from dropdown: " + locator);
            throw e;
        }
    }

    /**
     * Select from dropdown by value
     */
    public void selectByValue(By locator, String value) {
        try {
            WebElement element = waitHelper.waitForElementVisible(locator);
            Select select = new Select(element);
            select.selectByValue(value);
            logger.debug("Selected value '" + value + "' from dropdown: " + locator);
        } catch (Exception e) {
            logger.error("Failed to select by value from dropdown: " + locator);
            throw e;
        }
    }

    /**
     * Select from dropdown by index
     */
    public void selectByIndex(By locator, int index) {
        try {
            WebElement element = waitHelper.waitForElementVisible(locator);
            Select select = new Select(element);
            select.selectByIndex(index);
            logger.debug("Selected index " + index + " from dropdown: " + locator);
        } catch (Exception e) {
            logger.error("Failed to select by index from dropdown: " + locator);
            throw e;
        }
    }

    /**
     * Hover over element
     */
    public void hover(By locator) {
        try {
            WebElement element = waitHelper.waitForElementVisible(locator);
            actions.moveToElement(element).perform();
            logger.debug("Hovered over element: " + locator);
        } catch (Exception e) {
            logger.error("Failed to hover over element: " + locator);
            throw e;
        }
    }

    /**
     * Double click element
     */
    public void doubleClick(By locator) {
        try {
            WebElement element = waitHelper.waitForElementClickable(locator);
            actions.doubleClick(element).perform();
            logger.debug("Double clicked element: " + locator);
        } catch (Exception e) {
            logger.error("Failed to double click element: " + locator);
            throw e;
        }
    }

    /**
     * Right click element
     */
    public void rightClick(By locator) {
        try {
            WebElement element = waitHelper.waitForElementClickable(locator);
            actions.contextClick(element).perform();
            logger.debug("Right clicked element: " + locator);
        } catch (Exception e) {
            logger.error("Failed to right click element: " + locator);
            throw e;
        }
    }

    /**
     * Drag and drop
     */
    public void dragAndDrop(By sourceLocator, By targetLocator) {
        try {
            WebElement source = waitHelper.waitForElementVisible(sourceLocator);
            WebElement target = waitHelper.waitForElementVisible(targetLocator);
            actions.dragAndDrop(source, target).perform();
            logger.debug("Dragged element from " + sourceLocator + " to " + targetLocator);
        } catch (Exception e) {
            logger.error("Failed to drag and drop");
            throw e;
        }
    }

    /**
     * Scroll to element
     */
    public void scrollToElement(By locator) {
        try {
            WebElement element = waitHelper.waitForElementPresent(locator);
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].scrollIntoView(true);", element);
            logger.debug("Scrolled to element: " + locator);
        } catch (Exception e) {
            logger.error("Failed to scroll to element: " + locator);
            throw e;
        }
    }

    /**
     * Scroll by pixel amount
     */
    public void scrollByPixels(int x, int y) {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("window.scrollBy(" + x + "," + y + ")");
            logger.debug("Scrolled by pixels: x=" + x + ", y=" + y);
        } catch (Exception e) {
            logger.error("Failed to scroll by pixels");
            throw e;
        }
    }

    /**
     * Get all elements matching locator
     */
    public List<WebElement> getElements(By locator) {
        try {
            List<WebElement> elements = driver.findElements(locator);
            logger.debug("Found " + elements.size() + " elements: " + locator);
            return elements;
        } catch (Exception e) {
            logger.error("Failed to get elements: " + locator);
            throw e;
        }
    }

    /**
     * Get count of elements
     */
    public int getElementCount(By locator) {
        return getElements(locator).size();
    }

    /**
     * Press key on element
     */
    public void pressKey(By locator, Keys key) {
        try {
            WebElement element = waitHelper.waitForElementVisible(locator);
            element.sendKeys(key);
            logger.debug("Pressed key " + key + " on element: " + locator);
        } catch (Exception e) {
            logger.error("Failed to press key on element: " + locator);
            throw e;
        }
    }

    /**
     * Highlight element (for debugging)
     */
    public void highlightElement(By locator) {
        try {
            WebElement element = driver.findElement(locator);
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].style.border='3px solid red'", element);
            Thread.sleep(500);
            js.executeScript("arguments[0].style.border=''", element);
        } catch (Exception e) {
            logger.debug("Failed to highlight element: " + locator);
        }
    }
}
