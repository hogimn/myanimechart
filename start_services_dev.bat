@echo off

start "serviceregistry" cmd /c ".\start_dev serviceregistry 8761"
start "gateway" cmd /c ".\start_dev gateway 8100"
start "app" cmd /c ".\start_dev app 8900"

