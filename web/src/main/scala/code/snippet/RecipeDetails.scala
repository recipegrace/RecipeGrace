package code
package snippet

import code.helpers.SessionHolder._
import com.recipegrace.web.{ErrorRecipe, Recipe, RecipeToViewConverter}

import scala.xml.NodeSeq

class RecipeDetails extends RecipeToViewConverter {


  def getSelectedRecipe(): Recipe = {
    selectedRecipe.get match {
      case Some(x: Recipe) => x
      case _ => ErrorRecipe

    }

  }

  def showDetails(in: NodeSeq): NodeSeq = {

    recipeToView(getSelectedRecipe())

  }
}