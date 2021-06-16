#VERSION="$(grep -m 1 -Po -e '\d+.\d+.\d+' build.sbt)"
VERSION=1.1.5
SCALAVERSION=3.0.0
SCALACOMPAT=3

JARFILE="introprog_$SCALACOMPAT-$VERSION.jar"
DEST="$LUCATID@fileadmin.cs.lth.se:/Websites/Fileadmin/pgk/"

sbt package
echo Copying $JARFILE to $DEST
scp  "target/scala-$SCALAVERSION/$JARFILE" $DEST
