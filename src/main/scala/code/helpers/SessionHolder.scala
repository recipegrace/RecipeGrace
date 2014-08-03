package code.helpers

import net.liftweb.http.SessionVar

object SessionHolder {
  object sessionSearchTerm extends SessionVar[String]("")

}