@echo off
echo ===================================
echo    UPDATE FILE PATHS
echo ===================================
echo.
echo This script will update file paths in Java source files
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
echo Running path update script...
powershell -ExecutionPolicy Bypass -File UpdatePaths.ps1

if %ERRORLEVEL% EQU 0 (
    echo.
    echo Path updates completed successfully!
) else (
    echo.
    echo An error occurred during path updates.
)

echo.
pause