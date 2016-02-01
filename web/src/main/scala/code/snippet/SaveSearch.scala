package code
package snippet

import net.liftweb._
import http._
import net.liftweb.http.js.JsCmds.FocusOnLoad
import util.Helpers._
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
    "name=searchTerm" #> FocusOnLoad(SHtml.text("",f=> searchTerm=f,  "class"->"form-control input-lg" )) & // set the name
      "type=submit" #> SHtml.onSubmitUnit(process)
  }
}

