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
    General Search & Faceting
    Spatial Search
    Pivot-table
  Web
    Read-only pages for non-owned objects
      Taxon
      Image
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


ResourceSync

DwCA to ResourceSync
-1) Mark Relationship or entity e.g. for images we call 
  insert into annotation (annotated_object_id, annotated_object_type, other_annotated_object_id, other_annotated_object_type, job_id, date_time, authority_id, type, code, record_type) select i.id, 'Image', t.id, 'Taxon', :jobId, :dateTime, :authorityId, 'Warn', 'Absent', 'Image' from image i join image_taxon i_t on (i.id = i_t.image_id) join taxon t on (i_t.taxon_id = t.id) where i.authority_id = :authority
1) Identify creates / updates (based on modified time) & deletes (missing items)
2) Identify deleted joins - for the skipped and updated any-to-manys,
2) Order by max(created,updated) deletes go at the end. Sort DwCA file in this way too (as it is not guarenteed to be in order) and filter out the unchanged lines
3) Duplicate change list entries for multiple rows
3) Iterate over changelist & file using composite item reader
two underlying readers called in order
 changeListEntry = changeListReader.read()

 if(!changeListEntry.type().equals(ChangeType.DELETE)) {
    //
 }

# Architecture
			CATE Process
Embedded Single Server
Private Cluster
Cloud (AWS)
