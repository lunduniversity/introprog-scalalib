# Instruction for repo maintainers

First two sections are preparations only done once for all or once per machine. Last comes what is when actually publishing.

## Already done once and for all: Setup publication to Sonatype

These instructions have already been followed for this repo by Bjorn Regnell who has claimed the name space se.lth.cs and the artefact id introprog:

* https://www.scala-sbt.org/release/docs/Using-Sonatype.html#Sonatype+setup

* Instruction videos: https://central.sonatype.org/pages/ossrh-guide.html

* New project ticket (requires login to Jira): https://issues.sonatype.org/browse/OSSRH-42634?filter=-2

## Sbt config and GPG Key setup (done once per machine)

Read and adapt these instructions:

* https://www.scala-sbt.org/release/docs/Using-Sonatype.html
  * Be aware that step 1 was not used, instead the instructions from this link were used to create keys:
    * https://github.com/scalacenter/sbt-release-early/wiki/How-to-create-a-gpg-key

  * Step 2-4 from above was used. Then after key generation, step 5 should work according to "How to publish" below. See the last parts of this repo's `build.sbt` and these instructions:

Issue commands below one at a time to make files in `~/.sbt/` and key pair in ascii in `~/.sbt/gpg` and publish key in `~/ci-keys` and then copy to `.sbt/gpg` tested on Ubuntu 18.04 using `gpg --version` at 2.2.4. 

```
cd ~
mkdir ci-keys 
chmod -R go-rwx ci-keys
cd ci-keys
gpg --homedir . --gen-key
gpg --homedir . -a --export > pubring.asc
gpg --homedir . -a --export-secret-keys > secring.asc
gpg --homedir . --list-key  
# <copy> the pub hex string e.g E7232FE8B8357EEC786315FE821738D92B63C95F
gpg --homedir . --keyserver hkp://pool.sks-keyservers.net --send-keys <paste>
mkdir -p ~/.sbt/gpg
cd ~/.sbt/gpg
cp -R ~/ci-keys/* .
```

After this you should have this these files `~/.sbt/gpg`:

```
$ cat ~/.sbt/1.0/plugins/gpg.sbt 
addSbtPlugin("com.jsuereth" % "sbt-pgp" % "1.1.1")

$ cat ~/.sbt/sonatype_credential 
realm=Sonatype Nexus Repository Manager
host=oss.sonatype.org
user=<YOURUSERID>
password=<YOURPASSWORD>

$ cat ~/.sbt/1.0/sonatype.sbt 
credentials += Credentials(Path.userHome / ".sbt" / "sonatype_credential")

$ ls ~/.sbt/gpg
crls.d            private-keys-v1.d  pubring.kbx   secring.asc
openpgp-revocs.d  pubring.asc        trustdb.gpg

```

Here is some more info on (unnecessary) config that was **NOT** used:
* https://www.scala-sbt.org/sbt-pgp/usage.html

## How to publish

1. Build and test locally.

2. Bump version in `build.sbt`, run `sbt package`. We also want a release on github and the course home page aligned with the release on Sonatype Central. Therefore You should also:
  - Don't forget to uppdate the `rootdoc.txt` file with current version information and package contents etc.: https://github.com/lunduniversity/introprog-scalalib/blob/master/src/rootdoc.txt
  - commit all changes and push and *then* create a github release with the packaged jar uploaded to https://github.com/lunduniversity/introprog-scalalib/releases
  - Publish the jar to the course home page at http://cs.lth.se/lib using  `sh publish-jar.sh`
  - Publish updated docs to the course home page at http://cs.lth.se/api using script `sh publish-doc.sh`
  - Copy the introprog-scalalib/src the workspace subdir at https://github.com/lunduniversity/introprog to enable eclipse project generation with internal dependency of projects using `sh publish-workspace.sh`. Then run `sbt eclipse` IN THAT repo and `sh package.sh` to create `workspace.zip` etc. TODO: For the future it would be **nice** to have another repo introprog-workspace and factor out code to that repo and solve the problem of dependency between latex code and the workspace.
  - Update the link http://www.cs.lth.se/pgk/lib in typo3 so that it links to the right http://fileadmin.cs.lth.se/pgk/introprog_2.12-x.y.z.jar

3. In `sbt` run `publishSigned`

4. Log into Sonatype Nexus here: (if the page does not load, clear the browser's cache by pressing Ctrl+F5) https://oss.sonatype.org/#welcome

5. Click on *Staging Repositories* in the Build Promotion list to the left. Click "Refresh" if list is empty. https://oss.sonatype.org/#stagingRepositories

6. Scroll down and select selthcs-100X and select the *Contents* tab and expand until leaf level of the tree where you can see the `introprog_2.12-x.y.z.jar`

7. Download the staged jar by clicking on it and selecting the *Artifact* tab to the right and click the Repository Path to download. Save it e.g. in `tmp`.

8. Verify that the staged jar downloaded from sonatype works by running `scala -cp introprog_2.12-x.y.z.jar` and in REPL e.g. `val w = new introprog.PixelWindow`. The reason for this step is that there has been incidents where the uploading has failed and the jar was empty. A published jar can not be retracted even if corrupted according to Sonatype policies.

9. Click the *Close* icon with a diskett above the repository list to "close" the staging repository.

10. After a while (typically a couple of minutes) the *Release* icon with a chain above the repository list is enabled. Click it when enabled. You can keep the "Automatically Drop" checkbox checked, which means that when the repo is published on Central the staging repo is removed from the list.

11. By searching here you can see the repo in progress of being published but it takes a while before it is publicly visible on Central (typically 10-15 minutes). https://oss.sonatype.org/#nexus-search;quick~se.lth.cs

12. When visible on Central at https://repo1.maven.org/maven2/se/lth/cs/introprog_2.12/ verify with a simple sbt project that it works as shown in [README usage instructions for sbt](https://github.com/lunduniversity/introprog-scalalib/blob/master/README.md#using-sbt).
