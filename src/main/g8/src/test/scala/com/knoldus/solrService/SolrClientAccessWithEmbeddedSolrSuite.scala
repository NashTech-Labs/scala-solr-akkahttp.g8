package com.knoldus.solrService

import com.knoldus.solrService.factories.{BookDetails, SolrAccess, SolrClientAccess}
import org.apache.solr.client.solrj.embedded.EmbeddedSolrServer
import org.apache.solr.core.CoreContainer
import org.junit.runner.RunWith
import org.scalatest.{BeforeAndAfterAll, FunSuite, Sequential}
import org.scalatest.junit.JUnitRunner
import org.scalatest.mock.MockitoSugar


@RunWith(classOf[JUnitRunner])
class SolrClientAccessWithEmbeddedSolrSuite extends FunSuite with MockitoSugar with BeforeAndAfterAll {
  Sequential

  var server: EmbeddedSolrServer = _
  var solrClientAccess: SolrClientAccess = _
  val bookDetails = BookDetails("5", Array("Oddy"), "Spiral", "Classmate",
    Some("education"), 2, "education", inStock = true, 121.1D, 254)


  override def beforeAll(): Unit = {
    val container = new CoreContainer()
    container.load()

    server = new EmbeddedSolrServer(container, "test_embedded")
    solrClientAccess = new SolrClientAccess(server, None)

  }

  override def afterAll(): Unit = {
    server.close()
  }

  test("test insert record") {
    val result = solrClientAccess.insertRecord(bookDetails)
    assert(result.isDefined)
  }

  test("test fetch all data") {
    val result = solrClientAccess.fetchData("Spiral")
    assert(result.isDefined)
  }

  test("test fetch record with data not found") {
    val result = solrClientAccess.fetchData("book")
    assert(result == Some(List()))
  }

}