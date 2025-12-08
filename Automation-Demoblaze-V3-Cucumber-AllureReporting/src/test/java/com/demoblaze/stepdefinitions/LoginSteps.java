package com.demoblaze.stepdefinitions;

import com.demoblaze.pages.HomePage;
import com.demoblaze.pages.LoginPage;
import com.demoblaze.utils.DriverManager;
import com.demoblaze.utils.TestDataGenerator;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

/**
 * LoginSteps - Step definitions for Login feature
 */
public class LoginSteps {
    private static final Logger logger = LogManager.getLogger(LoginSteps.class);
    
    private WebDriver driver;
    private LoginPage loginPage;
    private HomePage homePage;
    private String currentUsername;
    private String currentPassword;

    public LoginSteps() {
        this.driver = DriverManager.getDriver();
    }

    @Given("I have a registered account with username {string} and password {string}")
    public void iHaveARegisteredAccountWithUsernameAndPassword(String username, String password) {
        logger.info("User has registered account: " + username);
        // This is a precondition - user should already exist
        this.currentUsername = username;
        this.currentPassword = password;
    }

    @When("I enter login username {string}")
    public void iEnterLoginUsername(String username) {
        logger.info("Entering login username: " + username);
        loginPage = new LoginPage(driver);
        loginPage.enterUsername(username);
        this.currentUsername = username;
    }

    @When("I enter login password {string}")
    public void iEnterLoginPassword(String password) {
        logger.info("Entering login password");
        loginPage = new LoginPage(driver);
        loginPage.enterPassword(password);
        this.currentPassword = password;
    }

    @When("I leave the login username field empty")
    public void iLeaveTheLoginUsernameFieldEmpty() {
        logger.info("Leaving login username field empty");
        loginPage = new LoginPage(driver);
        loginPage.leaveUsernameEmpty();
    }

    @When("I leave the login password field empty")
    public void iLeaveTheLoginPasswordFieldEmpty() {
        logger.info("Leaving login password field empty");
        loginPage = new LoginPage(driver);
        loginPage.leavePasswordEmpty();
    }

    @When("I leave all login fields empty")
    public void iLeaveAllLoginFieldsEmpty() {
        logger.info("Leaving all login fields empty");
        loginPage = new LoginPage(driver);
        loginPage.leaveAllFieldsEmpty();
    }

    @When("I click the {string} button on login modal")
    public void iClickTheButtonOnLoginModal(String buttonText) {
        logger.info("Clicking " + buttonText + " on login modal");
        loginPage = new LoginPage(driver);
        
        if (buttonText.equals("Log in")) {
            loginPage.clickLoginButton();
        } else if (buttonText.equals("Close")) {
            loginPage.clickClose();
        }
    }

    @When("I click the login {string} button")
    public void iClickTheLoginButton(String buttonText) {
        logger.info("Clicking login " + buttonText + " button");
        loginPage = new LoginPage(driver);
        
        if (buttonText.equals("Close")) {
            loginPage.clickClose();
        }
    }

    @When("I login with username {string} and password {string}")
    public void iLoginWithUsernameAndPassword(String username, String password) {
        logger.info("Logging in with username: " + username);
        homePage = new HomePage(driver);
        loginPage = homePage.clickLogin();
        loginPage.login(username, password);
        this.currentUsername = username;
        this.currentPassword = password;
    }

    @When("I login with the same credentials")
    public void iLoginWithTheSameCredentials() {
        logger.info("Logging in with same credentials");
        // Assuming credentials are stored from signup
        SignUpSteps signUpSteps = new SignUpSteps();
        String username = signUpSteps.getGeneratedUsername();
        String password = signUpSteps.getGeneratedPassword();
        
        iLoginWithUsernameAndPassword(username, password);
    }

    @When("I login as {string} with password {string}")
    public void iLoginAsWithPassword(String username, String password) {
        iLoginWithUsernameAndPassword(username, password);
    }

    @Then("I should be logged in successfully")
    public void iShouldBeLoggedInSuccessfully() {
        logger.info("Verifying successful login");
        homePage = new HomePage(driver);
        
        try {
            Thread.sleep(1500); // Wait for login to process
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        Assert.assertTrue(homePage.isUserLoggedIn(), "User should be logged in");
        Assert.assertTrue(homePage.isLogoutLinkVisible(), "Logout link should be visible");
    }

    @Then("I should remain logged out")
    public void iShouldRemainLoggedOut() {
        logger.info("Verifying user remains logged out");
        homePage = new HomePage(driver);
        Assert.assertFalse(homePage.isUserLoggedIn(), "User should remain logged out");
    }

    @Then("the login modal should be displayed")
    public void theLoginModalShouldBeDisplayed() {
        logger.info("Verifying login modal is displayed");
        loginPage = new LoginPage(driver);
        Assert.assertTrue(loginPage.isModalDisplayed(), "Login modal should be displayed");
    }

    @Then("the login modal should be closed")
    public void theLoginModalShouldBeClosed() {
        logger.info("Verifying login modal is closed");
        loginPage = new LoginPage(driver);
        Assert.assertTrue(loginPage.isModalClosed(), "Login modal should be closed");
    }

    @Then("login should be successful")
    public void loginShouldBeSuccessful() {
        iShouldBeLoggedInSuccessfully();
    }

    @Then("login should succeed")
    public void loginShouldSucceed() {
        iShouldBeLoggedInSuccessfully();
    }

    @When("I login with valid credentials")
    public void iLoginWithValidCredentials() {
        logger.info("Logging in with valid credentials");
        String username = TestDataGenerator.generateUsername();
        String password = TestDataGenerator.getDefaultPassword();
        
        // First register the user
        homePage = new HomePage(driver);
        homePage.clickSignUp();
        com.demoblaze.pages.SignUpPage signUpPage = new com.demoblaze.pages.SignUpPage(driver);
        signUpPage.signUpAndGetAlertMessage(username, password);
        
        // Then login
        iLoginWithUsernameAndPassword(username, password);
    }

    @When("I attempt to login")
    public void iAttemptToLogin() {
        logger.info("Attempting to login");
        loginPage = new LoginPage(driver);
        loginPage.clickLoginButton();
        
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
