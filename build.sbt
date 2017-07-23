name := "rock-paper-scissors-puzzle"

lazy val commonSettings = Seq(
  organization := "com.faccuo",
  version := "1.0",
  scalaVersion := "2.11.8",
  libraryDependencies ++= Seq(
    "org.scalatest" %% "scalatest" % "3.0.1" % "test"
  )
)

lazy val core = (project in file("core"))
  .settings(
    commonSettings
  )

lazy val console = (project in file("console"))
  .settings(
    commonSettings,
    mainClass in Compile := Some("com.faccuo.puzzle.Main")
  ).dependsOn(core)

lazy val root = (project in file("."))
  .aggregate(core)