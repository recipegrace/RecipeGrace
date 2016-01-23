package com.recipe{
  
  
  trait RecipeReader[T] {
      def readRecipeList(files: String*) = {

    files.flatMap(f => readRecipes(f)).toList
  }
      def readRecipes(x:String):List[T]
      
      def loadRecipe(lines: String): T
      
 

  }
  
  trait Recipe{
    def getTitle():String
    def getIngredientNames():Seq[String]
    def getProcess:Seq[String]
    def getOriginalLines:String
    def recipeType:String
    def getCategories:Seq[String]
    
  }
  object ErrorRecipe  extends Recipe {
        def getTitle()="Error"
    def getIngredientNames()=Seq()
    def getProcess=Seq()
    def getOriginalLines =""
    def recipeType=""
    def getCategories=Seq()
  }
  
 //<Recipe Name="" ID="" CookbookChapterID="" Servings="" PreparationTime="" CookingTime="" ReadyInTime=""
  //RecipeTypes="" Source="Nestlé" WebPage="www.verybestmeals.com" CreateDate="12/03/2006">
  case class FDXRecipe(title: String, servingSize: String, prepTime:String,cookingTime: String, readyTime:String,
      categories: Seq[String],ingredients: Seq[FDXIngredient], process: Seq[String],
      source:String,webPage:String,createDate:String ) extends Recipe {
    def getTitle():String= title
    def getIngredientNames():Seq[String]= ingredients.map(f=> f.ingredientName)
    def getProcess:Seq[String]=process
    import net.liftweb.json._
    import net.liftweb.json.Serialization.{write}
    implicit val formats = Serialization.formats(NoTypeHints)
    def getOriginalLines=write(this)
    def recipeType="FDX"
    def getCategories= categories
    
  }
  
  //<RecipeIngredient Quantity="" Unit="" Ingredient=""  IngredientName="" Heading=""/>    
  case class FDXIngredient(amount: String, measure: String, ingredientName: String,heading:Boolean)    
  
  case class MXPRecipe(title: String, servingSize: String, cookingTime: String, 
    categories: List[String], ingredients: ListOfMXPIngredient, process: List[String],
    source: String, credit: String, originalLines:List[String])  extends Recipe {
    def getTitle():String =title
    def getIngredientNames():Seq[String]= ingredients.getContent()
    									   .flatMap(f=>f._2.map(p=>p.getIngredients))
    									   .flatten
    									   .map(f=>f.ingredient)
    									   
    def getProcess:Seq[String] = process
    def getOriginalLines= originalLines.mkString("\n")
    def recipeType="MXP"
    def getCategories= categories
  }
    
    
  trait MXPIngredient {
    def getIngredients(): List[MXPSingleIngredient]
  }

  case class MXPSingleIngredient(amount: String, measure: String, ingredient: String, notes: List[String] = List(), optional: Boolean = false) extends MXPIngredient {
    override def getIngredients() = this :: Nil
  }

  case class MXPSubsituteIngredients(ingredients: List[MXPSingleIngredient]) extends MXPIngredient {
    override def getIngredients() = ingredients
  }
  case class RecipeNote(note: List[String]) extends MXPIngredient { 
    override def getIngredients() = Nil
  }



  abstract class ListOfTypes[T]() {
    var listOfLists: List[T] = List()
    var start = false
    def startNew() = {
      start = true
    }

    def getContent() = {
      listOfLists
    }

    def getContent(i: Int): T = {
      require(listOfLists.size > i, "Invalid index" + listOfLists)
      listOfLists(i)
    }
    def getSize() = {
      listOfLists.size
    }
  }
  case class ListOfList() extends ListOfTypes[List[String]] {

    def add(text: String) = {

      if (start == false && !listOfLists.isEmpty) {
        val currentList = listOfLists.last
        val newList = currentList ++ List(text)
        listOfLists = listOfLists.updated(listOfLists.size - 1, newList)
      } else {
        listOfLists = listOfLists ++ List(List(text))
        start = false;
      }
    }
    def getContent(i: Int, j: Int): String = {
      require(listOfLists.size > i, "Invalid index" + i)
      val list = listOfLists(i)
      require(list.size > j, "Invalid index" + j)
      list(j)
    }
    
  }
  case class ListOfMXPIngredient() extends ListOfTypes[(String, List[MXPIngredient])] {

    var startText = "Default"
    def startNew(text: String) = {

      start = true
      startText = text.replace("FOR THE ", "").replace("THE ", "").toLowerCase().trim.capitalize
    }

    def endsWith(x: MXPSingleIngredient): Boolean = {
      if (x.notes.isEmpty && x.ingredient.toLowerCase().endsWith(" or")) true
      else if (!x.notes.isEmpty && x.notes.last.toLowerCase().equals("or")) true
      else false
    }
    def startsWith(x: MXPSingleIngredient): Boolean = {
      // if(x.ingredient.toLowerCase().startsWith("or ")) println("HOL"+x.ingredient)
      if (x.ingredient.toLowerCase().startsWith("or ")) true
      else false
    }

    def endOrStart(ingredients: List[MXPIngredient], applyFtn: (MXPSingleIngredient) => Boolean): Boolean = {

      ingredients.last match {
        case x: MXPSingleIngredient => {
          applyFtn(x)
        }
        case y: MXPSubsituteIngredients => {
          applyFtn(y.ingredients.last)
        }
      }

    }

    def addNotes(notes: List[String]) = {
     
      if (listOfLists.isEmpty) List(RecipeNote(notes))
      else {
        val lastOne = listOfLists.last._2.last match {
          case x: MXPSingleIngredient => {
            MXPSingleIngredient(x.amount, x.measure, x.ingredient, x.notes ++ notes)
          }
          case y: MXPSubsituteIngredients => {
            require(!y.ingredients.isEmpty, "Adding to substitutable list, so it cannot be empty")
            val x = y.ingredients.last
            MXPSubsituteIngredients(y.ingredients.updated(y.ingredients.size - 1,
              MXPSingleIngredient(x.amount, x.measure, x.ingredient, x.notes ++ notes)))

          }
        }
        val newList = listOfLists.last._2.updated(listOfLists.last._2.size - 1, lastOne)
        listOfLists = listOfLists.updated(listOfLists.size - 1, (listOfLists.last._1, newList))
      }
    }
    def makeSubstituteList(last: MXPIngredient, current: MXPSingleIngredient): MXPSubsituteIngredients = {
      last match {
        case x: MXPSingleIngredient => MXPSubsituteIngredients(List(x, current))
        case y: MXPSubsituteIngredients => MXPSubsituteIngredients(y.ingredients ++ List(current))
      }
    }
    def add(ingredient: MXPSingleIngredient) = {

      if (!start) { 

        listOfLists = if (listOfLists.isEmpty)
          List((startText, List(ingredient)))
        else {
          val currentList = listOfLists.last._2
          if (endOrStart(currentList, endsWith) || endOrStart(List(ingredient), startsWith)) {
            // println("New", currentList.last, ingredient)
            val substituteList = makeSubstituteList(currentList.last, ingredient)
            val newList = (listOfLists.last._1, currentList.drop(currentList.size - 1) ++ List(substituteList))
            listOfLists.updated(listOfLists.size - 1, newList)
          } else {
            val newList = (listOfLists.last._1, currentList ++ List(ingredient))
            listOfLists.updated(listOfLists.size - 1, newList)
          }
        }
      } else {

        listOfLists = listOfLists ++ List((startText, List(ingredient)))
        start = false;
      }
    }
    def getContent(i: Int, j: Int): MXPIngredient = {
      require(listOfLists.size > i, "Invalid index" + i)
      val list = listOfLists(i)
      require(list._2.size > j, "Invalid index" + j)
      list._2(j)
    }
    def getHeadings() = {
      listOfLists.map(f => f._1)
    }
    
    

  }
 

}