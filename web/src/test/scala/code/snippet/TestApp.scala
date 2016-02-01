package code.snippet

import com.recipegrace.web.FDXRecipeReader
import net.liftweb.json.Serialization.write
import net.liftweb.json._

object TestApp extends App {


  implicit val formats = Serialization.formats(NoTypeHints)


  val recipes = FDXRecipeReader.readRecipes("data/FDX/diabetic.fdx")

  recipes.map(f => println(write(f)))

}