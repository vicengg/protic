Feature: Work experience visibility
  Check the work experiences visibility business rules.

  Background:
    Given a user with id "user_1"
    And a user with id "user_2"
    And the user "user_1" starts to fill the create work experience form
    And the user "user_1" fills the job title as "developer" with "private" visibility
    And the user "user_1" fills the company as "Google" with "private" visibility
    And the user "user_1" adds a technology "Java" with "private" visibility
    And the user "user_1" adds a work period from "2016-08-16" to present with "private" visibility
    And the user "user_1" adds a salary of 50000 "EUR" with "private" visibility
    And the user "user_1" sends his work experience to create, anonymously, with reference ID "work_experience_1"
    And the user "user_2" starts to fill the create work experience form
    And the user "user_2" fills the job title as "developer" with "private" visibility
    And the user "user_2" fills the company as "Amazon" with "private" visibility
    And the user "user_2" adds a technology "JavaScript" with "private" visibility
    And the user "user_2" adds a work period from "2017-05-12" to present with "private" visibility
    And the user "user_2" adds a salary of 60000 "EUR" with "private" visibility
    And the user "user_2" sends his work experience to create, anonymously, with reference ID "work_experience_2"

  Scenario: Without negotiation, User 2 can't see anything about User 1's experience
    When the user "user_2" gets work experiences of others
    Then the work experience "work_experience_1" is present
    And the work experience job title is NOT present
    And the work experience company is NOT present
    And the work experience technologies are NOT present
    And the work experience work period is NOT present
    And the work experience salary is NOT present
    And the work experience user is anonymous

  Scenario:User 2 creates a negotiation asking for visibility for all fields and offering all the fields of his work experience
    When the user "user_2" creates a data request to unlock "work_experience_1" offering "work_experience_2"
    And the user "user_2" adds a negotiation step
    And asks for visibility for "job title, company, technologies, work period and salary"
    And offers visibility for "job title, company, technologies, work period and salary"
    And "user_2" sends the negotiation
    And "user_1" accepts the negotiation
    When the user "user_2" gets work experiences of others
    Then the work experience "work_experience_1" is present
    And the work experience job title is present
    And the work experience company is present
    And the work experience technologies are present
    And the work experience work period is present
    And the work experience salary is present
    And the work experience user is anonymous
    When the user "user_1" gets work experiences of others
    Then the work experience "work_experience_2" is present
    And the work experience job title is present
    And the work experience company is present
    And the work experience technologies are present
    And the work experience work period is present
    And the work experience salary is present
    And the work experience user is anonymous

  Scenario: User 2 creates a negotiation asking for visibility for some fields of the User 1's work experience and offering some fields of his work experience
    When the user "user_2" creates a data request to unlock "work_experience_1" offering "work_experience_2"
    And the user "user_2" adds a negotiation step
    And asks for visibility for "job title, work period and salary"
    And offers visibility for "company and technologies"
    And "user_2" sends the negotiation
    And "user_1" accepts the negotiation
    When the user "user_2" gets work experiences of others
    Then the work experience "work_experience_1" is present
    And the work experience job title is present
    And the work experience company is NOT present
    And the work experience technologies are NOT present
    And the work experience work period is present
    And the work experience salary is present
    And the work experience user is anonymous
    When the user "user_1" gets work experiences of others
    Then the work experience "work_experience_2" is present
    And the work experience job title is NOT present
    And the work experience company is present
    And the work experience technologies are present
    And the work experience work period is NOT present
    And the work experience salary is NOT present
    And the work experience user is anonymous

