@echo off

cls

set currentPath=%cd%


set JAVA_HOME=%currentPath%\Java
echo Entorno JAVA: %JAVA_HOME%

set CATALINA_HOME=%currentPath%\apache-tomcat-8.0.26
echo Entorno Tomcat: %CATALINA_HOME%

echo Arrancando Tomcat...


%CATALINA_HOME%\bin\startup.bat