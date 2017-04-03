package com.knoldus.solrService

import com.knoldus.solrService.factories.{BookDetails, SolrClientAccess}
import org.scalatest.FunSuite

class SolrClientAccessSpec extends FunSuite {

  val book = BookDetails("17", Array("computer", "science", "result"), "let us C", "Yashwant Kanetkar", Some("2"), 1, "education", true, 1.3, 22)
  private val solrAccessClient = new SolrClientAccess

  test("to create or update record if already created") {
    val result = solrAccessClient.insertRecord(book)
    assert(result.contains(0))
  }

  test("to fetch data matching a keyword") {
    val response = solrAccessClient.fetchData("id:17")
    val result = response.getOrElse(Nil).map(_.author)
    assert(result.contains(book.author))
  }

/*
  test("to catch exception when record is not inserted") {
     val response = solrAccessClient.insertRecord(null)
     assert(response == None)
  }
*/


}
