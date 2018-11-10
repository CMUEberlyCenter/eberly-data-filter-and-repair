@echo off
cls
echo "Building packages ..."
REM call mvn clean compile assembly:single
call mvn compile assembly:single
echo "Executing ..."
java -cp ./target/datafiltering-1.0-SNAPSHOT-jar-with-dependencies.jar edu.cmu.eberly.DataFiltering -v --operation json2xml --target 8 --format t --input ./data/dev-03_export_zminerof.tsv --output ./output/dev-03_export_zminerof.filtered.tsv
REM java -cp ./target/datafiltering-1.0-SNAPSHOT-jar-with-dependencies.jar edu.cmu.eberly.DataFiltering -h