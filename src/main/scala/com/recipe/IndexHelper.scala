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
import org.apache.lucene.index.IndexReader

object IndexHelper { 

  val indexPath = "recipeIndex"
  val version = Version.LUCENE_4_9

  def createIndex(recipes: List[Recipe], dir: Directory) = {
    def indexRecipes(writer: IndexWriter, f: Recipe) = {
      val doc = new Document();
      doc.add(new TextField("process", new StringReader(f.getProcess.mkString("\n"))));
      doc.add(new TextField("title", new StringReader(f.getTitle)));
      doc.add(new TextField("ingredients", new StringReader(f.getIngredientNames.mkString(","))));
      doc.add(new StringField("originalLines", f.getOriginalLines.mkString("\n"), Field.Store.YES));
      doc.add(new StringField("recipeType", f.recipeType, Field.Store.YES));
      writer.addDocument(doc);
    }
    require(!recipes.isEmpty, "Something wrong, indexing an empty list")
    //  val dir = new RAMDirectory()//FSDirectory.open(new File(indexPath))
    val analyzer = new StandardAnalyzer(version)
    val iwc = new IndexWriterConfig(version, analyzer);
    val writer = new IndexWriter(dir, iwc);

    recipes.foreach(f => {
      indexRecipes(writer, f);
      //    println("added")
    })
    // println("closed")
    writer.close();
  }
  def searchRecipeSize(text: String, reader: Directory): Int = {

    //DirectoryReader.open(FSDirectory.open(new File(indexPath)));
    getScoreDocs(text, reader).size

  }

  private def getScoreDocs(text: String, reader: Directory) = {
      require(text.trim().length() >0,"search text is empty")
    val searcher = new IndexSearcher(DirectoryReader.open(reader));

    val analyzer = new StandardAnalyzer(version)
    val parser = new QueryParser(version, "title", analyzer);
    val query = parser.parse(text);
    val results = searcher.search(query, 100);
    results.scoreDocs
  }
  def loadRecipe(recipe:Recipe):Recipe = {
   loadRecipe (recipe.recipeType, recipe.getOriginalLines)
  }
  def loadRecipe(recipeType:String, content:String):Recipe = {
    recipeType match {
      case "FDX" => FDXRecipeReader.loadRecipe(content)
      case  "MXP" =>MXPRecipeReader.loadRecipe(content)
      case _=> ErrorRecipe
    }
  }
  def searchRecipes(text: String, curPage: Int, itemsPerPage: Int, reader: Directory): List[Recipe] = {
    
  
    val searcher = new IndexSearcher(DirectoryReader.open(reader));
    val contentAndType=getScoreDocs(text, reader).toList.map(
    				f => (searcher.doc(f.doc).get("originalLines"),searcher.doc(f.doc).get("recipeType"))
    				)
    
    val allPages= (
        
         
        for (i <- contentAndType) yield loadRecipe(i._1,i._2))
      .toList
      .grouped(itemsPerPage)
      
      if(allPages.isEmpty)List() else allPages.toList(curPage)

  }

  def main(args: Array[String]) {
    val root = "data/MXP/mxpfiles"
    val files = new File(root).listFiles()
    val recipes = (for (file <- files) yield readRecipeList(file.getAbsolutePath())).toList.flatten
    for {
      files <- Option(new File(indexPath).listFiles)
      file <- files
    } file.delete()
    val directory = FSDirectory.open(new File(indexPath))
    createIndex(recipes, directory)
    val reader = DirectoryReader.open(directory)
    println("Total documents:" + reader.numDocs())

  }

}