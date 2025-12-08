package com.demoblaze.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * LoginPage - Page Object for Login Modal
 */
public class LoginPage extends BasePage {

    // Locators
    private static final By LOGIN_MODAL = By.id("logInModal");
    private static final By LOGIN_MODAL_TITLE = By.xpath("//h5[@id='logInModalLabel']");
    private static final By USERNAME_INPUT = By.id("loginusername");
    private static final By PASSWORD_INPUT = By.id("loginpassword");
    private static final By LOGIN_BUTTON = By.xpath("//button[text()='Log in']");
    private static final By CLOSE_BUTTON = By.xpath("//div[@id='logInModal']//button[text()='Close']");
    private static final By CLOSE_X_BUTTON = By.xpath("//div[@id='logInModal']//button[@class='close']");

    @FindBy(id = "loginusername")
    private WebElement usernameInput;

    @FindBy(id = "loginpassword")
    private WebElement passwordInput;

    @FindBy(xpath = "//button[text()='Log in']")
    private WebElement loginButton;

    /**
     * Constructor
     */
    public LoginPage(WebDriver driver) {
        super(driver);
        waitForModalToBeVisible();
    }

    /**
     * Wait for modal to be visible
     */
    private void waitForModalToBeVisible() {
        waitHelper.waitForElementVisible(LOGIN_MODAL);
        logger.debug("Login modal is visible");
    }

    /**
     * Check if page is loaded
     */
    @Override
    public boolean isPageLoaded() {
        try {
            return isElementDisplayed(LOGIN_MODAL) && 
                   isElementDisplayed(USERNAME_INPUT) &&
                   isElementDisplayed(PASSWORD_INPUT) &&
                   isElementDisplayed(LOGIN_BUTTON);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Check if modal is displayed
     */
    public boolean isModalDisplayed() {
        return isElementDisplayed(LOGIN_MODAL);
    }

    /**
     * Enter username
     */
    public LoginPage enterUsername(String username) {
        logger.info("Entering username: " + username);
        type(USERNAME_INPUT, username);
        return this;
    }

    /**
     * Enter password
     */
    public LoginPage enterPassword(String password) {
        logger.info("Entering password");
        type(PASSWORD_INPUT, password);
        return this;
    }

    /**
     * Leave username empty
     */
    public LoginPage leaveUsernameEmpty() {
        logger.info("Leaving username field empty");
        elementHelper.clear(USERNAME_INPUT);
        return this;
    }

    /**
     * Leave password empty
     */
    public LoginPage leavePasswordEmpty() {
        logger.info("Leaving password field empty");
        elementHelper.clear(PASSWORD_INPUT);
        return this;
    }

    /**
     * Leave all fields empty
     */
    public LoginPage leaveAllFieldsEmpty() {
        logger.info("Leaving all fields empty");
        elementHelper.clear(USERNAME_INPUT);
        elementHelper.clear(PASSWORD_INPUT);
        return this;
    }

    /**
     * Click Login button
     */
    public HomePage clickLoginButton() {
        logger.info("Clicking Login button");
        click(LOGIN_BUTTON);
        waitHelper.hardWait(1); // Wait for login to process
        return new HomePage(driver);
    }

    /**
     * Login with credentials
     */
    public HomePage login(String username, String password) {
        logger.info("Logging in with username: " + username);
        enterUsername(username);
        enterPassword(password);
        return clickLoginButton();
    }

    /**
     * Attempt login and expect failure
     */
    public void attemptLogin(String username, String password) {
        logger.info("Attempting login with username: " + username);
        enterUsername(username);
        enterPassword(password);
        click(LOGIN_BUTTON);
    }

    /**
     * Click Close button
     */
    public HomePage clickClose() {
        logger.info("Clicking Close button");
        click(CLOSE_BUTTON);
        waitHelper.waitForElementInvisible(LOGIN_MODAL);
        return new HomePage(driver);
    }

    /**
     * Click X (close) button
     */
    public HomePage clickCloseX() {
        logger.info("Clicking X button");
        click(CLOSE_X_BUTTON);
        waitHelper.waitForElementInvisible(LOGIN_MODAL);
        return new HomePage(driver);
    }

    /**
     * Get modal title
     */
    public String getModalTitle() {
        return getText(LOGIN_MODAL_TITLE);
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
     * Check if login button is displayed
     */
    public boolean isLoginButtonDisplayed() {
        return isElementDisplayed(LOGIN_BUTTON);
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
     * Login and handle alert (for failed login)
     */
    public String loginAndGetAlertMessage(String username, String password) {
        attemptLogin(username, password);
        waitHelper.hardWait(1); // Wait for alert to appear
        return getAlertTextAndAccept();
    }

    /**
     * Verify login modal is closed
     */
    public boolean isModalClosed() {
        return !isElementDisplayed(LOGIN_MODAL);
    }
}
