package com.recipe

import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import com.recipe.IndexHelper._
import org.apache.lucene.store.RAMDirectory
import com.recipe.MXPRecipeReader._
import org.apache.lucene.store.FSDirectory
import java.io.File
import org.apache.lucene.store.Directory
import code.helpers.TestHelper

class IndexHelperTest extends FunSuite with ShouldMatchers with TestHelper {

 
    
  def indexAndSearch(directory :Directory) ={
   
    createIndex(List(testRecipe), directory)

     searchRecipeSize("apple",  directory) should equal(1)
     searchRecipeSize("mango",  directory) should equal(0)

    searchRecipes("butter", 0, 10, directory)(0).originalLines.mkString("\n").trim should equal(recipeLines.trim)    
  }
  test("RAM Test: Index and Search") {

   indexAndSearch(new RAMDirectory)
  
  }
  test("File Test: Index and Search") {

      indexAndSearch( FSDirectory.open(new File("recipeTest")))
  
    for {
      files <- Option(new File("recipeTest").listFiles)
      file <- files
    } file.delete()

  }
}