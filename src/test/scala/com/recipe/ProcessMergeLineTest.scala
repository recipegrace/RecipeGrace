package com.recipe

import org.scalatest.FunSuite
import org.scalatest.MustMatchers


class ProcessMergeLineTest extends FunSuite 
	with MustMatchers with TestHelper with RecipeToViewConverter{
  
  test("process merge test") {
   val orginalProcess = testRecipe.getProcess
   
 //  println(testRecipe.get)
   val process=mergeLines(orginalProcess) 
   process.size must equal (2)
   println(orginalProcess)
   process(0) must equal ("I make apple butter in my crock pot and it is very easy and very tasty.")
   process(1) must equal ("Pour into jelly jars and put in refrigerator or freezer - after they cool." )
  }

}