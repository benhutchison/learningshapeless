name := "ShapelessExercises"

version := "0.1"

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  "com.chuusai" %% "shapeless" % "2.3.0-RC4",
  "org.specs2" %% "specs2-core" % "3.7" % "test"
)

scalacOptions in Test ++= Seq("-Yrangepos")