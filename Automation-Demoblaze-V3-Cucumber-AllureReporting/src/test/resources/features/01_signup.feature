@regression @signup
Feature: User Registration
  As a new user
  I want to create an account
  So that I can make purchases on the website

  Background:
    Given I am on the DemoBlaze homepage

  @smoke @critical @TC_SIGNUP_001
  Scenario: Successful user registration with valid credentials
    When I click on the "Sign up" link
    And I enter username "testuser_{timestamp}"
    And I enter password "SecurePass123"
    And I click the "Sign up" button
    Then I should see an alert with message "Sign up successful."
    And the user should be created in the system

  @negative @validation @TC_SIGNUP_002
  Scenario: User registration fails with duplicate username
    Given a user "existinguser" already exists
    When I click on the "Sign up" link
    And I enter username "existinguser"
    And I enter password "AnyPassword123"
    And I click the "Sign up" button
    Then I should see an alert with message "This user already exist."
    And the registration should not be completed

  @negative @validation @TC_SIGNUP_003
  Scenario: User registration fails with empty username
    When I click on the "Sign up" link
    And I leave the username field empty
    And I enter password "Password123"
    And I click the "Sign up" button
    Then I should see an alert with message "Please fill out Username and Password."

  @negative @validation @TC_SIGNUP_004
  Scenario: User registration fails with empty password
    When I click on the "Sign up" link
    And I enter username "testuser123"
    And I leave the password field empty
    And I click the "Sign up" button
    Then I should see an alert with message "Please fill out Username and Password."

  @negative @validation @TC_SIGNUP_005
  Scenario: User registration fails with all fields empty
    When I click on the "Sign up" link
    And I leave all fields empty
    And I click the "Sign up" button
    Then I should see an alert with message "Please fill out Username and Password."

  @positive @specialchars @TC_SIGNUP_006
  Scenario: User registration with special characters in credentials
    When I click on the "Sign up" link
    And I enter username "test@user#123_{timestamp}"
    And I enter password "P@ss!w0rd#123"
    And I click the "Sign up" button
    Then I should see an alert with message "Sign up successful."

  @ui @modal @TC_SIGNUP_007
  Scenario: Close signup modal without registering
    When I click on the "Sign up" link
    And the signup modal is displayed
    And I click the "Close" button
    Then the signup modal should be closed
    And I should remain on the homepage

  @datadriven @TC_SIGNUP_008
  Scenario Outline: Register multiple users with different credentials
    When I click on the "Sign up" link
    And I enter username "<username>_{timestamp}"
    And I enter password "<password>"
    And I click the "Sign up" button
    Then I should see an alert with message "<expected_message>"

    Examples:
      | username  | password      | expected_message        |
      | user1     | Pass@123      | Sign up successful.     |
      | user2     | Test!456      | Sign up successful.     |
      | user3     | Secure#789    | Sign up successful.     |
