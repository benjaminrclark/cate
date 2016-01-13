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


Import Process:

Any File -> DwC/A -> DwC/A + resourcesync

e.g. DwC/A is the canonical representation of data, and DwC/A + resourcesync is the "patch" being applied to this database. Implies a point in time, which suggests locking.

Annotations

Batch Axis
JobExecution
	StepExecution
		Item
			Only one item annotation per item per step e.g. either
                        skipInRead
                        readError
                        processError
                        skipInProcess
                        writeError (multiple items in single tx)
                        skipInWrite
Data Axis
	File
		Row (Item)

# Copy input archive in from url to upload bucket

# Copy from upload bucket to local machine

# Unpack

# Validate Format e.g. is this a valid DwC/A? 
	meta.xml 
	eml.xml? (optional)
	files?
	meta and files agree?
# Does the DwC/A declare a dataset? this should be in th
  If the dataset exists in the database
    Then we should have some idea about how to distinguish records which belong to this database vs records which are referenced - by knowing the databases identifier prefix. 
    # Filter out objects which dont belong - i.e. which have an identifier format which is not the same as the datasets.
    # Or filter out data types according to another set of rules e.g. for this dataset exclude x, y, and z types
# ResourceSync

DwCA to ResourceSync
-1) Mark Relationship or entity e.g. for images we call 
  insert into lookup (object_id, object_type, other_object_id, other_object_type, job_id, date_time, authority_id, type, code, record_type) select i.id, 'Image', t.id, 'Taxon', :jobId, :dateTime, :authorityId, 'Warn', 'Absent', 'Image' from image i join image_taxon i_t on (i.id = i_t.image_id) join taxon t on (i_t.taxon_id = t.id) where i.authority_id = :authority
1) Identify creates / updates (based on modified time) & deletes (missing items)
d2) Identify deleted joins - for the skipped and updated any-to-manys,
2) Order by max(created,updated) deletes go at the end. Sort DwCA file in this way too (as it is not guarenteed to be in order) and filter out the unchanged lines
3) Duplicate change list entries for multiple rows
3) Iterate over changelist & file using composite item reader
two underlying readers called in order
 changeListEntry = changeListReader.read()

 if(!changeListEntry.type().equals(ChangeType.DELETE)) {
    // insert or update records
 } else {
    // skip to delete
 }


