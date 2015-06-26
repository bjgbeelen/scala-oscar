import sbt._
import Keys._
import spray.revolver.RevolverPlugin.Revolver
import sbtassembly.AssemblyPlugin.autoImport._

object Build extends Build {
  import BuildInfo._
  import Dependencies._
  import Formatting._
  import Packager._
  import TestCoverage._
  import BlazeArtifactory._

  val targetScalaVersion = "2.11.6"

  lazy val basicSettings = Seq(
    organization := "com.xebia",
    version := "0.1.0",
    scalaVersion := targetScalaVersion,
    scalacOptions := basicScalacOptions,
    incOptions := incOptions.value.withNameHashing(true)
  )

  lazy val libSettings = basicSettings ++ dependencySettings ++ formattingSettings ++ testCoverageSetings
  lazy val integrationTestSettings = Defaults.itSettings
  lazy val appSettings = libSettings ++ Revolver.settings ++ integrationTestSettings

  lazy val scalaoscar = Project("scala-oscar-innovationday", file("."))
    .configs(IntegrationTest)
    .settings(appSettings: _*)
    .settings(internalDependencyClasspath in IntegrationTest <++= (exportedProducts in Test))
    .settings(assemblyJarName := "scala-oscar.jar")
    .settings(mainClass := Some("com.xebia.innovation.Main"))
    .settings(buildInfoGeneratorSettings("com.xebia.innovation"): _*)
    .settings(packagerSettings: _*)
    .settings(javaOptions in Revolver.reStart := List("-Djava.library.path=/usr/local/lib"))
    //.settings(javaOptions in Revolver.reStart := List("-Dconfig.file=./etc/local.conf"))
    .settings(libraryDependencies ++=
      compile(
        scalaReflect,
        akkaActor,
        akkaSlf4j,
        ficus,
        sprayCache,
        sprayCan,
        sprayRouting,
        sprayClient,
        sprayJson,
        logback,
        logstashLogbackEncoder,
        oscarLinprog,
        jodaTime      
        ) ++
      test(
        scalatest))

  // ==========================================================================
  // ScalacOptions
  // ==========================================================================

  val basicScalacOptions = Seq(
    "-encoding", "utf8",
    "-target:jvm-1.7",
    "-feature",
    "-language:implicitConversions",
    "-language:postfixOps",
    "-unchecked",
    "-deprecation",
    "-Xlog-reflective-calls"
  )

  val fussyScalacOptions = basicScalacOptions ++ Seq(
    "-Ywarn-unused",
    "-Ywarn-unused-import"
  )
}
