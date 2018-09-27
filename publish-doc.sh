sbt doc
echo "*** scp docs to web.cs.lth.se"
scp -r target/scala-2.12/api $LUCATID@web.cs.lth.se:/Websites/Fileadmin/pgk/
