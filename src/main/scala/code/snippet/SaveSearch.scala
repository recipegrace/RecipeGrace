package code
package snippet

import net.liftweb._
import http._
import util.Helpers._
import scala.xml.NodeSeq
import code.helpers.SessionHolder._
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
    "name=searchTerm" #> SHtml.onSubmit(searchTerm = _) & // set the name
      "type=submit" #> SHtml.onSubmitUnit(process)
  }
}

