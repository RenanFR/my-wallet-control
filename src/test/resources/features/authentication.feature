Feature: user authentication

  Scenario: authenticate with valid user credentials
    Given valid user credentials
    When i access the login page
    When i input my credentials and click login button
    Then i should be able to see the dashboard
