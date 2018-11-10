clear
echo "Executing ..."
java -cp ./dist/datafiltering-1.0-SNAPSHOT-jar-with-dependencies.jar edu.cmu.eberly.DataFiltering -v --operation json2xml --target 9 --format t --input ./data/dev-03_export_zminerof.tsv
