@echo off

start "serviceregistry" cmd /c ".\start_dev serviceregistry 8761"
start "gateway" cmd /c ".\start_dev gateway 8100"
start "batch" cmd /c ".\start_dev batch 8900"
start "query" cmd /c ".\start_dev query 8901"
start "execute" cmd /c ".\start_dev execute 8902"
