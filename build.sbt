lazy val Version = "1.1.5"
lazy val Name    = "introprog"
lazy val scala213 = "2.13.6"
lazy val scala3  = "3.0.0" 
lazy val supportedScalaVersions = List(scala213, scala3)

// to avoid strange warnings, these lines with excludeLintKeys are needed:
  Global / excludeLintKeys += ThisBuild / Compile / console / fork
  Global / excludeLintKeys += ThisBuild / Compile / doc / scalacOptions


lazy val introprog = (project in file("."))
  .settings(
    name := Name,
    version := Version,
    scalaVersion := scala3,
    crossScalaVersions := supportedScalaVersions,
  )

ThisBuild / Compile / console / fork := true

//https://github.com/scalacenter/sbt-version-policy
ThisBuild / versionScheme := Some("early-semver")
ThisBuild / versionPolicyIntention := Compatibility.None
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

ThisBuild / Compile / compile / javacOptions ++= Seq("-target", "1.8")

ThisBuild / Compile / doc / scalacOptions ++= Seq(
  "-implicits",
  "-groups",
  "-doc-title", Name,
  "-doc-footer", "Dep. of Computer Science, Lund University, Faculty of Engineering LTH",
  "-sourcepath", (ThisBuild/baseDirectory).value.toString,
  "-doc-version", Version,
  "-doc-root-content", (ThisBuild/baseDirectory).value.toString + "/src/rootdoc.txt",
  "-doc-source-url", s"https://github.com/lunduniversity/introprog-scalalib/tree/master€{FILE_PATH}.scala"
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

//https://oss.sonatype.org/#stagingRepositories
//https://oss.sonatype.org/#nexus-search;quick~se.lth.cs
//https://repo1.maven.org/maven2/se/lth/cs/introprog_2.12/

//usePgpKeyHex("E7232FE8B8357EEC786315FE821738D92B63C95F")

//https://github.com/sbt/sbt-pgp