@echo off
echo ===================================
echo   Super Mario Game - Compilation
echo ===================================
echo.

REM Check if Java is installed
java -version >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Java is not installed or not in your PATH.
    echo Please install Java Development Kit (JDK) and try again.
    pause
    exit /b 1
)

echo Compiling SuperMarioGame.java...
javac SuperMarioGame.java

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo ERROR: Compilation failed!
    echo Please check the error messages above.
    pause
    exit /b 1
)

echo.
echo Compilation successful!
echo.
echo ===================================
echo   Starting Super Mario Game
echo ===================================
echo.
echo Controls:
echo - Arrow keys or WASD to move
echo - Space or Up to jump
echo - Collect all coins to win
echo - Jump on enemies to defeat them
echo.

java SuperMarioGame

echo.
echo Game closed.
echo Thanks for playing!
pause