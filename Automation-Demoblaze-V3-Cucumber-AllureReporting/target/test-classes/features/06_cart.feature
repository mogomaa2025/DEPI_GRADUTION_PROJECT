@regression @cart
Feature: Shopping Cart
  As a user
  I want to manage my shopping cart
  So that I can review and modify my purchases

  Background:
    Given I am on the DemoBlaze homepage

  @smoke @critical @TC_CART_001
  Scenario: View shopping cart with added products
    Given I have added "Samsung galaxy s6" to my cart
    And I have added "Nokia lumia 1520" to my cart
    When I navigate to the cart page
    Then I should see both products in the cart
    And I should see the product names displayed
    And I should see the product prices displayed
    And each product should have a delete option

  @TC_CART_002
  Scenario: View empty shopping cart
    Given I have not added any products to cart
    When I navigate to the cart page
    Then the cart should be empty
    And the total should be "0"
    And no products should be displayed in the table

  @calculation @critical @TC_CART_003
  Scenario: Cart total calculates correctly with multiple products
    Given I have added "Samsung galaxy s6" priced at "$360" to cart
    And I have added "Nokia lumia 1520" priced at "$820" to cart
    When I navigate to the cart page
    Then the total price should be "$1180"
    And the total should be the sum of all product prices

  @delete @TC_CART_004
  Scenario: Remove product from shopping cart
    Given I have "Samsung galaxy s6" and "Nokia lumia 1520" in my cart
    When I navigate to the cart page
    And I click the "Delete" link for "Samsung galaxy s6"
    Then "Samsung galaxy s6" should be removed from cart
    And the total price should be updated
    And only "Nokia lumia 1520" should remain in cart
    And the total should be "$820"

  @persistence @TC_CART_005
  Scenario: Cart maintains products across page navigation
    Given I have added "Samsung galaxy s6" to cart
    When I navigate to the homepage
    And I navigate to a product details page
    And I navigate back to the cart page
    Then "Samsung galaxy s6" should still be in the cart
    And the cart count should be maintained

  @TC_CART_006
  Scenario: Multiple quantities of same product display correctly
    Given I have added "Samsung galaxy s6" to cart twice
    When I navigate to the cart page
    Then I should see "Samsung galaxy s6" listed twice
    And each entry should show the same price "$360"
    And the total should be "$720"

  @checkout @critical @TC_CART_007
  Scenario: Place Order button is visible with products in cart
    Given I have added products to my cart
    When I navigate to the cart page
    Then I should see the "Place Order" button
    And the button should be clickable
    And the button should be enabled

  @TC_CART_008
  Scenario: Cart page displays correct table headers
    When I navigate to the cart page
    Then I should see the following table headers:
      | Header  |
      | Pic     |
      | Title   |
      | Price   |
      | X       |
    And the Total label should be visible

  @datadriven @TC_CART_009
  Scenario Outline: Add multiple different products to cart
    When I add product "<product>" priced at "<price>" to cart
    And I navigate to the cart page
    Then I should see "<product>" in the cart
    And the total should be "<price>"

    Examples:
      | product           | price |
      | Samsung galaxy s6 | $360  |
      | Nokia lumia 1520  | $820  |
      | Sony vaio i5      | $790  |
      | Apple monitor 24  | $400  |
