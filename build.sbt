name := "cslib"
organization := "se.lth.cs"
version := "0.0.1"
scalaVersion := "2.12.5"
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
fork := true
