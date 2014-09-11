package code.helpers

import net.liftweb.http.SessionVar
import com.recipe._

object SessionHolder {
  object sessionSearchTerm extends SessionVar[String]("")
  object selectedRecipe extends SessionVar[Option[Recipe]](None)

}