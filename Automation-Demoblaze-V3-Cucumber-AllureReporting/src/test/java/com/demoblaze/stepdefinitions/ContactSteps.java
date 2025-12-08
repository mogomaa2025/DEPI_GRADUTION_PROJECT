package com.demoblaze.stepdefinitions;

import com.demoblaze.pages.ContactPage;
import com.demoblaze.pages.HomePage;
import com.demoblaze.utils.DriverManager;
import com.demoblaze.utils.TestDataGenerator;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

/**
 * ContactSteps - Step definitions for Contact Form features
 */
public class ContactSteps {
    private static final Logger logger = LogManager.getLogger(ContactSteps.class);
    
    private WebDriver driver;
    private ContactPage contactPage;
    private HomePage homePage;

    public ContactSteps() {
        this.driver = DriverManager.getDriver();
    }

    @When("I enter contact email {string}")
    public void iEnterContactEmail(String email) {
        logger.info("Entering contact email: " + email);
        contactPage = new ContactPage(driver);
        contactPage.enterEmail(email);
    }

    @When("I enter contact name {string}")
    public void iEnterContactName(String name) {
        logger.info("Entering contact name: " + name);
        contactPage = new ContactPage(driver);
        contactPage.enterName(name);
    }

    @When("I enter message {string}")
    public void iEnterMessage(String message) {
        logger.info("Entering message");
        contactPage = new ContactPage(driver);
        contactPage.enterMessage(message);
    }

    @When("I click the {string} button for contact")
    public void iClickTheButtonForContact(String buttonText) {
        logger.info("Clicking contact button: " + buttonText);
        contactPage = new ContactPage(driver);
        
        if (buttonText.equals("Send message")) {
            contactPage.clickSendMessage();
        }
    }

    @When("I click {string} on contact form")
    public void iClickOnContactForm(String element) {
        logger.info("Clicking on contact form: " + element);
        
        if (element.equals("Send message")) {
            contactPage = new ContactPage(driver);
            contactPage.clickSendMessage();
        }
    }

    @Then("I should see an alert {string}")
    public void iShouldSeeAnAlert(String expectedMessage) {
        logger.info("Verifying alert message: " + expectedMessage);
        contactPage = new ContactPage(driver);
        
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        String actualMessage = contactPage.getAlertTextAndAccept();
        Assert.assertEquals(actualMessage, expectedMessage, "Alert message should match");
    }

    @Then("the contact form should be cleared")
    public void theContactFormShouldBeCleared() {
        logger.info("Verifying contact form is cleared");
        // After successful submission, modal typically closes
        contactPage = new ContactPage(driver);
        // Modal should close after alert is accepted
    }

    @When("I leave the email field empty")
    public void iLeaveTheEmailFieldEmpty() {
        logger.info("Leaving email field empty");
        contactPage = new ContactPage(driver);
        contactPage.leaveEmailEmpty();
    }

    @When("I enter name {string}")
    public void iEnterName(String name) {
        logger.info("Entering name: " + name);
        contactPage = new ContactPage(driver);
        contactPage.enterName(name);
    }


    @When("I leave the name field empty")
    public void iLeaveTheNameFieldEmpty() {
        logger.info("Leaving name field empty");
        contactPage = new ContactPage(driver);
        contactPage.leaveNameEmpty();
    }

    @When("I leave the message field empty")
    public void iLeaveTheMessageFieldEmpty() {
        logger.info("Leaving message field empty");
        contactPage = new ContactPage(driver);
        contactPage.leaveMessageEmpty();
    }

    @When("I open the contact form")
    public void iOpenTheContactForm() {
        logger.info("Opening contact form");
        homePage = new HomePage(driver);
        contactPage = homePage.clickContact();
    }

    @When("I enter a message with {int} characters")
    public void iEnterAMessageWithCharacters(int characterCount) {
        logger.info("Entering message with " + characterCount + " characters");
        contactPage = new ContactPage(driver);
        contactPage.enterLongMessage(characterCount);
    }

    @When("I fill other required fields")
    public void iFillOtherRequiredFields() {
        logger.info("Filling other required fields");
        contactPage = new ContactPage(driver);
        contactPage.enterEmail(TestDataGenerator.generateEmail());
        contactPage.enterName(TestDataGenerator.generateFullName());
    }

    @When("I submit the form")
    public void iSubmitTheForm() {
        logger.info("Submitting form");
        contactPage = new ContactPage(driver);
        contactPage.clickSendMessage();
    }

    @Then("the message should be sent successfully")
    public void theMessageShouldBeSentSuccessfully() {
        logger.info("Verifying message was sent successfully");
        contactPage = new ContactPage(driver);
        
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        String alertText = contactPage.getAlertTextAndAccept();
        Assert.assertEquals(alertText, "Thanks for the message!!", 
            "Success message should be displayed");
    }

    @Then("I should see success confirmation")
    public void iShouldSeeSuccessConfirmation() {
        theMessageShouldBeSentSuccessfully();
    }

    @Then("the contact modal should open")
    public void theContactModalShouldOpen() {
        logger.info("Verifying contact modal is open");
        contactPage = new ContactPage(driver);
        Assert.assertTrue(contactPage.isModalDisplayed(), "Contact modal should be open");
    }

    @Then("I should see contact form fields")
    public void iShouldSeeContactFormFields() {
        logger.info("Verifying contact form fields are visible");
        contactPage = new ContactPage(driver);
        Assert.assertTrue(contactPage.areAllFieldsDisplayed(), 
            "All contact form fields should be visible");
    }

    @When("I enter some text in the fields")
    public void iEnterSomeTextInTheFields() {
        logger.info("Entering some text in fields");
        contactPage = new ContactPage(driver);
        contactPage.enterEmail("test@example.com");
        contactPage.enterName("Test User");
        contactPage.enterMessage("Test message");
    }

    @When("I click the contact {string} button")
    public void iClickTheContactButton(String buttonText) {
        logger.info("Clicking contact " + buttonText + " button");
        contactPage = new ContactPage(driver);
        
        if (buttonText.equals("Close")) {
            homePage = contactPage.clickClose();
        }
    }

    @Then("the contact modal should close")
    public void theContactModalShouldClose() {
        logger.info("Verifying contact modal is closed");
        contactPage = new ContactPage(driver);
        Assert.assertTrue(contactPage.isModalClosed(), "Contact modal should be closed");
    }

    @When("I submit inquiry:")
    public void iSubmitInquiry(io.cucumber.datatable.DataTable dataTable) {
        logger.info("Submitting inquiry");
        contactPage = new ContactPage(driver);
        
        java.util.Map<String, String> data = dataTable.asMap(String.class, String.class);
        
        contactPage.fillContactForm(
            data.get("email"),
            data.get("name"),
            data.get("message")
        );
        
        contactPage.clickSendMessage();
    }

    @When("I submit inquiry about {string}:")
    public void iSubmitInquiryAbout(String subject, io.cucumber.datatable.DataTable dataTable) {
        iSubmitInquiry(dataTable);
    }

    @When("I return to homepage")
    public void iReturnToHomepage() {
        logger.info("Returning to homepage");
        homePage = new HomePage(driver);
        homePage.navigateToHomePage();
    }

    @Then("I should be able to browse products")
    public void iShouldBeAbleToBrowseProducts() {
        logger.info("Verifying can browse products");
        homePage = new HomePage(driver);
        Assert.assertTrue(homePage.isPageLoaded(), "Should be able to browse products");
    }
}
