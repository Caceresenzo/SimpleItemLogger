@ECHO off

NET SESSION
IF %ERRORLEVEL% NEQ 0 GOTO NEED_ELEVATE
GOTO ADMINTASKS

:NEED_ELEVATE
cls

echo x
echo x
echo x
echo x Vous devez executer cette application avec les privileges administrateurs.
echo x
echo x
echo x

pause
EXIT

:ADMINTASKS
cls

if not exist "%~DP0\working\" mkdir "%~DP0\working\"

"%~DP0\bin\shortcut.exe" /a:c /i:"%~DP0\resources\icon.ico" /f:"C:\Users\%username%\Desktop\Negro MAT..lnk" /t:"%~DP0\simpleitemlogger.cmd"
"%~DP0\bin\shortcut.exe" /a:c /i:"%~DP0\resources\icon.ico" /f:"C:\ProgramData\Microsoft\Windows\Start Menu\Programs\Negro MAT..lnk" /t:"%~DP0\simpleitemlogger.cmd"

pause;