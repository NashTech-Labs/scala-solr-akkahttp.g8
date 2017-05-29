package com.knoldus.solrService

import akka.http.scaladsl.model.HttpEntity
import akka.http.scaladsl.model.MediaTypes._
import akka.http.scaladsl.testkit.ScalatestRouteTest
import com.knoldus.solrService.factories.{BookDetails, SolrAccess, SolrClientAccess}
import com.knoldus.solrService.routes.{SolrJsonFormatter, SolrService}
import org.apache.solr.client.solrj.embedded.EmbeddedSolrServer
import org.apache.solr.core.CoreContainer
import org.junit.runner.RunWith
import org.scalatest._
import org.scalatest.junit.JUnitRunner
import org.scalatest.mock.MockitoSugar

@RunWith(classOf[JUnitRunner])
class SolrRouteWithEmbeddedSpec extends WordSpec with MockitoSugar with BeforeAndAfterAll with Matchers with ScalatestRouteTest {

  Sequential

  var server: EmbeddedSolrServer = _
  var solrClientAccess: SolrClientAccess = _
  var solrAccess: SolrAccess = _
  val solrJsonFormatter = new SolrJsonFormatter
  var solrService: SolrService = _

  val bookDetails = BookDetails("1", Array(), "Solr", "Henry",
    Some("education"), 2, "education", inStock = true, 1253.1D, 2569)

  override def beforeAll(): Unit = {
    val container = new CoreContainer()
    container.load()

    server = new EmbeddedSolrServer(container, "test_embedded")
    server.deleteByQuery("*:*")

    solrClientAccess = new SolrClientAccess(server, None)
    solrAccess = new SolrAccess(solrClientAccess)
    solrService = new SolrService(solrAccess, solrJsonFormatter)
  }

  override def afterAll(): Unit = {
    server.close()
  }

  val json_data =
    """{
      |"id" :"1",
      |"cat" : ["abc"],
      |"name" :"Solr",
      |"author" : "Henry",
      |"series_t" : "education",
      |"sequence_i" : 2,
      |"genre_s" : "education",
      |"inStock" : true,
      |"price" : 1253.1,
      |"pages_i" : 2569
      |}""".stripMargin

  "Routes" should {

    "be able to insert record" in {
      Post("/insert", HttpEntity(`application/json`, json_data)) ~>
        solrService.solrRoutes ~>
        check {
          val response = responseAs[String]
          response.contains("Data is successfully persisted") shouldEqual true
        }
    }

    "be able to get all record" in {
      Get("/getall") ~>
        solrService.solrRoutes ~>
        check {
          val response = responseAs[String]
          response.contains("Find List of books when fetch all records :") shouldEqual true
        }
    }

    "be able to search record with a keyword" in {
      val keyword = "education"
      Get(s"/search/keyword/$keyword") ~>
        solrService.solrRoutes ~>
        check {
          val response = responseAs[String]
          response.contains(s"Find books for $keyword and name is :") shouldEqual true
        }
    }

    "not be able to search record with a keyword" in {
      val keyword = "fantasy"
      Get(s"/search/keyword/$keyword") ~>
        solrService.solrRoutes ~>
        check {
          val response = responseAs[String]
          response.contains(s"No Book Found books for $keyword") shouldEqual true
        }
    }

    "be able to search record according to key and value" in {
      val key = "genre_s"
      val value = "education"
      Get(s"/searchVia/key/$key/value/$value") ~>
        solrService.solrRoutes ~>
        check {
          val response = responseAs[String]
          response.contains(s"Find books for key : $key & value : $value and name is : ") shouldEqual true
        }
    }

    "not be able to search record according to key and value" in {
      val key = "genre_s"
      val value = "fantasy"
      Get(s"/searchVia/key/$key/value/$value") ~>
        solrService.solrRoutes ~>
        check {
          val response = responseAs[String]
          response.contains(s"No Books Found for key : $key & value : $value") shouldEqual true
        }
    }
  }

}
