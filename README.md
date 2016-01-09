# CATE

[![Build Status](https://travis-ci.org/benjaminrclark/cate.svg?branch=master)](https://travis-ci.org/benjaminrclark/cate)
[![Coverage Status](https://coveralls.io/repos/benjaminrclark/cate/badge.svg)](https://coveralls.io/r/benjaminrclark/cate)
[![Download](https://api.bintray.com/packages/benjaminrclark/rpm/cate/images/download.svg)](https://bintray.com/benjaminrclark/rpm/cate/_latestVersion)

  CATE helps you discover, organize, and publish taxonomic information

## Features

 - Modern [spring-boot](http://projects.spring.io/spring-boot/) application 
 - Easy to [install](#installation)
 - Cloud-native [design](#design)
 - [Multi-tenant](#multitenancy) 
 - Built around open standards like [Darwin-Core Archive](#darwincore)
 - [Imports and exports](#importexport) data formats used by taxonomists such as DELTA, SDD, bibtex, csv
 - [Integrates](#integration) with other web services 

## Installation

  CATE is distributed as an rpm, hosted in a yum repository on [bintray](http://bintray.com). 

```bash
$ wget https://bintray.com/benjaminrclark/rpm/rpm -O /etc/yum.repos.d/bintray-benjaminrclark-rpm.repo

$ yum install cate
```

### Starting CATE

 Once CATE is installed it can be started without any further configuration, running by default in 'embedded' mode. 
 
 This means that it will use an in-memory database and a local solr server and filesystem. To configure CATE to use other 
 services and/or persist data to a specific location, see [configuration](#configuration), below.

#### As a service

```bash
$ chkconfig --level 345 cate on

$ service cate start
```

#### As a java process

```bash
$ java -jar /var/lib/cate/cate.jar
```

### Configuration 

  CATE requires Java Development Kit 1.7 (either Oracle JDK or OpenJDK). CATE uses FFMPEG and ImageMagick to process multimedia files and these packages must be installed locally on the server. 
  These dependencies are specified as dependencies of the package and are verfied / installed automatically if you install the rpm.

  CATE as a system depends upon a number of other services. The location and configuration of these services is relatively flexible. By default, CATE will run in 
  embedded mode, meaning that no other external services are required.

  CATE follows the [approach used by spring boot](http://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-external-config.html) to passing configuration parameters to the application.
  Many of the configuration properties are generic properties defined by spring-boot. Not all of them are listed below, but can be found in the [spring-boot documentation](http://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-external-config.html). Others are specific to CATE.

  Some properties are specific to a deployment on [Amazon Web Services](http://aws.amazon.com), and are listed under the heading AWS. CATE can also be deployed on to private servers using open-source software. 

#### Database
  
  CATE uses a relational database as the canonical data store. Currently it is able to make use of H2 or MySQL. The properties used to configure it are standard spring-boot properties 

 - spring.datasource.driver-class-name=com.mysql.jdbc.Driver
 - spring.datasource.username=root
 - spring.datasource.password=
 - liquibase.contexts=mysql

#### Solr
 
  CATE uses solr to power the free-text search and faceting.
 
 - solr.server.url=http://localhost:8983
 - solr.connection.timeout=100
 - solr.so.timeout=3000

#### Redis

 - spring.redis.database=0
 - spring.redis.host=localhost
 - spring.redis.port=6397
 - spring.redis.password=

#### Filesystem / Object Store 

##### Local Filesystem / Network-Attached Shared Filesystem

 - upload.file.directory
 - static.file.directory

##### AWS S3

 - cloudformation.uploadBucketArn

#### Messaging

##### ActiveMQ

 - spring.activemq.broker.url=
 - spring.activemq.in.memory=false
 - spring.activemq.user=
 - spring.activemq.password=

##### AWS SNS & SQS

 - cloudformation.topicArn
 - cloudformation.queueArn
 
#### Email 

## Design

  CATE is a web application which is designed to work at scale, deployed on virtual servers, and supporting many users and tenant projects.

  In terms of scalability, its worth noting that the CATE application itself, and the application server it runs on, is not stateful. State is
  held in the following services:

 - Data: The relational database, plus a denormalized copy of the data is held in solr
 - Media: Media files are held in the object store (either NAS or S3) and are fetched to the application server as required. They are served to clients directly from
   the store
 - Session: When running in clustered mode (i.e. 2+ application servers), CATE stores session state in a redis key-value store.
 
  Events (job requests and tenant events) are distributed using a message broker. Tenant events are distributed to all instances using a topic. Job requests are distributed across
  application servers using a single queue which is polled by all servers.
