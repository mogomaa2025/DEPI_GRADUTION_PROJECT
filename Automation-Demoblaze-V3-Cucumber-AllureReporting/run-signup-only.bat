@echo off
echo ========================================
echo Running SignUp Tests Only
echo ========================================

echo Running signup-related test scenarios...
echo.

call mvn test -Dtest=SmokeTestRunner "-Dcucumber.filter.tags=@signup" -q

if %errorlevel% equ 0 (
    echo.
    echo ✅ SignUp tests completed successfully!
) else (
    echo.
    echo ❌ Some SignUp tests failed
)

echo.
echo ========================================
echo SignUp test execution completed
echo ========================================
pause