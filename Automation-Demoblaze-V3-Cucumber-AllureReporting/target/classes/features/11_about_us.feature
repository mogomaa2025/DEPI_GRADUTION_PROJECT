@regression @aboutus
Feature: About Us
  As a user
  I want to learn about the company
  So that I can understand the business

  Background:
    Given I am on the DemoBlaze homepage

  @ui @TC_ABOUT_001
  Scenario: About Us modal opens and displays video
    When I click on "About us" link
    Then the about modal should open
    And I should see a video player
    And the modal should have a close button

  @TC_ABOUT_002
  Scenario: About Us video plays successfully
    When I open the About Us modal
    And I click the play button on video
    Then the video should start playing
    And video controls should be visible

  @ui @TC_ABOUT_003
  Scenario: Close About Us modal
    When I open the About Us modal
    And I click the "Close" button
    Then the modal should close
    And I should remain on the homepage
