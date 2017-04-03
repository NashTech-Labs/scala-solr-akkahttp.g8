package com.knoldus.solrService

import com.knoldus.solrService.factories.{BookDetails, SolrAccess, SolrClientAccess}
import org.junit.runner.RunWith
import org.mockito.Mockito._
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import org.scalatest.mock.MockitoSugar

@RunWith(classOf[JUnitRunner])
class SolrAccessSpec extends FunSuite with MockitoSugar {

  private val solrAccessClient = mock[SolrClientAccess]
  private val solrAccess = new SolrAccess(solrAccessClient)
  val book = BookDetails("12", Array("science"), "chemistry", "Prateek", Some("second"), 1, "reference", true, 1.3, 22)

  test("to create or update record if already created") {
    when(solrAccessClient.insertRecord(book)).thenReturn(Some(1))
    val result: Option[Int] = solrAccess.createOrUpdateRecord(book)
    assert(result.contains(1))
  }

  test("to  find all record") {
    when(solrAccessClient.fetchData("*:*")).thenReturn(Some(List(book)))
    val result: Option[List[BookDetails]] = solrAccess.findAllRecord
    assert(result.contains(List(book)))
  }

  test("to  find record with keyword") {
    when(solrAccessClient.fetchData("reference")).thenReturn(Some(List(book)))
    val result: Option[List[BookDetails]] = solrAccess.findRecordWithKeyword("reference")
    assert(result.contains(List(book)))
  }

  test("to  find record with key and value") {
    when(solrAccessClient.fetchData("id:12")).thenReturn(Some(List(book)))
    val result: Option[List[BookDetails]] = solrAccess.findRecordWithKeyAndValue("id", "12")
    assert(result.contains(List(book)))
  }

  test("catch an exception while fetching record") {
    when(solrAccessClient.fetchData("id:17")).thenReturn(null.asInstanceOf[Option[List[BookDetails]]])
    val result = solrAccess.findRecordWithKeyAndValue("id", "17")
    assert(result == null)
  }



}
