package code
package snippet

import scala.xml.NodeSeq
import com.recipe.MXPRecipeReader._
import code.helpers.TestHelper
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
class RecipeDetails extends LineHelper with RecipeToViewConverter {

  
  def ifEmpty(string:String) = {
    if(string==null || string.length() ==0) "Not available"
    else string
  }
  def getSelectedRecipe() = {
    require(selectedRecipe.is._1 != List())
    require(selectedRecipe.is._2 != "")
    getRecipe(selectedRecipe._1)
  }
  def showDetails(in: NodeSeq): NodeSeq = {
    val recipe = getSelectedRecipe
<div>
<div class="row text-center" >        
    <h2> <r:title/></h2>
<dl>
  <dt>Credit</dt>
  <dd>{ifEmpty(recipe.credit)}</dd>
    <dt>Categories</dt>
  <dd>{ifEmpty(recipe.categories.mkString(","))}</dd>
</dl>
</div>
<div class="bs-example">
    <table class="table table-striped">
        <thead>
            <tr>
              <th>Specification</th>
                <th>Ingredient name and notes</th>
            </tr>
        </thead>
        <tbody>
        {convertMXPToIngrTable(recipe.ingredients.getContent)
          .map(f=>{
            
            <tr> <td> {f._1}</td> <td> {f._2}</td></tr>
          }  )
          
          }
            
        </tbody>
    </table>
  <p>{mergeNonEmptyLines(recipe.process)}</p>
</div>
  </div>
  }
}