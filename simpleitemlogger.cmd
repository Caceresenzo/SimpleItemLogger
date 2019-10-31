@ECHO off

cd /d "%~DP0\working\"
start "Negro MAT" /b "%~DP0\bin\jre8\bin\javaw.exe" -jar "%~DP0\simpleitemlogger.jar"

exit