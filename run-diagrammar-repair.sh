clear
echo "Executing ..."

if type -p java; then
    echo found java executable in PATH
    _java=java
elif [[ -n "$JAVA_HOME" ]] && [[ -x "$JAVA_HOME/bin/java" ]];  then
    echo found java executable in JAVA_HOME     
    _java="$JAVA_HOME/bin/java"
else
    echo "no java"
fi

if [[ "$_java" ]]; then
    version=$("$_java" -version 2>&1 | awk -F '"' '/version/ {print $2}')
    echo version "$version"
    if [[ "$version" > "1.5" ]]; then
        echo version is more than 1.5
    else         
        echo version is less than 1.5
    fi
fi

#java -cp ./dist/datafiltering-1.0-SNAPSHOT-jar-with-dependencies.jar edu.cmu.eberly.DataFiltering -v -w --operation removenewline --target 20 --iformat t --oformat t --input /Volumes/32GB/Data/diagrammar/diagram-F18.tsv --output /Volumes/32GB/Data/diagrammar/diagram-F18.repaired.tsv
java -cp ./dist/datafiltering-1.0-SNAPSHOT-jar-with-dependencies.jar edu.cmu.eberly.DataFiltering -v -w --operation removenewline --target 20 --iformat t --oformat t --input /Volumes/32GB/Data/diagrammar/diagram-M1.tsv --output /Volumes/32GB/Data/diagrammar/diagram-M1.repaired.tsv
java -cp ./dist/datafiltering-1.0-SNAPSHOT-jar-with-dependencies.jar edu.cmu.eberly.DataFiltering -v -w --operation removenewline --target 20 --iformat t --oformat t --input /Volumes/32GB/Data/diagrammar/diagram-M2.tsv --output /Volumes/32GB/Data/diagrammar/diagram-M2.repaired.tsv