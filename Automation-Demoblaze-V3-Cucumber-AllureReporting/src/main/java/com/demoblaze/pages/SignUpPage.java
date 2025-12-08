package com.demoblaze.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * SignUpPage - Page Object for Sign Up Modal
 */
public class SignUpPage extends BasePage {

    // Locators
    private static final By SIGNUP_MODAL = By.id("signInModal");
    private static final By SIGNUP_MODAL_TITLE = By.xpath("//h5[@id='signInModalLabel']");
    private static final By USERNAME_INPUT = By.id("sign-username");
    private static final By PASSWORD_INPUT = By.id("sign-password");
    private static final By SIGNUP_BUTTON = By.xpath("//button[text()='Sign up']");
    private static final By CLOSE_BUTTON = By.xpath("//div[@id='signInModal']//button[text()='Close']");
    private static final By CLOSE_X_BUTTON = By.xpath("//div[@id='signInModal']//button[@class='close']");

    @FindBy(id = "sign-username")
    private WebElement usernameInput;

    @FindBy(id = "sign-password")
    private WebElement passwordInput;

    @FindBy(xpath = "//button[text()='Sign up']")
    private WebElement signupButton;

    /**
     * Constructor
     */
    public SignUpPage(WebDriver driver) {
        super(driver);
        waitForModalToBeVisible();
    }

    /**
     * Wait for modal to be visible
     */
    private void waitForModalToBeVisible() {
        waitHelper.waitForElementVisible(SIGNUP_MODAL);
        logger.debug("Sign up modal is visible");
    }

    /**
     * Check if page is loaded
     */
    @Override
    public boolean isPageLoaded() {
        try {
            return isElementDisplayed(SIGNUP_MODAL) && 
                   isElementDisplayed(USERNAME_INPUT) &&
                   isElementDisplayed(PASSWORD_INPUT) &&
                   isElementDisplayed(SIGNUP_BUTTON);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Check if modal is displayed
     */
    public boolean isModalDisplayed() {
        return isElementDisplayed(SIGNUP_MODAL);
    }

    /**
     * Enter username
     */
    public SignUpPage enterUsername(String username) {
        logger.info("Entering username: " + username);
        type(USERNAME_INPUT, username);
        return this;
    }

    /**
     * Enter password
     */
    public SignUpPage enterPassword(String password) {
        logger.info("Entering password");
        type(PASSWORD_INPUT, password);
        return this;
    }

    /**
     * Leave username empty
     */
    public SignUpPage leaveUsernameEmpty() {
        logger.info("Leaving username field empty");
        elementHelper.clear(USERNAME_INPUT);
        return this;
    }

    /**
     * Leave password empty
     */
    public SignUpPage leavePasswordEmpty() {
        logger.info("Leaving password field empty");
        elementHelper.clear(PASSWORD_INPUT);
        return this;
    }

    /**
     * Leave all fields empty
     */
    public SignUpPage leaveAllFieldsEmpty() {
        logger.info("Leaving all fields empty");
        elementHelper.clear(USERNAME_INPUT);
        elementHelper.clear(PASSWORD_INPUT);
        return this;
    }

    /**
     * Click Sign up button
     */
    public void clickSignUpButton() {
        logger.info("Clicking Sign up button");
        click(SIGNUP_BUTTON);
    }

    /**
     * Sign up with credentials
     */
    public void signUp(String username, String password) {
        logger.info("Signing up with username: " + username);
        enterUsername(username);
        enterPassword(password);
        clickSignUpButton();
    }

    /**
     * Click Close button
     */
    public HomePage clickClose() {
        logger.info("Clicking Close button");
        click(CLOSE_BUTTON);
        waitHelper.waitForElementInvisible(SIGNUP_MODAL);
        return new HomePage(driver);
    }

    /**
     * Click X (close) button
     */
    public HomePage clickCloseX() {
        logger.info("Clicking X button");
        click(CLOSE_X_BUTTON);
        waitHelper.waitForElementInvisible(SIGNUP_MODAL);
        return new HomePage(driver);
    }

    /**
     * Get modal title
     */
    public String getModalTitle() {
        return getText(SIGNUP_MODAL_TITLE);
    }

    /**
     * Check if username field is displayed
     */
    public boolean isUsernameFieldDisplayed() {
        return isElementDisplayed(USERNAME_INPUT);
    }

    /**
     * Check if password field is displayed
     */
    public boolean isPasswordFieldDisplayed() {
        return isElementDisplayed(PASSWORD_INPUT);
    }

    /**
     * Check if signup button is displayed
     */
    public boolean isSignUpButtonDisplayed() {
        return isElementDisplayed(SIGNUP_BUTTON);
    }

    /**
     * Check if close button is displayed
     */
    public boolean isCloseButtonDisplayed() {
        return isElementDisplayed(CLOSE_BUTTON);
    }

    /**
     * Wait for alert and get text
     */
    public String getAlertTextAndAccept() {
        String alertText = getAlertText();
        acceptAlert();
        logger.info("Alert text: " + alertText);
        return alertText;
    }

    /**
     * Sign up and handle alert
     */
    public String signUpAndGetAlertMessage(String username, String password) {
        signUp(username, password);
        waitHelper.hardWait(1); // Wait for alert to appear
        return getAlertTextAndAccept();
    }

    /**
     * Verify signup modal is closed
     */
    public boolean isModalClosed() {
        return !isElementDisplayed(SIGNUP_MODAL);
    }
}
