package com.jacob.mxp

import scala.io.Source
import scala.collection.mutable.MutableList

case class MXPIngredient(amount:String,measure:String, ingredient:String)
case class MXPRecipe (title:String,servingSize:String,cookingTime:String,categories:Set[String], ingredients:Set[MXPIngredient], process:String)

object IngredientVerifier extends App {

  val lines = Source.fromFile("/Users/fjacob/projects/recipes/data/MXP/BBQ-ARC1.MXP")("ISO-8859-1").getLines()
//    val lines = Source.fromFile("/Users/fjacob/projects/recipes/data/MXP/webducks.mxp")("ISO-8859-1").getLines()

  var count = 0
  var count1 = 0
  val ingredeientPattern = """(Amount\s*Measure\s*Ingredient\s*--\s*Preparation Method)""".r
  val beginLine="*  Exported from  MasterCook"
  val endLine="""- - - - - - - - - - - - - - - - - - """
  var recipes=Set[MXPRecipe] ()
  for (line <- lines) {	   
    if(line.contains(beginLine)){
      count=count+1
      var recipeLines = List[String]()
      var currentLine= lines.next()
       while (!currentLine.contains(endLine)) {
  //  	   println(recipeLines.size)
           recipeLines=recipeLines:+(currentLine)
   //         println(recipeLines.size)
           currentLine=lines.next()
        }
      if(recipeLines.size >0)
      recipes=recipes+(parseRecipe(recipeLines))
    }
  }
  for(recipe<-recipes){
    println(recipe)
  }


  // (title:String,servingSize:String,categories:Set[String], ingredients:Set[MXPIngredient], process:String)
  def parseRecipe(recipeLines: List[String]) = {
	// println(recipeLines)
	 val title =recipeLines(1).trim()
	 val servingSizeAndTimePattern = """^.*Serving Size  : (.*) Preparation Time :(.*)$""".r
	 val servingSizeAndTimePattern(size,time)= recipeLines(4).trim()
	
	 var from =5
	 var line = recipeLines(from).trim.split(":")(1).trim()
	 var categories=Set[String]()
	 while(line.trim().size>0){
	   categories=categories++ getSetSplitByCommaAndSpace(line)
	   from =from +1
	   line=recipeLines(from).trim
	 }
	   from =from +3
	   line=recipeLines(from).trim	   
	 var ingredients=Set[MXPIngredient]()
	 while(line.trim().size>0){
	   ingredients=ingredients+ getIngredients(line)
	   from =from +1
	   line=recipeLines(from).trim
	 }
	 
     val recipe= MXPRecipe(title,size.trim(),time.trim(), categories,Set[MXPIngredient](),"")
    
     recipe
    }

  def getSetSplitByCommaAndSpace(line: String) = {
	 val set= line.split(",").toSet.foldLeft(MutableList[String]())((r,c)=>	 r.++( c.split("    ").toList)).toSet
	 val modifiedSet=for{
	   each <- set
	   if(each.trim().size>0)
	 }
	   yield each.trim()
   modifiedSet.toSet
  }

  def getIngredients(line: String) = {
	  println(line)
    	 val ingredientDetail = """^(.*)\s+(.*)\s+(.*)$""".r
    	 val ingredientDetail(amout,measure,ingredient) = line
    	 val ingred= MXPIngredient(amout,measure,ingredient)
    	 ingred
  }

}