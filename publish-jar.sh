#VERSION="$(grep -m 1 -Po -e '\d+.\d+.\d+' build.sbt)"
VERSION=1.2.0
SCALAVERSION=3.0.1
SCALACOMPAT=3

JARFILE="introprog_$SCALACOMPAT-$VERSION.jar"
DEST="$LUCATID@fileadmin.cs.lth.se:/Websites/Fileadmin/pgk/"

sbt package
echo Copying $JARFILE to $DEST
scp  "target/scala-$SCALAVERSION/$JARFILE" $DEST
