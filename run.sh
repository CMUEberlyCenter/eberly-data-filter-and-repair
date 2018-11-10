clear
echo "Executing ..."
java -cp ./dist/datafiltering-1.0-SNAPSHOT-jar-with-dependencies.jar edu.cmu.eberly.DataFiltering -v --operation json2xml --target 9 --format t --input ./data/dev-03_export_zminerof.tsv --output ./output/dev-03_export_zminerof.filtered.tsv
#java -cp ./dist/datafiltering-1.0-SNAPSHOT-jar-with-dependencies.jar edu.cmu.eberly.DataFiltering -v --operation removewhitespace --target 8 --format t --input ./data/person-tabseparator.txt --output ./output/person-tabseparator.filtered.tsv
