package com.recipe
import com.recipe._
trait RecipeToViewConverter {
 def convertFDXToIngrTable(ingredients:Seq[FDXIngredient]) :Seq[(String, String)] = {
   ingredients
   .map(f=> if(f.heading) (List(f.amount, f.measure).mkString(" "), f.ingredientName)
   	        else ("", f.ingredientName))
       
   
 }
  
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
     
  def ifEmpty(string:String) = {
    if(string==null || string.length() ==0) "Not available"
    else string
  }
  def recipeToView(recipe:Recipe) = {
    recipe.recipeType  match {
      case "FDX" => convertToView(recipe.asInstanceOf[FDXRecipe])
      case  "MXP" => convertToView(recipe.asInstanceOf[MXPRecipe])
      case _=> <div/>
    }
  } 
       def mergeNonEmptyLines(lines:Seq[String]) = {
      lines.foldLeft(List():List[String])((p,q)=>{
        if(q.trim().length() > 0)  q.trim::p
        else p
        
      })
   }
  def convertToView(recipe:MXPRecipe) = {
    <div>
<div class="row text-center" >        
    <h2> <r:title/></h2>
<dl>
  <dt>Credit</dt>
  <dd>{ifEmpty(recipe.credit)}</dd>
    <dt>Categories</dt>
  <dd>{ifEmpty(recipe.categories.mkString(","))	}</dd>
</dl>
</div>
<div class="bs-example">
    <table class="table table-striped">
        <thead>
            <tr>
              <th>Specification</th>
                <th>Ingredient name and notes</th>
            </tr>
        </thead>
        <tbody>
        {convertMXPToIngrTable(recipe.ingredients.getContent)
          .map(f=>{
            
            <tr> <td> {f._1}</td> <td> {f._2}</td></tr>
          }  )
          
          }
            
        </tbody>
    </table>
  <p>{mergeNonEmptyLines(recipe.process)}</p>
</div>
  </div>
  }
   def convertToView(recipe:FDXRecipe) = {
    <div>
<div class="row text-center" >        
    <h2> <r:title/></h2>
<dl>
  <dt>Credit</dt>
  <dd>{ifEmpty(recipe.source)}</dd>
    <dt>Categories</dt>
  <dd>{ifEmpty(recipe.categories.mkString(","))	}</dd>
</dl>
</div>
<div class="bs-example">
    <table class="table table-striped">
        <thead>
            <tr>
              <th>Specification</th>
                <th>Ingredient name and notes</th>
            </tr>
        </thead>
        <tbody>
        {convertFDXToIngrTable(recipe.ingredients)
          .map(f=>{
            
            <tr> <td> {f._1}</td> <td> {f._2}</td></tr>
          }  )
          
          }
            
        </tbody>
    </table>
  <p>{mergeNonEmptyLines(recipe.process)}</p>
</div>
  </div>
  }
}