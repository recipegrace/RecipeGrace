package code.snippet
import com.recipe.IndexHelper._
import org.apache.lucene.store.FSDirectory
import java.io.File
import com.recipe.RecipeToViewConverter
import com.recipe.MXPIngredient
import com.recipe.MXPRecipe

object SearchApp extends App with RecipeToViewConverter {

    val directory =FSDirectory.open(new File(indexPath))
  val recipes =searchRecipes ("indian flat bread",0, 10,directory)
  
  println(convertMXPToIngrTable(recipes(2).asInstanceOf[MXPRecipe].ingredients.getContent).mkString("\n"))
}