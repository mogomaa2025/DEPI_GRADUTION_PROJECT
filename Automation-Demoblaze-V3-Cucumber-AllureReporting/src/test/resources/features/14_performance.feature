@performance @playwright
Feature: Performance Testing
  Verify application performance meets standards

  @TC_PERF_001
  Scenario: Page load times are within acceptable limits
    When I navigate to homepage
    Then the page should load within 3 seconds
    And all resources should be loaded
    
    When I navigate to cart page
    Then the page should load within 2 seconds
    
    When I navigate to product details
    Then the page should load within 2 seconds

  @TC_PERF_002
  Scenario: Product images load quickly
    Given I am on the homepage
    When I measure image load times
    Then all product images should load within 2 seconds
    And no broken images should be present

  @TC_PERF_003
  Scenario: Filter operations are fast
    Given I am on the homepage
    When I click on "Phones" filter
    Then products should filter within 1 second
    When I click on "Laptops" filter
    Then products should filter within 1 second

  @TC_PERF_004
  Scenario: Add to cart is responsive
    Given I am on a product details page
    When I click "Add to cart"
    Then the alert should appear within 1 second
    And the operation should complete successfully

  @TC_PERF_005
  Scenario: Checkout process is performant
    Given I have products in cart
    When I initiate checkout
    Then the order modal should open within 1 second
    When I submit the order
    Then confirmation should appear within 3 seconds
