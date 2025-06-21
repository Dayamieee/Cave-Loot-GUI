@echo off
echo Compiling CaveLootChallenge.java and ItemRenderer.java...
javac -cp . src\CaveLootChallenge.java src\ItemRenderer.java
if %errorlevel% neq 0 (
    echo Compilation failed!
    pause
    exit /b %errorlevel%
)
echo Running CaveLootChallenge...
java -cp . CaveLootChallenge
pause