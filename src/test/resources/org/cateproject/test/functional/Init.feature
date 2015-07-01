Feature: One-Time Init
When a CATE portal starts up for the first time, it is not configured with a default tenant.
Before you can use the application, you need to add an admin and owner account for the 
default tenant. Once this is done, you're redirected to the tenant page via the login page.
  
Background:
  
Scenario: First time init
  As the CATE administrator, I would like to be able to configure the default tenant for CATE
  quickly, so that I can make the application available to its intended users.
  When the CATE application is uninitialized
  And I go to the index page
  Then an info alert should say "Before you can start using CATE, you need to create an administrator account, and the default CATE tenant"
  When I enter the following information in the init_default_tenant form
    | adminEmail    | admin@example.com |
    | adminPassword | admin             |
    | ownerEmail    | owner@example.com | 
    | ownerPassword | owner             |
  And I submit the init_default_tenant form
  Then the page title should be "Sign In"
  When I enter the following information in the login form
    | username | admin@example.com |
    | password | admin             |
  And I submit the login form
  Then the page title should be "localhost"
  And a success alert should say "Tenant CATE created successfully"
