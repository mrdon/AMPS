

@echo off
if "%OS%" == "Windows_NT" setlocal enabledelayedexpansion

rem ---------------------------------------------------------------
rem Find absolute path to the program
rem ---------------------------------------------------------------

set PRGDIR=%~dp0
set CURRENTDIR=%cd%
cd /d %PRGDIR%..
set ATLAS_HOME=%cd%
cd /d %CURRENTDIR%

echo A new plugin structure is required for JIRA 5 plugins due to significant changes in the APIs.
echo Create a plugin for?
:prompt
echo 1) Shiny new JIRA 5
echo 2) Regular 'ol JIRA 4 (or earlier)
SET /P useJira5="#?"
if not "%useJira5%" == "1" if not "%useJira5%" == "2" goto prompt

if "%useJira5%" == "1" (
	"%ATLAS_HOME%\bin\atlas-create-jira5-plugin.bat"
) else (
	"%ATLAS_HOME%\bin\atlas-create-jira4-plugin.bat"
)