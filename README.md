# CATE

[![Build Status](https://travis-ci.org/benjaminrclark/cate.svg?branch=master)](https://travis-ci.org/benjaminrclark/cate)
[![Coverage Status](https://coveralls.io/repos/benjaminrclark/cate/badge.svg)](https://coveralls.io/r/benjaminrclark/cate)
[![Download](https://api.bintray.com/packages/benjaminrclark/rpm/cate/images/download.svg)](https://bintray.com/benjaminrclark/rpm/cate/_latestVersion)

  CATE helps you discover, organize, and publish taxonomic information

## Features

 - Modern [spring-boot](http://boot.spring.io) application 
 - Easy to [install](#installation)
 - Cloud-native [design](#architecture)
 - [Multi-tenant](#multitenancy) 
 - Built around open standards like [Darwin-Core Archive](#darwincore)
 - [Imports and exports](#importexport) data formats used by taxonomists such as DELTA, SDD, bibtex, csv
 - [Integrates](#integration) with other web services 

## Installation

  CATE is distributed as an rpm. 

```bash
$ wget https://bintray.com/benjaminrclark/rpm/rpm -O /etc/yum.repos.d/bintray-benjaminrclark-rpm.repo

$ yum install cate
```

### Starting CATE

 Once CATE is installed it can be started without any further configuration, running by default in 'embedded' mode. 
 
 This means that it will use an in-memory database and a local solr server and filesystem. To configure CATE to use other 
 services or persist data to a specific location, see [configuration](#configuration), below.

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
  These dependencies are specified as dependencies of the package and installed automatically.

  CATE as a system depends upon a number of other services. The location and configuration of these services is relatively flexible. By default, CATE will run in 
  embedded mode, meaning that no other external services are required.

#### Database
  
  CATE is currently able to make use of H2 or MySQL. 
#### Solr

#### Redis

#### Filesystem

#### Messaging Queue and Topic

#### Email Server

