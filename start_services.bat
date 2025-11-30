@echo off

start "serviceregistry" cmd /c ".\start serviceregistry 8761"
start "gateway" cmd /c ".\start gateway 8100"
start "app" cmd /c ".\start app 8900"

