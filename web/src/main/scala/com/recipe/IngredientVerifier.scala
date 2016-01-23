package com.recipe
import com.recipe.MXPRecipeReader._
import com.recipe.MXPRecipeReader._
import java.io.File


object IngredientVerifier extends App {

 
/*val recipes=readRecipes("data/MXP/b1q97.txt");
println(s"Found ${recipes.size} recipes")

recipes.foreach(f=>println(f.title))*/
/*val ingredients= ( (recipes.flatMap(f => f.ingredients.getContent().flatMap(j => j._2)))
			    .foreach(f=> f match {
			      case x: MXPSubsituteIngredients => println(x)
			      case _ =>
			    } 
			        )) */
//				.map(k => k.ingredient).distinct.foreach(println(_))

//saltPepper.distinct.foreach(f=> println(f))
  
  val files = new File("data/FDX").listFiles()
  val fDXFiles= for(file <-files
 if(file.getName().endsWith("fdx")))
   
   yield file
   
  val recipesList= (for(fDXFile <- fDXFiles)
    
    yield (FDXRecipeReader.readRecipes(fDXFile.getAbsolutePath()))).flatten
println(s"Found ${recipesList.size} recipes")
}