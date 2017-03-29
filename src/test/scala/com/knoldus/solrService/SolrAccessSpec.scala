package com.knoldus.solrService

import com.knoldus.solrService.factories.{BookDetails, SolrAccess}
import com.typesafe.config.ConfigFactory
import org.apache.solr.client.solrj.impl.HttpSolrClient
import org.apache.solr.client.solrj.response.UpdateResponse
import org.apache.solr.common.SolrInputDocument
import org.apache.solr.common.util.NamedList
import org.scalatest.FunSuite
import org.mockito.Mockito._
import org.scalatest.mock.MockitoSugar


class SolrAccessSpec extends FunSuite with MockitoSugar {
  /*val config = ConfigFactory.load("application.conf")
  val url = config.getString("solr.url")
  val collection_name = config.getString("solr.collection")
  val url_final = url + collection_name*/
  //val solrClientForInsert: HttpSolrClient = new HttpSolrClient.Builder(url).build()
  //val solrClientForExecute: HttpSolrClient = new HttpSolrClient.Builder(url_final).build()

  val mockConnection = mock[HttpSolrClient]

  class MockableSolrAccess extends SolrAccess(mockConnection, mockConnection)

  val mockableSolrAccess = mock[MockableSolrAccess]
  val mockSDOC: SolrInputDocument = mock[SolrInputDocument]

  test("insert data") {
    val book_Details = BookDetails("124569-0000-363",
      Array("book", "education", "solr"),
      "Solr",
      "Henry/",
      Some("education"),
      2,
      "education",
      true,
      1253.1,
      2569)

    /*   when(mockSDOC.addField("id", book_Details.id)).thenReturn(mockSDOC.addField("id",
    book_Details.id))
       when(mockSDOC.addField("cat", book_Details.cat)) thenReturn
       (mockSDOC.addField("cat", book_Details.cat))
       when(mockSDOC.addField("name", book_Details.name)) thenReturn
       (mockSDOC.addField("name", book_Details.name))
       when(mockSDOC.addField("author", book_Details.author)) thenReturn
       (mockSDOC.addField("author", book_Details.author))
       when(mockSDOC.addField("series_t", book_Details.series_t)) thenReturn
       (mockSDOC.addField("series_t", book_Details.series_t))
       when(mockSDOC.addField("sequence_i", book_Details.sequence_i)) thenReturn
       (mockSDOC.addField("sequence_i", book_Details.sequence_i))
       when(mockSDOC.addField("genre_s", book_Details.genre_s)) thenReturn
       (mockSDOC.addField("genre_s", book_Details.genre_s))
       when(mockSDOC.addField("inStock", book_Details.inStock)) thenReturn
       (mockSDOC.addField("inStock", book_Details.inStock))
       when(mockSDOC.addField("price", book_Details.price)) thenReturn
       (mockSDOC.addField("price", book_Details.price))
       when(mockSDOC.addField("pages_i", book_Details.pages_i)) thenReturn
       (mockSDOC.addField("pages_i", book_Details.pages_i))
   */
    mockSDOC.addField("id", book_Details.id)
    mockSDOC.addField("cat", book_Details.cat)
    mockSDOC.addField("name", book_Details.name)
    mockSDOC.addField("author", book_Details.author)
    mockSDOC.addField("series_t", book_Details.series_t)
    mockSDOC.addField("sequence_i", book_Details.sequence_i)
    mockSDOC.addField("genre_s", book_Details.genre_s)
    mockSDOC.addField("inStock", book_Details.inStock)
    mockSDOC.addField("price", book_Details.price)
    mockSDOC.addField("pages_i", book_Details.pages_i)

    val uR = new UpdateResponse
    val a : NamedList[AnyRef] = new NamedList[AnyRef](1)
    a.add("responseHeader", "{status=0,QTime=408}")
    /*a.add("status",new Integer(0))
    a.add("QTime",new Integer(408))*/
    uR.setResponse(a)
print(":::::::::::::::::::::: UR " + uR)
    when(mockConnection
      .add("gettingStarted", mockSDOC)) thenReturn
    (uR)
    val result: Option[Int] = mockableSolrAccess.createOrUpdateRecord(book_Details)
    println("::::::::::::::::::: result value " + result)
    assert(result.contains(0))
  }

}

