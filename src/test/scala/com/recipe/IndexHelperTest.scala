package com.recipe

import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import com.recipe.IndexHelper._
import org.apache.lucene.store.RAMDirectory
import com.recipe.MXPRecipeReader._
import org.apache.lucene.store.FSDirectory
import java.io.File
import org.apache.lucene.store.Directory
import org.scalatest.MustMatchers

class IndexHelperTest extends FunSuite with MustMatchers with TestHelper {

 
    
  def indexAndSearch(directory :Directory) ={
   
    createIndex(List(testRecipe), directory)

     searchRecipeSize("apple",  directory) must equal(1)
     searchRecipeSize("mango",  directory) must equal(0)

    searchRecipes("butter", 0, 10, directory)(0).getOriginalLines.trim must equal(recipeLines.trim)    
  }
  test("RAM Test: Index and Search") {

   indexAndSearch(new RAMDirectory)
  
  }
  test("File Test: Index and Search") {

     
  
    for {
      files <- Option(new File("recipeTest").listFiles)
      file <- files
      if(file.exists())
    } file.delete()
     indexAndSearch( FSDirectory.open(new File("recipeTest")))

  }
}