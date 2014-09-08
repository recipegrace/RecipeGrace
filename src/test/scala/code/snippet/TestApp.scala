package code.snippet

import com.recipe.LineHelper
import code.helpers.TestHelper
import net.liftweb._
import net.liftweb.http._
import util._
import common._
import Helpers._
import scala.xml.NodeSeq
import com.recipe.RecipeToViewConverter
import scala.xml.Attribute._

object TestApp extends App with LineHelper with TestHelper with RecipeToViewConverter {
  def showDetails(in: NodeSeq): NodeSeq = {

    val recipe = testRecipe
    val temp = bind("r", in,
      "title" -> recipe.title,
      "categories" -> recipe.categories.mkString(","),
      "credit" -> recipe.credit)

    mergeNonEmptyLines(recipe.process).flatMap(f => {
      bind("m", temp,
        "process" -> f)

    })

  }
  def showIngredients(in: NodeSeq): NodeSeq = {
    val recipe = testRecipe
    convertMXPToIngrTable(recipe.ingredients.getContent()).flatMap(t =>

      bind("p", in,
        "specification" -> t._1,
        "ingredient" -> t._2))
  }

  
    val in = 	<div>  
<div class="row text-center" data-lift="lift:RecipeDetails.showDetails">        
    <h2> <r:title/></h2>
<dl>
  <dt>Credit</dt>
  <dd><r:credit/></dd>
    <dt>Categories</dt>
  <dd><r:categories/></dd>
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
           <tr data-lift="lift:RecipeDetails.showIngredients">
                <td><p:specification/></td>
                <td><p:ingredient /></td>
                </tr>

            
        </tbody>
    </table>
  <p><m:process/></p>
</div>
   </div>
  println(showIngredients(in))

}