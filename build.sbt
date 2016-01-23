
val projectName = "RecipeGrace"
val projectVersion = "0.0.5"
val projectOrg = "com.recipegrace"
val liftVersion = "2.6.2"
val electricVersion = "0.0.9"

lazy val coreSettings= Seq(
scalaVersion := "2.11.7",
version := projectVersion,
organization := projectOrg,
resolvers ++= Seq("snapshots"     at "https://oss.sonatype.org/content/repositories/snapshots",
                  "releases"      at "https://oss.sonatype.org/content/repositories/releases",
  "Recipegrace repo" at "http://recipegrace.com:8080/nexus/content/repositories/releases/",
  "Recipegrace snapshots" at "http://recipegrace.com:8080/nexus/content/repositories/snapshots/"

//"MVN Repo" at "http://central.maven.org/maven2/"
),
libraryDependencies ++= {
  Seq(
     "org.apache.lucene" % "lucene-core" % "4.9.0",
     "org.apache.lucene" % "lucene-analyzers-common" % "4.9.0",
      "org.apache.lucene" % "lucene-queryparser" % "4.9.0",   
       "com.recipegrace.electric" %% "core" %electricVersion,
       "org.dbpedia.extraction" % "core" % "4.1-SNAPSHOT")})

lazy val core = (project in file("core")).
 settings(coreSettings:_*).
 settings(name:="core")

lazy val web = (project in file("web")).
 settings(coreSettings:_*).
enablePlugins(TomcatPlugin).
// settings(webSettins :_*).
 settings(name:="web",
//unmanagedResourceDirectories in Test <+= (baseDirectory) { _ / "src/main/webapp" },
scalacOptions ++= Seq("-deprecation", "-unchecked", "-feature"),
libraryDependencies ++= {
  Seq(
    "net.liftweb"       %% "lift-webkit"        % liftVersion        % "compile",
    "net.liftweb"       %% "lift-mapper"        % liftVersion        % "compile",
    "net.liftmodules"   %% "fobo_2.6"           % "1.4"     % "compile",
    "net.liftweb" %% "lift-mongodb-record" % liftVersion % "compile",
    "net.liftmodules" %% "mongoauth_2.6" % "0.6-SNAPSHOT",
    "net.liftmodules" %% "extras_2.6" % "0.4-SNAPSHOT" % "compile",
    "com.foursquare" %% "rogue-lift" % "2.4.0",
    "net.liftmodules" %% "google-analytics_2.6" % "1.1-SNAPSHOT",
        "org.eclipse.jetty" % "jetty-webapp"        % "8.1.17.v20150415"  % "container,test",
        "org.eclipse.jetty" % "jetty-plus"          % "8.1.17.v20150415"  % "container,test", // For Jetty Config
        "org.eclipse.jetty.orbit" % "javax.servlet" % "3.0.0.v201112011016" % "container,test" artifacts Artifact("javax.servlet", "jar", "jar")
)
}).dependsOn(core)
 


