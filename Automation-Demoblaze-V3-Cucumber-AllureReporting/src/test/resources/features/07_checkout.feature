@regression @order @checkout
Feature: Order Placement
  As a user with products in cart
  I want to place an order
  So that I can complete my purchase

  Background:
    Given I have products in my cart
    And I am on the cart page

  @smoke @critical @TC_ORDER_001
  Scenario: Successfully place order with all details
    When I click the "Place Order" button for checkout
    And I fill in the order form:
      | Field       | Value              |
      | Name        | John Doe           |
      | Country     | USA                |
      | City        | New York           |
      | Credit card | 4532123456789012   |
      | Month       | 12                 |
      | Year        | 2025               |
    And I click the "Purchase" button
    Then I should see order confirmation
    And the confirmation should display purchase ID
    And the confirmation should display amount
    And the confirmation should display card number
    And the confirmation should display name "John Doe"
    And the confirmation should display current date
    And my cart should be empty after order

  @critical @TC_ORDER_002
  Scenario: Place order with minimum required fields
    Given I have products totaling "$360" in my cart
    When I click the "Place Order" button for checkout
    And I enter Name "Jane Smith"
    And I enter Credit card "1234567890123456"
    And I click the "Purchase" button
    Then order should be placed successfully
    And confirmation should show amount "$360"
    And my cart should be cleared

  @validation @negative @TC_ORDER_003
  Scenario: Order fails with empty name field
    When I click the "Place Order" button for checkout
    And I leave the Name field empty
    And I enter Country "USA"
    And I enter City "Boston"
    And I enter Credit card "4532123456789012"
    And I enter Month "12"
    And I enter Year "2025"
    And I click the "Purchase" button
    Then I should see a validation alert
    And the order should not be placed

  @validation @negative @TC_ORDER_004
  Scenario: Order fails with empty credit card field
    When I click the "Place Order" button for checkout
    And I enter Name "John Doe"
    And I enter Country "USA"
    And I enter City "Boston"
    And I leave Credit card field empty
    And I enter Month "12"
    And I enter Year "2025"
    And I click the "Purchase" button
    Then I should see a validation alert
    And the order should not be placed

  @ui @TC_ORDER_005
  Scenario: Close order modal without placing order
    When I click the "Place Order" button for checkout
    And the order modal is displayed
    And I click the "Close" button on order modal
    Then the modal should close
    And I should remain on the cart page
    And my cart should still contain products

  @TC_ORDER_006
  Scenario: Order modal displays all input fields
    When I click the "Place Order" button for checkout
    Then the order modal should display
    And I should see the following fields:
      | Field       |
      | Name        |
      | Country     |
      | City        |
      | Credit card |
      | Month       |
      | Year        |
    And I should see "Purchase" button
    And I should see "Close" button

  @critical @TC_ORDER_007
  Scenario: Order confirmation displays all required details
    When I complete an order with valid details
    Then the confirmation popup should display:
      | Field  | Description                    |
      | Id     | Unique order identification    |
      | Amount | Total purchase amount          |
      | Card   | Credit card number used        |
      | Name   | Customer name                  |
      | Date   | Order date and time            |

  @critical @TC_ORDER_008
  Scenario: Cart is automatically cleared after successful order
    Given my cart contains "Samsung galaxy s6" and "Nokia lumia 1520"
    When I successfully place an order
    And I navigate to the cart page
    Then my cart should be empty
    And the total should be "0"

  @calculation @critical @TC_ORDER_009
  Scenario: Order amount matches cart total exactly
    Given I have "Samsung galaxy s6" ($360) and "Nokia lumia 1520" ($820) in cart
    When I view my cart
    Then the total should be "$1180"
    When I place an order
    Then the order confirmation amount should be "1180"

  @TC_ORDER_010
  Scenario: System accepts various credit card formats
    When I click the "Place Order" button for checkout
    And I enter Name "Test User"
    And I enter Credit card "<card_number>"
    And I click the "Purchase" button
    Then the order should be placed successfully

    Examples:
      | card_number        |
      | 1234567890123456   |
      | 4111-1111-1111-1111|
      | 4111111111111111   |

  @datadriven @TC_ORDER_011
  Scenario Outline: Place orders with different customer details
    When I click the "Place Order" button for checkout
    And I fill in order details:
      | Name      | <name>      |
      | Country   | <country>   |
      | City      | <city>      |
      | Card      | <card>      |
      | Month     | <month>     |
      | Year      | <year>      |
    And I click the "Purchase" button
    Then order should be confirmed
    And confirmation should show name "<name>"

    Examples:
      | name          | country | city      | card             | month | year |
      | Alice Brown   | USA     | Seattle   | 4532123456789012 | 01    | 2026 |
      | Bob Johnson   | Canada  | Toronto   | 5432109876543210 | 06    | 2025 |
      | Carol Smith   | UK      | London    | 6543210987654321 | 12    | 2027 |
