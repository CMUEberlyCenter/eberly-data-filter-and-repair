clear
echo "Executing ..."
java -cp ./dist/datafiltering-1.0-SNAPSHOT-jar-with-dependencies.jar edu.cmu.eberly.DataFiltering -v --operation toupper --target 8 --format t --input ./data/person-tabseparator.txt --output ./output/person-tabseparator-toupper.filtered.tsv
