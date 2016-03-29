version in ThisBuild := "0.1"

scalaVersion in ThisBuild := "2.11.7"

lazy val main = Project("ShapelessExercises", file("."))
  .settings(
    libraryDependencies ++= Seq(
      "com.chuusai" %% "shapeless" % "2.3.0",
      "org.scala-lang" % "scala-reflect" % "2.11.7",
      "io.spray" %%  "spray-json" % "1.3.2",
      "org.specs2" %% "specs2-core" % "3.7" % "test"
    ),
    scalacOptions in Test ++= Seq("-Yrangepos")
  )

