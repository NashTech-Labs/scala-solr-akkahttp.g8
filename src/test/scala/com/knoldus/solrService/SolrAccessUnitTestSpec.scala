package com.knoldus.solrService

import com.knoldus.solrService.factories.{BookDetails, SolrAccess, SolrClientAccess}
import org.junit.runner.RunWith
import org.mockito.Mockito._
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import org.scalatest.mock.MockitoSugar

@RunWith(classOf[JUnitRunner])
class SolrAccessUnitTestSpec extends FunSuite with MockitoSugar {

  private val solrAccessClient = mock[SolrClientAccess]
  private val solrAccess = new SolrAccess(solrAccessClient)
  val book = BookDetails("12", Array("abc"), "jhvh", "ghvghv", Some("avc"), 1, "dfg", true, 1.3, 22)

  test("to create or update record if already created") {
    when(solrAccessClient.insertRecord(book)).thenReturn(Some(1))
    val result: Option[Int] = solrAccess.createOrUpdateRecord(book)
    assert(result == Some(1))
  }

  test("to  find all record") {
    when(solrAccessClient.fetchData("*:*")).thenReturn(Some(List(book)))
    val result: Option[List[BookDetails]] = solrAccess.findAllRecord
    assert(result == Some(List(book)))
  }

  test("to  find record with keyword") {
    when(solrAccessClient.fetchData(" ")).thenReturn(Some(List(book)))
    val result: Option[List[BookDetails]] = solrAccess.findRecordWithKeyword(" ")
    assert(result == Some(List(book)))
  }

  test("to  find record with key and value") {
    when(solrAccessClient.fetchData("id:12")).thenReturn(Some(List(book)))
    val result: Option[List[BookDetails]] = solrAccess.findRecordWithKeyAndValue("id", "12")
    assert(result == Some(List(book)))
  }



}
