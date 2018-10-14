# Instruction for repo maintainers

## Already done once and for all: Setup publication to Sonatype

These instructions have already been followed for this repo by Bjorn Regnell who has claimed the name space se.lth.cs and the artefact id introprog:

* https://www.scala-sbt.org/release/docs/Using-Sonatype.html#Sonatype+setup

* Instruction videos: https://central.sonatype.org/pages/ossrh-guide.html

* New project ticket (requires login to Jira): https://issues.sonatype.org/browse/OSSRH-42634?filter=-2

## How to publish

1. Build and test locally.

2. Bump version in `build.sbt`, run `sbt package`. We also want a release on github and the course home page aligned with the release on Sonatype Central. Therefore You should also:
  - Don't forget to uppdate the `rootdoc.txt` file with current version information and package contents etc.: https://github.com/lunduniversity/introprog-scalalib/blob/master/src/rootdoc.txt
  - commit all changes and push and *then* create a github release with the packaged jar uploaded to https://github.com/lunduniversity/introprog-scalalib/releases
  - Publish the jar to the course home page at http://cs.lth.se/lib using  `sh publish-jar.sh`
  - Publish updated docs to the course home page at http://cs.lth.se/api using script `sh publish-doc.sh`
  - Copy the introprog-scalalib/src the workspace subdir at https://github.com/lunduniversity/introprog to enable eclipse project generation with internal dependency of projects using `sh publish-workspace.sh`. Then run `sbt eclipse` in that repo and `sh package.sh` to create `workspace.zip` etc. TODO: For the future it would be nice to have another repo introprog-workspace and factor out code to that repo and solve the problem of dependency between latex code and the workspace.

3. In `sbt` run `publishedSigned`

4. Log into Sonatype Nexus here: (if the page does not load, clear the browser's cache by pressing Ctrl+F5) https://oss.sonatype.org/#welcome

5. Click on *Staging Repositories* in the Build Promotion list to the left. Click "Refresh" if list is empty. https://oss.sonatype.org/#stagingRepositories

6. Scroll down and select selthcs-100X and select the *Contents* tab and expand until leaf level of the tree where you can see the `introprog_2.12-x.y.z.jar`

7. Download the staged jar by clicking on it and selecting the *Artifact* tab to the right and click the Repository Path to download. Save it e.g. in `tmp`.

8. Verify that the staged jar downloaded from sonatype works by running `scala -cp introprog-xxx.jar` and in REPL e.g. `val w = new introprog.PixelWindow`. The reason for this step is that there has been incidents where the uploading has failed and the jar was empty. A published jar can not be retracted even if corrupted according to Sonatype policies.

9. Click the *Close* icon with a diskett above the repository list to "close" the staging repository.

10. After a while (typically a couple of minutes) the *Release* icon with a chain above the repository list is enabled. Click it when enabled. You can keep the "Automatically Drop" checkbox checked, which means that when the repo is published on Central the staging repo is removed from the list.

11. By searching here you can see the repo in progress of being published but it takes a while before it is publicly visible on Central (typically 10-15 minutes). https://oss.sonatype.org/#nexus-search;quick~se.lth.cs

12. When visible on Central at https://repo1.maven.org/maven2/se/lth/cs/introprog_2.12/ verify with a simple sbt project that it works as shown in [README usage instructions for sbt](https://github.com/lunduniversity/introprog-scalalib/blob/master/README.md#using-sbt).
