package com.knoldus.solrService

import com.knoldus.solrService.factories.{BookDetails, SolrAccess, SolrClientAccess}
import org.apache.solr.client.solrj.embedded.EmbeddedSolrServer
import org.apache.solr.core.CoreContainer
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.mock.MockitoSugar
import org.scalatest.{BeforeAndAfterAll, FunSuite, Sequential}

@RunWith(classOf[JUnitRunner])
class SolrAccessWithEmbedddedSolrSpec extends FunSuite with MockitoSugar with BeforeAndAfterAll {
  Sequential

  var server: EmbeddedSolrServer = _
  var solrAccess: SolrAccess = _
  val bookDetails = BookDetails("1", Array(), "Solr", "Henry",
    Some("education"), 2, "education", inStock = true, 1253.1D, 2569)

  override def beforeAll(): Unit = {
    val container = new CoreContainer()
    container.load()

    server = new EmbeddedSolrServer(container, "test_embedded")

    val solrClientAccess = new SolrClientAccess(server)
    solrAccess = new SolrAccess(solrClientAccess)
  }
  override def afterAll(): Unit = {
    server.close
  }

  test("test insert") {
    val response = solrAccess.createOrUpdateRecord(bookDetails)
    assert(response.isDefined)
  }
}
