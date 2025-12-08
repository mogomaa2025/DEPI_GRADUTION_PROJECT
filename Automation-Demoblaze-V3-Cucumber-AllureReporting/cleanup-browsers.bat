@echo off
echo ========================================
echo Browser Process Cleanup Utility
echo ========================================

echo Checking for running browser processes...
echo.

:: Check for Chrome processes
echo Chrome processes:
tasklist /FI "IMAGENAME eq chrome.exe" 2>nul | find /I "chrome.exe" && set CHROME_FOUND=true
tasklist /FI "IMAGENAME eq chromedriver.exe" 2>nul | find /I "chromedriver.exe" && set CHROMEDRIVER_FOUND=true

:: Check for Firefox processes
echo.
echo Firefox processes:
tasklist /FI "IMAGENAME eq firefox.exe" 2>nul | find /I "firefox.exe" && set FIREFOX_FOUND=true
tasklist /FI "IMAGENAME eq geckodriver.exe" 2>nul | find /I "geckodriver.exe" && set GECKODRIVER_FOUND=true

:: Check for Edge processes
echo.
echo Edge processes:
tasklist /FI "IMAGENAME eq msedge.exe" 2>nul | find /I "msedge.exe" && set EDGE_FOUND=true
tasklist /FI "IMAGENAME eq msedgedriver.exe" 2>nul | find /I "msedgedriver.exe" && set EDGEDRIVER_FOUND=true

echo.
echo ========================================

:: Ask user for confirmation
set /p CONFIRM="Do you want to kill all browser and driver processes? (y/N): "

if /I "%CONFIRM%"=="y" (
    echo.
    echo Killing browser processes...
    
    :: Kill Chrome
    if defined CHROME_FOUND (
        echo Killing Chrome processes...
        taskkill /F /IM chrome.exe /T >nul 2>&1
    )
    if defined CHROMEDRIVER_FOUND (
        echo Killing ChromeDriver processes...
        taskkill /F /IM chromedriver.exe /T >nul 2>&1
    )
    
    :: Kill Firefox
    if defined FIREFOX_FOUND (
        echo Killing Firefox processes...
        taskkill /F /IM firefox.exe /T >nul 2>&1
    )
    if defined GECKODRIVER_FOUND (
        echo Killing GeckoDriver processes...
        taskkill /F /IM geckodriver.exe /T >nul 2>&1
    )
    
    :: Kill Edge
    if defined EDGE_FOUND (
        echo Killing Edge processes...
        taskkill /F /IM msedge.exe /T >nul 2>&1
    )
    if defined EDGEDRIVER_FOUND (
        echo Killing EdgeDriver processes...
        taskkill /F /IM msedgedriver.exe /T >nul 2>&1
    )
    
    echo.
    echo ✅ Browser cleanup completed!
    echo.
    
    :: Wait a moment and check again
    timeout /t 2 >nul
    
    echo Verifying cleanup...
    tasklist /FI "IMAGENAME eq chrome.exe" 2>nul | find /I "chrome.exe" >nul || echo ✅ Chrome: Clean
    tasklist /FI "IMAGENAME eq chromedriver.exe" 2>nul | find /I "chromedriver.exe" >nul || echo ✅ ChromeDriver: Clean
    tasklist /FI "IMAGENAME eq firefox.exe" 2>nul | find /I "firefox.exe" >nul || echo ✅ Firefox: Clean
    tasklist /FI "IMAGENAME eq geckodriver.exe" 2>nul | find /I "geckodriver.exe" >nul || echo ✅ GeckoDriver: Clean
    tasklist /FI "IMAGENAME eq msedge.exe" 2>nul | find /I "msedge.exe" >nul || echo ✅ Edge: Clean
    tasklist /FI "IMAGENAME eq msedgedriver.exe" 2>nul | find /I "msedgedriver.exe" >nul || echo ✅ EdgeDriver: Clean
    
) else (
    echo.
    echo Cleanup cancelled by user.
)

echo.
echo ========================================
echo Cleanup utility finished
echo ========================================
pause