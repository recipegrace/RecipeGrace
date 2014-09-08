package code.helpers

import net.liftweb.http.SessionVar

object SessionHolder {
  object sessionSearchTerm extends SessionVar[String]("")
  object selectedRecipe extends SessionVar[(List[String],String)](List(),"")

}