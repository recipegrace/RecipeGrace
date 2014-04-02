package com.jacob

class DataStructures {
  
  case class Amount( amount:String)
  case class Measure(measure:String)
  case class Ingredient ( amount:Amount,  measure:Measure,   ingredient:String,  preparationMethod:String)

}