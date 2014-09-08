package com.recipe

trait RecipeToViewConverter {

   def convertMXPToIngrTable(ingredients:List[(String,List[MXPIngredient])]) :List[(String, String)] = {
   ingredients.foldLeft(List(): List[(String,String)])( (p,q)=> {
     
     def convertSingleIngr(x:MXPSingleIngredient) = {
        (List(x.amount.trim , x.measure.trim).mkString(" "), 
            List(x.ingredient, if(x.notes.isEmpty)""else"--" , x.notes.mkString(",")).mkString(" ").trim)
     }
     def convertIngredients(ingrds:List[MXPIngredient]):List[(String,String)]= {
       ingrds.foldLeft (List():List[(String,String)])( (p,q) =>{
          q match {
            case x: MXPSingleIngredient => 
             convertSingleIngr(x):: p
            case y: MXPSubsituteIngredients => {
             val ingreList=y.getIngredients.flatMap(f=>List(convertSingleIngr(f),("", "OR")))
            val result= if(!ingreList.isEmpty) ingreList.slice(0, ingreList.size-2)
             else List()
             
             result ++ p
            }
            case z: RecipeNote => z.note.map(f=>("", f)) ++ p
          }
         
       })
     }
     
       if(q._1!="Default")  ( "", q._1 ):: convertIngredients(q._2) else  convertIngredients(q._2)
   })
  } 
}