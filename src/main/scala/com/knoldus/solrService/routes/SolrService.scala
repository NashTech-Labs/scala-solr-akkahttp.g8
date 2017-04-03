package com.knoldus.solrService.routes

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{ExceptionHandler, Route}
import com.knoldus.solrService.factories.{BookDetails, SolrAccess}
import com.typesafe.config.ConfigFactory
import spray.json.DefaultJsonProtocol

object BookJsonSupport extends DefaultJsonProtocol with SprayJsonSupport {
  implicit val PortofolioFormats = jsonFormat10(BookDetails)
}

import com.knoldus.solrService.routes.BookJsonSupport._

class SolrService(SolrAccess: SolrAccess) {

  val config = ConfigFactory.load("application.conf")

  implicit def myExceptionHandler = {
    ExceptionHandler {
      case e: ArithmeticException =>
        extractUri { uri =>
          complete(HttpResponse(StatusCodes.InternalServerError,
            entity = s"Data is not persisted and something went wrong"))
        }
    }
  }

  val solrRoutes = {
    post {
      path("insert") {
        entity(as[BookDetails]) { entity =>
          complete {
            try {
              val isPersisted: Option[Int] = SolrAccess.createOrUpdateRecord(entity)
              isPersisted match {
                case Some(data) => HttpResponse(StatusCodes.Created,
                  entity = "Data is successfully persisted")
                case None => HttpResponse(StatusCodes.InternalServerError,
                  entity = "Error while persisting data")
              }
            } catch {
              case ex: Throwable =>
                println(ex, ex.getMessage)
                HttpResponse(StatusCodes.InternalServerError,
                  entity = "Error while persisting data")
            }
          }
        }
      }
    } ~ path("getall") {
      get {
        complete {
          try {
            val idAsRDD: Option[List[BookDetails]] = SolrAccess.findAllRecord
            idAsRDD match {
              case Some(data) =>
                if (data.nonEmpty) {
                  val book_name: String = data.map(book_record => book_record.name).mkString(",")
                  println("List of books when fetch all records : " + book_name)
                  HttpResponse(StatusCodes.OK,
                    entity = s"Find List of books when fetch all records : $book_name")
                } else {
                  HttpResponse(StatusCodes.OK, entity = s"No Book Found")
                }
              case None => HttpResponse(StatusCodes.InternalServerError,
                entity = s"Data is not fetched and something went wrong")
            }
          } catch {
            case ex: Throwable =>
              println(ex, ex.getMessage)
              HttpResponse(StatusCodes.InternalServerError,
                entity = s"Error found for data")
          }
        }
      }
    } ~ path("search" / "keyword" / Segment) { (keyword: String) =>
      get {
        complete {
          try {
            val isSearched: Option[List[BookDetails]] = SolrAccess.findRecordWithKeyword(keyword)
            isSearched match {
              case Some(data) =>
                if (data.nonEmpty) {
                  val book_name: String = data.map(book_record => book_record.name).mkString(",")
                  println(s"List of books when fetch record with keyword: $keyword: " + book_name)
                  HttpResponse(StatusCodes.OK,
                    entity = s"Find books for $keyword and name is : $book_name")
                } else {
                  HttpResponse(StatusCodes.OK, entity = s"No Book Found books for $keyword")
                }
              case None => HttpResponse(StatusCodes.InternalServerError,
                entity = s"Error found data for keyword : $keyword")
            }
          } catch {
            case ex: Throwable =>
              println(ex, ex.getMessage)
              HttpResponse(StatusCodes.InternalServerError,
                entity = s"Error found for keyword : $keyword")
          }
        }
      }
    } ~ path("searchVia" / "key" / Segment / "value" / Segment) { (key: String, value: String) =>
      get {
        complete {
          try {
            val isSearched: Option[List[BookDetails]] = SolrAccess
              .findRecordWithKeyAndValue(key, value)
            isSearched match {
              case Some(data) =>
                if (data.nonEmpty) {
                  val book_name: String = data.map(book_record => book_record.name).mkString(",")
                  println(s"List of books when fetch ecord with key : $key and value : $value : " +
                          book_name)
                  HttpResponse(StatusCodes.OK,
                    entity = s"Find books for key : $key & value : $value and name is : $book_name")
                } else {
                  HttpResponse(StatusCodes.OK,
                    entity = s"No Books Found for key : $key & value : $value")
                }
              case None => HttpResponse(StatusCodes.InternalServerError,
                entity = s"Error found data for key : $key & value : $value")
            }
          } catch {
            case ex: Throwable =>
              println(ex, ex.getMessage)
              HttpResponse(StatusCodes.InternalServerError,
                entity = s"Error found for data for key : $key & value : $value")
          }
        }
      }
    }
  }
}
