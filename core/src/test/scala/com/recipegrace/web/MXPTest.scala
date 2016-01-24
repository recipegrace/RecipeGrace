package com.recipe

import com.recipegrace.web.MXPRecipeReader
import org.scalatest.FunSuite
import MXPRecipeReader._
import java.io.File
import scala.Array.canBuildFrom
import org.scalatest.MustMatchers

 
class MXPTest extends FunSuite with MustMatchers{ 
 test("MXP File parsing test 1 ") {
   
    val root = "data/MXP"
    val recipes = readRecipeList(root+"webducks.mxp",
        root+ "b3q96.txt", root+ "b4q96.txt",
         root+ "BBQ-ARC1.MXP", root+ "BBQ-ARC2.MXP"
        )
    recipes.size must equal(2940)
    }

 test("MXP File parsing test 2") {
   
    val root = "data/MXP/mxpfiles"
     val files = new File(root).listFiles()
     
     val recipes= (for(file <- files) yield readRecipeList(file.getAbsolutePath())).toList.flatten

    recipes.size must equal(5910)
    }
}
