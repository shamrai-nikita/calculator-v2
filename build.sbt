scalaVersion := "2.13.11"

name := "calculator"
organization := "com.wix"
version := "1.0"

scalacOptions ++= Seq(
  "-Xlint",
  "-Wconf:any:e",
)

libraryDependencies ++= Seq("org.specs2" %% "specs2-core" % "4.19.2" % "test")