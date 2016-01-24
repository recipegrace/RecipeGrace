package code.snippet

import com.recipe.LineHelper
import com.recipe.TestHelper
import net.liftweb._
import net.liftweb.http._
import util._
import common._
import Helpers._
import scala.xml.NodeSeq
import com.recipe.RecipeToViewConverter
import scala.xml.Attribute._
import net.liftweb.json._
import net.liftweb.json.Serialization.{read, write}
import com.recipe.FDXRecipeReader
object TestApp extends App {


implicit val formats = Serialization.formats(NoTypeHints)


 val recipes =FDXRecipeReader.readRecipes("data/FDX/diabetic.fdx")
 
 recipes.map(f=> println(write(f)))

}