Feature: Verify Order Book E2E flow

  Scenario: Validate CRUD operations for Sample book APIs
    Given I registered a new api client
    When I Create a new order
    And  I get all books
    When I retrieve the order by Id
    And  I update the order
    When  I delete the order
    Then  Validate that order has been deleted