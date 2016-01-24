package com.recipegrace.web

import scala.Array.canBuildFrom
import scala.collection.immutable.Stack
import scala.io.Codec.string2codec
import scala.io.Source
import scala.util.matching.Regex

object MXPRecipeReader extends RecipeReader[MXPRecipe] {
  implicit class StringImprovements(val s: String) {
    def matches(expression: Regex): Boolean = {
      expression.findFirstIn(s) match {
        case Some(_) => true;
        case _ => false
      }
    }
  }

  val beginLine = """^\s*\**\s*Exported\s+from\s+MasterCook.*\**\s*$""".r
  val endLine = """- - - - - - - - - - - - - - - - - - """


  def equalOpenClose(s: String) = {
    s.count(p => p.equals("(")) == s.filter(p => p.equals(")")).size
  }

  def mergeBracket(list: List[String]): List[String] = {
    val init: (List[String], List[String]) = (List(), List())
    list.foldLeft(init)((p, q) => {

      if (q.trim.startsWith("(") && !q.trim.endsWith(")")) {

        val notes: List[String] = List("-- " + q.trim.substring(1))
        ("(":: p._1, p._2 ++ notes)
      } else if (!q.trim.startsWith("(") && q.trim.endsWith(")") && !equalOpenClose(q.trim)) {
        require(p._1.nonEmpty, "Stack cannot be empty: " + q.trim)
        val notes: List[String] = List("-- " + q.trim.substring(0, q.trim().length() - 1))
        (p._1.tail, p._2 ++ notes)
      } else if (q.trim.startsWith("(") && q.trim.endsWith(")")) {
        val notes: List[String] = List("-- " + q.trim.substring(1, q.trim().length() - 1))
        (p._1, p._2 ++ notes)
      } else if (p._1.nonEmpty) {
        val notes: List[String] = List("-- " + q)
        (p._1, p._2 ++ notes)
      } else {
        val notes: List[String] = List(q)
        (p._1, p._2 ++ notes)
      }

    })._2 

  } 
  
 override def loadRecipe(content:String) = {
  //  println("MXP here-" +content)
    getRecipe(content.split("\\n").toList)
  }
  def getRecipe(lines: List[String]): MXPRecipe = {
    def getIngredients(lines: List[String]): ListOfMXPIngredient = {
      val ingredient = ListOfMXPIngredient()
      lines.foldLeft(ingredient)((acc, current) => {
        val IngredientHeading1 = """^[\.\*]+([^\*]*)[\.\*]+$""".r
        val IngredientHeading2 = """^\-+([^-].*[^-])\-+$""".r
        val IngredientHeading3 = """^(.*):\s*$""".r
        val IngredientDetail = """^(.*)\s\s+(.*)\s\s+(.*)\s*$""".r
        val IngredientDetailWithNotes = """^(.*)\s\s+(.*)\s\s+(.*)\s+--\s*(.*)\s*$""".r
        val CookingNotes1 = """^--\s*([^\s].*[^\s])$""".r
        val CookingNotes2 = """^\(\s*([^\s].*[^\s])\s*\).*$""".r
        val IngredientName = """^([aA-zZ].*[^\s])$""".r

        current.trim match {
          case IngredientHeading1(x) => { acc.startNew(x) }
          case IngredientHeading2(x) => { acc.startNew(x) }
          case IngredientHeading3(x) => { acc.startNew(x) }
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
            acc.add(MXPSingleIngredient("", "", name))
          }
          case _ => {

            //if(!current.contains("salt") && !current.contains("pepper") )
            require(current.trim().length() == 0, "Ingredient not caught")
            println("******" + current.trim)
            //  if(current.trim.startsWith("--"))
            //   saltPepper= current::saltPepper
            //    
          }
        }

        acc
      })

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

      require(content(0).contains("Recipe By     :") && content(0).split(":", -1).size > 1, "Should have 'Recipe By     :' to split " + content(0))
      val recipeBy = content(0).split(":", -1)(1)
      (recipeBy.trim(), sizeTime.get._1, sizeTime.get._2, categories)
    }
    def getBody(content: List[List[String]]):List[String] = {
      content.foldLeft(List():List[String])((p,q)=>{
        q.mapConserve(f=>f.trim).mkString(" ").trim :: p
      })
    }

    //    println(lines.mkString("ST","\n","END"))
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

    val ingredients = getIngredients(result.getContent(2).splitAt(2)._2.filter(p => p.distinct.length() > 2))

//    println("inside this"+result.getContent().slice(3, result.getContent().size).flatten)
    val body = if (result.getSize < 4) List("No description available") else getBody(result.getContent().splitAt(3)._2)

    MXPRecipe(title, details._2, details._3, details._4, ingredients, body, "Online BBQ Mailing List", details._1, lines)
  }

  def readRecipes(filePath: String) = {

    require(filePath != null && filePath.trim().length() > 0, "Should be a valid file" + filePath)

    def endsInED(line: String): Boolean = {

      !(getEDWord(line).isEmpty)
    }
    def getEDWord(line: String): String = {
      val words = line.split("\\s+")
      val filtered = words.filter(p => p.endsWith("ed"))
      if (filtered.isEmpty) "" else filtered(0)
    }

    val lines = Source.fromFile(filePath)("ISO-8859-1").getLines()
    val recipeText = ListOfList()
    val stack = Stack()
    lines.foldLeft((recipeText, stack))((acc, current) => {
      if (current.matches(beginLine)) {
        require(stack.isEmpty, "Unclosed bracket")
        acc._1.startNew()
      } else if (current.contains(endLine)) {
        require(stack.isEmpty, "Unclosed bracket")
        ""
      } else
        acc._1.add(current)
      acc
    })
    val modifiedText = recipeText.getContent().mapConserve(f => mergeBracket(f))
    val recipes = for (recipe <- modifiedText)
      yield (getRecipe(recipe))
    recipes
    //  recipes.flatMap(f => f.ingredients.getHeadings).distinct.foreach(j => println(j))
  }

} 













