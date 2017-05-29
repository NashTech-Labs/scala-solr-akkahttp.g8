package com.knoldus.solrService.routes

import com.knoldus.solrService.factories.BookDetails
import org.json4s.DefaultFormats
import org.json4s.jackson.JsonMethods.parse

class SolrJsonFormatter {

  def formatBookDetails(bookDetails: String): BookDetails = {
    implicit val formats = DefaultFormats
    parse(bookDetails.replaceAll("\n", "").replaceAll("\\s+", " ")).extract[BookDetails]
  }
}
