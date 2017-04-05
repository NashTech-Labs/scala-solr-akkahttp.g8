
package com.knoldus.solrService

import akka.http.scaladsl.model.HttpEntity
import akka.http.scaladsl.model.MediaTypes._
import akka.http.scaladsl.testkit.ScalatestRouteTest
import com.knoldus.solrService.factories.{BookDetails, SolrAccess}
import com.knoldus.solrService.routes.{SolrJsonFormatter, SolrService}
import org.junit.runner.RunWith
import org.mockito.Mockito._
import org.scalatest.junit.JUnitRunner
import org.scalatest.mock.MockitoSugar
import org.scalatest.{Matchers, WordSpec}

@RunWith(classOf[JUnitRunner])
class SolrRouteSpec extends WordSpec with Matchers with ScalatestRouteTest with MockitoSugar {

  val solrAccess = mock[SolrAccess]
  val solrJsonFormatter = mock[SolrJsonFormatter]
  val solrService = new SolrService(solrAccess, solrJsonFormatter)
  val bookDetails = BookDetails("1", Array(), "Solr", "Henry",
    Some("education"), 2, "education", inStock = true, 1253.1D, 2569)

  val json_data =
    """{
      |"id" :"1",
      |"cat" : [],
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
      when(solrJsonFormatter.formatBookDetails(json_data)).thenReturn(bookDetails)
      when(solrAccess.createOrUpdateRecord(bookDetails)).thenReturn(Some(1))
      Post("/insert", HttpEntity(`application/json`, json_data)) ~>
        solrService.solrRoutes ~>
        check {
          val res = responseAs[String]
          res.contains("Data is successfully persisted") shouldEqual true
        }
    }

    "not be able to insert record when data is None" in {
      when(solrJsonFormatter.formatBookDetails(json_data)).thenReturn(bookDetails)
      when(solrAccess.createOrUpdateRecord(bookDetails)).thenReturn(None)
      Post("/insert", HttpEntity(`application/json`, json_data)) ~>
        solrService.solrRoutes ~>
        check {
          val res = responseAs[String]
          res.contains("Error while persisting data") shouldEqual true
        }
    }

    "throw exception for insert record" in {
      when(solrJsonFormatter.formatBookDetails(json_data)).thenReturn(null.asInstanceOf[BookDetails])
      when(solrAccess.createOrUpdateRecord(bookDetails)).thenReturn(Some(1))
      Post("/insert", HttpEntity(`application/json`, json_data)) ~>
        solrService.solrRoutes ~>
        check {
          val res = responseAs[String]
          res.contains("Error while persisting data") shouldEqual true
        }
    }

    "be able to get all record" in {
      when(solrAccess.findAllRecord).thenReturn(Some(List(bookDetails)))
      Get("/getall") ~>
        solrService.solrRoutes ~>
        check {
          val response = responseAs[String]
          response.contains("Find List of books when fetch all records :") shouldEqual true
        }
    }

    "not be able to get all record" in {
      when(solrAccess.findAllRecord).thenReturn(Some(List()))
      Get("/getall") ~>
        solrService.solrRoutes ~>
        check {
          val response = responseAs[String]
          response.contains("No Book Found") shouldEqual true
        }
    }

    "not be able to get record when data is None" in {
      when(solrAccess.findAllRecord).thenReturn(None)
      Get("/getall") ~>
        solrService.solrRoutes ~>
        check {
          val response = responseAs[String]
          response.contains("Data is not fetched and something went wrong") shouldEqual true
        }
    }

    "throw an exception while trying to get all record" in {
      when(solrAccess.findAllRecord).thenReturn(null.asInstanceOf[Option[List[BookDetails]]])
      Get("/getall") ~>
        solrService.solrRoutes ~>
        check {
          val response = responseAs[String]
          response.contains("Error found for data") shouldEqual true
        }
    }

    "be able to search record with a keyword" in {
      val keyword = "fantasy"
      when(solrAccess.findRecordWithKeyword(keyword)).thenReturn(Some(List(bookDetails)))
      Get(s"/search/keyword/$keyword") ~>
        solrService.solrRoutes ~>
        check {
          val response = responseAs[String]
          response.contains(s"Find books for $keyword and name is :") shouldEqual true
        }
    }

    "not be able to search record with a keyword" in {
      val keyword = "fantasy"
      when(solrAccess.findRecordWithKeyword(keyword)).thenReturn(Some(List()))
      Get(s"/search/keyword/$keyword") ~>
        solrService.solrRoutes ~>
        check {
          val response = responseAs[String]
          response.contains(s"No Book Found books for $keyword") shouldEqual true
        }
    }

    "not be able to search record with a keyword when data is None" in {
      val keyword = "fantasy"
      when(solrAccess.findRecordWithKeyword(keyword)).thenReturn(None)
      Get(s"/search/keyword/$keyword") ~>
        solrService.solrRoutes ~>
        check {
          val response = responseAs[String]
          response.contains(s"Error found data for keyword : $keyword") shouldEqual true
        }
    }

    "throw exception while trying to search record with a keyword" in {
      val keyword = "fantasy"
      when(solrAccess.findRecordWithKeyword(keyword)).thenReturn(null.asInstanceOf[Option[List[BookDetails]]])
      Get(s"/search/keyword/$keyword") ~>
        solrService.solrRoutes ~>
        check {
          val response = responseAs[String]
          response.contains(s"Error found for keyword : $keyword") shouldEqual true
        }
    }

    "be able to search record according to key and value" in {
      val key = "genre_s"
      val value = "fantasy"
      when(solrAccess.findRecordWithKeyAndValue(key, value)).thenReturn(Some(List(bookDetails)))
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
      when(solrAccess.findRecordWithKeyAndValue(key, value)).thenReturn(Some(List()))
      Get(s"/searchVia/key/$key/value/$value") ~>
        solrService.solrRoutes ~>
        check {
          val response = responseAs[String]
          response.contains(s"No Books Found for key : $key & value : $value") shouldEqual true
        }
    }

    "not be able to search record according to key and value when data is None" in {
      val key = "genre_s"
      val value = "fantasy"
      when(solrAccess.findRecordWithKeyAndValue(key, value)).thenReturn(None)
      Get(s"/searchVia/key/$key/value/$value") ~>
        solrService.solrRoutes ~>
        check {
          val response = responseAs[String]
          response.contains(s"Error found data for key : $key & value : $value") shouldEqual true
        }
    }

    "throw an exception while trying to search record according to key and value" in {
      val key = "genre_s"
      val value = "fantasy"
      when(solrAccess.findRecordWithKeyAndValue(key, value))
        .thenReturn(null.asInstanceOf[Option[List[BookDetails]]])
      Get(s"/searchVia/key/$key/value/$value") ~>
        solrService.solrRoutes ~>
        check {
          val response = responseAs[String]
          response.contains(s"Error found for data for key : $key & value : $value") shouldEqual true
        }
    }
  }

}
