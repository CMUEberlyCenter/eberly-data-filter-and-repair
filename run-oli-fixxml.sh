clear

outputFile=$2
unameOut="$(uname -s)"

if [ -z "$1" ] ; then
    echo "No input file supplied"
    exit 1
fi

if [ -z "$2" ] ; then
    echo "No output file supplied, using input argument"
    outputFile="$1.fixed.tsv"
fi

echo "Writing to: ${outputFile}"

if type -p java; then
    echo found java executable in PATH
    _java=java
elif [[ -n "$JAVA_HOME" ]] && [[ -x "$JAVA_HOME/bin/java" ]];  then
    #echo found java executable in JAVA_HOME     
    _java="$JAVA_HOME/bin/java"
else
    echo "no java"
    exit 1
fi

#if [[ "$_java" ]]; then
#    version=$("$_java" -version 2>&1 | awk -F '"' '/version/ {print $2}')
#    echo Found Java version: "$version"
#    if [[ "$version" > "1.5" ]]; then
#        noop
#    else         
#        echo Java version is less than 1.5
#        exit 1
#    fi
#fi

java -cp ./dist/datafiltering-1.0-SNAPSHOT-jar-with-dependencies.jar edu.cmu.eberly.DataFiltering --cell_operation fixxml --target 18 --iformat t --oformat t --input $1 --output $outputFile
