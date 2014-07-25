package code.snippet
import net.liftweb._
import net.liftweb.http._
import util._
import common._
import Helpers._
import TimeHelpers._
import net.liftweb.common.Logger
import net.liftweb.mapper._
import scala.xml._
import code.model.User
import net.liftweb.http.js.JsCmd

class ListUsers extends PaginatorSnippet[User] {
  
    val total = findSize()
  override def page = findPage (curPage, itemsPerPage)
  override def count = total

def submitHandler(user: User): JsCmd = {
    S.redirectTo("listusers", () => {
      val deleteEmail = user.email
      val result =User.delete_!(user)
      if(result)
      S.notice("Deleted " + deleteEmail  )
      else
         S.error("Delete:" + deleteEmail  + " failed")
    })
  }
    def renderPage(in: NodeSeq): NodeSeq = page.flatMap(t =>
    bind("m", in,
        "creationdate" -> t.whenCreated.toDateTime().toString(),
          "username" ->t.username.get,
          "email" -> t.email.get,
          "submit" -> SHtml.submit("X", () => submitHandler(t))
        )

  )

    
  def findSize () = {
    User.count
  }
  def findPage (curPage:Int, itemsPerPage:Int) = {
      val combinations = User.findAll.grouped(itemsPerPage)
       if (combinations.isEmpty) List() else combinations.toList(curPage)
  }
}