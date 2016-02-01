package code.helpers

import com.recipegrace.web.Recipe
import net.liftweb.http.SessionVar

object SessionHolder {

  object sessionSearchTerm extends SessionVar[String]("")

  object selectedRecipe extends SessionVar[Option[Recipe]](None)

}