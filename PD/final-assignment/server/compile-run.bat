@echo off
setLocal enableDelayedExpansion

set path_src=.\src
set path_out=.\out
set path_lib=.\lib

set external_lib=

set path_main_class=.\src\pt\isec\metapd\Server.java
set package_main_class=pt.isec.metapd.Server

if not exist "%path_out%" mkdir %path_out%

cd %path_lib%
FOR /F "tokens=* USEBACKQ" %%F IN (`dir /s /b *.jar`) DO (SET external_lib=%%F;!external_lib!)
cd ..

javac -d %path_out% -sourcepath %path_src% -classpath !external_lib! %path_main_class%
cd %path_out%
java -classpath !external_lib! %package_main_class% localhost 2420 localhost
cd ..

echo.
echo.
echo DONE!
echo.
pause