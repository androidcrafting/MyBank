# ========================================
# Script de Fix Build Error - MyBank
# ========================================

Write-Host "🔧 Fixing MyBank Build Error..." -ForegroundColor Cyan
Write-Host ""

# Navigate to project directory
Set-Location "C:\Users\AdMin\AndroidStudioProjects\MyBank"

Write-Host "📁 Current directory: $(Get-Location)" -ForegroundColor Yellow
Write-Host ""

# Step 1: Delete app\build
Write-Host "Step 1/3: Deleting app\build folder..." -ForegroundColor Green
if (Test-Path ".\app\build") {
    Remove-Item -Recurse -Force ".\app\build"
    Write-Host "✅ app\build deleted" -ForegroundColor Green
} else {
    Write-Host "⚠️  app\build not found (already clean)" -ForegroundColor Yellow
}
Write-Host ""

# Step 2: Delete root build
Write-Host "Step 2/3: Deleting root build folder..." -ForegroundColor Green
if (Test-Path ".\build") {
    Remove-Item -Recurse -Force ".\build"
    Write-Host "✅ build deleted" -ForegroundColor Green
} else {
    Write-Host "⚠️  build not found (already clean)" -ForegroundColor Yellow
}
Write-Host ""

# Step 3: Delete .gradle
Write-Host "Step 3/3: Deleting .gradle folder..." -ForegroundColor Green
if (Test-Path ".\.gradle") {
    Remove-Item -Recurse -Force ".\.gradle"
    Write-Host "✅ .gradle deleted" -ForegroundColor Green
} else {
    Write-Host "⚠️  .gradle not found (already clean)" -ForegroundColor Yellow
}
Write-Host ""

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "✅ BUILD FOLDERS CLEANED SUCCESSFULLY!" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

Write-Host "📋 NEXT STEPS:" -ForegroundColor Yellow
Write-Host "1. Go back to Android Studio" -ForegroundColor White
Write-Host "2. Click: Build → Rebuild Project" -ForegroundColor White
Write-Host "3. Wait for build to complete (2-3 min)" -ForegroundColor White
Write-Host "4. Click Run (▶️)" -ForegroundColor White
Write-Host ""
Write-Host "Press any key to close..." -ForegroundColor Gray
$null = $Host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown")
