@echo off

start "discovery" cmd /c ".\start_dev discovery 8761"
start "gateway" cmd /c ".\start_dev gateway 8100"
start "application" cmd /c ".\start_dev application 8900"

