package com.demoblaze.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.ArrayList;
import java.util.List;

/**
 * CartPage - Page Object for Shopping Cart Page
 */
public class CartPage extends BasePage {

    // Locators
    private static final By CART_TABLE = By.cssSelector("#tbodyid");
    private static final By CART_ITEMS = By.cssSelector("#tbodyid tr");
    private static final By PRODUCT_TITLES = By.cssSelector("#tbodyid tr td:nth-child(2)");
    private static final By PRODUCT_PRICES = By.cssSelector("#tbodyid tr td:nth-child(3)");
    private static final By DELETE_LINKS = By.cssSelector("#tbodyid tr td:nth-child(4) a");
    private static final By TOTAL_PRICE = By.id("totalp");
    private static final By PLACE_ORDER_BUTTON = By.cssSelector("button[data-target='#orderModal']");
    private static final By HOME_LINK = By.linkText("Home");

    @FindBy(id = "totalp")
    private WebElement totalPrice;

    @FindBy(css = "button[data-target='#orderModal']")
    private WebElement placeOrderButton;

    /**
     * Constructor
     */
    public CartPage(WebDriver driver) {
        super(driver);
        waitHelper.waitForPageLoad();
    }

    /**
     * Check if page is loaded
     */
    @Override
    public boolean isPageLoaded() {
        try {
            return getCurrentUrl().contains("cart.html") &&
                   isElementDisplayed(TOTAL_PRICE) &&
                   isElementDisplayed(PLACE_ORDER_BUTTON);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Get number of items in cart
     */
    public int getCartItemCount() {
        List<WebElement> items = elementHelper.getElements(CART_ITEMS);
        int count = items.size();
        logger.info("Cart has " + count + " item(s)");
        return count;
    }

    /**
     * Check if cart is empty
     */
    public boolean isCartEmpty() {
        return getCartItemCount() == 0;
    }

    /**
     * Get all product names in cart
     */
    public List<String> getProductNamesInCart() {
        List<WebElement> productElements = elementHelper.getElements(PRODUCT_TITLES);
        List<String> productNames = new ArrayList<>();
        
        for (WebElement element : productElements) {
            productNames.add(element.getText());
        }
        
        logger.info("Products in cart: " + productNames);
        return productNames;
    }

    /**
     * Check if product is in cart
     */
    public boolean isProductInCart(String productName) {
        List<String> products = getProductNamesInCart();
        boolean inCart = products.contains(productName);
        logger.info("Is '" + productName + "' in cart? " + inCart);
        return inCart;
    }

    /**
     * Get all product prices in cart
     */
    public List<String> getProductPricesInCart() {
        List<WebElement> priceElements = elementHelper.getElements(PRODUCT_PRICES);
        List<String> prices = new ArrayList<>();
        
        for (WebElement element : priceElements) {
            prices.add(element.getText());
        }
        
        logger.info("Product prices in cart: " + prices);
        return prices;
    }

    /**
     * Get total price text
     */
    public String getTotalPriceText() {
        String total = getText(TOTAL_PRICE);
        logger.info("Total price: " + total);
        return total;
    }

    /**
     * Get total price value (numeric)
     */
    public double getTotalPriceValue() {
        String totalText = getTotalPriceText();
        if (totalText.isEmpty() || totalText.equals("0")) {
            return 0.0;
        }
        return Double.parseDouble(totalText);
    }

    /**
     * Calculate expected total from individual prices
     */
    public double calculateExpectedTotal() {
        List<String> prices = getProductPricesInCart();
        double total = 0.0;
        
        for (String price : prices) {
            total += Double.parseDouble(price);
        }
        
        logger.info("Calculated expected total: " + total);
        return total;
    }

    /**
     * Verify total price is correct
     */
    public boolean isTotalPriceCorrect() {
        double displayed = getTotalPriceValue();
        double expected = calculateExpectedTotal();
        boolean isCorrect = Math.abs(displayed - expected) < 0.01;
        logger.info("Total price correct? " + isCorrect + " (Expected: " + expected + ", Actual: " + displayed + ")");
        return isCorrect;
    }

    /**
     * Delete product from cart by name
     */
    public CartPage deleteProduct(String productName) {
        logger.info("Deleting product: " + productName);
        List<String> productNames = getProductNamesInCart();
        int index = productNames.indexOf(productName);
        
        if (index != -1) {
            List<WebElement> deleteLinks = elementHelper.getElements(DELETE_LINKS);
            deleteLinks.get(index).click();
            waitHelper.hardWait(1); // Wait for deletion to complete
            logger.info("Product deleted: " + productName);
        } else {
            logger.warn("Product not found in cart: " + productName);
        }
        
        return this;
    }

    /**
     * Delete product by index
     */
    public CartPage deleteProductByIndex(int index) {
        logger.info("Deleting product at index: " + index);
        List<WebElement> deleteLinks = elementHelper.getElements(DELETE_LINKS);
        
        if (index >= 0 && index < deleteLinks.size()) {
            deleteLinks.get(index).click();
            waitHelper.hardWait(1);
            logger.info("Product deleted at index: " + index);
        } else {
            logger.warn("Invalid index: " + index);
        }
        
        return this;
    }

    /**
     * Clear all items from cart
     */
    public CartPage clearCart() {
        logger.info("Clearing all items from cart");
        int itemCount = getCartItemCount();
        
        while (itemCount > 0) {
            deleteProductByIndex(0);
            itemCount = getCartItemCount();
        }
        
        logger.info("Cart cleared");
        return this;
    }

    /**
     * Click Place Order button
     */
    public CheckoutPage clickPlaceOrder() {
        logger.info("Clicking Place Order button");
        click(PLACE_ORDER_BUTTON);
        return new CheckoutPage(driver);
    }

    /**
     * Check if Place Order button is displayed
     */
    public boolean isPlaceOrderButtonDisplayed() {
        return isElementDisplayed(PLACE_ORDER_BUTTON);
    }

    /**
     * Check if Place Order button is enabled
     */
    public boolean isPlaceOrderButtonEnabled() {
        return isElementEnabled(PLACE_ORDER_BUTTON);
    }

    /**
     * Navigate to home
     */
    public HomePage navigateToHome() {
        logger.info("Navigating to home");
        click(HOME_LINK);
        return new HomePage(driver);
    }

    /**
     * Get cart table element
     */
    public boolean isCartTableDisplayed() {
        return isElementDisplayed(CART_TABLE);
    }

    /**
     * Verify cart contains expected products
     */
    public boolean containsProducts(List<String> expectedProducts) {
        List<String> actualProducts = getProductNamesInCart();
        boolean containsAll = actualProducts.containsAll(expectedProducts);
        logger.info("Cart contains all expected products? " + containsAll);
        return containsAll;
    }

    /**
     * Verify cart contains specific product count
     */
    public boolean hasProductCount(int expectedCount) {
        int actualCount = getCartItemCount();
        boolean matches = actualCount == expectedCount;
        logger.info("Cart has " + actualCount + " items (expected: " + expectedCount + ")");
        return matches;
    }
}
