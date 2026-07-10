param()

$version = "3.9.6"
$zipUrl = "https://archive.apache.org/dist/maven/maven-3/$version/binaries/apache-maven-$version-bin.zip"
$repoRoot = Split-Path -Parent $MyInvocation.MyCommand.Definition
$destDir = Join-Path $repoRoot ".mvn\apache-maven-$version"

if (Test-Path $destDir) {
    Write-Host "Maven $version já está instalado em: $destDir"
    exit 0
}

$tempZip = Join-Path $env:TEMP "apache-maven-$version-bin.zip"

Write-Host "Baixando Apache Maven $version..."
Invoke-WebRequest -Uri $zipUrl -OutFile $tempZip -UseBasicParsing

Write-Host "Extraindo para .mvn..."
New-Item -ItemType Directory -Path (Join-Path $repoRoot ".mvn") -Force | Out-Null
Expand-Archive -LiteralPath $tempZip -DestinationPath (Join-Path $repoRoot ".mvn") -Force

# Criar mvnw.cmd
$mvnwCmdPath = Join-Path $repoRoot "mvnw.cmd"
$mavenHomeRel = ".mvn\apache-maven-$version"

# Create mvnw.cmd using a literal here-string to avoid PowerShell parsing issues
$mvnwCmdContent = @'
@echo off
setlocal
set MAVEN_HOME=%~dp0MAVEN_HOME_PLACEHOLDER%
set PATH=%MAVEN_HOME%\bin;%PATH%
"%MAVEN_HOME%\bin\mvn.cmd" %*
endlocal
'@

$mvnwCmdContent = $mvnwCmdContent.Replace('MAVEN_HOME_PLACEHOLDER', $mavenHomeRel)

Write-Host "Criando mvnw.cmd..."
Set-Content -Path $mvnwCmdPath -Value $mvnwCmdContent -Encoding ASCII

# Create mvnw (Unix) optionally
$mvnwShPath = Join-Path $repoRoot "mvnw"
$mvnwShContent = @'
#!/bin/sh
MAVEN_HOME="$(dirname "$0")/.mvn/apache-maven-VERSION_PLACEHOLDER"
"$MAVEN_HOME/bin/mvn" "$@"
'@

$mvnwShContent = $mvnwShContent.Replace('VERSION_PLACEHOLDER', $version)
Set-Content -Path $mvnwShPath -Value $mvnwShContent -Encoding ASCII

Write-Host "Removendo arquivo temporário..."
Remove-Item -Path $tempZip -Force

Write-Host "Maven $version instalado localmente. Use '.\\mvnw.cmd -v' para verificar."