name := "Lift 2.6 starter template"

version := "0.0.3"

organization := "net.liftweb"

scalaVersion := "2.10.0"

resolvers ++= Seq("snapshots"     at "http://oss.sonatype.org/content/repositories/snapshots",
                "releases"        at "http://oss.sonatype.org/content/repositories/releases"
                )

seq(com.github.siasia.WebPlugin.webSettings :_*)

unmanagedResourceDirectories in Test <+= (baseDirectory) { _ / "src/main/webapp" }

scalacOptions ++= Seq("-deprecation", "-unchecked")

libraryDependencies ++= { 
  val liftVersion = "2.6-M2"
  Seq(
    "net.liftweb"       %% "lift-webkit"        % liftVersion        % "compile",
    "net.liftmodules"   %% "lift-jquery-module_2.6" % "2.5",
    "net.liftmodules"   %% "fobo_2.6"           % "1.2"              % "compile",
    "net.liftweb" %% "lift-mapper" % liftVersion % "compile" withSources() ,
    "net.liftweb"               %% "lift-mongodb-record"      % liftVersion,
    "org.eclipse.jetty" % "jetty-webapp"        % "8.1.7.v20120910"  % "container,test",
    "org.eclipse.jetty.orbit" % "javax.servlet" % "3.0.0.v201112011016" % "container,test" artifacts Artifact("javax.servlet", "jar", "jar"),
    "com.foursquare"             %% "rogue-core"          % "2.2.0" intransitive(),
    "ch.qos.logback"    % "logback-classic"     % "1.0.6",
    "org.specs2"        %% "specs2"             % "1.14"            % "test",
    "com.h2database"    % "h2"                  % "1.3.167"
  )
}

