package com.jacob.mxp

import scala.io.Source
import scala.collection.mutable.MutableList
import com.jacob.mxp.MXPRecipeReader._

object IngredientVerifier extends App {


val recipes=readRecipes("/Users/fjacob/backup/fjacob/projects/recipes/data/MXP/BBQ-ARC1.MXP");
//val ingredients= ( (recipes.flatMap(f => f.ingredients.getContent().flatMap(j => j._2))).flatMap(f=> f.notes)).distinct.foreach(println(_))

//saltPepper.distinct.foreach(f=> println(f))
  
  

}