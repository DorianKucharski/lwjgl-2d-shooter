# Builds a standalone Windows package (game .exe + bundled Java runtime) into dist/.
# Requires JDK 17+ (for jpackage). Uses $env:JAVA_HOME if set, otherwise javac/jpackage from PATH.
#
# Usage:  powershell -ExecutionPolicy Bypass -File build.ps1

$ErrorActionPreference = "Stop"
$root = $PSScriptRoot
$name = "NightRaid"
$version = "1.1.0"

function Tool($t) {
    if ($env:JAVA_HOME) { return Join-Path $env:JAVA_HOME "bin\$t.exe" }
    return $t
}

$build = Join-Path $root "build"
$classes = Join-Path $build "classes"
$inputDir = Join-Path $build "input"
$dist = Join-Path $root "dist"
$appDir = Join-Path $dist $name

Remove-Item -Recurse -Force $build, $appDir -ErrorAction SilentlyContinue
New-Item -ItemType Directory -Force $classes, $inputDir, $dist | Out-Null

Write-Host "Compiling..."
$sources = Get-ChildItem -Recurse -Filter *.java (Join-Path $root "src") | ForEach-Object { $_.FullName }
& (Tool javac) -encoding UTF-8 --release 11 -nowarn -cp "$root\libs\*" -d $classes $sources
if ($LASTEXITCODE -ne 0) { throw "javac failed" }
Copy-Item -Recurse (Join-Path $root "src\shaders") $classes

Write-Host "Creating jar..."
& (Tool jar) --create --file (Join-Path $inputDir "game.jar") --main-class gra.game.Main -C $classes .
if ($LASTEXITCODE -ne 0) { throw "jar failed" }
Copy-Item (Join-Path $root "libs\*.jar") $inputDir

Write-Host "Running jpackage..."
& (Tool jpackage) --type app-image --name $name --app-version $version `
    --input $inputDir --main-jar game.jar --main-class gra.game.Main `
    --add-modules java.base,java.desktop,jdk.unsupported `
    --jlink-options "--strip-debug --no-header-files --no-man-pages --strip-native-commands" `
    --dest $dist
if ($LASTEXITCODE -ne 0) { throw "jpackage failed" }

# The game loads assets relative to the working directory, so they sit next to the .exe.
Write-Host "Copying game assets..."
foreach ($d in "audio", "levels", "textures", "saves") {
    Copy-Item -Recurse (Join-Path $root $d) $appDir
}
Copy-Item (Join-Path $root "Wyniki.bin") $appDir
New-Item -ItemType Directory -Force (Join-Path $appDir "screenshots") | Out-Null

Write-Host "Zipping..."
$zip = Join-Path $dist "$name-$version-windows.zip"
Remove-Item -Force $zip -ErrorAction SilentlyContinue
Compress-Archive -Path $appDir -DestinationPath $zip

Write-Host "Done: $appDir\$name.exe"
Write-Host "      $zip"
