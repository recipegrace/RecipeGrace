package com.jacob.mxp

object ParserHelper {

  case class MXPIngredient(amount: String, measure: String, ingredient: String, notes: List[String])
  case class MXPRecipe(title: String, servingSize: String, cookingTime: String,
    categories: List[String], ingredients: ListOfMXPIngredient, process: List[String],
    source: String, credit: String)

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

    def endingWithOr(ingredients:List[MXPIngredient] ):Boolean ={
      if(ingredients.last.notes.isEmpty &&ingredients.last.ingredient.toLowerCase().endsWith(" or")) true
      else if ( !ingredients.last.notes.isEmpty &&ingredients.last.notes.last.toLowerCase().endsWith(" or")) true
      else false
    } 
    
    def add(ingredient: MXPIngredient) = {

      if (start == false) {
       

        listOfLists = if (listOfLists.isEmpty)
          List((startText, List(ingredient)))
        else {
        
          val currentList = listOfLists.last._2  
          if(endingWithOr(currentList) ) println(currentList.last,  ingredient)
          val newList = (listOfLists.last._1, currentList ++ List(ingredient))
          listOfLists.updated(listOfLists.size - 1, newList)
          
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