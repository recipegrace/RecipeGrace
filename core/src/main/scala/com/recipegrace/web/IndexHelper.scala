package com.recipegrace.web

import java.io.{File, StringReader}

import com.recipegrace.biglibrary.core.ZipArchive
import org.apache.lucene.analysis.standard.StandardAnalyzer
import org.apache.lucene.document.{Document, Field, StringField, TextField}
import org.apache.lucene.index.{DirectoryReader, IndexWriter, IndexWriterConfig}
import org.apache.lucene.queryparser.classic.QueryParser
import org.apache.lucene.search.IndexSearcher
import org.apache.lucene.store.{Directory, FSDirectory}
import org.apache.lucene.util.Version

object IndexHelper extends ZipArchive {

  val indexPath = "recipeIndex"
  val version = Version.LUCENE_4_9

  //val indexed = init()


  def createIndex(recipes: List[Recipe], dir: Directory) = {
    def indexRecipes(writer: IndexWriter, f: Recipe) = {
      val doc = new Document();
      doc.add(new TextField("process", new StringReader(f.getProcess.mkString("\n"))));
      doc.add(new TextField("title", new StringReader(f.getTitle)));
      doc.add(new TextField("ingredients", new StringReader(f.getIngredientNames.mkString(","))));
      doc.add(new StringField("originalLines", f.getOriginalLines, Field.Store.YES));
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
    logger.info("index created at " + dir)
    writer.close();
  }

  def searchRecipeSize(text: String, reader: Directory): Int = {

    getScoreDocs(text, reader).size

  }

  private def getScoreDocs(text: String, reader: Directory) = {
    require(text.trim().length() > 0, "search text is empty")
    val searcher = new IndexSearcher(DirectoryReader.open(reader));

    val analyzer = new StandardAnalyzer(version)
    val parser = new QueryParser(version, "title", analyzer);
    val query = parser.parse(text);
    val results = searcher.search(query, 100);
    results.scoreDocs
  }

  def loadRecipe(recipe: Recipe): Recipe = {
    loadRecipe(recipe.recipeType, recipe.getOriginalLines)
  }

  def loadRecipe(content: String, recipeType: String): Recipe = {

    recipeType.trim match {
      case "FDX" => FDXRecipeReader.loadRecipe(content)
      case "MXP" => MXPRecipeReader.loadRecipe(content)
    }
  }

  def searchRecipes(text: String, curPage: Int, itemsPerPage: Int, reader: Directory): List[Recipe] = {

    logger.info(s"searching for $text")
    val searcher = new IndexSearcher(DirectoryReader.open(reader));
    val contentAndType = getScoreDocs(text, reader).toList.map(
      f => (searcher.doc(f.doc).get("originalLines"), searcher.doc(f.doc).get("recipeType"))
    )

    val allPages = (


      for (i <- contentAndType) yield loadRecipe(i._1, i._2))
      .toList
      .grouped(itemsPerPage)

    if (allPages.isEmpty) List() else allPages.toList(curPage)

  }

  def createIndexAndReport(recipes: List[Recipe]) = {
    val directory = FSDirectory.open(new File(indexPath))
    createIndex(recipes, directory)
    val reader = DirectoryReader.open(directory)
    logger.info("Total recipes:" + reader.numDocs())
  }

  def deleteIndex() = {
    for {
      files <- Option(new File(indexPath).listFiles)
      file <- files
    } file.delete()
  }

  def createMXP(filePath: String) = {

    val files = new File(filePath).listFiles()
    val recipes = (for (file <- files) yield MXPRecipeReader.readRecipeList(file.getAbsolutePath())).toList.flatten
    createIndexAndReport(recipes)
  }

  def init() = {
    deleteIndex()
    val root = unZip(this.getClass.getResourceAsStream("/data.zip"))
    createMXP(root + File.separator + "data" + File.separator + "MXP" + File.separator + "mxpfiles")
    createFDX(root + File.separator + "data" + File.separator + "FDX")
    logger.info("index created!")
  }

  def createFDX(filePath: String) = {
    val files = new File(filePath).listFiles()
    val recipes = (
      for (file <- files
           if file.getName.toLowerCase.endsWith("fdx")
      )
        yield FDXRecipeReader.readRecipeList(file.getAbsolutePath)).toList.flatten
    createIndexAndReport(recipes)

  }

}