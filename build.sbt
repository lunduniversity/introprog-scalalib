lazy val Version = "18.0.1"
lazy val Name    = "cslib"

name := Name
organization := "se.lth.cs"
version := Version
scalaVersion := "2.12.5"
fork := true

scalacOptions ++= Seq(
  "-encoding", "UTF-8",
  "-unchecked",
  "-deprecation",
  "-Xfuture",
  "-Yno-adapted-args",
  "-Ywarn-dead-code",
  "-Ywarn-numeric-widen",
//  "-Ywarn-value-discard",
  "-Ywarn-unused"
)

scalacOptions in (Compile, doc) ++= Seq(
  "-implicits",
  "-groups",
  "-doc-title", Name,
  "-doc-footer", "Lund University, Faculty of Engineering LTH",
  "-sourcepath", (baseDirectory in ThisBuild).value.toString,
  "-doc-version", Version,
  "-doc-root-content", (sourceDirectory in Compile).value.toString + "/scala/rootdoc.txt",
  "-doc-source-url", s"https://github.com/lunduniversity/cslib-scala/tree/master€{FILE_PATH}.scala"
)
