clear

outputFile=$2
unameOut="$(uname -s)"
salt="fixedstring"

case "${unameOut}" in
    Linux*)     salt=$(cat /proc/sys/kernel/random/uuid);;
    Darwin*)    salt=`uuidgen`;;
    CYGWIN*)    salt=$(cat /proc/sys/kernel/random/uuid);;
    MINGW*)     salt="NOTAVAILABLEONMINGW";;
    *)          salt="NOTAVAILABLEONUNKNOWN"
esac

if [ -z "$1" ] ; then
    echo "No input file supplied"
    exit 1
fi

if [ -z "$2" ] ; then
    echo "No output file supplied, using input argument"
    outputFile="$1.anonymized.tsv"
fi

if [ -z "$3" ] ; then
    echo "No custom salt provided, using generated version: ${salt}"
else    
    salt=$3
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

java -cp ./dist/datafiltering-1.0-SNAPSHOT-jar-with-dependencies.jar edu.cmu.eberly.DataFiltering --cell_operation hashcode --s $salt --target 2 --iformat t --oformat t --input $1 --output $outputFile

echo "Anonymized with salt ${salt}, please write down this string and keep in a safe place"
