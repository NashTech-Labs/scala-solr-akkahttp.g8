package com.knoldus.solrService.factories

import com.google.inject.Inject

case class BookDetails(
                        id: String,
                        cat: Array[String],
                        name: String,
                        author: String,
                        series_t: Option[String],
                        sequence_i: Int,
                        genre_s: String,
                        inStock: Boolean,
                        price: Double,
                        pages_i: Int)

class SolrAccess @Inject()(solrClientAccess: SolrClientAccess) {

  /**
    * This method takes a parameter of Book_Details and then insert data or update data if that is
    * present into solr collection. It match unique key and in our case that is id.
    *
    * @param book_Details
    * @return
    */

  def createOrUpdateRecord(book_Details: BookDetails): Option[Int] = {
    solrClientAccess.insertRecord(book_Details)
  }

  /**
    * This method will return total count of records in your solr
    *
    * @return
    */

  def findAllRecord: Option[List[BookDetails]] = {

    solrClientAccess.fetchData("*:*") match {
      case Some(data) => Some(data)
      case None => None
    }
  }

  /**
    * This method will take a keyword and fetch all the record related to that keyword
    *
    * @param keyword eg: fantasy
    * @return total count of the record
    */

  def findRecordWithKeyword(keyword: String): Option[List[BookDetails]] = {
    solrClientAccess.fetchData(keyword)
  }

  /**
    * This method will take a key and value, after this it will fetch all the record related to
    * that
    * key and value. eg: ("name", "scala")
    *
    * @param key   : name
    * @param value : scala
    * @return
    */

  def findRecordWithKeyAndValue(key: String, value: String): Option[List[BookDetails]] = {
    val keyValue = s"$key:" + s"${value.trim}"
    solrClientAccess.fetchData(keyValue)
  }
}
