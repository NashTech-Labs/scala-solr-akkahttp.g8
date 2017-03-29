name := """akka-scala-solr"""

version := "1.0"

scalaVersion := "2.11.8"

organization := "com.knoldus"

val akkaV = "2.4.5"
libraryDependencies ++= Seq(
 "com.google.code.gson" %  "gson" % "2.8.0","org.json4s" % "json4s-native_2.11" % "3.5.0",
 "io.spray" %%  "spray-json" % "1.3.3",
 "org.json4s"           %  "json4s-native_2.11" % "3.5.0",
 "junit"                %  "junit" % "4.12",
 "org.specs2"           %  "specs2-junit_2.11" % "3.8.8",
 "com.typesafe.akka"    %% "akka-http-core" % akkaV,
 "com.typesafe.akka"    %% "akka-http-experimental" % akkaV,
 "com.typesafe.akka"    %% "akka-http-testkit" % akkaV % "test",
 "com.typesafe.akka"    %% "akka-http-spray-json-experimental" % akkaV,
 "org.scalatest"        %% "scalatest" % "2.2.6" % "test",
 "org.slf4j"            %  "slf4j-api" % "1.7.23",
 "org.apache.solr"      %  "solr-solrj" % "6.4.1" exclude("org.slf4j", "slf4j-api"),
 "org.mockito"          %  "mockito-all" % "1.9.5"

)
