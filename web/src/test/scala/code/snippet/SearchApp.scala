package code.snippet

import java.io.File

import com.recipegrace.web.RecipeToViewConverter
import org.apache.lucene.store.FSDirectory
import com.recipegrace.web.IndexHelper._

object SearchApp extends App with RecipeToViewConverter {

  val directory = FSDirectory.open(new File(indexPath))
  val recipes = searchRecipes("Beets & Carrots With West Indian Spices", 0, 10, directory)

  //println(convertFDXToIngrTable(recipes(0).asInstanceOf[FDXRecipe].ingredients).mkString("\n"))
  println(recipes(0).getProcess)
  println(mergeLines(recipes(0).getProcess))
} 