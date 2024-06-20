lazy val Version = "1.4.0"
lazy val Name    = "introprog"
lazy val scala3  = "3.3.3" 

Global / onChangedBuildSource := ReloadOnSourceChanges

// to avoid strange warnings, these lines with excludeLintKeys are needed:
Global / excludeLintKeys += ThisBuild / Compile / console / fork

lazy val introprog = (project in file("."))
  .settings(
    name := Name,
    version := Version,
    scalaVersion := scala3,
    libraryDependencies += "org.scalameta" %% "munit" % "0.7.29" % Test,
  )

ThisBuild / Compile / console / fork := true

//https://github.com/scalacenter/sbt-version-policy
ThisBuild / versionScheme := Some("early-semver")
ThisBuild / versionPolicyIntention := Compatibility.None
//ThisBuild / versionPolicyIntention := Compatibility.None
//ThisBuild / versionPolicyIntention := Compatibility.BinaryAndSourceCompatible
//ThisBuild / versionPolicyIntention := Compatibility.BinaryCompatible
//In the sbt shell check version using:
//sbt> versionCheck
//sbt> versionPolicyCheck
//sbt> last versionPolicyFindDependencyIssues
//sbt> last mimaPreviousClassfiles

ThisBuild / scalacOptions ++= Seq(
  "-encoding", "UTF-8",
  "-unchecked",
  "-deprecation",
//  "-Xfuture",
//  "-Yno-adapted-args",
//  "-Ywarn-dead-code",
//  "-Ywarn-numeric-widen",
//  "-Ywarn-value-discard",
//  "-Ywarn-unused"
)

ThisBuild / Compile / compile / javacOptions ++= Seq("-target", "1.8") // for backward compat

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
// usage inside sbt: BUT READ PUBLISH.md FIRST  - the plus is needed for cross building all versions
// sbt> + publishSigned
// DON'T PANIC: it takes looong time to run it

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

publishConfiguration := publishConfiguration.value.withOverwrite(true)
publishLocalConfiguration := publishLocalConfiguration.value.withOverwrite(true)
//pushRemoteCacheConfiguration := pushRemoteCacheConfiguration.value.withOverwrite(true)

credentials += Credentials(Path.userHome / ".sbt" / ".credentials")

//https://oss.sonatype.org/#stagingRepositories
//https://oss.sonatype.org/#nexus-search;quick~se.lth.cs
//https://repo1.maven.org/maven2/se/lth/cs/introprog_2.12/


//https://github.com/sbt/sbt-pgp

