@regression @logout
Feature: User Logout
  As a logged in user
  I want to logout from my account
  So that I can secure my session

  @smoke @critical @TC_LOGOUT_001
  Scenario: User successfully logs out
    Given I am logged in as "testuser"
    When I click on the "Log out" link
    Then I should be logged out successfully
    And the "Log in" and "Sign up" links should be visible
    And the "Welcome" message should not be displayed
    And the "Log out" link should not be visible

  @negative @TC_LOGOUT_002
  Scenario: Logout button not accessible when not logged in
    Given I am on the DemoBlaze homepage
    And I am not logged in
    Then the "Log out" link should not be visible
    And only "Log in" and "Sign up" links should be available
