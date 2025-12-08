package com.demoblaze.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

/**
 * HomePage - Page Object for DemoBlaze Home Page
 */
public class HomePage extends BasePage {

    // Locators
    private static final By LOGO = By.id("nava");
    private static final By HOME_LINK = By.linkText("Home");
    private static final By CONTACT_LINK = By.linkText("Contact");
    private static final By ABOUT_US_LINK = By.linkText("About us");
    private static final By CART_LINK = By.id("cartur");
    private static final By LOGIN_LINK = By.id("login2");
    private static final By SIGNUP_LINK = By.id("signin2");
    private static final By LOGOUT_LINK = By.id("logout2");
    private static final By WELCOME_USER = By.id("nameofuser");
    
    // Category links
    private static final By PHONES_CATEGORY = By.linkText("Phones");
    private static final By LAPTOPS_CATEGORY = By.linkText("Laptops");
    private static final By MONITORS_CATEGORY = By.linkText("Monitors");
    
    // Product list
    private static final By PRODUCT_CARDS = By.cssSelector(".card");
    private static final By PRODUCT_NAMES = By.cssSelector(".card-title a");
    private static final By PRODUCT_PRICES = By.cssSelector("h5");
    private static final By PRODUCT_IMAGES = By.cssSelector(".card-img-top");
    
    // Pagination
    private static final By NEXT_BUTTON = By.id("next2");
    private static final By PREVIOUS_BUTTON = By.id("prev2");
    
    // Carousel
    private static final By CAROUSEL = By.id("carouselExampleIndicators");
    private static final By CAROUSEL_NEXT = By.cssSelector(".carousel-control-next");
    private static final By CAROUSEL_PREV = By.cssSelector(".carousel-control-prev");

    @FindBy(id = "nava")
    private WebElement logo;

    @FindBy(id = "login2")
    private WebElement loginLink;

    @FindBy(id = "signin2")
    private WebElement signupLink;

    /**
     * Constructor
     */
    public HomePage(WebDriver driver) {
        super(driver);
    }

    /**
     * Navigate to home page
     */
    public HomePage navigateToHomePage() {
        navigateTo(config.getAppUrl());
        logger.info("Navigated to DemoBlaze home page");
        return this;
    }

    /**
     * Check if page is loaded
     */
    @Override
    public boolean isPageLoaded() {
        try {
            return isElementDisplayed(LOGO) && 
                   isElementDisplayed(PRODUCT_CARDS) &&
                   getPageTitle().contains(config.getProperty("app.title", "STORE"));
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Click on Login link
     */
    public LoginPage clickLogin() {
        logger.info("Clicking Login link");
        click(LOGIN_LINK);
        return new LoginPage(driver);
    }

    /**
     * Click on Sign up link
     */
    public SignUpPage clickSignUp() {
        logger.info("Clicking Sign up link");
        click(SIGNUP_LINK);
        return new SignUpPage(driver);
    }

    /**
     * Click on Cart link
     */
    public CartPage clickCart() {
        logger.info("Clicking Cart link");
        click(CART_LINK);
        return new CartPage(driver);
    }

    /**
     * Click on Contact link
     */
    public ContactPage clickContact() {
        logger.info("Clicking Contact link");
        click(CONTACT_LINK);
        return new ContactPage(driver);
    }

    /**
     * Click on About Us link
     */
    public void clickAboutUs() {
        logger.info("Clicking About Us link");
        click(ABOUT_US_LINK);
    }

    /**
     * Click on Home link
     */
    public HomePage clickHome() {
        logger.info("Clicking Home link");
        click(HOME_LINK);
        return this;
    }

    /**
     * Click on Logout
     */
    public HomePage clickLogout() {
        logger.info("Clicking Logout");
        click(LOGOUT_LINK);
        waitHelper.hardWait(1); // Wait for logout to complete
        return this;
    }

    /**
     * Click on Logo
     */
    public HomePage clickLogo() {
        logger.info("Clicking Logo");
        click(LOGO);
        return this;
    }

    /**
     * Check if user is logged in
     */
    public boolean isUserLoggedIn() {
        return isElementDisplayed(WELCOME_USER);
    }

    /**
     * Get welcome user text
     */
    public String getWelcomeUserText() {
        if (isUserLoggedIn()) {
            return getText(WELCOME_USER);
        }
        return "";
    }

    /**
     * Get logged in username
     */
    public String getLoggedInUsername() {
        String welcomeText = getWelcomeUserText();
        if (welcomeText.startsWith("Welcome ")) {
            return welcomeText.substring(8);
        }
        return "";
    }

    /**
     * Check if Login link is visible
     */
    public boolean isLoginLinkVisible() {
        return isElementDisplayed(LOGIN_LINK);
    }

    /**
     * Check if Signup link is visible
     */
    public boolean isSignupLinkVisible() {
        return isElementDisplayed(SIGNUP_LINK);
    }

    /**
     * Check if Logout link is visible
     */
    public boolean isLogoutLinkVisible() {
        return isElementDisplayed(LOGOUT_LINK);
    }

    /**
     * Filter by Phones category
     */
    public HomePage filterByPhones() {
        logger.info("Filtering by Phones category");
        click(PHONES_CATEGORY);
        waitHelper.hardWait(1); // Wait for products to load
        return this;
    }

    /**
     * Filter by Laptops category
     */
    public HomePage filterByLaptops() {
        logger.info("Filtering by Laptops category");
        click(LAPTOPS_CATEGORY);
        waitHelper.hardWait(1);
        return this;
    }

    /**
     * Filter by Monitors category
     */
    public HomePage filterByMonitors() {
        logger.info("Filtering by Monitors category");
        click(MONITORS_CATEGORY);
        waitHelper.hardWait(1);
        return this;
    }

    /**
     * Filter by category name
     */
    public HomePage filterByCategory(String category) {
        logger.info("Filtering by category: " + category);
        switch (category.toLowerCase()) {
            case "phones":
                return filterByPhones();
            case "laptops":
                return filterByLaptops();
            case "monitors":
                return filterByMonitors();
            default:
                throw new IllegalArgumentException("Invalid category: " + category);
        }
    }

    /**
     * Get all product names
     */
    public List<WebElement> getProductNames() {
        return elementHelper.getElements(PRODUCT_NAMES);
    }

    /**
     * Get product count
     */
    public int getProductCount() {
        return elementHelper.getElementCount(PRODUCT_CARDS);
    }

    /**
     * Click on product by name
     */
    public ProductDetailPage clickProductByName(String productName) {
        logger.info("Clicking on product: " + productName);
        By productLocator = By.linkText(productName);
        click(productLocator);
        return new ProductDetailPage(driver);
    }

    /**
     * Click on product by index
     */
    public ProductDetailPage clickProductByIndex(int index) {
        logger.info("Clicking on product at index: " + index);
        List<WebElement> products = getProductNames();
        if (index >= 0 && index < products.size()) {
            products.get(index).click();
            return new ProductDetailPage(driver);
        }
        throw new IndexOutOfBoundsException("Invalid product index: " + index);
    }

    /**
     * Check if product exists by name
     */
    public boolean isProductDisplayed(String productName) {
        By productLocator = By.linkText(productName);
        return isElementDisplayed(productLocator);
    }

    /**
     * Click Next pagination button
     */
    public HomePage clickNext() {
        logger.info("Clicking Next pagination button");
        click(NEXT_BUTTON);
        waitHelper.hardWait(1);
        return this;
    }

    /**
     * Click Previous pagination button
     */
    public HomePage clickPrevious() {
        logger.info("Clicking Previous pagination button");
        click(PREVIOUS_BUTTON);
        waitHelper.hardWait(1);
        return this;
    }

    /**
     * Check if Next button is enabled
     */
    public boolean isNextButtonEnabled() {
        return isElementEnabled(NEXT_BUTTON);
    }

    /**
     * Check if Previous button is enabled
     */
    public boolean isPreviousButtonEnabled() {
        return isElementEnabled(PREVIOUS_BUTTON);
    }

    /**
     * Check if carousel is displayed
     */
    public boolean isCarouselDisplayed() {
        return isElementDisplayed(CAROUSEL);
    }

    /**
     * Click carousel next button
     */
    public HomePage clickCarouselNext() {
        logger.info("Clicking carousel next button");
        click(CAROUSEL_NEXT);
        return this;
    }

    /**
     * Click carousel previous button
     */
    public HomePage clickCarouselPrevious() {
        logger.info("Clicking carousel previous button");
        click(CAROUSEL_PREV);
        return this;
    }

    /**
     * Get all product cards
     */
    public List<WebElement> getProductCards() {
        return elementHelper.getElements(PRODUCT_CARDS);
    }

    /**
     * Verify at least minimum products are displayed
     */
    public boolean areProductsDisplayed(int minimumCount) {
        int actualCount = getProductCount();
        logger.info("Products displayed: " + actualCount);
        return actualCount >= minimumCount;
    }
}
