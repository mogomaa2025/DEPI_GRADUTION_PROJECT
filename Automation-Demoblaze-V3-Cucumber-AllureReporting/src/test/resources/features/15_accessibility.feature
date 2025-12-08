@accessibility @a11y @playwright
Feature: Accessibility Testing
  Verify application meets accessibility standards

  @TC_A11Y_001
  Scenario: Homepage meets accessibility standards
    Given I am on the homepage
    When I run accessibility checks
    Then there should be no critical accessibility violations
    And all images should have alt text
    And all interactive elements should be keyboard accessible

  @TC_A11Y_002
  Scenario: Forms have proper labels and ARIA attributes
    When I open the signup modal
    Then all form fields should have labels
    And form elements should have proper ARIA attributes
    When I open the login modal
    Then all form fields should have labels
    When I open the contact modal
    Then all form fields should have labels

  @TC_A11Y_003
  Scenario: Keyboard navigation works correctly
    Given I am on the homepage
    When I navigate using Tab key
    Then focus should move through interactive elements in order
    And focus should be visually indicated
    When I press Enter on a product link
    Then product details should open

  @TC_A11Y_004
  Scenario: Color contrast meets WCAG standards
    Given I am on the homepage
    When I check color contrast ratios
    Then all text should have sufficient contrast
    And all interactive elements should meet contrast requirements

  @TC_A11Y_005
  Scenario: Screen reader compatibility
    Given I am using a screen reader
    When I navigate the homepage
    Then all content should be announced properly
    And navigation should be clear and logical
