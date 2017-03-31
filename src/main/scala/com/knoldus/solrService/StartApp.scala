package com.knoldus.solrService

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.knoldus.solrService.factories.{SolrAccess, SolrClientAccess}
import com.knoldus.solrService.routes.{SolrJsonFormatter, SolrService}
import com.typesafe.config.ConfigFactory

object StartApp extends App {
  implicit val system: ActorSystem = ActorSystem("Solr-Akka-Service")
  implicit val executor = system.dispatcher
  implicit val materializer = ActorMaterializer()
  val solrAccessClient = new SolrClientAccess
  val solrAccess = new SolrAccess(solrAccessClient)
  val solrJsonFormatter = new SolrJsonFormatter()
  val solrService = new SolrService(solrAccess, solrJsonFormatter)
  val config = ConfigFactory.load("application.conf")
  val serverUrl = config.getString("http.interface")
  val port = config.getInt("http.port")
  val server = Http().bindAndHandle(solrService.solrRoutes, serverUrl, port)
}