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
  aggregators import data as published by its owners. Equally, data publishers need to be able to discover authoritative sources of data which they can link to. 

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

  CATE considers a Darwin Core Archive to be a single Dataset and expects the dataset be complete and consistent in that it should not contain unresolvable identifiers. The exception to this is a
  Darwin Core Archive which is also a valid ResourceSync change dump (see below). Resolvable in this sense means that each property value with a non empty identifier should have a corresponding
  valid row in the archive. 

  Some of the identifiers and entities in the Dataset may belong to another dataset published by a different organisation. To ensure that publishers are in control of the data aggregated, CATE 
  will not import entities which belong to a different dataset. This means that any publisher can link to another publisher's data by using their identifiers in their own data, but they cannot
  change the entity that the identifier links to (by e.g. changing values in the corresponding row in their own darwin core archive). This approach leads to the problem of ordering in importing 
  data. If a darwin core record contains references to entities which belong to a different dataset and does not exist in the database already, CATE will 

## Darwin Core + resourcesync
## Design

  The first principle is that we've tried to keep the mapping between Darwin Core and the internal data model as 
  close as possible to try to make import and export easy. Translating between internal data formats and 
  an exchange format, especially if the translation is lossy, makes for complex code and a poor user experience.

  To support additional data formats like SDD and Delta, we've chosen to use a single import pipeline where data in
  these formats are translated into Darwin Core first, and then imported as Darwin Core. It sounds complex, but it 
  ensures that data is imported in a consistent way. Translation to DwC/A from another format may be lossy, but import
  from DwC/A is not.
  CATE can operate as an a
