package com.knoldus.solrService

import akka.http.scaladsl.model.MediaTypes._
import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
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

    "be able to get all record" in {
      when(solrJsonFormatter.formatBookDetails(json_data)).thenReturn(bookDetails)
      when(solrAccess.findAllRecord).thenReturn(Some(List(bookDetails)))
      Get("/getall") ~>
        solrService.solrRoutes ~>
        check {
          val response = responseAs[String]
          response.contains("Find List of books when fetch all records :") shouldEqual true
        }
    }

    "be able to search record with a keyword" in {
      val keyword = "fantasy"
      when(solrJsonFormatter.formatBookDetails(json_data)).thenReturn(bookDetails)
      when(solrAccess.findRecordWithKeyword(keyword)).thenReturn(Some(List(bookDetails)))
      Get(s"/search/keyword/$keyword") ~>
        solrService.solrRoutes ~>
        check {
          val response = responseAs[String]
          response.contains(s"Find books for $keyword and name is :") shouldEqual true
        }
    }

    "not be able to search record with a keyword" in {
      val keyword = "horror"
      when(solrJsonFormatter.formatBookDetails(json_data)).thenReturn(bookDetails)
      when(solrAccess.findRecordWithKeyword(keyword)).thenReturn(Some(List(bookDetails)))
      Get(s"/search/keyword/$keyword") ~>
        solrService.solrRoutes ~>
        check {
          val response = responseAs[String]
          response.contains(s"Find books for $keyword and name is :") shouldEqual true
        }
    }

    "be able to search record according to key and value" in {
      val key = "genre_s"
      val value = "fantasy"
      when(solrJsonFormatter.formatBookDetails(json_data)).thenReturn(bookDetails)
      when(solrAccess.findRecordWithKeyAndValue(key, value)).thenReturn(Some(List(bookDetails)))
      Get(s"/searchVia/key/$key/value/$value") ~>
        solrService.solrRoutes ~>
        check {
          val response = responseAs[String]
          response.contains(s"Find books for key : $key & value : $value and name is") shouldEqual true
        }
    }

    "not be able to search record according to key and value" in {
      val key = "genre_s"
      val value = "horror"
      when(solrJsonFormatter.formatBookDetails(json_data)).thenReturn(bookDetails)
      when(solrAccess.findRecordWithKeyAndValue(key, value)).thenReturn(Some(List(bookDetails)))
      Get(s"/searchVia/key/$key/value/$value") ~>
        solrService.solrRoutes ~>
        check {
          val response = responseAs[String]
          response.contains(s"Find books for key : $key & value : $value and name is :") shouldEqual true
        }
    }
  }

}