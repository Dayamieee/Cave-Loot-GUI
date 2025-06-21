@echo off
echo Compiling and running Image Load Test...

REM Compile the test
javac ImageLoadTest.java

REM Check if compilation was successful
if %errorlevel% neq 0 (
    echo Compilation failed!
    pause
    exit /b %errorlevel%
)

REM Run the test
java ImageLoadTest

REM Pause to see any error messages
pause