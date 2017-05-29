

This is a Giter8 template for Neo4j, Akka in Scala.

sbt new knoldus/scala-solr-akkahttp.g8

# scala-solr-akkahttp

This repository contains the basic starter app with update and search operations for Solr With Scala and Akka-http.

## Prerequisites:

Solr must have been installed on your system and running on port : 8983. If you have not downloaded it See the link [here](http://www-eu.apache.org/dist/lucene/solr/6.4.1/)
After Downloading Solr use these commands which will create collection and indexes for data :

1. Start Solr : 

    `<base_dir>/bin/solr start -e cloud -noprompt`

2. Create Index for Json and load data : 

    `<base_dir>/bin/post -c gettingstarted example/exampledocs/books.json`

# How to run this :

There are two ways to test this project :
  
         mvn clean compile test
  

There are two ways to run this project :
  
         mvn install
         
         mvn package exec:java -Dexec.mainClass=com.knoldus.solrService.StartApp

After running this, you can hit its endpoint for performing the operation. eg: 
    
    0.0.0.0:8080/search/keyword/fantasy


# License

Written in 2017 by Knoldus Software LLP [other author/contributor lines as appropriate]

To the extent possible under law, the author(s) have dedicated all copyright and related and neighboring rights to this template to the public domain worldwide. This template is distributed without any warranty. See http://creativecommons.org/publicdomain/zero/1.0/.

