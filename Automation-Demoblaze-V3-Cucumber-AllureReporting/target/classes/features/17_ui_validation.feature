@ui @validation
Feature: UI Elements Validation
  Verify all UI elements display and function correctly

  @TC_UI_001
  Scenario: Homepage carousel functionality
    Given I am on the homepage
    Then the carousel should be visible
    And carousel images should auto-rotate
    When I click the "Next" carousel button
    Then the carousel should advance to next slide
    When I click the "Previous" carousel button
    Then the carousel should go to previous slide

  @TC_UI_002
  Scenario: Footer displays company information
    Given I am on any page
    Then the footer should be visible
    And footer should display "About Us" information
    And footer should display contact information:
      | Field   | Value                    |
      | Address | 2390 El Camino Real      |
      | Phone   | +440 123456              |
      | Email   | demo@blazemeter.com      |
    And footer should display "PRODUCT STORE" logo

  @TC_UI_003
  Scenario: Footer copyright information
    Given I am on the homepage
    When I scroll to the footer
    Then I should see copyright text "Copyright © Product Store"

  @TC_UI_004
  Scenario: Navigation bar is sticky
    Given I am on the homepage
    When I scroll down the page
    Then the navigation bar should remain visible
    And the navigation bar should be at the top

  @TC_UI_005
  Scenario: Product cards display consistently
    Given I am on the homepage
    Then all product cards should have consistent styling
    And all product cards should display:
      | Element |
      | Image   |
      | Title   |
      | Price   |
    And all images should be same size

  @TC_UI_006
  Scenario: Modal dialogs display correctly
    When I open signup modal
    Then modal should be centered on screen
    And modal should have overlay background
    And overlay should dim the background
    And close button should be visible
    
    When I close modal and open login modal
    Then login modal should display correctly
    
    When I close modal and open contact modal
    Then contact modal should display correctly

  @TC_UI_007
  Scenario: Buttons have hover effects
    Given I am on the homepage
    When I hover over "Sign up" button
    Then the button should show hover effect
    When I hover over product cards
    Then the cards should show hover effect

  @TC_UI_008
  Scenario: Loading states are displayed
    When I perform an action that takes time
    Then a loading indicator should be shown
    And the interface should provide feedback

  @TC_UI_009
  Scenario: Alert messages are properly styled
    When I complete signup successfully
    Then the alert should be centered
    And the alert should have OK button
    And the alert text should be readable
    
    When I trigger an error
    Then the error alert should display clearly

  @TC_UI_010
  Scenario: Category sidebar is visible
    Given I am on the homepage
    Then the categories sidebar should be visible
    And it should display:
      | Category  |
      | CATEGORIES|
      | Phones    |
      | Laptops   |
      | Monitors  |
    And each category should be clickable
