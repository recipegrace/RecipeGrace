package com.recipe

import org.scalatest.FunSuite
import org.specs2.matcher.ShouldMatchers
import code.helpers.TestHelper
import org.scalatest.matchers.MustMatchers

class MXPRecipetoViewConverterTest extends FunSuite with MustMatchers with TestHelper with RecipeToViewConverter {

  test ("MXPRecipe to View") {
    
    /*
     * 			3             pounds  apples
			1                cup  apple cider -- to 2 cups 
			1                cup  sugar -- more or less
     */
     val a =convertMXPToIngrTable(testRecipe.ingredients.getContent)
     println(a)
     a.size must equal (3)
     a(0) must equal ( ("1 cup"),("sugar -- more or less"))
     a(1) must equal ( ("1 cup"),("apple cider -- to 2 cups"))
    a(2) must equal ( ("3 pounds"),("apples"))
  }
}