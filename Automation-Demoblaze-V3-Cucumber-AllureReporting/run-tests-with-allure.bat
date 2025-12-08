@echo off
setlocal EnableDelayedExpansion

echo ========================================
echo DemoBlaze Test Automation with Allure
echo ========================================

:: Default values
set TEST_SUITE=smoke
set VIDEO_ENABLED=true
set GENERATE_REPORT=true
set SERVE_REPORT=false
set BROWSER=chrome
set HEADLESS=false

:: Parse command line arguments
:parse_args
if "%1"=="" goto end_parse
if "%1"=="-suite" (
    set TEST_SUITE=%2
    shift
    shift
    goto parse_args
)
if "%1"=="-browser" (
    set BROWSER=%2
    shift
    shift
    goto parse_args
)
if "%1"=="-headless" (
    set HEADLESS=true
    shift
    goto parse_args
)
if "%1"=="-no-video" (
    set VIDEO_ENABLED=false
    shift
    goto parse_args
)
if "%1"=="-no-report" (
    set GENERATE_REPORT=false
    shift
    goto parse_args
)
if "%1"=="-serve" (
    set SERVE_REPORT=true
    shift
    goto parse_args
)
if "%1"=="-help" (
    goto show_help
)
shift
goto parse_args

:end_parse

echo Configuration:
echo - Test Suite: %TEST_SUITE%
echo - Browser: %BROWSER%
echo - Headless: %HEADLESS%
echo - Video Recording: %VIDEO_ENABLED%
echo - Generate Report: %GENERATE_REPORT%
echo - Serve Report: %SERVE_REPORT%
echo.

:: Clean previous results
echo Cleaning previous test results...
if exist target\allure-results rmdir /s /q target\allure-results
if exist target\allure-report rmdir /s /q target\allure-report
if exist target\screenshots rmdir /s /q target\screenshots
if exist target\videos rmdir /s /q target\videos
if exist target\cucumber-reports rmdir /s /q target\cucumber-reports

:: Set system properties
set MAVEN_OPTS=-Dbrowser=%BROWSER% -Dheadless=%HEADLESS% -Dvideo.recording.enabled=%VIDEO_ENABLED%

:: Run tests based on suite
echo Running %TEST_SUITE% tests...
if "%TEST_SUITE%"=="smoke" (
    call mvn clean test -Dtest=SmokeTestRunner -P%TEST_SUITE%
) else if "%TEST_SUITE%"=="regression" (
    call mvn clean test -Dtest=RegressionTestRunner -P%TEST_SUITE%
) else if "%TEST_SUITE%"=="e2e" (
    call mvn clean test -Dtest=E2ETestRunner -P%TEST_SUITE%
) else if "%TEST_SUITE%"=="critical" (
    call mvn clean test -Dtest=CriticalTestRunner -P%TEST_SUITE%
) else (
    echo Error: Unknown test suite '%TEST_SUITE%'
    goto show_help
)

set TEST_EXIT_CODE=%errorlevel%

echo.
echo Test execution completed with exit code: %TEST_EXIT_CODE%

:: Generate Allure report
if "%GENERATE_REPORT%"=="true" (
    echo.
    echo Generating Allure report...
    call mvn allure:report
    
    if !errorlevel! equ 0 (
        echo Allure report generated successfully at: target\allure-report\index.html
    ) else (
        echo Failed to generate Allure report
    )
)

:: Serve Allure report
if "%SERVE_REPORT%"=="true" (
    echo.
    echo Starting Allure server...
    echo Report will be available at: http://localhost:54321
    call mvn allure:serve
)

:: Show summary
echo.
echo ========================================
echo Test Execution Summary
echo ========================================
if exist target\allure-results (
    echo Allure results: target\allure-results
)
if exist target\screenshots (
    echo Screenshots: target\screenshots
)
if exist target\videos (
    echo Videos: target\videos
)
if exist target\cucumber-reports (
    echo Cucumber reports: target\cucumber-reports
)
if exist target\allure-report (
    echo Allure report: target\allure-report\index.html
)

if %TEST_EXIT_CODE% equ 0 (
    echo.
    echo ✅ All tests PASSED
) else (
    echo.
    echo ❌ Some tests FAILED - Check reports for details
)

goto end

:show_help
echo.
echo Usage: run-tests-with-allure.bat [options]
echo.
echo Options:
echo   -suite ^<name^>     Test suite to run (smoke, regression, e2e, critical)
echo   -browser ^<name^>   Browser to use (chrome, firefox, edge)
echo   -headless         Run tests in headless mode
echo   -no-video         Disable video recording
echo   -no-report        Skip Allure report generation
echo   -serve            Start Allure server after tests
echo   -help             Show this help message
echo.
echo Examples:
echo   run-tests-with-allure.bat
echo   run-tests-with-allure.bat -suite regression -browser firefox
echo   run-tests-with-allure.bat -suite smoke -headless -serve
echo   run-tests-with-allure.bat -suite e2e -no-video -serve
echo.

:end
pause