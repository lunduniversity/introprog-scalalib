lazy val Version = "0.1.4"
lazy val Name    = "introprog"

name := Name
organization := "se.lth.cs"
version := Version
scalaVersion := "2.12.6"
fork in (Compile, console) := true

artifactName := { (sv: ScalaVersion, module: ModuleID, artifact: Artifact) =>
  artifact.name + "-" + module.revision + "." + artifact.extension
}

scalacOptions ++= Seq(
  "-encoding", "UTF-8",
  "-unchecked",
  "-deprecation",
  "-Xfuture",
//  "-Yno-adapted-args",
  "-Ywarn-dead-code",
  "-Ywarn-numeric-widen",
//  "-Ywarn-value-discard",
//  "-Ywarn-unused"
)

scalacOptions in (Compile, doc) ++= Seq(
  "-implicits",
  "-groups",
  "-doc-title", Name,
  "-doc-footer", "Dep. of Computer Science, Lund University, Faculty of Engineering LTH",
  "-sourcepath", (baseDirectory in ThisBuild).value.toString,
  "-doc-version", Version,
  "-doc-root-content", (baseDirectory in ThisBuild).value.toString + "/src/rootdoc.txt",
  "-doc-source-url", s"https://github.com/lunduniversity/introprog-scalalib/tree/master€{FILE_PATH}.scala"
)
