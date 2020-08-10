SCALAVERSION=2.13
sbt doc
echo "*** scp docs to web.cs.lth.se"
scp -r target/scala-$SCALAVERSION/api $LUCATID@web.cs.lth.se:/Websites/Fileadmin/pgk/
