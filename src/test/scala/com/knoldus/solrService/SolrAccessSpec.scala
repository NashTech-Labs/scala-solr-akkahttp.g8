/*
package com.knoldus.solrService

import java.io.File

import org.apache.solr.client.solrj.embedded.EmbeddedSolrServer
import org.apache.solr.core.CoreContainer
import org.scalatest.{BeforeAndAfterAll, FunSuite}
import org.scalatest.mock.MockitoSugar

class SolrAccessSpec extends FunSuite with MockitoSugar with BeforeAndAfterAll {

  var server: EmbeddedSolrServer = null

  override def beforeAll() {
    val container = new CoreContainer("testConf")
    container.load()
    server = new EmbeddedSolrServer(container, "collection1")
  }

  override def afterAll(): Unit = {
    server.close()
  }
}
*/
