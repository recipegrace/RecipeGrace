package code
package snippet
import net.liftweb._
import net.liftweb.http._
import util._
import common._
import Helpers._
import TimeHelpers._
import net.liftweb.common.Logger
import net.liftweb.mapper._
import scala.xml._
import net.liftweb.http.js.JsCmd
import com.recipe.MXPRecipeReader._
import com.recipe.MXPRecipeReader._
import com.recipe.MXPRecipe
class ListRecipes extends PaginatorSnippet[MXPRecipe] {
  
  override def count = findSize()
  override def page = findPage (curPage, itemsPerPage)



def submitHandler(recipe: MXPRecipe): JsCmd = {
    S.redirectTo("listusers", () => {
      val recipeName = recipe.title 
    
      S.notice("Clicked " + recipeName  )

    })
  }
    def renderPage(in: NodeSeq): NodeSeq = page.flatMap(t =>
    bind("m", in, 
        "name" -> t.title,
          "categories" ->t.categories.mkString(","),
        
          "submit" -> SHtml.submit("( ͡° ͜ʖ ͡°)", () => submitHandler(t))
        )

  )

    
  def findSize () = {
       val recipes =readRecipes("/Users/fjacob/backup/fjacob/projects/recipes/data/MXP/BBQ-ARC1.MXP").toList
    //println("Total, there are "+recipes.size)
    recipes.size
  }
      
  def findPage (curPage:Int, itemsPerPage:Int) = {
     val recipes =readRecipes("/Users/fjacob/backup/fjacob/projects/recipes/data/MXP/BBQ-ARC1.MXP").toList
   
      val combinations =recipes.grouped(itemsPerPage)
       if (combinations.isEmpty) List() else combinations.toList(curPage)
  }
}