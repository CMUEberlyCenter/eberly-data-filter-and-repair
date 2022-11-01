clear

echo "Testing installation ..."

#if type -p java; then
#    echo found java executable in PATH
#    _java=java
#elif [[ -n "$JAVA_HOME" ]] && [[ -x "$JAVA_HOME/bin/java" ]];  then
#    #echo found java executable in JAVA_HOME     
#    _java="$JAVA_HOME/bin/java"
#else
#    echo "no java"
#    exit 1
#fi

#if [[ "$_java" ]]; then
#    version=$("$_java" -version 2>&1 | awk -F '"' '/version/ {print $2}')
#    #echo version "$version"
#    if [[ "$version" > "1.5" ]]; then
#        #echo version is more than 1.5
#        echo
#    else         
#        echo Java version is less than 1.5
#        exit 1
#    fi
#fi

echo "Cleaning ..."
rm -rf ./dist/*.jar
rm -rf ./target/*.jar

echo "Building packages ..."
mvn compile assembly:single

echo "Copying ..."
cp -v ./target/*.jar ./dist
