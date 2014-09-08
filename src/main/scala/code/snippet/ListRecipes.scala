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
import com.recipe.MXPRecipe
import com.recipe.IndexHelper._
import org.apache.lucene.store.FSDirectory
import java.io.File
import code.helpers.SessionHolder._

class ListRecipes extends PaginatorSnippet[MXPRecipe] {
  
  val directory =FSDirectory.open(new File(indexPath))
  
  override def count = searchRecipeSize(sessionSearchTerm.get,directory)
  override def page = searchRecipes (sessionSearchTerm.get,curPage, itemsPerPage,directory)



def submitHandler(recipe: MXPRecipe): JsCmd = {
    S.redirectTo("details", () => {
      val recipeName = recipe.title 
       selectedRecipe.set( (recipe.originalLines,"MXP"))
      S.notice("Selected " + recipeName  )

    })
  }	
    def renderPage(in: NodeSeq): NodeSeq = page.flatMap(t =>
    bind("m", in, 
        "name" -> t.title,
          "categories" ->t.categories.mkString(","),
        
          "submit" -> SHtml.submit("( ͡° ͜ʖ ͡°)", () => submitHandler(t))
        )

  )

    

   
}