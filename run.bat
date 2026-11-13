@echo off
title Anti-Ragging Incident Reporting System - Kanishk Sharma
color 0B
cls
echo =======================================================================
echo    ANTI-RAGGING INCIDENT REPORTING SYSTEM - NSP MINOR PROJECT
echo    Student: Kanishk Sharma ^| Student ID: PIET25CR023 ^| Branch: 3CSD
echo =======================================================================
echo.

:: Step 1: Check Java
echo [1/3] Checking Java environment...
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo [ERROR] Java runtime is not found in PATH!
    echo Please install JDK 11 or higher and add it to system PATH.
    echo.
    pause
    exit /b 1
)

:: Step 2: Compile source files
echo [2/3] Compiling project source code...
if not exist "target\classes" mkdir "target\classes"

javac -encoding UTF-8 -cp "lib\mysql-connector-j-8.3.0.jar;src\main\resources" -d "target\classes" src\main\java\com\antiragging\*.java src\main\java\com\antiragging\db\*.java src\main\java\com\antiragging\model\*.java src\main\java\com\antiragging\dao\*.java src\main\java\com\antiragging\service\*.java src\main\java\com\antiragging\util\*.java src\main\java\com\antiragging\ui\*.java

if %errorlevel% neq 0 (
    echo [ERROR] Compilation failed! Please check Java errors above.
    echo.
    pause
    exit /b %errorlevel%
)

:: Copy configuration files
copy /Y "src\main\resources\*" "target\classes\" >nul 2>&1

:: Step 3: Launch Application
echo [3/3] Starting Application GUI...
echo.
echo Starting Anti-Ragging Portal...
echo Close this window or the application to exit.
echo.

java -cp "target\classes;lib\mysql-connector-j-8.3.0.jar;src\main\resources" com.antiragging.Main

if %errorlevel% neq 0 (
    echo.
    echo Application exited with code %errorlevel%.
    pause
)
