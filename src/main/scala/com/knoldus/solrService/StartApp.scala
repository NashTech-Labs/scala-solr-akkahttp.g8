package com.knoldus.solrService

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.knoldus.solrService.factories.{SolrAccess, SolrClientAccess}
import com.knoldus.solrService.routes.{SolrJsonFormatter, SolrService}
import com.typesafe.config.ConfigFactory
import org.apache.solr.client.solrj.impl.HttpSolrClient

object StartApp extends App {
  implicit val system = ActorSystem("Solr-Akka-Service")
  implicit val executor = system.dispatcher
  implicit val materializer = ActorMaterializer()
  val solrJsonFormatter = new SolrJsonFormatter()

  val config = ConfigFactory.load("application.conf")
  val url = config.getString("solr.url")
  val serverUrl = config.getString("http.interface")
  val port = config.getInt("http.port")
  val solrClient = new HttpSolrClient.Builder(url).build()
  val solrAccessClient = new SolrClientAccess(solrClient)
  val solrAccess = new SolrAccess(solrAccessClient)
  val solrService = new SolrService(solrAccess, solrJsonFormatter)
  Http().bindAndHandle(solrService.solrRoutes, serverUrl, port)
}