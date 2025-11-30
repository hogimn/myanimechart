@echo off

set "TARGET_DIR=%~dp0\myanimechart-website"
start "" cmd /k "cd /d "%TARGET_DIR%" && yarn start"
