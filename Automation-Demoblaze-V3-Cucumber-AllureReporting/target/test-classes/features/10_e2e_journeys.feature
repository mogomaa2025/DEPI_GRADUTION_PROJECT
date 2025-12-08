@e2e @endtoend
Feature: End-to-End User Journeys
  Complete user workflows from start to finish

  @critical @smoke @TC_E2E_001
  Scenario: New user completes full purchase journey
    Given I am a new user on the DemoBlaze homepage
    
    # Registration
    When I sign up with username "newuser_{timestamp}" and password "Pass123"
    Then I should see signup success message
    
    # Login
    When I login with the same credentials
    Then I should see "Welcome newuser_{timestamp}" in navigation
    
    # Browse and filter products
    When I filter products by "Phones"
    Then only phone products should be displayed
    
    # Add product to cart
    When I click on product "Samsung galaxy s6"
    And I add the product to cart
    Then I should see "Product added" alert
    
    # View cart
    When I navigate to cart
    Then I should see "Samsung galaxy s6" in cart
    And the total should be "$360"
    
    # Place order
    When I click "Place Order" on checkout
    And I complete the order form with valid details:
      | Name    | John Doe         |
      | Country | USA              |
      | City    | New York         |
      | Card    | 4532123456789012 |
      | Month   | 12               |
      | Year    | 2025             |
    And I submit the order
    Then I should see order confirmation
    And the confirmation amount should be "360"
    And my cart should be empty
    
    # Logout
    When I logout
    Then I should see "Log in" and "Sign up" links

  @critical @regression @TC_E2E_002
  Scenario: Existing user purchases multiple products
    Given I have a registered account "existinguser" with password "ExistPass123"
    
    # Login
    When I login as "existinguser" with password "ExistPass123"
    Then I should be logged in successfully
    
    # Add multiple products from different categories
    When I navigate to "Laptops" category
    And I add "Sony vaio i5" to cart
    Then I should see success alert
    
    When I navigate to "Phones" category
    And I add "Nokia lumia 1520" to cart
    Then I should see success alert
    
    # Verify cart
    When I go to cart
    Then I should see 2 products in cart
    And the total should be "$1610"
    
    # Complete checkout
    When I place an order with:
      | Name    | Country | City   | Card         | Month | Year |
      | John D  | USA     | Boston | 123456789012 | 11    | 2025 |
    Then order should be confirmed
    And cart should be cleared
    
    # Logout
    When I logout
    Then I should be on the homepage

  @guest @TC_E2E_003
  Scenario: Guest user browses and adds products without login
    Given I am on the DemoBlaze homepage
    And I am not logged in
    
    # Browse products
    When I view products on homepage
    Then I should see product listings
    
    # Filter by category
    When I filter by "Monitors"
    Then only monitor products should display
    
    # Add to cart without login
    When I select product "Apple monitor 24"
    And I add to cart
    Then product should be added successfully
    
    # Verify cart
    When I navigate to cart
    Then "Apple monitor 24" should be in cart
    And I should be able to see "Place Order" button
    
    # Complete purchase as guest
    When I place order as guest with valid details
    Then order should be confirmed

  @comparison @TC_E2E_004
  Scenario: User compares products before purchase
    Given I am on the homepage
    
    # View first product details
    When I view details for "Samsung galaxy s6"
    Then I should see price "$360"
    And I should see product specifications
    
    # Navigate back and view second product
    When I go back to homepage
    And I view details for "Samsung galaxy s7"
    Then I should see price "$800"
    And I should see product specifications
    
    # Choose and purchase
    When I add "Samsung galaxy s7" to cart
    And I proceed to checkout
    And I complete the purchase with valid details
    Then order should be successful
    And confirmation should show "Samsung galaxy s7"

  @support @TC_E2E_005
  Scenario: User contacts support about product and then purchases
    Given I am viewing product "Sony vaio i7"
    And I note the product name and price
    
    # Contact support
    When I navigate to Contact form
    And I submit inquiry:
      | email   | customer@example.com           |
      | name    | Customer Name                  |
      | message | Is Sony vaio i7 available?     |
    Then I should see success confirmation
    
    # Continue shopping and purchase
    When I return to homepage
    And I navigate to "Laptops" category
    And I add "Sony vaio i7" to cart
    And I complete checkout process
    Then order should be successful

  @persistence @playwright @TC_E2E_006
  Scenario: Cart persists across browser sessions
    Given I am a guest user
    When I add "Nokia lumia 1520" to cart
    And I verify it's in cart
    And I close the browser
    And I reopen the browser and navigate to the site
    And I go to cart
    Then "Nokia lumia 1520" should still be in cart
    And the cart total should match

  @TC_E2E_007
  Scenario: User registers, shops, and manages cart
    # Register
    Given I am a new user
    When I register with unique credentials
    Then registration should be successful
    
    # Login
    When I login with registered credentials
    Then I should see welcome message
    
    # Add multiple products
    When I add "Samsung galaxy s6" to cart
    And I add "Nokia lumia 1520" to cart
    And I add "Sony vaio i5" to cart
    Then cart should contain 3 products
    
    # Remove one product
    When I go to cart
    And I remove "Nokia lumia 1520"
    Then cart should contain 2 products
    And total should be updated correctly
    
    # Complete purchase
    When I place order
    Then order should be successful
    And cart should be empty

  @multiuser @TC_E2E_008
  Scenario: Multiple users in same session
    # First user
    Given I register and login as "user1_{timestamp}"
    When I add "Samsung galaxy s6" to cart
    And I logout
    
    # Second user
    When I register and login as "user2_{timestamp}"
    Then cart should be empty
    When I add "Nokia lumia 1520" to cart
    And I go to cart
    Then I should see only "Nokia lumia 1520"
    And I should not see "Samsung galaxy s6"
