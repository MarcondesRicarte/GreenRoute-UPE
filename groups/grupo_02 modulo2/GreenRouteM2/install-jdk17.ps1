param()

$repoRoot = Split-Path -Parent $MyInvocation.MyCommand.Definition
$destDir = Join-Path $repoRoot ".jdk\jdk-17"

if (Test-Path $destDir) {
    Write-Host "JDK 17 já está instalado em: $destDir"
    exit 0
}

$apiUrl = "https://api.adoptium.net/v3/binary/latest/17/ga/windows/x64/jdk/hotspot/normal/adoptium"
$tempZip = Join-Path $env:TEMP "openjdk17.zip"

Write-Host "Baixando JDK 17 via Adoptium API..."
Invoke-WebRequest -Uri $apiUrl -OutFile $tempZip -UseBasicParsing

Write-Host "Extraindo JDK para .jdk..."
New-Item -ItemType Directory -Path (Join-Path $repoRoot ".jdk") -Force | Out-Null
Expand-Archive -LiteralPath $tempZip -DestinationPath (Join-Path $repoRoot ".jdk") -Force

# After extraction, find the extracted folder (may include version in name)
$extracted = Get-ChildItem -Path (Join-Path $repoRoot ".jdk") -Directory | Where-Object { $_.Name -like "jdk*" -or $_.Name -like "*17*" } | Select-Object -First 1

if ($null -eq $extracted) {
    Write-Error "Falha ao localizar a pasta extraída do JDK"
    exit 1
}

# Move/rename to .jdk\jdk-17
if (Test-Path $destDir) { Remove-Item -Recurse -Force $destDir }
Move-Item -Path $extracted.FullName -Destination $destDir

Remove-Item -Path $tempZip -Force -ErrorAction SilentlyContinue

Write-Host "JDK 17 instalado localmente em: $destDir"
Write-Host "Verifique com: .\\mvnw.cmd -v"