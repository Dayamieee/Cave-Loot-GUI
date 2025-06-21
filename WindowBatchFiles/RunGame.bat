@echo off
echo ===================================
echo    CAVE LOOT CHALLENGE LAUNCHER
echo ===================================
echo.
echo Compiling Cave Loot Challenge...
javac CaveLootChallenge.java ItemRenderer.java

if %ERRORLEVEL% EQU 0 (
    echo.
    echo Compilation successful! Starting the game...
    echo.
    java CaveLootChallenge
    echo.
    echo Game closed. Thanks for playing!
) else (
    echo.
    echo Compilation failed. Please check for errors.
    echo Make sure you have Java Development Kit (JDK) installed.
)

echo.
echo Press any key to exit...
pause > nul