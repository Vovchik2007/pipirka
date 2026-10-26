@echo off
echo Requires JDK 17 and Gradle/IntelliJ IDEA with Gradle support.
gradle build
if errorlevel 1 pause
echo If successful, see build\libs\nocturne-0.1.0.jar
pause
