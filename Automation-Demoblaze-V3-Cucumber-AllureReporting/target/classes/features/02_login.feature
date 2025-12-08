@regression @login
Feature: User Authentication
  As a registered user
  I want to login to my account
  So that I can access personalized features

  Background:
    Given I am on the DemoBlaze homepage

  @smoke @critical @TC_LOGIN_001
  Scenario: Successful login with valid credentials
    Given I have a registered account with username "testuser" and password "TestPass123"
    When I click on the "Log in" link
    And I enter login username "testuser"
    And I enter login password "TestPass123"
    And I click the "Log in" button on login modal
    Then I should be logged in successfully
    And I should see "Welcome testuser" in the navigation bar
    And the "Log out" link should be visible

  @negative @validation @TC_LOGIN_002
  Scenario: Login fails with incorrect password
    Given a user "testuser" exists with password "CorrectPass123"
    When I click on the "Log in" link
    And I enter login username "testuser"
    And I enter login password "WrongPassword"
    And I click the "Log in" button on login modal
    Then I should see an alert with message "Wrong password."
    And I should remain logged out

  @negative @validation @TC_LOGIN_003
  Scenario: Login fails with non-existent username
    When I click on the "Log in" link
    And I enter login username "nonexistentuser999"
    And I enter login password "AnyPassword123"
    And I click the "Log in" button on login modal
    Then I should see an alert with message "User does not exist."

  @negative @validation @TC_LOGIN_004
  Scenario: Login fails with empty username field
    When I click on the "Log in" link
    And I leave the login username field empty
    And I enter login password "Password123"
    And I click the "Log in" button on login modal
    Then I should see an alert with message "Please fill out Username and Password."

  @negative @validation @TC_LOGIN_005
  Scenario: Login fails with empty password field
    When I click on the "Log in" link
    And I enter login username "testuser"
    And I leave the login password field empty
    And I click the "Log in" button on login modal
    Then I should see an alert with message "Please fill out Username and Password."

  @negative @validation @TC_LOGIN_006
  Scenario: Login fails with both fields empty
    When I click on the "Log in" link
    And I leave all login fields empty
    And I click the "Log in" button on login modal
    Then I should see an alert with message "Please fill out Username and Password."

  @ui @modal @TC_LOGIN_007
  Scenario: Close login modal without authentication
    When I click on the "Log in" link
    And the login modal is displayed
    And I click the login "Close" button
    Then the login modal should be closed
    And I should remain on the homepage

  @validation @casesensitive @TC_LOGIN_008
  Scenario: Case-sensitive username validation
    Given a user "TestUser" exists
    When I click on the "Log in" link
    And I enter login username "testuser"
    And I enter login password "CorrectPass123"
    And I click the "Log in" button on login modal
    Then I should see an alert with message "User does not exist."

  @datadriven @TC_LOGIN_009
  Scenario Outline: Login with various credentials
    When I click on the "Log in" link
    And I enter login username "<username>"
    And I enter login password "<password>"
    And I click the "Log in" button on login modal
    Then I should see an alert with message "<expected_message>"

    Examples:
      | username        | password        | expected_message                        |
      | validuser       | ValidPass123    | success                                 |
      | validuser       | WrongPass       | Wrong password.                         |
      | invaliduser     | AnyPass123      | User does not exist.                    |
      |                 | Password123     | Please fill out Username and Password.  |
      | testuser        |                 | Please fill out Username and Password.  |
