@crossbrowser @compatibility
Feature: Cross-Browser Compatibility
  Verify application works correctly across different browsers

  @smoke @TC_BROWSER_001
  Scenario Outline: Core functionality works across all browsers
    Given I am using browser "<browser>"
    And I am on the DemoBlaze homepage
    
    # Test sign up
    When I sign up with unique credentials
    Then signup should be successful
    
    # Test login
    When I login with the same credentials
    Then login should be successful
    
    # Test add to cart
    When I add a product to cart
    Then product should be added
    
    # Test view cart
    When I view the cart
    Then cart should display product correctly
    
    # Test checkout (without completing)
    When I click "Place Order" on checkout
    Then order modal should open
    
    Examples:
      | browser |
      | Chrome  |
      | Firefox |
      | Edge    |

  @safari @TC_BROWSER_002
  Scenario: Core functionality on Safari
    Given I am using browser "Safari"
    When I perform basic user workflows
    Then all functionality should work correctly

  @responsive @TC_BROWSER_003
  Scenario Outline: Responsive design across different viewports
    Given I set viewport to "<width>"x"<height>"
    When I navigate through the application
    Then the layout should be responsive
    And all elements should be properly displayed

    Examples:
      | width | height | device         |
      | 1920  | 1080   | Desktop        |
      | 1366  | 768    | Laptop         |
      | 768   | 1024   | Tablet         |
      | 375   | 667    | Mobile         |

  @TC_BROWSER_004
  Scenario: Browser back and forward functionality
    Given I am on the homepage
    When I navigate to product details
    And I use browser back button
    Then I should return to homepage
    When I use browser forward button
    Then I should return to product details
