@security @validation
Feature: Security and Input Validation
  Verify application handles malicious inputs safely

  @TC_SEC_001
  Scenario: SQL injection protection in signup
    Given I am on the signup modal
    When I enter username "admin' OR '1'='1"
    And I enter password "password"
    And I attempt to sign up
    Then the input should be sanitized or rejected
    And no SQL injection should occur

  @TC_SEC_002
  Scenario: XSS protection in input fields
    Given I am on the signup modal
    When I enter username "<script>alert('XSS')</script>"
    And I enter password "password"
    And I attempt to sign up
    Then the script should not execute
    And the input should be escaped or sanitized

  @TC_SEC_003
  Scenario: XSS protection in contact form
    Given I am on the contact form
    When I enter message "<script>alert('XSS')</script>"
    And I fill other required fields
    And I submit the form
    Then the script should not execute
    And the message should be safely handled

  @TC_SEC_004
  Scenario: Long input handling - username
    Given I am on the signup modal
    When I enter username with 1000 characters
    And I enter password "password"
    And I attempt to sign up
    Then the system should handle it gracefully
    And no buffer overflow should occur

  @TC_SEC_005
  Scenario: Special characters in all input fields
    When I test special characters in signup form
    Then the application should handle them safely
    When I test special characters in login form
    Then the application should handle them safely
    When I test special characters in contact form
    Then the application should handle them safely
    When I test special characters in order form
    Then the application should handle them safely

  @TC_SEC_006
  Scenario: Session management after logout
    Given I am logged in as "testuser"
    When I logout
    Then my session should be terminated
    And I should not be able to access logged-in features
    When I use browser back button
    Then I should still be logged out

  @TC_SEC_007
  Scenario: HTML injection in input fields
    When I enter "<b>bold text</b>" in username field
    Then HTML should not be rendered
    And the text should be escaped
