package com.jacob

import scala.collection.mutable.MutableList

object FoldLeftTest extends App {

  println(getSetSplitByCommaAndSpace("Ferosh,Jacob   Help, Something Else,Jacob"))
  
    def getSetSplitByCommaAndSpace(line: String) = {
	  line.split(",").toSet.foldLeft(MutableList[String]())((r,c)=>	 r.++( c.split("  ").toList)).toSet
    }

}