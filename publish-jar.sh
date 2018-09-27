sbt package
VERSION="$(grep -m 1 -Po -e '\d+.\d+.\d+' build.sbt)"
JARFILE="introprog_2.12-$VERSION.jar"
DEST="$LUCATID@web.cs.lth.se:/Websites/Fileadmin/pgk/"
echo Copying $JARFILE to $DEST
scp  "target/scala-2.12/$JARFILE" $DEST
