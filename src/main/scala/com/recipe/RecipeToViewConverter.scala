package com.recipe

trait RecipeToViewConverter {
	def convertFDXToIngrTable(recipe:Recipe) :Seq[(String, String)] = {
			val ingredients = recipe.asInstanceOf[FDXRecipe].ingredients

					ingredients
					.map(f=> if(f.heading) (List(f.amount, f.measure).mkString(" "), f.ingredientName)
					else ("", f.ingredientName))


	}

	def convertMXPToIngrTable(recipe:Recipe) :Seq[(String, String)] = {
			val ingredients:List[(String,List[MXPIngredient])] = recipe.asInstanceOf[MXPRecipe].ingredients.getContent
					ingredients.foldLeft(List(): List[(String,String)])( (p,q)=> {

						def convertSingleIngr(x:MXPSingleIngredient) = {
							(List(x.amount.trim , x.measure.trim).mkString(" "), 
									List(x.ingredient, if(x.notes.isEmpty)""else"--" , x.notes.mkString(",")).mkString(" ").trim)
						}
						def convertIngredients(ingrds:List[MXPIngredient]):List[(String,String)]= {
							ingrds.foldLeft (List():List[(String,String)])( (p,q) =>{
								q match {
								case x: MXPSingleIngredient => 
								convertSingleIngr(x)::p
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

						if(q._1!="Default") ( "", q._1 ):: convertIngredients(q._2)  else  convertIngredients(q._2)
					})
	} 

	def ifEmpty(string:String) = {
		if(string==null || string.length() ==0) "Not available"
			else string
	}
	def recipeToView(recipe:Recipe) = {
		recipe.recipeType  match {
		case "FDX" => convertToView(recipe,convertFDXToIngrTable)
		case  "MXP" => convertToView(recipe,convertMXPToIngrTable)
		case _=> <div/>
		}
	} 
	def mergeLines(lines:Seq[String]) = {
			if(lines.nonEmpty )
		lines.foldLeft(List():List[String])((p,q)=>{
			if(q.trim().length() ==0)  p
			else if(q.trim().head.toLower == q.trim().head) {
			   if(p.nonEmpty)
				p.updated(p.size-1, p.last.trim +" " + q)
				else p
			}
			else q::p

		})
		else lines
	}

	def convertToView(recipe:Recipe, ingredientTble:(Recipe)=>Seq[(String,String)]) = {

		<div>
		<div class="row text-center">
		<h2>
		{recipe.getTitle}
		</h2>
		<dl>
		<dt>Categories</dt>
		<dd>{ifEmpty(recipe.getCategories.mkString(",")) }</dd>
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
		{ingredientTble(recipe)
			.map(f=>{

				<tr>
				<td> {f._1}</td>
				<td> {f._2}</td>
				</tr>
			} )

		}

		</tbody>
		</table>

		{mergeLines(recipe.getProcess)
			.map(f=> {
				<p class="text-justify"> {f}</p>
			})}
		</div>
		</div>
	}



}