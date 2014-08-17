package com.jacob.mxp

import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import com.jacob.mxp.MXPRecipeReader._

class MXPTest extends FunSuite with ShouldMatchers{
 test("Random Sample test ") {
   
    val root ="data/MXP/"
    val recipes = readRecipeList(root+"webducks.mxp",
        root+ "b3q96.txt", root+ "b4q96.txt",
         root+ "BBQ-ARC1.MXP", root+ "BBQ-ARC2.MXP"
        )
    recipes.size should equal(2940)
    }
 
}
