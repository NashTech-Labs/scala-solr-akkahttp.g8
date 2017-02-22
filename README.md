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
  
         activator test
         
         sbt test

There are two ways to run this project :
  
         activator run
         
         sbt run

After running this, you can hit its endpoint for performing the operation. eg: 
    
    0.0.0.0:8080/search/keyword/fantasy

