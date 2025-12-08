@visual @playwright
Feature: Visual Regression Testing
  Verify visual consistency across releases

  @TC_VISUAL_001
  Scenario: Homepage displays correctly
    Given I am on the homepage
    When I take a screenshot of the page
    Then the screenshot should match the baseline
    And no visual regressions should be detected

  @TC_VISUAL_002
  Scenario: Product details page visual consistency
    Given I am on product details page for "Samsung galaxy s6"
    When I take a screenshot
    Then the page should match visual baseline
    And product image should be displayed correctly

  @TC_VISUAL_003
  Scenario: Cart page visual consistency
    Given I have products in cart
    When I navigate to cart page
    And I take a screenshot
    Then the cart layout should match baseline

  @TC_VISUAL_004
  Scenario: Modal dialogs visual consistency
    When I open signup modal
    And I take modal screenshot
    Then modal should match baseline
    
    When I close modal and open login modal
    And I take modal screenshot
    Then modal should match baseline

  @TC_VISUAL_005
  Scenario: Category filter visual feedback
    When I click on "Phones" category
    Then the category should be visually highlighted
    And product list should update visually
