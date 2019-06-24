lazy val Version = "1.1.2"
lazy val Name    = "introprog"

name := Name
version := Version
scalaVersion := "2.12.8"
fork in (Compile, console) := true

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

javacOptions in (Compile, compile) ++= Seq("-target", "1.8")

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

// Below enables publishing to central.sonatype.org according to
//  https://www.scala-sbt.org/release/docs/Using-Sonatype.html
// sbt> publishedSigned

useGpg := true

ThisBuild / organization := "se.lth.cs"
ThisBuild / organizationName := "LTH"
ThisBuild / organizationHomepage := Some(url("http://cs.lth.se/"))

ThisBuild / scmInfo := Some(
  ScmInfo(
    url("https://github.com/lunduniversity/introprog-scalalib"),
    "scm:git@github.com:lunduniversity/introprog-scalalib.git"
  )
)
ThisBuild / developers := List(
  Developer(
    id    = "bjornregnell",
    name  = "Bjorn Regnell",
    email = "bjorn.regnell@cs.lth.se",
    url   = url("http://cs.lth.se/bjornregnell")
  )
)

ThisBuild / description := "Scala utilities for introductory Computer Science teaching."
ThisBuild / licenses := List("BSD 2-Clause" -> new URL("https://opensource.org/licenses/BSD-2-Clause"))
ThisBuild / homepage := Some(url("https://github.com/lunduniversity/introprog-scalalib"))

// Remove all additional repository other than Maven Central from POM
ThisBuild / pomIncludeRepository := { _ => false }
ThisBuild / publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value) Some("snapshots" at nexus + "content/repositories/snapshots")
  else Some("releases" at nexus + "service/local/staging/deploy/maven2")
}
ThisBuild / publishMavenStyle := true

//https://oss.sonatype.org/#stagingRepositories
//https://oss.sonatype.org/#nexus-search;quick~se.lth.cs
//https://repo1.maven.org/maven2/se/lth/cs/introprog_2.12/
