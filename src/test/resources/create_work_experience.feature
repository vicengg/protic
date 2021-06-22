Feature: Work experience visibility
  Check the work experiences visibility business rules.

  Background:
    Given a user with id "user_1"
    And a user with id "user_2"
    And the user "user_1" starts to fill the create work experience form

  Scenario: User 1 creates a work experience associated to his profile with all fields public.
    When the user "user_1" fills the job title as "developer" with "public" visibility
    And the user "user_1" fills the company as "Google" with "public" visibility
    And the user "user_1" adds a technology "Java" with "public" visibility
    And the user "user_1" adds a work period from "2016-08-16" to present with "public" visibility
    And the user "user_1" adds a salary of 50000 "EUR" with "public" visibility
    And the user "user_1" sends his work experience to create, associated to his profile, with reference ID "work_experience_1"
    When the user "user_1" gets his own work experiences
    Then the work experience "work_experience_1" is present
    And the work experience job title is present
    And the work experience company is present
    And the work experience technologies are present
    And the work experience work period is present
    And the work experience salary is present
    And the work experience user is NOT anonymous

  Scenario: User 1 creates a work experience anonymously with all fields private, but he can see all data.
    When the user "user_1" fills the job title as "developer" with "private" visibility
    And the user "user_1" fills the company as "Google" with "private" visibility
    And the user "user_1" adds a technology "Java" with "private" visibility
    And the user "user_1" adds a work period from "2016-08-16" to present with "private" visibility
    And the user "user_1" adds a salary of 50000 "EUR" with "private" visibility
    And the user "user_1" sends his work experience to create, anonymously, with reference ID "work_experience_1"
    When the user "user_1" gets his own work experiences
    Then the work experience "work_experience_1" is present
    And the work experience job title is present
    And the work experience company is present
    And the work experience technologies are present
    And the work experience work period is present
    And the work experience salary is present
    And the work experience user is NOT anonymous

  Scenario: User 1 creates a work experience associated to his profile with all fields public, then User 2 can see all data.
    When the user "user_1" fills the job title as "developer" with "public" visibility
    And the user "user_1" fills the company as "Google" with "public" visibility
    And the user "user_1" adds a technology "Java" with "public" visibility
    And the user "user_1" adds a work period from "2016-08-16" to present with "public" visibility
    And the user "user_1" adds a salary of 50000 "EUR" with "public" visibility
    And the user "user_1" sends his work experience to create, associated to his profile, with reference ID "work_experience_1"
    When the user "user_2" gets his own work experiences
    Then the work experience "work_experience_1" is NOT present
    When the user "user_2" gets work experiences of others
    Then the work experience "work_experience_1" is present
    And the work experience job title is present
    And the work experience company is present
    And the work experience technologies are present
    And the work experience work period is present
    And the work experience salary is present
    And the work experience user is NOT anonymous

  Scenario: User 1 creates a work experience anonymously with all fields private, then User 2 can't see any field nor the owner identity.
    When the user "user_1" fills the job title as "developer" with "private" visibility
    And the user "user_1" fills the company as "Google" with "private" visibility
    And the user "user_1" adds a technology "Java" with "private" visibility
    And the user "user_1" adds a work period from "2016-08-16" to present with "private" visibility
    And the user "user_1" adds a salary of 50000 "EUR" with "private" visibility
    And the user "user_1" sends his work experience to create, anonymously, with reference ID "work_experience_1"
    When the user "user_2" gets his own work experiences
    Then the work experience "work_experience_1" is NOT present
    When the user "user_2" gets work experiences of others
    Then the work experience "work_experience_1" is present
    And the work experience job title is NOT present
    And the work experience company is NOT present
    And the work experience technologies are NOT present
    And the work experience work period is NOT present
    And the work experience salary is NOT present
    And the work experience user is anonymous
