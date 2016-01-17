Architecture
Portal (e-Monocot Portal)
  Search interface and read only pages
Harvester (~HIT)
  Uses Resources + Scheduled Jobs + Batch to harvest & Report on harvesting
Registry (~GBRDS)
  Clients and users create Organisations and resources 	
Publisher (~IPT)
   connects to registry, is authorized to create / update organisatio  organization, create / update resource
   Uses config and scheduled job to create harvestable resource

Editor (~CATE 2)
Base
  User
    User & Group domain model #
    User Manager #
    Edit user details
    One time activation / reset
   / Regsitration / Security
  One time init #
  Multi-Tenancy #
  Domain #
  Search
    General Search & Faceting #
    Spatial Search
    Pivot-table
  Web
    Read-only pages for non-owned objects
      Taxon
      Multimedia
      Key
      Term
      Occurrence
      Phylogeny
      Dataset
      Reference
    Read-write pages for all objects
  Reconciliation Service
  Duplicate Detection / merging
  Persistence #
    Audit #
  Remoting / Resilience
  Look & Feel
    Static Resource Serving #
    Theme Source #
    LESS -> CSS Conversion
  Integration #
  Batch # 
  	Job Launch Request
    Jobs #
      Job Execution #
        Step Execution
        Job Output
  Scheduler
    Scheduled Job
  OAuth Client
  OAuth Server
  Registry
  	Organisation
  	Installation
  	Dataset
  Harvester


