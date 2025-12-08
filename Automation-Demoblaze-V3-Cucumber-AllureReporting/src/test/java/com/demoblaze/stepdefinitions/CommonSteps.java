package com.demoblaze.stepdefinitions;

import com.demoblaze.pages.*;
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
 * CommonSteps - Common step definitions used across multiple features
 */
public class CommonSteps {
    private static final Logger logger = LogManager.getLogger(CommonSteps.class);
    
    private WebDriver driver;
    private HomePage homePage;
    private String currentUsername;
    private String currentPassword;

    public CommonSteps() {
        this.driver = DriverManager.getDriver();
    }

    @Given("I am on the DemoBlaze homepage")
    public void iAmOnTheDemoBlazeHomepage() {
        logger.info("Navigating to DemoBlaze homepage");
        homePage = new HomePage(driver);
        homePage.navigateToHomePage();
        Assert.assertTrue(homePage.isPageLoaded(), "Homepage not loaded properly");
    }

    @Given("I am not logged in")
    public void iAmNotLoggedIn() {
        logger.info("Verifying user is not logged in");
        homePage = new HomePage(driver);
        if (homePage.isUserLoggedIn()) {
            homePage.clickLogout();
        }
        Assert.assertFalse(homePage.isUserLoggedIn(), "User should not be logged in");
    }

    @Given("I am logged in as {string}")
    public void iAmLoggedInAs(String username) {
        logger.info("Logging in as: " + username);
        homePage = new HomePage(driver);
        homePage.navigateToHomePage(); // Ensure we're on the homepage
        
        if (!homePage.isUserLoggedIn()) {
            // First check if user exists, if not create one
            String password = TestDataGenerator.getDefaultPassword();
            
            // Try to create the user first (ignore if already exists)
            try {
                logger.info("Creating user account: " + username);
                SignUpPage signUpPage = homePage.clickSignUp();
                signUpPage.signUp(username, password);
                
                // Wait for signup completion and handle alert
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
                if (homePage.isAlertPresent()) {
                    String alertText = homePage.getAlertText();
                    homePage.acceptAlert();
                    logger.info("Signup result: " + alertText);
                }
            } catch (Exception e) {
                logger.warn("User creation failed or user already exists: " + e.getMessage());
            }
            
            // Now login with the user
            logger.info("Attempting to login with user: " + username);
            homePage = new HomePage(driver);
            LoginPage loginPage = homePage.clickLogin();
            loginPage.login(username, password);
            
            // Wait for login to complete
            try {
                Thread.sleep(2000); // Give time for login to process
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            
            // Check if login was successful
            homePage = new HomePage(driver);
            if (!homePage.isUserLoggedIn()) {
                // If login failed, check for alert
                if (homePage.isAlertPresent()) {
                    String alertText = homePage.getAlertText();
                    homePage.acceptAlert();
                    logger.error("Login failed with alert: " + alertText);
                    
                    // Try with a different password if "Wrong password"
                    if (alertText.contains("Wrong password")) {
                        logger.info("Trying with alternative password");
                        loginPage = homePage.clickLogin();
                        loginPage.login(username, "password123"); // Try common password
                        try {
                            Thread.sleep(1500);
                        } catch (InterruptedException ie) {
                            Thread.currentThread().interrupt();
                        }
                        
                        if (homePage.isAlertPresent()) {
                            homePage.acceptAlert();
                        }
                    }
                }
            }
        }
        
        // Final verification
        homePage = new HomePage(driver);
        if (!homePage.isUserLoggedIn()) {
            logger.error("Failed to login user: " + username);
            throw new RuntimeException("Unable to login with user: " + username + 
                ". The test requires a pre-existing user or the login functionality is not working.");
        }
        
        logger.info("Successfully logged in as: " + username);
    }

    @When("I click on the {string} link")
    public void iClickOnTheLink(String linkText) {
        logger.info("Clicking on link: " + linkText);
        homePage = new HomePage(driver);
        
        switch (linkText) {
            case "Sign up":
                homePage.clickSignUp();
                break;
            case "Log in":
                homePage.clickLogin();
                break;
            case "Log out":
                homePage.clickLogout();
                break;
            case "Contact":
                homePage.clickContact();
                break;
            case "About us":
                homePage.clickAboutUs();
                break;
            case "Cart":
                homePage.clickCart();
                break;
            case "Home":
                homePage.clickHome();
                break;
            default:
                throw new IllegalArgumentException("Unknown link: " + linkText);
        }
    }

    @When("I click on {string} in the navigation bar")
    public void iClickOnInTheNavigationBar(String linkText) {
        iClickOnTheLink(linkText);
    }

    @Then("I should be on the homepage")
    public void iShouldBeOnTheHomepage() {
        logger.info("Verifying user is on homepage");
        homePage = new HomePage(driver);
        Assert.assertTrue(homePage.isPageLoaded(), "Should be on homepage");
    }

    @Then("I should remain on the homepage")
    public void iShouldRemainOnTheHomepage() {
        iShouldBeOnTheHomepage();
    }

    @Then("the {string} link should be visible")
    public void theLinkShouldBeVisible(String linkText) {
        logger.info("Verifying link is visible: " + linkText);
        homePage = new HomePage(driver);
        
        boolean isVisible = false;
        switch (linkText) {
            case "Log in":
                isVisible = homePage.isLoginLinkVisible();
                break;
            case "Sign up":
                isVisible = homePage.isSignupLinkVisible();
                break;
            case "Log out":
                isVisible = homePage.isLogoutLinkVisible();
                break;
        }
        
        Assert.assertTrue(isVisible, linkText + " should be visible");
    }

    @Then("the {string} link should not be visible")
    public void theLinkShouldNotBeVisible(String linkText) {
        logger.info("Verifying link is not visible: " + linkText);
        homePage = new HomePage(driver);
        
        boolean isVisible = true;
        switch (linkText) {
            case "Log out":
                isVisible = homePage.isLogoutLinkVisible();
                break;
        }
        
        Assert.assertFalse(isVisible, linkText + " should not be visible");
    }

    @Then("the {string} and {string} links should be visible")
    public void theAndLinksShouldBeVisible(String link1, String link2) {
        theLinkShouldBeVisible(link1);
        theLinkShouldBeVisible(link2);
    }

    @Then("I should see {string} in the navigation bar")
    public void iShouldSeeInTheNavigationBar(String text) {
        logger.info("Verifying text in navigation bar: " + text);
        homePage = new HomePage(driver);
        String welcomeText = homePage.getWelcomeUserText();
        Assert.assertTrue(welcomeText.contains(text), 
            "Navigation bar should contain: " + text + ", but found: " + welcomeText);
    }

    @Then("the {string} message should not be displayed")
    public void theMessageShouldNotBeDisplayed(String message) {
        logger.info("Verifying message is not displayed: " + message);
        homePage = new HomePage(driver);
        
        if (message.equals("Welcome")) {
            Assert.assertFalse(homePage.isUserLoggedIn(), "Welcome message should not be displayed");
        }
    }

    @Then("I should see an alert with message {string}")
    public void iShouldSeeAnAlertWithMessage(String expectedMessage) {
        logger.info("Verifying alert message: " + expectedMessage);
        homePage = new HomePage(driver);
        String actualMessage = homePage.getAlertText();
        homePage.acceptAlert();
        Assert.assertEquals(actualMessage, expectedMessage, "Alert message mismatch");
    }

    @Then("I accept the alert")
    public void iAcceptTheAlert() {
        logger.info("Accepting alert");
        homePage = new HomePage(driver);
        homePage.acceptAlert();
    }

    @Then("the page URL should contain {string}")
    public void thePageUrlShouldContain(String urlPart) {
        logger.info("Verifying URL contains: " + urlPart);
        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains(urlPart), 
            "URL should contain: " + urlPart + ", but found: " + currentUrl);
    }

    @Then("I should be on the {string} page")
    public void iShouldBeOnThePage(String pageName) {
        logger.info("Verifying on page: " + pageName);
        String currentUrl = driver.getCurrentUrl();
        
        switch (pageName.toLowerCase()) {
            case "home":
                Assert.assertTrue(currentUrl.contains("index.html"), "Should be on home page");
                break;
            case "cart":
                Assert.assertTrue(currentUrl.contains("cart.html"), "Should be on cart page");
                break;
            default:
                throw new IllegalArgumentException("Unknown page: " + pageName);
        }
    }

    // Helper methods for storing test data
    public void setCurrentUsername(String username) {
        this.currentUsername = username;
    }

    public String getCurrentUsername() {
        return this.currentUsername;
    }

    public void setCurrentPassword(String password) {
        this.currentPassword = password;
    }

    public String getCurrentPassword() {
        return this.currentPassword;
    }
}
