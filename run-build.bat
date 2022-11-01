@echo off
cls
echo "Building packages ..."
REM call mvn clean compile assembly:single
call mvn compile assembly:single
echo "Copy ..."
copy /B /Y target\*.jar dist\
