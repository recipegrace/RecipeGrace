package com.recipe

import org.apache.lucene.store.FSDirectory
import java.io.File
import org.apache.lucene.analysis.standard.StandardAnalyzer
import org.apache.lucene.index.IndexWriterConfig
import org.apache.lucene.util.Version
import org.apache.lucene.index.IndexWriter
import org.apache.lucene.document.Document
import org.apache.lucene.document.TextField
import java.io.BufferedReader
import java.io.StringReader
import org.apache.lucene.document.StringField
import org.apache.lucene.document.Field
import org.apache.lucene.index.Term
import org.apache.lucene.index.DirectoryReader
import org.apache.lucene.search.IndexSearcher
import org.apache.lucene.queryparser.classic.QueryParser
import com.recipe.MXPRecipeReader._
import org.apache.lucene.store.RAMDirectory
import org.apache.lucene.store.Directory

object IndexHelper {
  
  
  val indexPath = "recipeIndex"
  val version =Version.LUCENE_4_9
 
      def createIndex(recipes :List[MXPRecipe], dir:Directory) = {
          def indexRecipes(writer:IndexWriter, f:MXPRecipe) = {
          val doc = new Document();
           doc.add(new TextField("process",new StringReader(f.process.mkString("\n"))));
           doc.add(new TextField("title",new StringReader(f.title)));
           val ingredientText = f.ingredients.getContent().flatMap(f=>f._2).flatMap(f=> f.getIngredients).map(f=>f.ingredient).mkString(",")
           doc.add(new TextField("ingredients",new StringReader(ingredientText)));
           doc.add(new StringField("originalLines",f.originalLines.mkString("\n"), Field.Store.YES));
      
            writer.addDocument(doc);
      }
        require(!recipes.isEmpty, "Something wrong, indexing an empty list")
              //  val dir = new RAMDirectory()//FSDirectory.open(new File(indexPath))
      val analyzer = new StandardAnalyzer(version)
      val iwc = new IndexWriterConfig(version,analyzer);
      val writer = new IndexWriter(dir, iwc);
        
        recipes.foreach(f=>{
      indexRecipes(writer, f);
      println("added")
             })
              println("closed")
              writer.close();
      }

      def searchRecipes(text:String,curPage:Int, itemsPerPage:Int,reader:Directory):List[MXPRecipe] = { 
        
         //DirectoryReader.open(FSDirectory.open(new File(indexPath)));
    val searcher = new IndexSearcher(DirectoryReader.open(reader));
       
    val analyzer = new StandardAnalyzer(version)
    val parser = new QueryParser(version,"title", analyzer);
    val query = parser.parse(text);
    val results = searcher.search(query, 10 * itemsPerPage);
     (for(i <- results.scoreDocs.toList.map(f=>searcher.doc(f.doc).get("originalLines").split("\\n").toList)) yield  getRecipe(i)).toList
    
      }
      
      

}