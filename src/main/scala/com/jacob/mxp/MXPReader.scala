package com.jacob.mxp

import scala.io.Source
import scala.collection.mutable.MutableList
import com.jacob.mxp.ParserHelper._

object MXPReader{
  
  
def getMXPRecipes(location:String)= {
      val lines = Source.fromFile(location)("ISO-8859-1").getLines()
//    val lines = Source.fromFile("/Users/fjacob/projects/recipes/data/MXP/webducks.mxp")("ISO-8859-1").getLines()

  var count = 0
  var count1 = 0
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
      
     // println(emptyMap)
      recipes
  }

 val ServingSizeAndTimePattern = """^.*Serving Size  : (.*) Preparation Time :(.*)$""".r
 val RecipeBy ="""^\s*Recipe By     :\s*(.*)\s*$""".r
  // (title:String,servingSize:String,categories:Set[String], ingredients:Set[MXPIngredient], process:String)
 
 var emptyMap :Map[Int, Int] = Map()
  def parseRecipe(recipeLines: List[String]) = {
   val listOfList = ListOfList()
   recipeLines.foldLeft(listOfList)((acc, current) => {
     val isEmpty = current.trim.length ==0
     if(isEmpty) listOfList.startNew else listOfList.add(current)
     listOfList
   })
 // if(!listOfList.getContent(2,0).contains("Amount  Measure       Ingredient -- Preparation Method")) 
      println("helo "+listOfList.getContent(listOfList.getContent().size-1,0))
//   if(recipeLines1.head.trim.length()!=0) println(recipeLines1.head)
  // println(recipeLines1.size)
 //  val recipeLines = recipeLines1.splitAt(1)._2
  
   
 //  println(recipeLines.span(p => p.trim().length()!=0)._2.span(_.trim().length()!=0)._2)
 //   if(recipeLines.head.trim.length()!=0) println(recipeLines.head)
   // println(recipeLines.size)
 
     val emptyLines =recipeLines.foldLeft(0)((x,y) => if(y.trim().length()==0) x+1 else x)
     
     emptyMap = if(emptyMap.contains(emptyLines)){
       val value = emptyMap.get(emptyLines).get+1
       emptyMap.updated(emptyLines,value)   
     } 
     		else emptyMap.updated(emptyLines,1)
     
     		//println(emptyLines+ " empty")
	 val title =recipeLines(1).trim()

	 recipeLines(4).trim() match {
	   
	   case ServingSizeAndTimePattern(size, time) => {
	     (size,time)
	   
	   }
	   case _ => {
	  //   println("No size and time" + recipeLines(4))
	   }
	 }
		 recipeLines(3).trim() match {
	   
	   case RecipeBy(credit) => {
	     (credit)
	   
	   }
	   case _ => {
	     //println("no credit" + recipeLines(4))
	   }
	 }
	
	 var from =4
	 var line = recipeLines(from).trim.split(":")(1).trim()
	 var categories=List[String]()
	 while(line.trim().size>0){		
	   categories=categories++ getSetSplitByCommaAndSpace(line)
	   from =from +1
	   line=recipeLines(from).trim
	 }
	 
	 
	   from =from +3
	   line=recipeLines(from).trim	   
	 var ingredients=Set[MXPIngredient]()
	 while(line.trim().size>0){
	  ingredients= getIngredients(line) match {
	     case Some(x)=>  ingredients+ x
	     case _ => ingredients
	   }
	  
	   from =from +1
	   line=recipeLines(from).trim
	 }
	 
	 val PostedBy = """.*Posted.*by (.*) on (.*).*""".r
	 
	 val process = recipeLines.splitAt(from)._2.filter(p => {
	     p match {
	       case PostedBy(a,b) => {
	       
	         true
	       }
	       case _ => false
	     }
	 })
	
     val recipe= MXPRecipe(title,"","", categories,ListOfMXPIngredient(),List(),"", "")
    
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

  def getIngredients(line: String):Option[MXPIngredient] = {
	 
    	 val IngredientDetail = """^(.*)\s+(.*)\s+(.*)$""".r
    	 line match {
	    case IngredientDetail(amout,measure,ingredient)  => Some(MXPSingleIngredient(amout,measure,ingredient, Nil))
	    case _=> {
	     // println("No match"+ line)
	      None
	    }
	  }
    	
  }
  
}