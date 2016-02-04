package com.recipegrace.web

import java.io.File

import com.recipegrace.biglibrary.core.BaseTest
import com.recipegrace.web.IndexHelper._
import org.apache.lucene.store.FSDirectory

class IndexHelperTest extends BaseTest with TestHelper {


  test("index based test ") {

    val directory = FSDirectory.open(new File(indexPath))

   createIndex(List(testRecipe), directory)

    searchRecipeSize("apple", directory) should equal(1)
    searchRecipeSize("mango", directory) should equal(0)

    searchRecipes("butter", 0, 10, directory)(0).getOriginalLines.trim should equal(recipeLines.trim)
    deleteIndex()
  }
}