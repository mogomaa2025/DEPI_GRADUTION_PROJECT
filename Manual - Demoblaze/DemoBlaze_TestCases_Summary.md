# DemoBlaze E-Commerce Website - Test Cases Summary

## Project Information
**Website:** https://www.demoblaze.com/  
**Date Created:** January 2025  
**Test Design Pattern:** Page Object Model (POM)  
**Testing Approach:** Black Box Testing  
**Total Test Cases:** 86

---

## Test Case Distribution by Page

### 1. Sign Up Page (TC_SIGNUP_001 to TC_SIGNUP_010)
**Total Test Cases:** 10
- Valid registration scenarios: 1
- Invalid/Boundary scenarios: 7
- UI interaction tests: 2

**Key Test Areas:**
- Valid user registration
- Duplicate username validation
- Empty field validations
- Special characters handling
- Password length validation
- Username length validation
- Modal close functionality

---

### 2. Login Page (TC_LOGIN_001 to TC_LOGIN_010)
**Total Test Cases:** 10
- Valid login scenarios: 1
- Invalid/Security scenarios: 8
- Session management: 1

**Key Test Areas:**
- Successful login with valid credentials
- Invalid username/password handling
- Empty field validations
- SQL injection prevention
- Logout functionality
- Modal close functionality

---

### 3. Contact Page (TC_CONTACT_001 to TC_CONTACT_010)
**Total Test Cases:** 10
- Valid submission scenarios: 1
- Invalid/Boundary scenarios: 7
- UI interaction tests: 2

**Key Test Areas:**
- Valid contact form submission
- Email format validation
- Empty field validations
- Special characters in message
- Long message handling
- Modal close functionality

---

### 4. About Us Page (TC_ABOUTUS_001 to TC_ABOUTUS_004)
**Total Test Cases:** 4
- Modal display tests: 1
- Video player tests: 1
- UI interaction tests: 2

**Key Test Areas:**
- Modal opening and display
- Video player functionality
- Play/pause controls
- Modal close functionality

---

### 5. Home Page (TC_HOME_001 to TC_HOME_010)
**Total Test Cases:** 10
- Page load tests: 1
- Category filter tests: 4
- Pagination tests: 2
- Carousel tests: 2
- Navigation tests: 1

**Key Test Areas:**
- Home page load and display
- Product category filtering (Phones, Laptops, Monitors)
- Product pagination (Next/Previous)
- Carousel/slider navigation
- Navigation menu verification

---

### 6. Product Detail Page (TC_PRODUCT_001 to TC_PRODUCT_008)
**Total Test Cases:** 8
- Navigation tests: 2
- Add to cart tests: 2
- Display validation tests: 4

**Key Test Areas:**
- Product details display
- Navigation to product page
- Add to cart functionality
- Price display format
- Product description content
- Image carousel functionality

---

### 7. Cart Page (TC_CART_001 to TC_CART_008)
**Total Test Cases:** 8
- Cart display tests: 2
- Cart operations tests: 4
- Navigation tests: 2

**Key Test Areas:**
- Empty cart display
- Cart with products display
- Remove product from cart
- Cart total calculations (single/multiple products)
- Navigate to Place Order
- Empty cart order prevention
- Navigation functionality

---

### 8. Place Order Page (TC_PLACEORDER_001 to TC_PLACEORDER_016)
**Total Test Cases:** 16
- Valid order scenarios: 1
- Field validation tests: 11
- Edge case tests: 2
- UI interaction tests: 2

**Key Test Areas:**
- Successful order placement
- Name field validation
- Credit card field validation
- Optional fields handling (Country, City, Month, Year)
- Empty field validations
- Invalid credit card format
- Special characters in name
- Invalid month/year values
- Past year validation
- Order confirmation details
- Cart emptying after order
- Modal close functionality

---

## Test Case Breakdown by Type

### Valid/Positive Test Cases: 10
- TC_SIGNUP_001, TC_LOGIN_001, TC_CONTACT_001
- TC_ABOUTUS_001, TC_ABOUTUS_002, TC_HOME_001
- TC_PRODUCT_001, TC_CART_002, TC_PLACEORDER_001
- TC_PLACEORDER_015

### Invalid/Negative Test Cases: 50
- Empty field validations: 20
- Invalid format/data: 15
- Boundary value tests: 10
- Security tests: 5

### UI/Functional Test Cases: 26
- Navigation tests: 10
- Modal operations: 12
- Display validations: 4

---

## Black Box Testing Techniques Applied

### 1. Equivalence Partitioning
- Valid username/password combinations
- Invalid username/password combinations
- Email format validation (valid/invalid)
- Credit card number formats

### 2. Boundary Value Analysis
- Minimum password length (single character)
- Maximum username length (50+ characters)
- Maximum message length (1000+ characters)
- Invalid month values (0, 13)
- Past year values

### 3. Error Guessing
- SQL injection attempts in login
- Special characters in input fields
- Duplicate username registration
- Empty cart order placement

### 4. Decision Table Testing
- Login scenarios (username exists/password correct combinations)
- Form validation (multiple required fields)
- Cart operations (empty/with products)

### 5. State Transition Testing
- User authentication states (logged out → logged in → logged out)
- Cart states (empty → products added → order placed → empty)
- Modal states (closed → open → closed)

---

## Test Data Categories

### Username Test Data
- Valid: testuser123, newuser456
- Invalid: existinguser (duplicate), (empty)
- Boundary: verylongusernametestingmaximumlengthacceptance123456789
- Special: test@user#123

### Password Test Data
- Valid: Test@123, Pass@456
- Invalid: (empty)
- Boundary: a (single character)
- Special: Test!@#$%^&*()

### Email Test Data
- Valid: test@email.com, valid@domain.com
- Invalid: invalidemail, user@, @domain.com
- Boundary: verylongemail@verylongdomain.com

### Credit Card Test Data
- Valid: 1234567890123456
- Invalid: 1234 (too short), abcd1234efgh5678 (alphanumeric)
- Boundary: 16-digit number

---

## Test Execution Priority

### High Priority (P1) - 30 Test Cases
- All login/logout functionality
- Add to cart and checkout process
- Product display and navigation
- Order placement with valid data

### Medium Priority (P2) - 40 Test Cases
- Form validations (empty fields)
- Category filters
- Contact form
- Cart operations

### Low Priority (P3) - 16 Test Cases
- UI element tests
- Modal close operations
- About us page
- Special character handling

---

## Prerequisites for Test Execution

1. **Test Environment Setup**
   - Browser: Chrome/Firefox/Edge (latest versions)
   - Internet connection
   - Access to https://www.demoblaze.com/

2. **Test Data Requirements**
   - Pre-registered user account for login tests
   - New username for registration tests
   - Valid email addresses for contact form
   - Test credit card numbers

3. **Test Tools**
   - Manual testing or automation tool (Selenium, Playwright, Cypress)
   - Screen recording/screenshot tool
   - Test management tool for tracking results

---

## Notes for Testers

1. **Test Case Execution Order:**
   - Execute valid scenarios first
   - Follow with invalid/negative scenarios
   - Complete with boundary and edge cases

2. **Data Reset:**
   - Clear browser cache between test sessions
   - Use different usernames for signup tests
   - Clear cart before cart-related tests

3. **Expected Behaviors:**
   - Most modals can be closed using X button or Close button
   - Alert messages appear for most validation errors
   - Cart persists during session
   - Successful operations show confirmation messages

4. **Known Limitations:**
   - Some validation may only be server-side
   - Alert messages may vary slightly
   - Test data must be adjusted based on actual system behavior

---

## Test Coverage Summary

| Feature | Test Cases | Coverage |
|---------|-----------|----------|
| User Authentication | 20 | Complete |
| Contact Form | 10 | Complete |
| Product Browsing | 18 | Complete |
| Shopping Cart | 8 | Complete |
| Checkout Process | 16 | Complete |
| Navigation | 10 | Complete |
| UI Components | 4 | Basic |
| **TOTAL** | **86** | **Comprehensive** |

---

## Recommendations

1. **Automation Candidates:**
   - Regression suite: All valid positive test cases
   - Smoke suite: TC_LOGIN_001, TC_PRODUCT_001, TC_CART_002, TC_PLACEORDER_001
   - Critical path: End-to-end purchase flow

2. **Manual Testing Focus:**
   - UI/UX validation
   - Video playback testing
   - Cross-browser compatibility
   - Exploratory testing

3. **Additional Test Scenarios to Consider:**
   - Performance testing (page load times)
   - Security testing (XSS, CSRF)
   - Accessibility testing (WCAG compliance)
   - Mobile responsiveness testing
   - Multi-user concurrent order testing

---

**Document Version:** 1.0  
**Last Updated:** January 2025  
**Status:** Ready for Review
