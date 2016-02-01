package com.recipe

import com.recipegrace.biglibrary.core.BaseTest
import com.recipegrace.web.MXPRecipeReader
import org.scalatest.FunSuite
import org.apache.lucene.store.RAMDirectory
import org.apache.lucene.store.FSDirectory
import java.io.File
import org.apache.lucene.store.Directory
import com.recipegrace.web.IndexHelper._

class IndexHelperTest extends BaseTest with TestHelper {

 
    
  test("index based test "){
   
   // createIndex(List(testRecipe), directory)

    println(indexPath)
    val directory = FSDirectory.open(new File(indexPath))
     searchRecipeSize("apple",  directory) should  equal(1)
     searchRecipeSize("mango",  directory) should equal(0)

    searchRecipes("butter", 0, 10, directory)(0).getOriginalLines.trim should  equal(recipeLines.trim)
  }
}