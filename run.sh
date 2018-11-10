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

java -cp ./dist/datafiltering-1.0-SNAPSHOT-jar-with-dependencies.jar edu.cmu.eberly.DataFiltering -v --operation json2xml --target 9 --format t --input ./data/dev-03_export_zminerof.tsv --output ./output/dev-03_export_zminerof.filtered.tsv
#java -cp ./dist/datafiltering-1.0-SNAPSHOT-jar-with-dependencies.jar edu.cmu.eberly.DataFiltering -v --operation removewhitespace --target 8 --format t --input ./data/person-tabseparator.txt --output ./output/person-tabseparator.filtered.tsv
