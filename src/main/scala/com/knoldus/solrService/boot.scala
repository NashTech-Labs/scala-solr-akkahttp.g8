package com.knoldus.solrService

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.knoldus.solrService.routes.SolrService


class StartSolrjServer(implicit val system: ActorSystem,
                       implicit val materializer: ActorMaterializer) extends SolrService {
  def startServer(address: String, port: Int) = {
    Http().bindAndHandle(solrRoutes, address, port)
  }
}

object StartApplication extends App {
  StartApp
}

object StartApp {
  implicit val system: ActorSystem = ActorSystem("Solr-Akka-Service")
  implicit val executor = system.dispatcher
  implicit val materializer = ActorMaterializer()
  val server = new StartSolrjServer()
  val config = server.config
  val serverUrl = config.getString("http.interface")
  val port = config.getInt("http.port")
  server.startServer(serverUrl, port)
}