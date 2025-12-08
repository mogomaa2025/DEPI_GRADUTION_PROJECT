package com.demoblaze.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * ProductDetailPage - Page Object for Product Details Page
 */
public class ProductDetailPage extends BasePage {

    // Locators
    private static final By PRODUCT_NAME = By.cssSelector(".name");
    private static final By PRODUCT_PRICE = By.cssSelector(".price-container");
    private static final By PRODUCT_DESCRIPTION = By.id("more-information");
    private static final By PRODUCT_IMAGE = By.cssSelector(".product-image");
    private static final By ADD_TO_CART_BUTTON = By.cssSelector("a[onclick*='addToCart']");
    private static final By HOME_LINK = By.linkText("Home");

    @FindBy(css = ".name")
    private WebElement productName;

    @FindBy(css = ".price-container")
    private WebElement productPrice;

    @FindBy(css = "a[onclick*='addToCart']")
    private WebElement addToCartButton;

    /**
     * Constructor
     */
    public ProductDetailPage(WebDriver driver) {
        super(driver);
        waitHelper.waitForPageLoad();
    }

    /**
     * Check if page is loaded
     */
    @Override
    public boolean isPageLoaded() {
        try {
            return isElementDisplayed(PRODUCT_NAME) && 
                   isElementDisplayed(PRODUCT_PRICE) &&
                   isElementDisplayed(ADD_TO_CART_BUTTON) &&
                   getCurrentUrl().contains("prod.html");
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Get product name
     */
    public String getProductName() {
        String name = getText(PRODUCT_NAME);
        logger.info("Product name: " + name);
        return name;
    }

    /**
     * Get product price
     */
    public String getProductPrice() {
        String priceText = getText(PRODUCT_PRICE);
        logger.info("Product price: " + priceText);
        return priceText;
    }

    /**
     * Get price value (numeric)
     */
    public double getProductPriceValue() {
        String priceText = getProductPrice();
        // Extract numeric value from price text (e.g., "$360" -> 360.0)
        String numericPrice = priceText.replaceAll("[^0-9.]", "");
        return Double.parseDouble(numericPrice);
    }

    /**
     * Get product description
     */
    public String getProductDescription() {
        if (isElementDisplayed(PRODUCT_DESCRIPTION)) {
            return getText(PRODUCT_DESCRIPTION);
        }
        return "";
    }

    /**
     * Check if product image is displayed
     */
    public boolean isProductImageDisplayed() {
        return isElementDisplayed(PRODUCT_IMAGE);
    }

    /**
     * Check if Add to Cart button is displayed
     */
    public boolean isAddToCartButtonDisplayed() {
        return isElementDisplayed(ADD_TO_CART_BUTTON);
    }

    /**
     * Click Add to Cart button
     */
    public ProductDetailPage clickAddToCart() {
        logger.info("Clicking Add to Cart button");
        click(ADD_TO_CART_BUTTON);
        waitHelper.hardWait(1); // Wait for alert to appear
        return this;
    }

    /**
     * Add to cart and handle alert
     */
    public String addToCartAndGetAlertMessage() {
        clickAddToCart();
        String alertText = getAlertText();
        acceptAlert();
        logger.info("Product added to cart - Alert: " + alertText);
        return alertText;
    }

    /**
     * Add to cart without handling alert
     */
    public ProductDetailPage addToCartWithoutHandlingAlert() {
        clickAddToCart();
        logger.info("Product added to cart (alert not handled)");
        return this;
    }

    /**
     * Verify product was added (check alert message)
     */
    public boolean verifyProductAdded() {
        try {
            String alertText = getAlertText();
            boolean isAdded = alertText.contains("Product added");
            acceptAlert();
            return isAdded;
        } catch (Exception e) {
            logger.error("No alert present or error checking alert");
            return false;
        }
    }

    /**
     * Navigate back to home
     */
    public HomePage navigateToHome() {
        logger.info("Navigating back to home");
        click(HOME_LINK);
        return new HomePage(driver);
    }

    /**
     * Go back using browser back button
     */
    public HomePage goBack() {
        logger.info("Going back using browser back button");
        navigateBack();
        return new HomePage(driver);
    }

    /**
     * Verify all product details are displayed
     */
    public boolean areAllDetailsDisplayed() {
        return isElementDisplayed(PRODUCT_NAME) &&
               isElementDisplayed(PRODUCT_PRICE) &&
               isProductImageDisplayed() &&
               isAddToCartButtonDisplayed();
    }

    /**
     * Get page URL
     */
    public String getPageUrl() {
        return getCurrentUrl();
    }

    /**
     * Verify page URL contains product ID
     */
    public boolean urlContainsProductId() {
        return getCurrentUrl().contains("prod.html?idp_=");
    }
}
