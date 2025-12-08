@smoke @critical
Feature: Smoke Tests
  Critical path tests that must pass before further testing

  @TC_SMOKE_001
  Scenario: Application is accessible
    When I navigate to the DemoBlaze homepage
    Then the page should load successfully
    And the page title should be "STORE"
    And the main content should be visible

  @TC_SMOKE_002
  Scenario: Critical user flows work end-to-end
    Given I can access the application
    
    # Verify signup works
    When I attempt to sign up with unique credentials
    Then signup should succeed
    
    # Verify login works
    When I login with valid credentials
    Then login should succeed
    
    # Verify product browsing works
    When I browse products
    Then products should display
    
    # Verify add to cart works
    When I add a product to cart
    Then product should be added
    
    # Verify cart works
    When I view cart
    Then cart should display correctly
    
    # Verify checkout opens
    When I click Place Order
    Then checkout modal should open

  @TC_SMOKE_003
  Scenario: All main navigation links work
    Given I am on the homepage
    When I click on "Home"
    Then I should remain on homepage
    When I click on "Cart"
    Then cart page should open
    When I click on "Contact"
    Then contact modal should open
    When I click on "About us"
    Then about modal should open

  @TC_SMOKE_004
  Scenario: Core modals open and close
    Given I am on the homepage
    When I open signup modal
    Then modal should display
    When I close the modal
    Then modal should close
    
    When I open login modal
    Then modal should display
    When I close the modal
    Then modal should close

  @TC_SMOKE_005
  Scenario: Products display with required information
    Given I am on the homepage
    Then at least 9 products should be visible
    And each product should have:
      | Attribute |
      | Image     |
      | Name      |
      | Price     |
    And products should be clickable

  @TC_SMOKE_006
  Scenario: Category filters work
    Given I am on the homepage
    When I click "Phones" category
    Then products should filter to phones
    When I click "Laptops" category
    Then products should filter to laptops
    When I click "Monitors" category
    Then products should filter to monitors

  @TC_SMOKE_007
  Scenario: Add to cart and checkout flow
    Given I am on the homepage
    When I select any product
    And I add it to cart
    Then success message should appear
    When I navigate to cart
    Then the product should be in cart
    When I click Place Order
    Then order form should display
