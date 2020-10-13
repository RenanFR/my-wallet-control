Feature: Use registration
  As a visitor I want to register a new user in the system for myself

  Scenario: Successful user sign up
    Given I access the login page and doesnt have a user
    And I click the sign up button
    And fill my personal and financial data
    When I click submit
    Then my user is enabled and I can login
