package com.recipe
import com.recipe.MXPRecipeReader._
import com.recipe.MXPRecipeReader._


object IngredientVerifier extends App {

 
val recipes=readRecipes("data/MXP/b1q97.txt");
println(s"Found ${recipes.size} recipes")

recipes.foreach(f=>println(f.title))
/*val ingredients= ( (recipes.flatMap(f => f.ingredients.getContent().flatMap(j => j._2)))
			    .foreach(f=> f match {
			      case x: MXPSubsituteIngredients => println(x)
			      case _ =>
			    } 
			        )) */
//				.map(k => k.ingredient).distinct.foreach(println(_))

//saltPepper.distinct.foreach(f=> println(f))
  
  

}