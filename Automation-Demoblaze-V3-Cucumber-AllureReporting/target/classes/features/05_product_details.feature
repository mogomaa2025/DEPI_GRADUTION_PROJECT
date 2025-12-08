@regression @productdetails
Feature: Product Details
  As a user
  I want to view detailed product information
  So that I can make informed purchase decisions

  Background:
    Given I am on the DemoBlaze homepage

  @smoke @critical @TC_DETAIL_001
  Scenario: View complete product details
    When I click on product "Samsung galaxy s6"
    Then I should see the product details page
    And I should see the product name "Samsung galaxy s6"
    And I should see the product price "$360"
    And I should see the product image
    And I should see the product description
    And I should see an "Add to cart" button

  @cart @critical @TC_DETAIL_002
  Scenario: Add product to cart successfully
    When I navigate to product details page for "Samsung galaxy s6"
    And I click the "Add to cart" button
    Then I should see an alert with message "Product added."
    And the product should be added to my cart

  @cart @TC_DETAIL_003
  Scenario: Add same product multiple times to cart
    When I navigate to product details page for "Samsung galaxy s6"
    And I click the "Add to cart" button
    And I accept the alert
    And I click the "Add to cart" button again
    Then I should see another success alert
    And the product should appear twice in cart

  @cart @critical @guest @TC_DETAIL_004
  Scenario: Guest user can add products to cart without login
    Given I am not logged in
    When I navigate to product "Nokia lumia 1520"
    And I click the "Add to cart" button
    Then I should see an alert with message "Product added."
    And the product should be added to my cart
    And I should not be prompted to login

  @TC_DETAIL_005
  Scenario: Product price displays correctly with currency symbol
    When I view product details for "Samsung galaxy s6"
    Then the product price should display "$360"
    And the currency symbol should be visible

  @TC_DETAIL_006
  Scenario: Product description displays complete text
    When I view product details for "Samsung galaxy s6"
    Then the product description should be visible
    And the description should contain relevant product information

  @TC_DETAIL_007
  Scenario: Product image loads correctly on details page
    When I view product details for "Samsung galaxy s6"
    Then the product image should be displayed
    And the image should be loaded properly
    And the image should not be broken

  @datadriven @TC_DETAIL_008
  Scenario Outline: View details for different products
    When I navigate to product details page for "<product>"
    Then I should see the product name "<product>"
    And I should see the product price "<price>"
    And I should see an "Add to cart" button

    Examples:
      | product           | price |
      | Samsung galaxy s6 | $360  |
      | Nokia lumia 1520  | $820  |
      | Nexus 6           | $650  |
      | Samsung galaxy s7 | $800  |
