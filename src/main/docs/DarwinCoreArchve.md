# Darwin Core Archive

  This documentation covers how CATE uses Darwin Core Archive and how it interprets Darwin Core Archives.

  The principals behind this are important and are based on experiences with other projects in the same space. 

  The first principle is that we've tried to keep the mapping between Darwin Core and the internal data model as 
  close as possible to try to make import and export as easy as possible. Translating between internal data formats and 
  an exchange format, especially if the translation is lossy, makes for complex code and a poor user experience.

  To support additional data formats like SDD and Delta, we've chosen to use a single import pipeline where data in
  these formats are translated into Darwin Core first, and then imported as Darwin Core. It sounds complex, but it 
  ensures that data is imported in a consistent way. Translation to DwC/A from another format may be lossy, but import
  from DwC/A is not.

  Darwin Core's strength is it's simplcity and that it has very few absolute requirements. Allowing lots of flexibility
  is great for publishers, but terrible for downstream users as it means you can make very few assumptions about the 
  data you're going to get.

  The way that archives are interpreted comes from the perspective of CATE as an aggregator. That is: the primary use-case
  is importing archives from multiple different data sources using scheduled batch jobs. This use case creates specific 
  requirements which are important requirements of DwC/A itself. We have found it possible to meet these requirements 
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
  data entities like Taxa, Occurrences, Descriptions, Multimedia, Vernacular Names and so on. Those entities can be distinguished
  by their identifier (usually specified by dc:identifier, but sometimes by dwc:taxonID or dwc:occurrenceID), and which have a number
  of properties, most of which are optional. 

  Darwin Core Archives contain a number of files which each contain a number of rows or records. In CATE, records are mapped to entities using 
  their identifier - rows with the same identifier refer to the same entity. Depending on the record type, multiple records with the same identifier
  mean different things. 

  In core (Taxon) files, multiple records with the same identifier are interpreted as updates to the same entity i.e. the second row will overwrite the first and so on. 
  This includes all of the properties in the row.

  SimpleMultimedia, Term, Reference, TypeAndSpecimen, Dataset and IdentificationKey entities are considered to have a Many-to-Many relationship to core (Taxon) entities, so multiple
  records with the same identifier but different values in the taxonID column result in new links between the entity and a taxon. Duplicate (identifier, taxonID) tuples 
  result in existing links being overwritten. Properties other than identifier and taxonID are updated.

  Description, Distribution, MeasurementOrFact, Node and VernacularName entities are considered to have a Many-To-One relationship to core (Taxon) entities and so multiple records with
  the same identifier result in entities being overwritten.

  The Darwin Core specification does not mandate the values of properties or identifiers in Darwin Core Archives, but instead suggests
  values for particular properties. CATE has a stricter interpretation of properties and in general will exclude records which have invalid values. Invalid lines do not normally
  prevent the rest of the archive being imported, but large numbers of invalid lines might indicate a problem and may result in a job being terminated early.

## Identifiers

## Darwin Core + resourcesync

  CATE can operate as an a
