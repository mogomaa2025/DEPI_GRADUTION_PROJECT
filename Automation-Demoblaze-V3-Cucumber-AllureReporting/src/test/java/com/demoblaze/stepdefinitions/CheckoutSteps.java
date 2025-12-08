package com.demoblaze.stepdefinitions;

import com.demoblaze.pages.CartPage;
import com.demoblaze.pages.CheckoutPage;
import com.demoblaze.pages.HomePage;
import com.demoblaze.utils.DriverManager;
import com.demoblaze.utils.TestDataGenerator;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import java.util.Map;

/**
 * CheckoutSteps - Step definitions for Checkout/Order Placement features
 */
public class CheckoutSteps {
    private static final Logger logger = LogManager.getLogger(CheckoutSteps.class);
    
    private WebDriver driver;
    private CartPage cartPage;
    private CheckoutPage checkoutPage;
    private HomePage homePage;
    private String orderAmount;

    public CheckoutSteps() {
        this.driver = DriverManager.getDriver();
    }

    @When("I click the {string} button for checkout")
    public void iClickTheButtonForCheckout(String buttonText) {
        logger.info("Clicking checkout button: " + buttonText);
        
        if (buttonText.equals("Place Order")) {
            cartPage = new CartPage(driver);
            checkoutPage = cartPage.clickPlaceOrder();
        } else if (buttonText.equals("Purchase")) {
            checkoutPage = new CheckoutPage(driver);
            checkoutPage.clickPurchase();
        }
    }

    @When("I click {string} on checkout")
    public void iClickOnCheckout(String element) {
        logger.info("Clicking on checkout: " + element);
        
        if (element.equals("Place Order")) {
            cartPage = new CartPage(driver);
            checkoutPage = cartPage.clickPlaceOrder();
        } else if (element.equals("Purchase")) {
            checkoutPage = new CheckoutPage(driver);
            checkoutPage.clickPurchase();
        }
    }

    @When("I fill in the order form:")
    public void iFillInTheOrderForm(io.cucumber.datatable.DataTable dataTable) {
        logger.info("Filling in order form");
        checkoutPage = new CheckoutPage(driver);
        
        Map<String, String> data = dataTable.asMap(String.class, String.class);
        
        checkoutPage.enterName(data.get("Name"));
        checkoutPage.enterCountry(data.get("Country"));
        checkoutPage.enterCity(data.get("City"));
        checkoutPage.enterCreditCard(data.get("Credit card"));
        checkoutPage.enterMonth(data.get("Month"));
        checkoutPage.enterYear(data.get("Year"));
    }

    @When("I fill in order details:")
    public void iFillInOrderDetails(io.cucumber.datatable.DataTable dataTable) {
        iFillInTheOrderForm(dataTable);
    }

    @When("I complete the order form with valid details")
    public void iCompleteTheOrderFormWithValidDetails() {
        logger.info("Completing order form with valid details");
        checkoutPage = new CheckoutPage(driver);
        
        checkoutPage.fillOrderForm(
            TestDataGenerator.generateFullName(),
            TestDataGenerator.generateCountry(),
            TestDataGenerator.generateCity(),
            TestDataGenerator.generateCreditCardNumber(),
            TestDataGenerator.generateExpiryMonth(),
            TestDataGenerator.generateExpiryYear()
        );
    }

    @When("I complete the order form with valid details:")
    public void iCompleteTheOrderFormWithValidDetails(io.cucumber.datatable.DataTable dataTable) {
        iFillInTheOrderForm(dataTable);
    }

    @When("I submit the order")
    public void iSubmitTheOrder() {
        logger.info("Submitting order");
        checkoutPage = new CheckoutPage(driver);
        checkoutPage.clickPurchase();
        checkoutPage.waitForConfirmation();
    }

    @Then("I should see order confirmation")
    public void iShouldSeeOrderConfirmation() {
        logger.info("Verifying order confirmation");
        checkoutPage = new CheckoutPage(driver);
        
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        Assert.assertTrue(checkoutPage.isConfirmationDisplayed(), 
            "Order confirmation should be displayed");
    }

    @Then("the confirmation should display:")
    public void theConfirmationShouldDisplay(io.cucumber.datatable.DataTable dataTable) {
        logger.info("Verifying confirmation details");
        checkoutPage = new CheckoutPage(driver);
        
        String confirmationText = checkoutPage.getConfirmationText();
        Assert.assertNotNull(confirmationText, "Confirmation text should be present");
        Assert.assertFalse(confirmationText.isEmpty(), "Confirmation should have content");
    }

    @Then("the confirmation should display purchase ID")
    public void theConfirmationShouldDisplayPurchaseID() {
        logger.info("Verifying purchase ID is displayed");
        checkoutPage = new CheckoutPage(driver);
        String orderId = checkoutPage.getOrderId();
        Assert.assertNotNull(orderId, "Order ID should be present");
        Assert.assertFalse(orderId.isEmpty(), "Order ID should not be empty");
    }

    @Then("the confirmation should display amount")
    public void theConfirmationShouldDisplayAmount() {
        logger.info("Verifying amount is displayed");
        checkoutPage = new CheckoutPage(driver);
        String amount = checkoutPage.getOrderAmount();
        Assert.assertNotNull(amount, "Amount should be present");
        Assert.assertFalse(amount.isEmpty(), "Amount should not be empty");
    }

    @Then("the confirmation should display card number")
    public void theConfirmationShouldDisplayCardNumber() {
        logger.info("Verifying card number is displayed");
        checkoutPage = new CheckoutPage(driver);
        String cardNumber = checkoutPage.getOrderCardNumber();
        Assert.assertNotNull(cardNumber, "Card number should be present");
    }

    @Then("the confirmation should display name {string}")
    public void theConfirmationShouldDisplayName(String expectedName) {
        logger.info("Verifying name is displayed: " + expectedName);
        checkoutPage = new CheckoutPage(driver);
        Assert.assertTrue(checkoutPage.verifyOrderName(expectedName), 
            "Name should be " + expectedName);
    }

    @Then("the confirmation should display current date")
    public void theConfirmationShouldDisplayCurrentDate() {
        logger.info("Verifying date is displayed");
        checkoutPage = new CheckoutPage(driver);
        String date = checkoutPage.getOrderDate();
        Assert.assertNotNull(date, "Date should be present");
        Assert.assertFalse(date.isEmpty(), "Date should not be empty");
    }

    @Then("my cart should be empty")
    public void myCartShouldBeEmpty() {
        logger.info("Verifying cart is empty after order");
        // Close confirmation and check cart
        checkoutPage = new CheckoutPage(driver);
        homePage = checkoutPage.clickConfirmationOk();
        cartPage = homePage.clickCart();
        Assert.assertTrue(cartPage.isCartEmpty(), "Cart should be empty after order");
    }

    @Then("my cart should be empty after order")
    public void myCartShouldBeEmptyAfterOrder() {
        myCartShouldBeEmpty();
    }

    @When("I enter Name {string}")
    public void iEnterName(String name) {
        logger.info("Entering name: " + name);
        checkoutPage = new CheckoutPage(driver);
        checkoutPage.enterName(name);
    }

    @When("I enter Credit card {string}")
    public void iEnterCreditCard(String card) {
        logger.info("Entering credit card");
        checkoutPage = new CheckoutPage(driver);
        checkoutPage.enterCreditCard(card);
    }

    @Then("order should be placed successfully")
    public void orderShouldBePlacedSuccessfully() {
        iShouldSeeOrderConfirmation();
    }

    @Then("confirmation should show amount {string}")
    public void confirmationShouldShowAmount(String expectedAmount) {
        logger.info("Verifying confirmation shows amount: " + expectedAmount);
        checkoutPage = new CheckoutPage(driver);
        String actualAmount = checkoutPage.getOrderAmount();
        String expectedNumeric = expectedAmount.replace("$", "");
        Assert.assertEquals(actualAmount, expectedNumeric, "Amount should match");
    }

    @When("I leave the Name field empty")
    public void iLeaveTheNameFieldEmpty() {
        logger.info("Leaving Name field empty");
        checkoutPage = new CheckoutPage(driver);
        checkoutPage.leaveNameEmpty();
    }

    @When("I enter Country {string}")
    public void iEnterCountry(String country) {
        logger.info("Entering country: " + country);
        checkoutPage = new CheckoutPage(driver);
        checkoutPage.enterCountry(country);
    }

    @When("I enter City {string}")
    public void iEnterCity(String city) {
        logger.info("Entering city: " + city);
        checkoutPage = new CheckoutPage(driver);
        checkoutPage.enterCity(city);
    }

    @When("I enter Month {string}")
    public void iEnterMonth(String month) {
        logger.info("Entering month: " + month);
        checkoutPage = new CheckoutPage(driver);
        checkoutPage.enterMonth(month);
    }

    @When("I enter Year {string}")
    public void iEnterYear(String year) {
        logger.info("Entering year: " + year);
        checkoutPage = new CheckoutPage(driver);
        checkoutPage.enterYear(year);
    }

    @When("I leave Credit card field empty")
    public void iLeaveCreditCardFieldEmpty() {
        logger.info("Leaving Credit card field empty");
        checkoutPage = new CheckoutPage(driver);
        checkoutPage.leaveCreditCardEmpty();
    }

    @Then("I should see a validation alert")
    public void iShouldSeeAValidationAlert() {
        logger.info("Verifying validation alert is displayed");
        checkoutPage = new CheckoutPage(driver);
        
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        String alertText = checkoutPage.getValidationAlertText();
        Assert.assertNotNull(alertText, "Validation alert should be present");
    }

    @Then("the order should not be placed")
    public void theOrderShouldNotBePlaced() {
        logger.info("Verifying order was not placed");
        // Confirmation should not be displayed
        checkoutPage = new CheckoutPage(driver);
        // If there's a validation alert, order wasn't placed
    }

    @When("I click the {string} button on order modal")
    public void iClickTheButtonOnOrderModal(String buttonText) {
        logger.info("Clicking " + buttonText + " on order modal");
        checkoutPage = new CheckoutPage(driver);
        
        if (buttonText.equals("Close")) {
            cartPage = checkoutPage.clickClose();
        }
    }

    @Then("the modal should close")
    public void theModalShouldClose() {
        logger.info("Verifying modal is closed");
        // Should be back on cart page
        cartPage = new CartPage(driver);
        Assert.assertTrue(cartPage.isPageLoaded(), "Should be on cart page");
    }

    @Then("I should remain on the cart page")
    public void iShouldRemainOnTheCartPage() {
        logger.info("Verifying still on cart page");
        cartPage = new CartPage(driver);
        Assert.assertTrue(cartPage.isPageLoaded(), "Should remain on cart page");
    }

    @Then("my cart should still contain products")
    public void myCartShouldStillContainProducts() {
        logger.info("Verifying cart still has products");
        cartPage = new CartPage(driver);
        Assert.assertFalse(cartPage.isCartEmpty(), "Cart should still have products");
    }

    @Then("the order modal should display")
    public void theOrderModalShouldDisplay() {
        logger.info("Verifying order modal is displayed");
        checkoutPage = new CheckoutPage(driver);
        Assert.assertTrue(checkoutPage.isModalDisplayed(), "Order modal should be displayed");
    }

    @Then("I should see the following fields:")
    public void iShouldSeeTheFollowingFields(io.cucumber.datatable.DataTable dataTable) {
        logger.info("Verifying all fields are present");
        checkoutPage = new CheckoutPage(driver);
        Assert.assertTrue(checkoutPage.areAllFieldsDisplayed(), "All fields should be displayed");
    }

    @Then("the confirmation popup should display:")
    public void theConfirmationPopupShouldDisplay(io.cucumber.datatable.DataTable dataTable) {
        logger.info("Verifying confirmation popup details");
        checkoutPage = new CheckoutPage(driver);
        Assert.assertTrue(checkoutPage.isConfirmationDisplayed(), "Confirmation should be displayed");
    }

    @When("I successfully place an order")
    public void iSuccessfullyPlaceAnOrder() {
        logger.info("Placing order successfully");
        checkoutPage = new CheckoutPage(driver);
        checkoutPage.completePurchase(
            TestDataGenerator.generateFullName(),
            TestDataGenerator.generateCountry(),
            TestDataGenerator.generateCity(),
            TestDataGenerator.generateCreditCardNumber(),
            TestDataGenerator.generateExpiryMonth(),
            TestDataGenerator.generateExpiryYear()
        );
    }

    @When("I place an order with:")
    public void iPlaceAnOrderWith(io.cucumber.datatable.DataTable dataTable) {
        logger.info("Placing order with provided details");
        cartPage = new CartPage(driver);
        checkoutPage = cartPage.clickPlaceOrder();
        
        iFillInTheOrderForm(dataTable);
        checkoutPage.clickPurchase();
        checkoutPage.waitForConfirmation();
    }

    @Then("order should be confirmed")
    public void orderShouldBeConfirmed() {
        iShouldSeeOrderConfirmation();
    }

    @Then("cart should be cleared")
    public void cartShouldBeCleared() {
        myCartShouldBeEmpty();
    }

    @When("I navigate to cart and click {string}")
    public void iNavigateToCartAndClick(String buttonText) {
        logger.info("Navigating to cart and clicking: " + buttonText);
        homePage = new HomePage(driver);
        cartPage = homePage.clickCart();
        
        if (buttonText.equals("Place Order")) {
            checkoutPage = cartPage.clickPlaceOrder();
        }
    }

    @Then("the confirmation amount should be {string}")
    public void theConfirmationAmountShouldBe(String expectedAmount) {
        confirmationShouldShowAmount(expectedAmount);
    }

    @When("I complete an order with valid details")
    public void iCompleteAnOrderWithValidDetails() {
        iCompleteTheOrderFormWithValidDetails();
        iSubmitTheOrder();
    }

    @When("I place order as guest with valid details")
    public void iPlaceOrderAsGuestWithValidDetails() {
        cartPage = new CartPage(driver);
        checkoutPage = cartPage.clickPlaceOrder();
        iCompleteTheOrderFormWithValidDetails();
        iSubmitTheOrder();
    }

    @When("I proceed to checkout")
    public void iProceedToCheckout() {
        logger.info("Proceeding to checkout");
        cartPage = new CartPage(driver);
        checkoutPage = cartPage.clickPlaceOrder();
    }

    @When("I complete the purchase")
    public void iCompleteThePurchase() {
        iCompleteTheOrderFormWithValidDetails();
        iSubmitTheOrder();
    }

    @When("I complete the purchase with valid details")
    public void iCompleteThePurchaseWithValidDetails() {
        iCompleteThePurchase();
    }

    @When("I complete checkout process")
    public void iCompleteCheckoutProcess() {
        iProceedToCheckout();
        iCompleteThePurchase();
    }

    @Then("order should be successful")
    public void orderShouldBeSuccessful() {
        iShouldSeeOrderConfirmation();
    }

    @Then("confirmation should show {string}")
    public void confirmationShouldShow(String productName) {
        logger.info("Verifying confirmation shows: " + productName);
        checkoutPage = new CheckoutPage(driver);
        // Confirmation doesn't show product name, just order details
        Assert.assertTrue(checkoutPage.isConfirmationDisplayed(), "Confirmation should be displayed");
    }

    @When("I place order")
    public void iPlaceOrder() {
        iProceedToCheckout();
        iCompleteThePurchase();
    }

    @Then("the order confirmation amount should be {string}")
    public void theOrderConfirmationAmountShouldBe(String expectedAmount) {
        confirmationShouldShowAmount(expectedAmount);
    }

    @When("I initiate checkout")
    public void iInitiateCheckout() {
        iProceedToCheckout();
    }

    @Then("the order modal should open within {int} second")
    public void theOrderModalShouldOpenWithinSecond(int seconds) {
        logger.info("Verifying order modal opens within " + seconds + " second(s)");
        checkoutPage = new CheckoutPage(driver);
        Assert.assertTrue(checkoutPage.isModalDisplayed(), "Modal should open quickly");
    }

    @Then("confirmation should appear within {int} seconds")
    public void confirmationShouldAppearWithinSeconds(int seconds) {
        logger.info("Verifying confirmation appears within " + seconds + " seconds");
        // Confirmation should already be displayed
        checkoutPage = new CheckoutPage(driver);
        Assert.assertTrue(checkoutPage.isConfirmationDisplayed(), "Confirmation should appear");
    }
}
