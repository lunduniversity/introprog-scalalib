lazy val Name    = "introprog"
lazy val Version = "1.5.0"  // next version to be published
lazy val scala3  = "3.3.7"  // stay on 3.3 LTS for maximum compatibility until 3.9 LTS is established
lazy val munitVersion = "1.3.0" // https://mvnrepository.com/artifact/org.scalameta/munit

Global / onChangedBuildSource := ReloadOnSourceChanges

// to avoid strange warnings, these lines with excludeLintKeys are needed:
Global / excludeLintKeys += ThisBuild / Compile / console / fork

lazy val introprog = (project in file("."))
  .settings(
    name := Name,
    version := Version,
    scalaVersion := scala3,
    libraryDependencies += "org.scalameta" %% "munit" % munitVersion % Test,
  )

ThisBuild / Compile / console / fork := true

ThisBuild / scalacOptions ++= Seq(
  "-encoding", "UTF-8",
  "-unchecked",
  "-deprecation",
  "-new-syntax",
  "-Werror",
)

//ThisBuild / Compile / compile / javacOptions ++= Seq("-target", "1.8") // for backward compat

Compile / doc / scalacOptions ++= Seq(
  "-groups",
  "-project-version", Version,
  "-project-footer", "Dep. of Computer Science, Lund University, Faculty of Engineering LTH",
  "-siteroot", ".",
  "-doc-root-content", "./docs/index.md",
  "-source-links:github://lunduniversity/introprog-scalalib/master",
  "-social-links:github::https://github.com/lunduniversity/introprog-scalalib"
)

// Below enables publishing to central.sonatype.org 
// see PUBLISH.md for instructions
// base on https://www.scala-sbt.org/release/docs/Using-Sonatype.html
// usage inside sbt: BUT READ PUBLISH.md FIRST  
// sbt> publishSigned
// sbt> sonaUpload
// DON'T PANIC: it takes looong time to run it
// login in to https://central.sonatype.com
//   goto https://central.sonatype.com/publishing/deployments
//   and press the Publish button 
//   or do this in sbt with the same effect as pushing the button:
// sbt> sonaRelease

ThisBuild / versionScheme := Some("early-semver")


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
ThisBuild / licenses := List("BSD 2-Clause" -> url("https://opensource.org/licenses/BSD-2-Clause"))
ThisBuild / homepage := Some(url("https://github.com/lunduniversity/introprog-scalalib"))

// Remove all additional repository other than Maven Central from POM
ThisBuild / pomIncludeRepository := { _ => false }

ThisBuild / publishTo := {
  val centralSnapshots = "https://central.sonatype.com/repository/maven-snapshots/"
  if (isSnapshot.value) Some("central-snapshots" at centralSnapshots)
  else localStaging.value
}

ThisBuild / publishMavenStyle := true

publishConfiguration := publishConfiguration.value.withOverwrite(true)
publishLocalConfiguration := publishLocalConfiguration.value.withOverwrite(true)

//https://repo1.maven.org/maven2/se/lth/cs/introprog_2.12/



