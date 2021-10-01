echo "*** Generating docs and copy api to fileadmin then zip it for local download"
set -x

SCALAVERSION=3.0.2
sbt doc

ssh $LUCATID@fileadmin.cs.lth.se rm -r pgk/api

scp -r target/scala-$SCALAVERSION/api $LUCATID@fileadmin.cs.lth.se:/Websites/Fileadmin/pgk/

cd target/scala-$SCALAVERSION/
zip -rv api.zip api
scp api.zip $LUCATID@fileadmin.cs.lth.se:/Websites/Fileadmin/pgk/
cd ../..