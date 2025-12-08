package com.demoblaze.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * CheckoutPage - Page Object for Checkout/Order Modal
 */
public class CheckoutPage extends BasePage {

    // Locators
    private static final By ORDER_MODAL = By.id("orderModal");
    private static final By ORDER_MODAL_TITLE = By.xpath("//h5[@id='orderModalLabel']");
    private static final By NAME_INPUT = By.id("name");
    private static final By COUNTRY_INPUT = By.id("country");
    private static final By CITY_INPUT = By.id("city");
    private static final By CARD_INPUT = By.id("card");
    private static final By MONTH_INPUT = By.id("month");
    private static final By YEAR_INPUT = By.id("year");
    private static final By PURCHASE_BUTTON = By.xpath("//button[text()='Purchase']");
    private static final By CLOSE_BUTTON = By.xpath("//div[@id='orderModal']//button[text()='Close']");
    private static final By CLOSE_X_BUTTON = By.xpath("//div[@id='orderModal']//button[@class='close']");
    
    // Order confirmation
    private static final By CONFIRMATION_MODAL = By.cssSelector(".sweet-alert.showSweetAlert.visible");
    private static final By CONFIRMATION_TITLE = By.cssSelector(".sweet-alert h2");
    private static final By CONFIRMATION_TEXT = By.cssSelector(".sweet-alert .lead");
    private static final By CONFIRMATION_OK_BUTTON = By.cssSelector(".confirm.btn.btn-lg.btn-primary");

    @FindBy(id = "name")
    private WebElement nameInput;

    @FindBy(id = "card")
    private WebElement cardInput;

    @FindBy(xpath = "//button[text()='Purchase']")
    private WebElement purchaseButton;

    /**
     * Constructor
     */
    public CheckoutPage(WebDriver driver) {
        super(driver);
        waitForModalToBeVisible();
    }

    /**
     * Wait for modal to be visible
     */
    private void waitForModalToBeVisible() {
        waitHelper.waitForElementVisible(ORDER_MODAL);
        logger.debug("Order modal is visible");
    }

    /**
     * Check if page is loaded
     */
    @Override
    public boolean isPageLoaded() {
        try {
            return isElementDisplayed(ORDER_MODAL) && 
                   isElementDisplayed(NAME_INPUT) &&
                   isElementDisplayed(CARD_INPUT) &&
                   isElementDisplayed(PURCHASE_BUTTON);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Check if modal is displayed
     */
    public boolean isModalDisplayed() {
        return isElementDisplayed(ORDER_MODAL);
    }

    /**
     * Enter name
     */
    public CheckoutPage enterName(String name) {
        logger.info("Entering name: " + name);
        type(NAME_INPUT, name);
        return this;
    }

    /**
     * Enter country
     */
    public CheckoutPage enterCountry(String country) {
        logger.info("Entering country: " + country);
        type(COUNTRY_INPUT, country);
        return this;
    }

    /**
     * Enter city
     */
    public CheckoutPage enterCity(String city) {
        logger.info("Entering city: " + city);
        type(CITY_INPUT, city);
        return this;
    }

    /**
     * Enter credit card
     */
    public CheckoutPage enterCreditCard(String card) {
        logger.info("Entering credit card");
        type(CARD_INPUT, card);
        return this;
    }

    /**
     * Enter month
     */
    public CheckoutPage enterMonth(String month) {
        logger.info("Entering month: " + month);
        type(MONTH_INPUT, month);
        return this;
    }

    /**
     * Enter year
     */
    public CheckoutPage enterYear(String year) {
        logger.info("Entering year: " + year);
        type(YEAR_INPUT, year);
        return this;
    }

    /**
     * Fill complete order form
     */
    public CheckoutPage fillOrderForm(String name, String country, String city, 
                                       String card, String month, String year) {
        logger.info("Filling complete order form");
        enterName(name);
        enterCountry(country);
        enterCity(city);
        enterCreditCard(card);
        enterMonth(month);
        enterYear(year);
        return this;
    }

    /**
     * Fill order form with minimal required fields
     */
    public CheckoutPage fillMinimalOrderForm(String name, String card) {
        logger.info("Filling minimal order form");
        enterName(name);
        enterCreditCard(card);
        return this;
    }

    /**
     * Leave name field empty
     */
    public CheckoutPage leaveNameEmpty() {
        logger.info("Leaving name field empty");
        elementHelper.clear(NAME_INPUT);
        return this;
    }

    /**
     * Leave credit card field empty
     */
    public CheckoutPage leaveCreditCardEmpty() {
        logger.info("Leaving credit card field empty");
        elementHelper.clear(CARD_INPUT);
        return this;
    }

    /**
     * Click Purchase button
     */
    public CheckoutPage clickPurchase() {
        logger.info("Clicking Purchase button");
        click(PURCHASE_BUTTON);
        waitHelper.hardWait(1); // Wait for processing
        return this;
    }

    /**
     * Click Close button
     */
    public CartPage clickClose() {
        logger.info("Clicking Close button");
        click(CLOSE_BUTTON);
        waitHelper.waitForElementInvisible(ORDER_MODAL);
        return new CartPage(driver);
    }

    /**
     * Click X (close) button
     */
    public CartPage clickCloseX() {
        logger.info("Clicking X button");
        click(CLOSE_X_BUTTON);
        waitHelper.waitForElementInvisible(ORDER_MODAL);
        return new CartPage(driver);
    }

    /**
     * Get modal title
     */
    public String getModalTitle() {
        return getText(ORDER_MODAL_TITLE);
    }

    /**
     * Check if all fields are displayed
     */
    public boolean areAllFieldsDisplayed() {
        return isElementDisplayed(NAME_INPUT) &&
               isElementDisplayed(COUNTRY_INPUT) &&
               isElementDisplayed(CITY_INPUT) &&
               isElementDisplayed(CARD_INPUT) &&
               isElementDisplayed(MONTH_INPUT) &&
               isElementDisplayed(YEAR_INPUT);
    }

    /**
     * Check if purchase button is displayed
     */
    public boolean isPurchaseButtonDisplayed() {
        return isElementDisplayed(PURCHASE_BUTTON);
    }

    /**
     * Wait for confirmation modal
     */
    public CheckoutPage waitForConfirmation() {
        waitHelper.waitForElementVisible(CONFIRMATION_MODAL);
        logger.info("Order confirmation displayed");
        return this;
    }

    /**
     * Check if confirmation is displayed
     */
    public boolean isConfirmationDisplayed() {
        return isElementDisplayed(CONFIRMATION_MODAL);
    }

    /**
     * Get confirmation title
     */
    public String getConfirmationTitle() {
        if (isConfirmationDisplayed()) {
            return getText(CONFIRMATION_TITLE);
        }
        return "";
    }

    /**
     * Get confirmation text (contains order details)
     */
    public String getConfirmationText() {
        if (isConfirmationDisplayed()) {
            return getText(CONFIRMATION_TEXT);
        }
        return "";
    }

    /**
     * Get order ID from confirmation
     */
    public String getOrderId() {
        String confirmationText = getConfirmationText();
        String[] lines = confirmationText.split("\n");
        for (String line : lines) {
            if (line.startsWith("Id:")) {
                return line.substring(4).trim();
            }
        }
        return "";
    }

    /**
     * Get order amount from confirmation
     */
    public String getOrderAmount() {
        String confirmationText = getConfirmationText();
        String[] lines = confirmationText.split("\n");
        for (String line : lines) {
            if (line.startsWith("Amount:")) {
                return line.substring(8).trim().replace("USD", "").trim();
            }
        }
        return "";
    }

    /**
     * Get card number from confirmation
     */
    public String getOrderCardNumber() {
        String confirmationText = getConfirmationText();
        String[] lines = confirmationText.split("\n");
        for (String line : lines) {
            if (line.startsWith("Card Number:")) {
                return line.substring(13).trim();
            }
        }
        return "";
    }

    /**
     * Get name from confirmation
     */
    public String getOrderName() {
        String confirmationText = getConfirmationText();
        String[] lines = confirmationText.split("\n");
        for (String line : lines) {
            if (line.startsWith("Name:")) {
                return line.substring(6).trim();
            }
        }
        return "";
    }

    /**
     * Get date from confirmation
     */
    public String getOrderDate() {
        String confirmationText = getConfirmationText();
        String[] lines = confirmationText.split("\n");
        for (String line : lines) {
            if (line.startsWith("Date:")) {
                return line.substring(6).trim();
            }
        }
        return "";
    }

    /**
     * Click OK on confirmation
     */
    public HomePage clickConfirmationOk() {
        logger.info("Clicking OK on confirmation");
        click(CONFIRMATION_OK_BUTTON);
        return new HomePage(driver);
    }

    /**
     * Complete purchase with full details
     */
    public CheckoutPage completePurchase(String name, String country, String city, 
                                          String card, String month, String year) {
        fillOrderForm(name, country, city, card, month, year);
        clickPurchase();
        waitForConfirmation();
        logger.info("Purchase completed");
        return this;
    }

    /**
     * Complete purchase with minimal details
     */
    public CheckoutPage completePurchaseMinimal(String name, String card) {
        fillMinimalOrderForm(name, card);
        clickPurchase();
        waitForConfirmation();
        logger.info("Purchase completed with minimal info");
        return this;
    }

    /**
     * Verify confirmation contains expected amount
     */
    public boolean verifyOrderAmount(String expectedAmount) {
        String actualAmount = getOrderAmount();
        boolean matches = actualAmount.equals(expectedAmount);
        logger.info("Order amount matches? " + matches + " (Expected: " + expectedAmount + ", Actual: " + actualAmount + ")");
        return matches;
    }

    /**
     * Verify confirmation contains expected name
     */
    public boolean verifyOrderName(String expectedName) {
        String actualName = getOrderName();
        boolean matches = actualName.equals(expectedName);
        logger.info("Order name matches? " + matches);
        return matches;
    }

    /**
     * Get alert text for validation errors
     */
    public String getValidationAlertText() {
        try {
            waitHelper.hardWait(1);
            if (isAlertPresent()) {
                return getAlertText();
            }
        } catch (Exception e) {
            logger.debug("No validation alert present");
        }
        return "";
    }

    /**
     * Accept validation alert
     */
    public CheckoutPage acceptValidationAlert() {
        if (isAlertPresent()) {
            acceptAlert();
        }
        return this;
    }
}
