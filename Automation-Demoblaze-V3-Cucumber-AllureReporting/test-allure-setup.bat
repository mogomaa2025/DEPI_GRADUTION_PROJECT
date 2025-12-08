@echo off
echo ========================================
echo Testing Enhanced Allure Reporting Setup
echo ========================================

echo 1. Compiling project...
call mvn compile test-compile -q

if %errorlevel% neq 0 (
    echo ERROR: Compilation failed
    pause
    exit /b 1
)

echo ✅ Compilation successful

echo.
echo 2. Running a quick smoke test...
call mvn test -Dtest=SmokeTestRunner -q

echo.
echo 3. Generating Allure report...
call mvn allure:report

if exist target\allure-report\index.html (
    echo ✅ Allure report generated successfully
    echo 📊 Report location: target\allure-report\index.html
) else (
    echo ❌ Allure report generation failed
)

if exist target\screenshots (
    echo ✅ Screenshots directory created
)

if exist target\videos (
    echo ✅ Videos directory created
)

echo.
echo ========================================
echo Setup Test Complete!
echo ========================================
echo.
echo Next steps:
echo 1. Open target\allure-report\index.html in browser
echo 2. Run: mvn allure:serve (for live server)
echo 3. Use: ./run-tests-with-allure.bat for full features
echo.
pause