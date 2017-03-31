package com.knoldus.solrService


import akka.http.scaladsl.model.HttpEntity
import akka.http.scaladsl.testkit.ScalatestRouteTest
import com.knoldus.solrService.routes.SolrService
import org.scalatest.{Matchers, Sequential, WordSpec}
import akka.http.scaladsl.model.ContentType._
import akka.http.scaladsl.model.MediaTypes._
import com.knoldus.solrService.factories.{SolrAccess, SolrClientAccess}
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class SolrServiceSpec extends WordSpec with Matchers with ScalatestRouteTest {

  val solrAccessClient = new SolrClientAccess
  val solrAccess = new SolrAccess(solrAccessClient)
  val solrService = new SolrService(solrAccess)

  Sequential

  "The service" should {

    "be able to insert data in the solr" in {
      val json_data =
        """{
          |"id" :"124569-0000-363",
          |"cat" : ["book", "education"],
          |"name" :"Solr knowledge",
          |"author" : "Henry/",
          |"series_t" : "education",
          |"sequence_i" : 2,
          |"genre_s" : "education",
          |"inStock" : true,
          |"price" : 1253.1,
          |"pages_i" : 2569
          |}""".stripMargin
      Post("/insert", HttpEntity(`application/json`, json_data)) ~>
      solrService.solrRoutes ~>
      check {
        responseAs[String].contains("Data is successfully persisted") shouldEqual true
      }
    }


    "be able to update data in the solr" in {

      val json_data =
        """{
          |"id" :"124569-0000-363",
          |"cat" : ["book", "education", "solr"],
          |"name" :"Solr",
          |"author" : "Henry/",
          |"series_t" : "education",
          |"sequence_i" : 2,
          |"genre_s" : "education",
          |"inStock" : true,
          |"price" : 1253.1,
          |"pages_i" : 2569
          |}""".stripMargin
      Post("/insert", HttpEntity(`application/json`, json_data)) ~>
      solrService.solrRoutes ~>
      check {
        responseAs[String].contains("Data is successfully persisted") shouldEqual true
      }
    }

    "be able to fetch all data in the solr" in {
      Get("/getall") ~> solrService.solrRoutes ~> check {
        responseAs[String].contains("Find List of books when fetch all records :") shouldEqual true
      }
    }

    "be able to search data with the help of keyword in the solr" in {
      val keyword = "fantasy"
      Get(s"/search/keyword/$keyword") ~> solrService.solrRoutes ~> check {
        responseAs[String].contains(s"Find books for $keyword and name is :") shouldEqual true
      }
    }

    "not be able to search data with the help of keyword in the solr" in {
      val keyword = "Horror"
      Get(s"/search/keyword/$keyword") ~> solrService.solrRoutes ~> check {
        responseAs[String].contains(s"No Book Found books for $keyword") shouldEqual true
      }
    }

    "be able to search data with the help of key and value in the solr" in {
      val key = "genre_s"
      val value = "fantasy"
      Get(s"/searchVia/key/$key/value/$value") ~> solrService.solrRoutes ~> check {
        responseAs[String]
          .contains(s"Find books for key : $key & value : $value and name is :") shouldEqual true
      }
    }

    "not be able to search data with the help of key and value in the solr" in {
      val key = "genre_s"
      val value = "Horror"
      Get(s"/searchVia/key/$key/value/$value") ~> solrService.solrRoutes ~> check {
        responseAs[String]
          .contains(s"No Books Found for key : $key & value : $value") shouldEqual true
      }
    }
  }
}
