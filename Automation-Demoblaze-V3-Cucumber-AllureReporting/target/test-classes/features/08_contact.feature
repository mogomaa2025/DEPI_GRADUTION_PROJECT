@regression @contact
Feature: Contact Form
  As a user
  I want to send messages to support
  So that I can get help or provide feedback

  Background:
    Given I am on the DemoBlaze homepage

  @smoke @TC_CONTACT_001
  Scenario: Successfully submit contact form with all fields
    When I click on the "Contact" link
    And I enter contact email "test@example.com"
    And I enter contact name "Test User"
    And I enter message "This is a test message"
    And I click the "Send message" button
    Then I should see an alert "Thanks for the message!!"
    And the contact modal should close

  @validation @negative @TC_CONTACT_002
  Scenario: Contact form fails with empty email
    When I click on "Contact"
    And I leave the email field empty
    And I enter contact name "Test User"
    And I enter message "Test message"
    And I click "Send message"
    Then I should see a validation alert

  @validation @negative @TC_CONTACT_003
  Scenario: Contact form fails with empty name
    When I click on "Contact"
    And I enter contact email "test@example.com"
    And I leave the name field empty
    And I enter message "Test message"
    And I click "Send message"
    Then I should see a validation alert

  @validation @negative @TC_CONTACT_004
  Scenario: Contact form fails with empty message
    When I click on "Contact"
    And I enter contact email "test@example.com"
    And I enter contact name "Test User"
    And I leave the message field empty
    And I click "Send message"
    Then I should see a validation alert

  @TC_CONTACT_005
  Scenario: Submit contact form with long message
    When I open the contact form
    And I enter contact email "test@example.com"
    And I enter contact name "Test User"
    And I enter a message with 500 characters
    And I click "Send message"
    Then the message should be sent successfully
    And I should see success confirmation

  @ui @TC_CONTACT_006
  Scenario: Close contact modal without sending message
    When I click on "Contact"
    And the contact modal is displayed
    And I enter some text in the fields
    And I click the contact "Close" button
    Then the contact modal should close
    And I should remain on the homepage

  @TC_CONTACT_007
  Scenario: Contact modal displays all required fields
    When I click on "Contact"
    Then the contact modal should display
    And I should see the following fields:
      | Field         |
      | Contact Email |
      | Contact Name  |
      | Message       |
    And I should see "Send message" button
    And I should see "Close" button

  @datadriven @TC_CONTACT_008
  Scenario Outline: Submit contact form with various inputs
    When I click on "Contact"
    And I enter contact email "<email>"
    And I enter contact name "<name>"
    And I enter message "<message>"
    And I click "Send message"
    Then I should see an alert "Thanks for the message!!"

    Examples:
      | email              | name          | message                    |
      | user1@test.com     | John Doe      | Product inquiry            |
      | support@demo.com   | Jane Smith    | Technical support needed   |
      | feedback@test.com  | Bob Johnson   | Great website!             |
