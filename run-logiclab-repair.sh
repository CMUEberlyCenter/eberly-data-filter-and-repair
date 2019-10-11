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

echo "Cleaning ..."
java -cp ./target/datafiltering-1.0-SNAPSHOT-jar-with-dependencies.jar edu.cmu.eberly.DataFiltering -v -w -pc "removenewline|removeboundingquotes|fixxml" --target 9 --iformat c --oformat t --input /Volumes/32GB/Data/logiclab/80210-f18-save-file.csv --output /Volumes/32GB/Data/logiclab/80210-f18-save-file.fixed.tsv
#java -cp ./target/datafiltering-1.0-SNAPSHOT-jar-with-dependencies.jar edu.cmu.eberly.DataFiltering -v -w -pc "removenewline" --target 9 --iformat c --oformat t --input /Volumes/32GB/Data/logiclab/80210-f18-save-file.csv --output /Volumes/32GB/Data/logiclab/80210-f18-save-file.fixed.tsv

echo "Anonymizing ..."
java -cp ./target/datafiltering-1.0-SNAPSHOT-jar-with-dependencies.jar edu.cmu.eberly.DataFiltering -v -w -pc "hashcode" --target 2 -s "3ab41657-d916-4a1d-803d-3c831e70170c" --iformat t --oformat t --input /Volumes/32GB/Data/logiclab/80210-f18-save-file.fixed.tsv --output /Volumes/32GB/Data/logiclab/80210-f18-save-file.fixed.anonymized.tsv

echo "Exporting per row ..."
#java -cp ./target/datafiltering-1.0-SNAPSHOT-jar-with-dependencies.jar edu.cmu.eberly.DataFiltering -v -w -pr "export" --iformat t --input /Volumes/32GB/Data/logiclab/80210-f18-save-file.fixed.anonymized.tsv
