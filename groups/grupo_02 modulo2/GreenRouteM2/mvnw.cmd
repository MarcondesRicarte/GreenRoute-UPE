@echo off
setlocal
set MAVEN_HOME=%~dp0.mvn\apache-maven-3.9.6%
if not defined JAVA_HOME (
	set JAVA_HOME=%~dp0.jdk\jdk-17
)
set PATH=%JAVA_HOME%\bin;%MAVEN_HOME%\bin;%PATH%
"%MAVEN_HOME%\bin\mvn.cmd" %*
endlocal
