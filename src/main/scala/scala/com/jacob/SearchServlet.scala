package com.jacob

import org.scalatra._
import scalate.ScalateSupport

class SearchServlet extends RecipegraceStack {

  get("/") {
    contentType="text/html"
    ssp("index")
  }
   post("/search") {
    contentType="text/html"
    <html>
      <title>Recipe results for:{params("query")}</title>
    	<body>We don't have any results for anything including:{params("query")}</body>
</html>
  } 
}
