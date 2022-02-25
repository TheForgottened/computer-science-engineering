@echo off
setLocal enableDelayedExpansion

set path_src=.\src
set path_out=.\out

set path_main_class=.\src\pt\isec\metapd\LoadBalancer.java
set package_main_class=pt.isec.metapd.LoadBalancer

if not exist "%path_out%" mkdir %path_out%

javac -d %path_out% -sourcepath %path_src% %path_main_class%
cd %path_out%
java %package_main_class%
cd ..

echo.
echo.
echo DONE!
echo.
pause