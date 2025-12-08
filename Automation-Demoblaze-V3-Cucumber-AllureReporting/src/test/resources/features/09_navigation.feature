@regression @navigation
Feature: Navigation
  As a user
  I want to navigate the website easily
  So that I can access different sections

  Background:
    Given I am on the DemoBlaze homepage

  @smoke @TC_NAV_001
  Scenario Outline: Navigate using header menu links
    When I click on "<link>" in the navigation bar
    Then I should be on the "<page>" page
    And the page URL should contain "<url_part>"

    Examples:
      | link    | page     | url_part |
      | Home    | Home     | index    |
      | Cart    | Cart     | cart     |

  @TC_NAV_002
  Scenario: Navigate to homepage by clicking logo
    Given I am on the cart page
    When I click on the "PRODUCT STORE" logo
    Then I should be redirected to the homepage
    And the URL should be the base URL

  @browser @TC_NAV_003
  Scenario: Use browser back button for navigation
    When I navigate to a product details page
    And I click the browser back button
    Then I should return to the homepage
    And the product list should be displayed

  @TC_NAV_004
  Scenario: Navigate to Contact modal
    When I click on "Contact" in the navigation bar
    Then the contact modal should open
    And I should see contact form fields

  @TC_NAV_005
  Scenario: Navigate to About Us modal
    When I click on "About us" in the navigation bar
    Then the about modal should open
    And I should see the about video player

  @TC_NAV_006
  Scenario: Category navigation filters products
    When I click on "Phones" category
    Then only phone products should be displayed
    When I click on "Laptops" category
    Then only laptop products should be displayed
    When I click on "Monitors" category
    Then only monitor products should be displayed

  @TC_NAV_007
  Scenario: Navigation bar is visible on all pages
    Then the navigation bar should be visible
    When I navigate to cart page
    Then the navigation bar should be visible
    When I navigate to a product details page
    Then the navigation bar should be visible

  @TC_NAV_008
  Scenario: Logo is clickable from all pages
    When I am on cart page
    Then the logo should be clickable
    When I am on a product details page
    Then the logo should be clickable
