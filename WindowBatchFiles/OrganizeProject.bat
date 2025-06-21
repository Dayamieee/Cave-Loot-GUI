@echo off
echo ===================================
echo    CAVE GAME PROJECT ORGANIZER
echo ===================================
echo.
echo This script will organize your CaveGame project into a structured directory layout
echo and update all necessary file paths and run scripts.
echo.
echo The process includes:
echo  1. Creating a structured directory layout
echo  2. Moving files to appropriate directories
echo  3. Updating file paths in source code
echo  4. Updating run scripts
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
echo Step 1: Organizing files...
powershell -ExecutionPolicy Bypass -File OrganizeFiles.ps1

if %ERRORLEVEL% NEQ 0 (
    echo Error occurred during file organization.
    pause
    exit /b %ERRORLEVEL%
)

echo.
echo Step 2: Updating file paths...
powershell -ExecutionPolicy Bypass -File UpdatePaths.ps1

if %ERRORLEVEL% NEQ 0 (
    echo Error occurred during path updates.
    pause
    exit /b %ERRORLEVEL%
)

echo.
echo Step 3: Updating run scripts...
powershell -ExecutionPolicy Bypass -File UpdateRunScripts.ps1

if %ERRORLEVEL% NEQ 0 (
    echo Error occurred during run script updates.
    pause
    exit /b %ERRORLEVEL%
)

echo.
echo ===================================
echo    ORGANIZATION COMPLETE
echo ===================================
echo.
echo Your CaveGame project has been successfully organized!
echo.
echo New directory structure:
echo  - src/     (Source code files)
echo  - bin/     (Compiled class files)
echo  - scripts/ (Batch files to run the games)
echo  - docs/    (Documentation)
echo  - assets/  (Game assets)
echo.
echo To run the games, use the batch files in the scripts directory.
echo.
pause