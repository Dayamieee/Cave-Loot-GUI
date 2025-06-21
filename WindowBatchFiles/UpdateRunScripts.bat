@echo off
echo ===================================
echo    UPDATE RUN SCRIPTS
echo ===================================
echo.
echo This script will update the batch files used to run the games
echo after the directory reorganization.
echo.
echo Make sure you have already run OrganizeFiles.bat first!
echo.
set /p confirm=Are you sure you want to continue? (Y/N): 

if /i "%confirm%" neq "Y" (
    echo Operation cancelled.
    pause
    exit /b
)

echo.
echo Running script update script...
powershell -ExecutionPolicy Bypass -File UpdateRunScripts.ps1

if %ERRORLEVEL% EQU 0 (
    echo.
    echo Run script updates completed successfully!
) else (
    echo.
    echo An error occurred during run script updates.
)

echo.
pause