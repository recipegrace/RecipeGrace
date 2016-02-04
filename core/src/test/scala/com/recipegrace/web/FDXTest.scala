package com.recipegrace.web

import org.scalatest.{FunSuite, MustMatchers}

class FDXTest extends FunSuite with MustMatchers {

  val recipeXML =
    <fdx Source="Living Cookbook 1.05.28" FileVersion="1.1" date="2006-12-03">
      <Cookbooks>
        <Cookbook Name="Comfort Food" ID="4"/>
      </Cookbooks>
      <CookbookChapters>...</CookbookChapters>
      <Recipes>
        <Recipe Name="Barbecued Beef Sandwiches" ID="478" CookbookChapterID="69" Servings="8" PreparationTime="7" CookingTime="75" ReadyInTime="82" RecipeTypes="Barbecue, Main Dish, Meat, Sandwich" Source="Nestlé" WebPage="www.verybestmeals.com" CreateDate="12/03/2006">
          <RecipeIngredients>
            <RecipeIngredient Quantity="1 1/2" Unit="lbs" Ingredient="ground beef" Heading="N" LinkType="Ingredient" IngredientID="13302" IngredientName="Beef, ground, lean, (approximately 21% fat), raw" MeasureID="62750" Measure="1 lb" MeasureQuantity="1.5000" MeasureGramWeight="453.600"/>
          </RecipeIngredients>
          <RecipeProcedures>
            <RecipeProcedure Heading="N">
              <ProcedureText>
                Brown ground beef in a large skillet, stirring to crumble. Drain.
              </ProcedureText>
            </RecipeProcedure>
            <RecipeProcedure Heading="N">
              <ProcedureText>
                Add chopped celery, chopped onion, and chopped green bell pepper. Cook for 5 minutes, or until the onion is tender.
              </ProcedureText>
            </RecipeProcedure>
            <RecipeProcedure Heading="N">
              <ProcedureText>
                Stir in tomato sauce, ketchup, brown sugar, prepared barbecue sauce, vinegar, prepared mustard, worcestershire sauce, salt, and pepper.
              </ProcedureText>
            </RecipeProcedure>
            <RecipeProcedure Heading="N">
              <ProcedureText>
                Cover and simmer on low for 1 hour. Serve on hamburger buns.
              </ProcedureText>
            </RecipeProcedure>
          </RecipeProcedures>
          <RecipeNutrition ServingSize="1 serving" CalculatedFromRecipeFlag="Y" Calories="408.42" CaloriesFromFat="180.28" TotalFat="20.06" SaturatedFat="7.62" TransFattyAcids="0.00" Cholesterol="63.79" Sodium="1320.09" TotalCarbohydrate="36.27" Fiber="2.81" Protein="20.07" Calcium="92.69" Folate="62.24" Iron="3.79" Magnesium="40.91" Niacin="6.39" PantothenicAcid="0.77" Phosphorus="185.36" Potassium="638.23" VitaminA="724.24" VitaminB12="2.02" VitaminB6="0.38" VitaminC="21.92" VitaminE="1.84" Zinc="3.79" Thiamin="0.31"/>
          <SourceImage xmlns:dt="urn:schemas-microsoft-com:datatypes" dt:dt="bin.base64" FileType="gif" FileName="nestle">...</SourceImage>
        </Recipe>
      </Recipes>
    </fdx>
  test("Simple FDX format test") {

    val recipe = FDXRecipeReader.readRecipes(recipeXML)
    val fDXrecipe = recipe.head
    fDXrecipe.title must equal((recipeXML \\ "Recipe" \ "@Name").toString)
    fDXrecipe.categories.mkString(",") must equal((recipeXML \\ "@RecipeTypes").toString)
    fDXrecipe.cookingTime must equal((recipeXML \\ "@CookingTime").toString)
    fDXrecipe.readyTime must equal((recipeXML \\ "@ReadyInTime").toString)
    fDXrecipe.createDate must equal((recipeXML \\ "@CreateDate").toString)
    fDXrecipe.source must equal((recipeXML \\ "Recipe" \ "@Source").toString)
    fDXrecipe.servingSize must equal((recipeXML \\ "@Servings").toString)
    fDXrecipe.webPage must equal((recipeXML \\ "@WebPage").toString)
    val ingredient = fDXrecipe.ingredients.head
    ingredient.amount must equal((recipeXML \\ "@Quantity").toString)
    ingredient.measure must equal((recipeXML \\ "@Unit").toString)
    ingredient.ingredientName must equal((recipeXML \\ "@IngredientName").toString)
    ingredient.heading must equal(false)

  }

}