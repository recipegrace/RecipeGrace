package code
package snippet

import com.recipegrace.web.Recipe
import net.liftweb._
import net.liftweb.http._
import util._
import Helpers._
import scala.xml._
import net.liftweb.http.js.JsCmd
import org.apache.lucene.store.FSDirectory
import java.io.File
import code.helpers.SessionHolder._
import com.recipegrace.web.IndexHelper._
class ListRecipes extends PaginatorSnippet[Recipe] {
  
  val directory =FSDirectory.open(new File(indexPath))
  
  override def count = searchRecipeSize(sessionSearchTerm.get,directory)
  override def page = searchRecipes (sessionSearchTerm.get,curPage, itemsPerPage,directory)



def submitHandler(recipe: Recipe): JsCmd = {
    S.redirectTo("details", () => {
    
       selectedRecipe.set(Some(recipe))
      S.notice( recipe.getTitle )

    })
  }	
  
    def renderPage(in: NodeSeq): NodeSeq = page.flatMap(t =>{
     	 val button = <span>{t.getTitle}</span>
    bind("m", in, 
        "name" -> SHtml.link("", () => submitHandler(t),button),
          "categories" ->t.getCategories.mkString(",")
        )
    }	
  )

    

   
}