@echo off
echo Compiling SimpleMarioGame.java...
javac -cp . src\SimpleMarioGame.java

if %errorlevel% neq 0 (
    echo Compilation failed!
    pause
    exit /b %errorlevel%
)

echo Running SimpleMarioGame...
java -cp . src.SimpleMarioGame

if %errorlevel% neq 0 (
    echo Execution failed!
    pause
    exit /b %errorlevel%
)

pause