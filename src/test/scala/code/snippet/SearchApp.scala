package code.snippet
import com.recipe.IndexHelper._
import org.apache.lucene.store.FSDirectory
import java.io.File
import com.recipe.RecipeToViewConverter
import com.recipe.MXPIngredient
import com.recipe.MXPRecipe
import com.recipe.FDXRecipe

object SearchApp extends App with RecipeToViewConverter {

    val directory =FSDirectory.open(new File(indexPath))
  val recipes =searchRecipes ("Beets & Carrots With West Indian Spices",0, 10,directory)
  
  //println(convertFDXToIngrTable(recipes(0).asInstanceOf[FDXRecipe].ingredients).mkString("\n"))
  println( recipes(0).getProcess)
  println(mergeLines( recipes(0).getProcess))
} 