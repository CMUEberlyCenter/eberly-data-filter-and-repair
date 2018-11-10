clear
echo "Building packages ..."
mvn compile assembly:single
echo "Executing ..."
java -cp ./target/datafiltering-1.0-SNAPSHOT-jar-with-dependencies.jar edu.cmu.eberly.DataFiltering
