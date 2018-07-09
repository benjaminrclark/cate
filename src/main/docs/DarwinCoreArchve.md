# Darwin Core Archive

This documentation covers how CATE uses Darwin Core Archive and how it interprets Darwin Core Archives.

The principals behind this are important and are based on experiences with other projects in the same space. 

Darwin Core's strength is it's simplcity and that it has very few absolute requirements. Allowing lots of flexibility
is great for publishers, but terrible for downstream users because you can make very few assumptions about the 
data you're going to get.

The way that archives are interpreted comes from the perspective of CATE as an aggregator. That is: the primary use-case
is importing archives from multiple different data sources using scheduled batch jobs. This use case creates specific 
requirements which are not neccessarily requirements of DwC/A itself. We have found it possible to meet these requirements 
without changing the Darwin Core Archive specification, but it does require us to interpret the archives in a particular 
way to support particular features. 

However, we want to be able to support any valid archive, so we've tried to implement CATE in such a way that it can cope
with archives which do not follow the conventions which we take advantage of. If those conventions are followed, CATE can
do more when importing data.

## Use Case

CATE is designed to work as a publishing node and an aggregating node in a distributed system of biodiversity resources. 
Publishing nodes are systems which serve data in the form of Darwin Core Archives. Aggregating nodes have copies of that data
and would like to remanin in sync with those nodes (i.e. have an up-to-date copy of data from that node).

The data served by nodes is in Darwin Core Archive format. DwC/A is (at least, in this use case) a representation of data about
entities like Taxa, Occurrences, Descriptions, Multimedia, Vernacular Names and so on. Those entities can be distinguished
by their identifier (usually specified by dc:identifier, but sometimes by dwc:taxonID or dwc:occurrenceID). Entities have a number
of properties, most of which are optional. 

From the perspective of data publishers and aggregators, linking data from multiple publishers together is desirable as it
connects datasets up and reduces the amount of redundant information. Publishers are able to enhance their own data with good datasets from other sources
and aggregators are able to aggregate and serve larger connected datasets. This is achieved by one publisher publishing data with identifiers
and another publisher using those identifiers in its own dataset. It is important that the provenance of such information is preserved and that
aggregators import data as published by its owners without transforming it. Equally, data publishers need to be able to discover authoritative sources of data which they can link to. 

## Interpretation of the DwC/A format

Darwin Core Archives can contain a number of files which each contain a number of rows or records. In CATE, records are mapped to entities using 
their identifier - rows with the same identifier refer to the same entity. Depending on the record type, multiple records with the same identifier
mean different things. 

In core (Taxon) files, multiple records with the same identifier are interpreted as updates to the same entity i.e. the second row will overwrite the first and so on. 
This includes all of the properties in the row.

SimpleMultimedia, Term, Reference, TypeAndSpecimen, Dataset and IdentificationKey entities are considered to have a Many-to-Many relationship to core (Taxon) entities, so multiple
records with the same identifier but different values in the taxonID column result in new links between the entity and a taxon. Duplicate (identifier, taxonID) tuples 
result in existing links being overwritten. Properties other than identifier and taxonID are updated. These entities are considered to be able to exist without a related Taxon, so the
taxonID property is optional.

Description, Distribution, MeasurementOrFact, Node and VernacularName entities are considered to have a Many-To-One relationship to core (Taxon) entities and so multiple records with
the same identifier result in entities being overwritten. These entities only make sense in the context of a Taxon, so the taxonID is required.

The Darwin Core specification does not mandate the values of properties or identifiers in Darwin Core Archives, but instead suggests
values for particular properties. CATE has a stricter interpretation of properties and in general will exclude records which have invalid values. Invalid lines do not normally
prevent the rest of the archive being imported, but large numbers of invalid lines might indicate a problem and may result in a job being terminated early.

## Identifiers

In Darwin Core, the format of identifiers is suggested to be a globally-unique, permanently-assigned, resolvable, actionable identifier. However, this is not a strict requirement and the 
burden on data publishers to meet the requirements of publishing such identifiers means that requiring all identifiers to be permanently-assigned, resolvable and actionable would prevent
many publishers from publishing their data in this way. CATE interprets identifiers as being globally unique as it is relatively straightforward for data publishers to assign globally unique
identifiers to their data in the form of e.g. UUIDs.

CATE considers a Darwin Core Archive to be a single Dataset and expects the dataset be complete and consistent in that it should not contain unresolvable identifiers. Resolvable in this sense 
means that each property value with a non empty identifier should have a corresponding valid row in the archive. 

Some of the identifiers and entities in the Dataset may belong to another dataset published by a different organisation (foreign identifiers). To ensure that publishers are in control of the 
data aggregated, CATE will not import entities which belong to a different dataset. This means that any publisher can link to another publisher's data by using their identifiers in their own data, 
but they cannot change the representation of the entity that the identifier links to (by e.g. changing values in the corresponding row in their own darwin core archive). This approach leads to the 
problem of ordering in importing data. If a darwin core record contains references to entities which belong to a different dataset and does not exist in the database already, CATE will exit with a warning, 
or if the metadata for the dependent datasets is available, will attempt to synchronize those datasets first. This process relies on foreign object being present in the dataset and the object having a 
dataset property, and that dataset being available.

## Darwin Core + resourcesync

In general, we assume that datasets don't change very much once they are created. When re-synchronising with a publisher, an aggregator only needs the rows which have changed: creates and updates,
   plus deletes. Darwin Core Archives don't have a way of expressing this on their own. CATE can publish Darwin Core Archives as a Resource Sync Change Dump. This consists of a standard Darwin Core Archive
, plus a change dump manifest which lists the updates, creates and deletes. The Darwin Core Archive contains a complete dataset for that publisher, so it can contain rows which have not been created, updated
or deleted in the time interval which is covered by the change dump. In thich case they will not be listed in the change dump manifest and will be skipped by CATE on the assumption that they are already up-to-date. Entities which are updated multiple times in the 

   Normal Darwin Core Archives which do not contain a change dump manifest are first converted to this format by the aggregator before importing. 

## Design

   The first principle is that we've tried to keep the mapping between Darwin Core and the internal data model as 
   close as possible to try to make import and export easy. Translating between internal data formats and 
   an exchange format, especially if the translation is lossy, makes for complex code and a poor user experience.

   To support additional data formats like SDD and Delta, we've chosen to use a single import pipeline where data in
   these formats are translated into Darwin Core first, and then imported as Darwin Core. It sounds complex, but it 
   ensures that data is imported in a consistent way. Translation to DwC/A from another format may be lossy, but import
   from DwC/A is not.
   CATE can operate as an a


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
	DarwinCoreArchive
	- Contains copy of information from meta.xml
	File
	- Either Core File or Extension File
	Record



# Copy input archive in from url to upload bucket allowStartIfComplete *

	fileTransferSystem.copyFileOut(/tmp/cate/random_filename.zip,upload://random_filename.zip) | Retrieved archive from x

# Copy from upload bucket to local machine allowStartIfComplete=true *

	fileTransferSystem.copyFileIn(upload://random_filename.zip, /tmp/cate/random_filename.zip)

# Unpack & Validate Format e.g. is this a valid DwC/A? allowStartIfComplete=true

	archiveUnpackingTasklet.unzip(/tmp/cate/random_filename.zip, /tmp/cate/working_dir) | Unpacked archive successfully
        validateArchiveTasklet
	meta.xml exists!
	eml.xml exists?
	manifest.xml exists?
	Core File exists!
	Extension Files exist?

# Create DarwinCoreArchive record with data from meta and eml (optionally). | Read meta.xml, eml.xml - add terms *

# Validate Archive
        validateMetaTasklet
	meta.xml valid?
	meta file contains known terms

# Read core file into File object *

# Read core records into Record objects | Read # records from ${filename} *

# For each extension file *
# Read extension into File object *

# Read records into Record objects | Read # records from ${filename} *
        validatedFilesAgainstMeta
	meta and files agree (column numbers)?
	Identifiers resolvable
	Taxon only - parents have the correct status
	Validate values

#### can produce report at this stage - this is a 'valid' DwC/A.

# Does the DwC/A declare a dataset in the columns of the core and extension files as well as an identifier for 'this' dataset in the eml? If so, we can exclude some records as being foreign - we don't import those records, but we also need to check that the identifiers of foreign datasets are either known and up to date, or resolvable and we import them.


# Do we have a changedump manifest? if so, we can import changes, provided the change dump is the "next" change dump (need to check)

changedump?

# Otherwise this dataset exists in the database and we have modified dates for those entities. 

if dataset exists? 

	If the dataset exists in the database
for each object type in the sum of (object types already in database for that dataset, object types in darwin core archve)
	for each object in the database belonging to that dataset, does that object exist in the archive? If so, then it is either a skip if unchanged, an update if updated, or a delete if absent.
	Non-Owned Extension files will need to check (coreId, identifier) tuples to detect skip / updated / deleted joins
	for each object in the archive, does it exist in the database, if not, then it is a create.

# We can now write out a change dump DwC/A
for each object type in the sum of (object types already in database for that dataset, object types in darwin core archve)
	for each record in the file (including extra records which express deletes or deleted joins) we can write out an entry in the change dump manifest
	Duplicate change list entries for multiple rows or express them as ranges
	2) Order by max(created,updated) deletes go at the end. Sort DwCA file in this way too (as it is not guarenteed to be in order) and filter out the unchanged lines

else 
  

if declares dataset identifier in eml?
  if changedump
    if most recent changedump
      import
    else
      exit "INTERMEDIATE CHANGE DUMPS MISSING"
  else if has dataset identifiers in core and extension files
    if dataset in database
      generate changedump
      import
    else
      generate changedump (everything is a create, more or less)
      import
else
  exist "NO DATASET IDENTIFIER"

#### can produce change dump at this stage ####

	3) Iterate over records in files 
	two underlying readers called in order

	apply changes

## Import Modes

	* Resolution:
	Get Dataset / Endpoint url for entity
	Get entity from url
	scan for identifiers
	resolve entity
