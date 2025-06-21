@echo off
echo ===================================
echo    CAVE GAME FOLDER ORGANIZER
echo ===================================
echo.
echo This script will organize your CaveGame folder into a structured directory layout.
echo It will create the following directories:
echo  - src (for source code)
echo  - bin (for compiled classes)
echo  - scripts (for batch files)
echo  - docs (for documentation)
echo  - assets (for game assets)
echo.
echo WARNING: This will move files around in your directory.
echo Make sure you have a backup if needed before proceeding.
echo.
set /p confirm=Are you sure you want to continue? (Y/N): 

if /i "%confirm%" neq "Y" (
    echo Operation cancelled.
    pause
    exit /b
)

echo.
echo Running organization script...
powershell -ExecutionPolicy Bypass -File OrganizeFiles.ps1

if %ERRORLEVEL% EQU 0 (
    echo.
    echo Organization completed successfully!
) else (
    echo.
    echo An error occurred during organization.
)

echo.
echo You can now find:
echo  - Source code in the 'src' directory
echo  - Compiled classes in the 'bin' directory
echo  - Batch files in the 'scripts' directory
echo  - Documentation in the 'docs' directory
echo  - Game assets in the 'assets' directory
echo.
pause