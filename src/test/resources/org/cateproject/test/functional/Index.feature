Feature: Index Page
The index page is the first page of the CATE portal
  
Background:
  
Scenario: Show the index page
  Description of the scenario
  Given there is one tenant "localhost"
  When I go to the index page
  Then the hero title should be "Welcome to CATE"
