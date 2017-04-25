//enablePlugins(ScalaJSPlugin)

name := "editing-trees-with-zippers"

scalaVersion in ThisBuild := "2.11.8"

scalacOptions in Test ++= Seq("-Yrangepos")

libraryDependencies ++= Seq(
  "com.lihaoyi" %% "fastparse" % "0.4.2",
//  "org.scala-js" %%% "scalajs-dom" % "0.9.1",
//  "org.singlespaced" %%% "scalajs-d3" % "0.3.4",
  "org.specs2" %% "specs2-core" % "3.8.9" % "test",
  "io.argonaut" %% "argonaut" % "6.1"
)

//scalaJSUseMainModuleInitializer := true
