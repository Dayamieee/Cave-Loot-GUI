@echo off
echo Compiling and running Super Mario Game with debug output...

REM Compile the game
javac SuperMarioGame.java

REM Check if compilation was successful
if %errorlevel% neq 0 (
    echo Compilation failed!
    pause
    exit /b %errorlevel%
)

REM Run the game
java SuperMarioGame

REM Pause to see any error messages
pause