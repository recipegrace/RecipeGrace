name := "RecipeGrace"

version := "0.0.4"

organization := "net.liftweb"

scalaVersion := "2.11.1"

resolvers ++= Seq("snapshots"     at "https://oss.sonatype.org/content/repositories/snapshots",
                  "staging"       at "https://oss.sonatype.org/content/repositories/staging",
                  "Local Maven Repository" at "file://"+Path.userHome.absolutePath+"/.m2/repository",
                  "releases"      at "https://oss.sonatype.org/content/repositories/releases"
                 )

seq(webSettings :_*)

unmanagedResourceDirectories in Test <+= (baseDirectory) { _ / "src/main/webapp" }

scalacOptions ++= Seq("-deprecation", "-unchecked", "-feature")

libraryDependencies ++= {
  val liftVersion = "2.6-M4"
  Seq(
    "net.liftweb"       %% "lift-webkit"        % liftVersion        % "compile",
    "net.liftweb"       %% "lift-mapper"        % liftVersion        % "compile",
    "net.liftmodules"   %% "fobo_2.6"           % "1.3"     % "compile",
    "net.liftweb" %% "lift-mongodb-record" % liftVersion % "compile",
    "net.liftmodules" %% "mongoauth_3.0" % "0.6-SNAPSHOT",
    "net.liftmodules" %% "extras_2.6" % "0.4-SNAPSHOT" % "compile",
    "com.foursquare" %% "rogue-lift" % "2.4.0",
    "net.liftmodules" %% "google-analytics_2.6" % "1.1-SNAPSHOT",
     "org.apache.lucene" % "lucene-core" % "4.9.0",
     "org.apache.lucene" % "lucene-analyzers-common" % "4.9.0",
      "org.apache.lucene" % "lucene-queryparser" % "4.9.0",   
       "org.dbpedia.extraction" % "core" % "4.0-SNAPSHOT",
    "org.eclipse.jetty" % "jetty-webapp"        % "8.1.7.v20120910"  % "container,test",
    "org.eclipse.jetty" % "jetty-plus"          % "8.1.7.v20120910"  % "container,test", // For Jetty Config
    "org.eclipse.jetty.orbit" % "javax.servlet" % "3.0.0.v201112011016" % "container,test" artifacts Artifact("javax.servlet", "jar", "jar"),
    "ch.qos.logback"    % "logback-classic"     % "1.0.6",
    "org.specs2"        %% "specs2"             % "2.3.12"           % "test",
   "org.scalatest" %% "scalatest" % "2.2.1" % "test"
  )
} 


