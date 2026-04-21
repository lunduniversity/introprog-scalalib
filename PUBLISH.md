# Instruction for repo maintainers

First two sections are preparations only done once for all or once per machine. Last comes what is done when actually publishing.

## Already done once and for all: Setup publication to Sonatype

These instructions have already been followed for this repo by Bjorn Regnell who has claimed the name space se.lth.cs and the artefact id introprog:

* https://www.scala-sbt.org/release/docs/Using-Sonatype.html#Sonatype+setup

* Instruction videos: https://central.sonatype.org/pages/ossrh-guide.html

* New project ticket (requires login to Jira): https://issues.sonatype.org/browse/OSSRH-42634?filter=-2

## Sbt config and GPG Key setup (done once per machine)

Read carefully these instructions: https://www.scala-sbt.org/release/docs/Using-Sonatype.html

and adapt according to adaptations below 

### Adaptations 

In the file `~/.sbt/1.0/plugins/gpg.sbt` (instead of project/plugins.sbt) you should have:
```
addSbtPlugin("com.github.sbt" % "sbt-pgp" % "2.3.1")
```

In the file `~/.sbt/sonatype_central_credentials` you should have the userid and password of an active User Token. You only get to see that when you create one, so you need to put it somewhere safe upon creation. 

In the file `~/.sbt/1.0/credentials.sbt ` you should have:
```
credentials += Credentials(Path.userHome / ".sbt" / "sonatype_central_credentials")
```


## How to publish

1. Make sure your shell use Java 17: `sdk use java 17.0.18-tem` or similar

2. Build and test locally using `sbt "compile;test;doc"`

3. Bump `lazy val Version` in `build.sbt`, run `package` in sbt. Note no plus before package as from 1.2.0 we only publish for Scala 3. We also want a release on github and the course home page aligned with the release on Sonatype Central. Therefore You should also:
  - Don't forget to update the `doc/index.md` file with current version information and package contents etc. Read more on scaladoc here: https://docs.scala-lang.org/scala3/scaladoc.html
  - commit all changes and push and *then* create a github release with the packaged jar uploaded to https://github.com/lunduniversity/introprog-scalalib/releases
  - Publish the jar to the course home page at http://cs.lth.se/lib using  `sh publish-jar.sh`
  - Publish updated docs to the course home page at http://cs.lth.se/api using script `sh publish-doc.sh`
  - Copy the introprog-scalalib/src the workspace subdir at https://github.com/lunduniversity/introprog to enable eclipse project generation with internal dependency of projects using `sh publish-workspace.sh`. Then run `sbt eclipse` IN THAT repo and `sh package.sh` to create `workspace.zip` etc. TODO: For the future it would be **nice** to have another repo introprog-workspace and factor out code to that repo and solve the problem of dependency between latex code and the workspace.
  - Update the link http://www.cs.lth.se/pgk/lib in typo3 so that it links to the right http://fileadmin.cs.lth.se/pgk/introprog_3-x.y.z.jar


4. In `sbt>` run `publishSigned`  which requires correct credentials, see Adaptations above.

5. After you have done `sbt publishSigned` then log into Sonatype Nexus here: (if the page does not load, clear the browser's cache by pressing Ctrl+F5) https://central.sonatype.com/publishing/deployments

6. Or click on the upper left drop-down and choose *View deployments*. Click "Refresh" if list is empty. 

7. Expand the "Component Files" and download the published `introprog_3-x.y.z.jar`

8. Save it e.g. in `tmp`.

9.  Verify that the staged jar downloaded  works by running something similar to `scala-cli repl . -S 3.8.3 --jar introprog_3-1.5.0.jar` and in REPL e.g. `val w = new introprog.PixelWindow` or `introprog.examples.TestPixelWindow.main(Array())`. The reason for this step is that there has been incidents where the uploading has failed and the jar was empty. A published jar can not be retracted even if corrupted according to Sonatype policies.

10. Click the *Publish* button. This will take ages (if you are lucky 15 min or else hours)

11. Click the green arrow "Refresh" icon now and then. 

12. When visible on Central at https://repo1.maven.org/maven2/se/lth/cs/introprog_3/ verify with 
```
scala repl --dep "se.lth.cs::introprog:1.5.0" --jvm 21 -- --repl-init-script 'introprog.PixelWindow()'
```
that it is automatically identified and downloaded and a black window should appear.
