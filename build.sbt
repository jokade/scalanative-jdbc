organization in ThisBuild := "de.surfice"

version in ThisBuild := "0.0.1-SNAPSHOT"

scalaVersion in ThisBuild := "2.11.11"

val Version = new {
  val slogging    = "0.6.0-SNAPSHOT"
  val utest       = "0.4.8"
}

lazy val commonSettings = Seq(
  scalacOptions ++= Seq("-deprecation","-unchecked","-feature","-language:implicitConversions","-Xlint"),
  libraryDependencies ++= Seq(
    "com.lihaoyi" %%% "utest" % Version.utest % "test"
  ),
  testFrameworks += new TestFramework("utest.runner.Framework")
)

lazy val nativeSettings = Seq(
  nativeCompileOptions ++= Seq(),
  nativeLinkingOptions ++= Seq("-lsqlite3")
)

lazy val loggingSettings = Seq(
  libraryDependencies ++= Seq(
    "biz.enef" %%% "slogging" % Version.slogging
  )
)

lazy val root = project.in(file("."))
  .enablePlugins(ScalaNativePlugin)
  .aggregate(api,sqlite,sqlite_jdbc)
  .settings(commonSettings++nativeSettings++dontPublish:_*)
  .settings(
    name := "scalanative-jdbc"
  )

lazy val api = project
  .enablePlugins(ScalaNativePlugin)
  .settings(commonSettings++nativeSettings:_*)
  .settings(
    name := "scalanative-jdbc-api",
    // This is required to have incremental compilation to work in javalib.
    // We put our classes on scalac's `javabootclasspath` so that it uses them
    // when compiling rather than the definitions from the JDK.
    scalacOptions in Compile := {
      val previous = (scalacOptions in Compile).value
      val javaBootClasspath =
        scala.tools.util.PathResolver.Environment.javaBootClassPath
      val classDir  = (classDirectory in Compile).value.getAbsolutePath()
      val separator = sys.props("path.separator")
      "-javabootclasspath" +: s"$classDir$separator$javaBootClasspath" +: previous
    },
    // Don't include classfiles for javalib in the packaged jar.
    mappings in packageBin in Compile := {
      val previous = (mappings in packageBin in Compile).value
      previous.filter {
        case (file, path) =>
          !path.endsWith(".class")
      }
    }

  )

lazy val sqlite = project
  .enablePlugins(ScalaNativePlugin)
  .settings(commonSettings++nativeSettings:_*)
  .settings(
    name := "scalanative-sqlite"
  )

lazy val sqlite_jdbc = project
  .dependsOn(api % "compile->compile;test->test",sqlite)
  .enablePlugins(ScalaNativePlugin)
  .settings(commonSettings++nativeSettings++loggingSettings:_*)
  .settings(
    name := "scalanative-jdbc-sqlite",
    // This is required to have incremental compilation to work in javalib.
    // We put our API classes on scalac's `javabootclasspath` so that it uses them
    // when compiling rather than the definitions from the JDK.
    scalacOptions in Compile := {
      val previous = (scalacOptions in Compile).value
      val javaBootClasspath =
        scala.tools.util.PathResolver.Environment.javaBootClassPath
      val classDir  = (classDirectory in (api,Compile)).value.getAbsolutePath()
      val separator = sys.props("path.separator")
      "-javabootclasspath" +: s"$classDir$separator$javaBootClasspath" +: previous
    }

  )

 

lazy val dontPublish = Seq(
  publish := {},
  publishLocal := {},
  com.typesafe.sbt.pgp.PgpKeys.publishSigned := {},
  com.typesafe.sbt.pgp.PgpKeys.publishLocalSigned := {},
  publishArtifact := false,
  publishTo := Some(Resolver.file("Unused transient repository",file("target/unusedrepo")))
)
