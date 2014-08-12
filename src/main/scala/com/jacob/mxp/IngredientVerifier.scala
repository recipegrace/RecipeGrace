package com.jacob.mxp

import scala.io.Source
import scala.collection.mutable.MutableList
import com.jacob.mxp.MXPRecipeReader._
import com.jacob.mxp.ParserHelper._


object IngredientVerifier extends App {


val recipes=readRecipes("/Users/fjacob/backup/fjacob/projects/recipes/data/MXP/BBQ-ARC2.MXP");
val ingredients= ( (recipes.flatMap(f => f.ingredients.getContent().flatMap(j => j._2)))
			    .foreach(f=> f match {
			      case x: MXPSubsituteIngredients => println(x)
			      case _ =>
			    } 
			        )) 
//				.map(k => k.ingredient).distinct.foreach(println(_))

//saltPepper.distinct.foreach(f=> println(f))
  
  

}