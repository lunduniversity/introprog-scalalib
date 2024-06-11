#VERSION="$(grep -m 1 -Po -e '\d+.\d+.\d+' build.sbt)"
VERSION=1.4.0
SCALAVERSION=3.3.3
SCALACOMPAT=3

JARFILE="introprog_$SCALACOMPAT-$VERSION.jar"
DEST="$LUCATID@fileadmin.cs.lth.se:/Websites/Fileadmin/pgk/"

sbt package
echo Copying $JARFILE to $DEST
scp  "target/scala-$SCALAVERSION/$JARFILE" $DEST
