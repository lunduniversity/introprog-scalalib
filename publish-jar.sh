#VERSION="$(grep -m 1 -Po -e '\d+.\d+.\d+' build.sbt)"
VERSION=1.3.1
SCALAVERSION=3.0.2
SCALACOMPAT=3

JARFILE="introprog_$SCALACOMPAT-$VERSION.jar"
DEST="$LUCATID@fileadmin.cs.lth.se:/Websites/Fileadmin/pgk/"

sbt package
echo Copying $JARFILE to $DEST
scp  "target/scala-$SCALAVERSION/$JARFILE" $DEST
