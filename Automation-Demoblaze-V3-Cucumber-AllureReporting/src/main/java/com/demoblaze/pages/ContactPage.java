package com.demoblaze.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * ContactPage - Page Object for Contact Modal
 */
public class ContactPage extends BasePage {

    // Locators
    private static final By CONTACT_MODAL = By.id("exampleModal");
    private static final By CONTACT_MODAL_TITLE = By.xpath("//h5[@id='exampleModalLabel']");
    private static final By EMAIL_INPUT = By.id("recipient-email");
    private static final By NAME_INPUT = By.id("recipient-name");
    private static final By MESSAGE_INPUT = By.id("message-text");
    private static final By SEND_MESSAGE_BUTTON = By.xpath("//button[text()='Send message']");
    private static final By CLOSE_BUTTON = By.xpath("//div[@id='exampleModal']//button[text()='Close']");
    private static final By CLOSE_X_BUTTON = By.xpath("//div[@id='exampleModal']//button[@class='close']");

    @FindBy(id = "recipient-email")
    private WebElement emailInput;

    @FindBy(id = "recipient-name")
    private WebElement nameInput;

    @FindBy(id = "message-text")
    private WebElement messageInput;

    @FindBy(xpath = "//button[text()='Send message']")
    private WebElement sendMessageButton;

    /**
     * Constructor
     */
    public ContactPage(WebDriver driver) {
        super(driver);
        waitForModalToBeVisible();
    }

    /**
     * Wait for modal to be visible
     */
    private void waitForModalToBeVisible() {
        waitHelper.waitForElementVisible(CONTACT_MODAL);
        logger.debug("Contact modal is visible");
    }

    /**
     * Check if page is loaded
     */
    @Override
    public boolean isPageLoaded() {
        try {
            return isElementDisplayed(CONTACT_MODAL) && 
                   isElementDisplayed(EMAIL_INPUT) &&
                   isElementDisplayed(NAME_INPUT) &&
                   isElementDisplayed(MESSAGE_INPUT) &&
                   isElementDisplayed(SEND_MESSAGE_BUTTON);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Check if modal is displayed
     */
    public boolean isModalDisplayed() {
        return isElementDisplayed(CONTACT_MODAL);
    }

    /**
     * Enter contact email
     */
    public ContactPage enterEmail(String email) {
        logger.info("Entering email: " + email);
        type(EMAIL_INPUT, email);
        return this;
    }

    /**
     * Enter contact name
     */
    public ContactPage enterName(String name) {
        logger.info("Entering name: " + name);
        type(NAME_INPUT, name);
        return this;
    }

    /**
     * Enter message
     */
    public ContactPage enterMessage(String message) {
        logger.info("Entering message");
        type(MESSAGE_INPUT, message);
        return this;
    }

    /**
     * Fill complete contact form
     */
    public ContactPage fillContactForm(String email, String name, String message) {
        logger.info("Filling contact form");
        enterEmail(email);
        enterName(name);
        enterMessage(message);
        return this;
    }

    /**
     * Leave email field empty
     */
    public ContactPage leaveEmailEmpty() {
        logger.info("Leaving email field empty");
        elementHelper.clear(EMAIL_INPUT);
        return this;
    }

    /**
     * Leave name field empty
     */
    public ContactPage leaveNameEmpty() {
        logger.info("Leaving name field empty");
        elementHelper.clear(NAME_INPUT);
        return this;
    }

    /**
     * Leave message field empty
     */
    public ContactPage leaveMessageEmpty() {
        logger.info("Leaving message field empty");
        elementHelper.clear(MESSAGE_INPUT);
        return this;
    }

    /**
     * Click Send message button
     */
    public void clickSendMessage() {
        logger.info("Clicking Send message button");
        click(SEND_MESSAGE_BUTTON);
        waitHelper.hardWait(1); // Wait for alert
    }

    /**
     * Send message and get alert text
     */
    public String sendMessageAndGetAlert(String email, String name, String message) {
        fillContactForm(email, name, message);
        clickSendMessage();
        String alertText = getAlertText();
        acceptAlert();
        logger.info("Message sent - Alert: " + alertText);
        return alertText;
    }

    /**
     * Click Close button
     */
    public HomePage clickClose() {
        logger.info("Clicking Close button");
        click(CLOSE_BUTTON);
        waitHelper.waitForElementInvisible(CONTACT_MODAL);
        return new HomePage(driver);
    }

    /**
     * Click X (close) button
     */
    public HomePage clickCloseX() {
        logger.info("Clicking X button");
        click(CLOSE_X_BUTTON);
        waitHelper.waitForElementInvisible(CONTACT_MODAL);
        return new HomePage(driver);
    }

    /**
     * Get modal title
     */
    public String getModalTitle() {
        return getText(CONTACT_MODAL_TITLE);
    }

    /**
     * Check if email field is displayed
     */
    public boolean isEmailFieldDisplayed() {
        return isElementDisplayed(EMAIL_INPUT);
    }

    /**
     * Check if name field is displayed
     */
    public boolean isNameFieldDisplayed() {
        return isElementDisplayed(NAME_INPUT);
    }

    /**
     * Check if message field is displayed
     */
    public boolean isMessageFieldDisplayed() {
        return isElementDisplayed(MESSAGE_INPUT);
    }

    /**
     * Check if send message button is displayed
     */
    public boolean isSendMessageButtonDisplayed() {
        return isElementDisplayed(SEND_MESSAGE_BUTTON);
    }

    /**
     * Check if close button is displayed
     */
    public boolean isCloseButtonDisplayed() {
        return isElementDisplayed(CLOSE_BUTTON);
    }

    /**
     * Verify all fields are displayed
     */
    public boolean areAllFieldsDisplayed() {
        return isEmailFieldDisplayed() &&
               isNameFieldDisplayed() &&
               isMessageFieldDisplayed() &&
               isSendMessageButtonDisplayed();
    }

    /**
     * Get alert text and accept
     */
    public String getAlertTextAndAccept() {
        String alertText = getAlertText();
        acceptAlert();
        return alertText;
    }

    /**
     * Verify modal is closed
     */
    public boolean isModalClosed() {
        return !isElementDisplayed(CONTACT_MODAL);
    }

    /**
     * Enter long message (for testing)
     */
    public ContactPage enterLongMessage(int characterCount) {
        String longMessage = com.demoblaze.utils.TestDataGenerator.generateLongText(characterCount);
        return enterMessage(longMessage);
    }
}
