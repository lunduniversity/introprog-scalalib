VERSION="$(grep -m 1 -Po -e '\d+.\d+.\d+' build.sbt)"
SCALAVERSION=2.13

JARFILE="introprog_$SCALAVERSION-$VERSION.jar"
DEST="$LUCATID@web.cs.lth.se:/Websites/Fileadmin/pgk/"

sbt package
echo Copying $JARFILE to $DEST
scp  "target/scala-$SCALAVERSION/$JARFILE" $DEST
