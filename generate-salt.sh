salt="fixedstring"
unameOut="$(uname -s)"

case "${unameOut}" in
    Linux*)     salt=$(cat /proc/sys/kernel/random/uuid);;
    Darwin*)    salt=`uuidgen`;;
    CYGWIN*)    salt=$(cat /proc/sys/kernel/random/uuid);;
    MINGW*)     salt="NOTAVAILABLEONMINGW";;
    *)          salt="NOTAVAILABLEONUNKNOWN"
esac

echo $salt