package com.recipe
import com.recipe.MXPRecipeReader._
trait TestHelper {
 val recipeLines = """
			Apple Butter 

			Recipe By     :"Edria Roberts" <edriajean@hotmail.com>
			Serving Size  : 1     Preparation Time :0:00
			Categories    : Daily Bread Mailing List        Miscellaneous & Tips


			Amount  Measure       Ingredient -- Preparation Method
			--------  ------------  --------------------------------
			3             pounds  apples
			1                cup  apple cider -- to 2 cups 
			1                cup  sugar -- more or less

			I make apple butter
		 	in my crock pot and it is very easy and very tasty. 
   
			Pour into jelly jars and put in refrigerator or 
		 	freezer - after they cool. 



			"""
    val testRecipe = getRecipe(recipeLines.split("\\n", -1).toList)
}