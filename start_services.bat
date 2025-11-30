@echo off

start "discovery" cmd /c ".\start discovery 8761"
start "gateway" cmd /c ".\start gateway 8100"
start "application" cmd /c ".\start application 8900"

