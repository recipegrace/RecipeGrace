package com.jacob.mxp

import scala.io.Source
import com.jacob.mxp.ParserHelper._


object MXPRecipeReader {

  val beginLine = "*  Exported from  MasterCook"
  val endLine = """- - - - - - - - - - - - - - - - - - """
  def readRecipes(filePath: String) = {
    val lines = Source.fromFile(filePath)("ISO-8859-1").getLines()
    val recipeText = ListOfList()
    lines.foldLeft(recipeText)((acc, current) => {
      if (current.contains(beginLine)) acc.startNew()
      else if (current.contains(endLine)) ""
      else acc.add(current)
      acc
    })
    val recipes = for (recipe <- recipeText.getContent())
      yield (getRecipe(recipe))
      recipes
  //  recipes.flatMap(f => f.ingredients.getHeadings).distinct.foreach(j => println(j))
  }

  def getRecipe(lines: List[String]): MXPRecipe = {
    val recipe = ListOfList()
    //   println(lines)
    val result = lines.foldLeft(recipe)((acc, current) => {
      val isEmpty = current.trim.length == 0
      if (isEmpty) {
        //    println("start to"+ recipe.getContent.size)
        acc.startNew
      } else {
        //   println("addding to"+ recipe.getContent.size)
        acc.add(current)
      }
      acc
    })
    val title = getTitle(result.getContent(0, 0))

    val details = getDetails(result.getContent(1))

    val ingredients = getIngredients(result.getContent(2).splitAt(2)._2.filter(p => p.distinct.length() >2))

    val body = getBody(if (result.getSize < 4) List("No description available") else result.getContent(3))

    MXPRecipe(title, details._2, details._3, details._4, ingredients, body, "Online BBQ Mailing List", details._1)
  }

  def exclusionCategory(category: String): Boolean = {
    category.length() != 0 && !category.equalsIgnoreCase("BBQ List")
  }
  def getCategories(text: String): List[String] = {
    require(text.contains("Categories    :"), "Should have 'Categories    :' to split " + text)
    (text.split(":")(1).split(",")
      .flatMap(f => f.trim().split("  "))).toList
      .flatMap(f => f.trim().split(" And "))
      .map(k => k.trim).filter(exclusionCategory(_))
  }

  def getTitle(title: String) = {
    require(title != null && title.trim.length() > 1, "Should have title")
    title.trim
  }
  def getDetails(content: List[String]) = {
    require(content.size > 2, "Should have at least 3 lines, Recipe by, Size and Time, Categories " + content)
    val categories = getCategories(content.splitAt(2)._2.mkString(","))

    val ServingSizeAndTimePattern = """^.*Serving Size  : (.*) Preparation Time :(.*)$""".r
    val sizeTime: Option[(String, String)] = content(1) match {
      case ServingSizeAndTimePattern(size, time) => Some(size.trim, time.trim)
      case _ => None
    }
    require(sizeTime match { case Some(x) => true case _ => false }, "Should have 'Serving Size  &  Preparation Time " + content(1))
 
    require(content(0).contains("Recipe By     :"), "Should have 'Recipe By     :' to split " + content(0))
    val recipeBy = content(0).split(":")(1)
    (recipeBy.trim(), sizeTime.get._1, sizeTime.get._2, categories)
  }
  def getBody(content: List[String]) = {
    content.filter(p => p.trim().length() != 0)
  }
  var saltPepper:List[String] = Nil
  
  def getIngredients(lines: List[String]): ListOfMXPIngredient = {
    val ingredient = ListOfMXPIngredient()
    lines.foldLeft(ingredient)((acc, current) => {
      val IngredientHeading1 = """^\*+([^\*]*)\*+$""".r
        val IngredientHeading2 = """^\-+([^-].*[^-])\-+$""".r
        val IngredientHeading3 = """^(.*):\s*$""".r
      val IngredientDetail = """^(.*)\s\s+(.*)\s\s+(.*)\s*$""".r
      val IngredientDetailWithNotes = """^(.*)\s\s+(.*)\s\s+(.*)\s+--\s*(.*)\s*$""".r
      val CookingNotes1 = """^--\s*([^\s].*[^\s])$""".r
       val CookingNotes2 = """^\(\s*([^\s].*[^\s])\s*\).*$""".r
     val IngredientName = """^([aA-zZ].*[^\s])$""".r
      
       current.trim match {
        case IngredientHeading1(x) =>{acc.startNew(x)}
        case IngredientHeading2(x) =>{acc.startNew(x)}
        case IngredientHeading3(x) =>{acc.startNew(x)}
        case IngredientDetailWithNotes(amout, measure, ingredient, notes) => {
        //  println(instr)
          acc.add(MXPSingleIngredient(amout, measure, ingredient, notes.split("--").toList))
        }
        case IngredientDetail(amount, measure, ingredient) => {
  //         println(ingredient)
          acc.add(MXPSingleIngredient(amount, measure, ingredient))
        }
        case CookingNotes1(notes) => {
          acc.addNotes(notes.split("--").toList)
        }
        case CookingNotes2(notes) => {
          acc.addNotes(notes.split("--").toList)
        }

        case IngredientName(name) => {
           acc.add(MXPSingleIngredient("", "",name))
        }
        case _ =>{ 
        
          //if(!current.contains("salt") && !current.contains("pepper") )
            require(current.trim().length()==0, "Ingredient not caught"+ current)
            println("******"+current.trim)
         //  if(current.trim.startsWith("--"))
         //   saltPepper= current::saltPepper
        //    
        }
      }
     
      acc
    })
  
   
  }
  def endsInED(line:String):Boolean = {
       
      !( getEDWord(line).isEmpty)
  }
  def getEDWord(line:String):String = {
    val words = line.split("\\s+")
    val filtered=  words.filter(p=> p.endsWith("ed"))
    if(filtered.isEmpty)"" else filtered(0)
  }

} 













