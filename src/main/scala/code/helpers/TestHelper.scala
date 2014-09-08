package code.helpers
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

			I make apple butter in my crock pot and it is very easy and very tasty. Usually I use 3 pounds of Jonathan apples which I wash, core and chunk into a 3 quart crock pot. I add about 1 to 2 cups apple cider (sometimes I use just water) and cook on low til mushy. I chop the apples (peel and all) and run through a sieve and return to the crock pot. I add about 1 cup sugar (or more depending on taste preference), cover and cook on high til bubbly. I then remove the lid and cook on high til apples turn dark brown and thicken.

			Pour into jelly jars and put in refrigerator or freezer - after they cool. Tastes good on everything from biscuits to toast to even ice cream.




			"""
    val testRecipe = getRecipe(recipeLines.split("\\n", -1).toList)
}