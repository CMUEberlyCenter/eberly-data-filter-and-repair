clear
echo "Executing ..."
java -cp ./dist/datafiltering-1.0-SNAPSHOT-jar-with-dependencies.jar edu.cmu.eberly.DataFiltering -v -w --operation toupper --target 8 --iformat t --oformat t --input ./data/person-tabseparator.txt --output ./output/person-tabseparator-toupper.filtered.tsv
