package com.recipegrace.web.launcher

import java.io.File

import com.recipegrace.biglibrary.core.{BaseTest, ZipArchive}

/**
  * Created by Ferosh Jacob on 1/23/16.
  */
class ResourceLoaderTest extends BaseTest with ZipArchive {


  test("resource test s") {
    val root = unZip(this.getClass().getResourceAsStream("/data.zip"))

    println(root)
    new File(root).listFiles().foreach(f => println(f.getName))
  }
}
