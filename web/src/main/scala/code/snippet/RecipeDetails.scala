package code
package snippet

import scala.xml.NodeSeq
import com.recipe.MXPRecipeReader._
import net.liftweb._
import net.liftweb.http._
import util._
import common._
import Helpers._
import com.recipe.LineHelper
import com.recipe.MXPSingleIngredient
import com.recipe.MXPSingleIngredient
import com.recipe.RecipeToViewConverter
import code.helpers.SessionHolder._
import com.recipe.ErrorRecipe
class RecipeDetails extends LineHelper with RecipeToViewConverter {


  def getSelectedRecipe() = {
     selectedRecipe.get match {
       case Some(x) => x
       case _=>ErrorRecipe
       
     }

  }
  def showDetails(in: NodeSeq): NodeSeq = {
    
    recipeToView(getSelectedRecipe)
     
  }
}