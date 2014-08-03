package code
package snippet

import net.liftweb._
import http._
import scala.xml.NodeSeq
import code.helpers.SessionHolder._
/**
 * A snippet that grabs the query parameters
 * from the form POST and processes them
 */

object SaveSearch {
  def render(in: NodeSeq): NodeSeq = {
    
    // use a Scala for-comprehension to evaluate each parameter
    for {
   r <- S.request if r.post_? // make sure it's a post
   searchTerm <- S.param("searchTerm") // get the name field
    
    } {
      // if everything goes as expected,
      // display a notice and send the user 
      S.notice("Searching ...: "+searchTerm)
      sessionSearchTerm.set(searchTerm)
      S.redirectTo("/list")
    }
    // pass through the HTML if we don't get a post and
    // all the parameters
    in
  }
}
 
