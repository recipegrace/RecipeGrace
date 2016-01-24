package com.recipegrace.web

import net.liftweb.json.{Serialization, NoTypeHints}
import net.liftweb.json.Serialization._

import scala.xml.{Elem, Node, NodeSeq, XML}

object FDXRecipeReader extends RecipeReader[FDXRecipe] {

  def getRecipe(lines: List[String]): FDXRecipe = {
    val xml = XML.loadString(lines.mkString("\n"))
    readRecipes(xml).head

  }
  def readRecipes(file: String): List[FDXRecipe] = {
    val xml = XML.loadFile(file)
    readRecipes(xml)
  }
  def loadRecipe(content:String) = {
    implicit val formats = Serialization.formats(NoTypeHints)
    read[FDXRecipe](content)
  }

  def readRecipes(xml: Elem): List[FDXRecipe] = {

    def toFDXRecipe(recipeNode: Node): FDXRecipe = {
     
      def &(node: Node, x: String) = {
        node.attribute(x).getOrElse("").toString
      }

      def getIngredients(nodes: NodeSeq): Seq[FDXIngredient] = {

        nodes.map(f => {
          def &&(x: String) = {
            &(f, x)
          }
          val heading = if (&&("Heading") == "Y") true else false
          FDXIngredient(&&("Quantity"), &&("Unit"), &&("IngredientName"), heading)

        })
      }
      val ingredients = getIngredients(recipeNode \\ "RecipeIngredient")
      val process = (recipeNode \\ "ProcedureText").map(f => f.text)

      /*
							 *  //<Recipe Name="" ID="" CookbookChapterID="" Servings="" PreparationTime="" CookingTime="" ReadyInTime=""
  //RecipeTypes="" Source="Nestlé" WebPage="www.verybestmeals.com" CreateDate="12/03/2006">
  case class FDXRecipe(title: String, servingSize: String, cookingTime: String, readyTime:String,
      categories: Seq[String],ingredients: FDXIngredient, process: Seq[String],
      source:String,webPage:String,createDate:String ) 
							 * 
							 */
      def &&(x: String) = {
        &(recipeNode, x)
      }
      val categories: Seq[String] = &&("RecipeTypes").split(",")
      FDXRecipe(&&("Name"), &&("Servings"), &&("PreparationTime"), &&("CookingTime"), &&("ReadyInTime"), categories, ingredients, process, &&("Source"), &&("WebPage"), &&("CreateDate"))

    }
    val recipeNodes = (xml  \ "Recipes" \ "Recipe")

    (for (recipe <- recipeNodes)
      yield (toFDXRecipe(recipe))).toList
  }

}