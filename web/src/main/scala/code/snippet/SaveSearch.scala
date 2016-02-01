package code
package snippet

import code.helpers.SessionHolder._
import net.liftweb.http._
import net.liftweb.http.js.JsCmds.FocusOnLoad
import net.liftweb.util.Helpers._

/**
  * A snippet that grabs the query parameters
  * from the form POST and processes them
  */

object SaveSearch {

  def render = {
    var searchTerm = ""
    def process() = {
      if (searchTerm.trim().length() < 3) S.error("Too short!")
      else {
        S.notice("Searching ...: " + searchTerm)
        sessionSearchTerm.set(searchTerm)
        S.redirectTo("recipe/list")
      }
    }
    "name=searchTerm" #> FocusOnLoad(SHtml.text("", f => searchTerm = f, "class" -> "form-control input-lg")) & // set the name
      "type=submit" #> SHtml.onSubmitUnit(process)
  }
}

