@echo off
echo ========================================
echo    MyBank - Fix Build Error
echo ========================================
echo.

cd /d "%~dp0"

echo Current directory: %CD%
echo.

echo [1/3] Deleting app\build...
if exist "app\build" (
    rmdir /s /q "app\build"
    echo    ✓ app\build deleted
) else (
    echo    ! app\build not found (already clean)
)
echo.

echo [2/3] Deleting build...
if exist "build" (
    rmdir /s /q "build"
    echo    ✓ build deleted
) else (
    echo    ! build not found (already clean)
)
echo.

echo [3/3] Deleting .gradle...
if exist ".gradle" (
    rmdir /s /q ".gradle"
    echo    ✓ .gradle deleted
) else (
    echo    ! .gradle not found (already clean)
)
echo.

echo ========================================
echo    ✓ BUILD FOLDERS CLEANED!
echo ========================================
echo.
echo NEXT STEPS:
echo 1. Go to Android Studio
echo 2. Click: Build -^> Rebuild Project
echo 3. Wait 2-3 minutes
echo 4. Click Run (play button)
echo.
echo Press any key to close...
pause >nul
