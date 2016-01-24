package com.recipe

import com.recipegrace.biglibrary.core.BaseTest
import com.recipegrace.web.RecipeToViewConverter


class MXPRecipetoViewConverterTest extends BaseTest with TestHelper with RecipeToViewConverter {

  test ("MXPRecipe to View") {
    
    /*
     * 			3             pounds  apples
			1                cup  apple cider -- to 2 cups 
			1                cup  sugar -- more or less
     */
     val a =convertMXPToIngrTable(testRecipe)
     println(a)
     a.size should  equal (3)
     a(0) should equal ( """1 cup""","sugar -- more or less")
     a(1) should equal ( "1 cup","apple cider -- to 2 cups")
    a(2) should equal ( "3 pounds","apples")
     
  }
}