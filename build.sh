clear
echo "Building packages ..."
mvn compile assembly:single
echo "Copying ..."
cp -v ./target/*.jar ./dist