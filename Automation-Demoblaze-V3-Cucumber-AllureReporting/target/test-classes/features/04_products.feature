@regression @products
Feature: Product Browsing
  As a user
  I want to browse products
  So that I can find items to purchase

  Background:
    Given I am on the DemoBlaze homepage

  @smoke @critical @TC_PRODUCT_001
  Scenario: View all products on homepage
    Then I should see a list of products displayed
    And each product should have a name, price, and image
    And I should see at least 9 products on the first page

  @filter @TC_PRODUCT_002
  Scenario Outline: Filter products by category
    When I click on the "<category>" category link
    Then only "<category>" products should be displayed
    And all displayed products should belong to "<category>" category
    And the product count should be greater than 0

    Examples:
      | category |
      | Phones   |
      | Laptops  |
      | Monitors |

  @filter @phones @TC_PRODUCT_003
  Scenario: Filter products by Phones category
    When I click on the "Phones" category link
    Then only phone products should be displayed
    And I should see products like "Samsung galaxy s6"
    And I should see products like "Nokia lumia 1520"

  @filter @laptops @TC_PRODUCT_004
  Scenario: Filter products by Laptops category
    When I click on the "Laptops" category link
    Then only laptop products should be displayed
    And I should see products like "Sony vaio i5"
    And I should see products like "Sony vaio i7"

  @filter @monitors @TC_PRODUCT_005
  Scenario: Filter products by Monitors category
    When I click on the "Monitors" category link
    Then only monitor products should be displayed
    And I should see products like "Apple monitor 24"

  @pagination @TC_PRODUCT_006
  Scenario: Navigate to next page of products
    Given there are more than 9 products available
    When I click the "Next" pagination button
    Then the next set of products should be displayed
    And different products should be shown

  @pagination @TC_PRODUCT_007
  Scenario: Navigate to previous page of products
    Given I am on the second page of products
    When I click the "Previous" pagination button
    Then the previous set of products should be displayed
    And I should see the first page products

  @navigation @TC_PRODUCT_008
  Scenario: Navigate to product details by clicking image
    When I click on a product image
    Then I should be navigated to the product details page
    And the correct product information should be displayed
    And the URL should contain "prod.html"

  @navigation @TC_PRODUCT_009
  Scenario: Navigate to product details by clicking name
    When I click on product name "Samsung galaxy s6"
    Then I should be navigated to the product details page
    And the product name should be "Samsung galaxy s6"
    And the product price should be displayed

  @TC_PRODUCT_010
  Scenario: Product listing displays correct information
    Then each product card should display:
      | Element       |
      | Product Image |
      | Product Name  |
      | Product Price |
      | Link          |
