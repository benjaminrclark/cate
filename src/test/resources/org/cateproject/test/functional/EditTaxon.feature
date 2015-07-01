Feature: Edit Taxon
Taxa are the core domain objects in CATE. The edit pages provide the ability for editors
to list, create, update and delete taxa.
  
Background:
  
Scenario: Create a taxon
  As an editor, I would like to create individual taxa records as required, so that I can keep
  my taxonomic information up-to-date and reflect the current state of knowledge about my chosen
  taxonomic group
  Given there is one tenant "localhost"
  And I am logged in to CATE as admin@example.com with password admin
  And the edit feature is enabled
  When I select the admin link from the user_menu menu
  Then the user_menu dropdown is visible
  When I select the Edit link from the user_menu menu
  Then the page title should be "Edit CATE"
  When I select the List all Taxa link from the edit_menu menu
  Then the page title should be "List all Taxa"
  And there should be 0 results in the list_org_cateproject_domain_Taxon table
  When I click the create_new_taxon button
  Then the page title should be "Create new Taxon"
  When I enter the following information in the create_org_cateproject_domain_Taxon form
    | taxonId                     | NEW_IDENTIFIER    |
    | scientificName              | Arum italicum     |
    | scientificNameAuthorship    | L.                | 
    | taxonRank                   | SPECIES           |
  And I submit the create_org_cateproject_domain_Taxon form
  Then the page title should be "Show Taxon"
  And a success alert should say "Taxon<NEW_IDENTIFIER> created"
  And the following data about the org_cateproject_domain_Taxon should be displayed 
    | scientificName              | Arum italicum     |
    | scientificNameAuthorship    | L.                | 
    | taxonId                     | NEW_IDENTIFIER    |
    | taxonRank                   | SPECIES           |
