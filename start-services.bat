@echo off

start "serviceregistry" cmd /c ".\start serviceregistry 8761"
start "gateway" cmd /c ".\start gateway 8100"
start "batch" cmd /c ".\start batch 8900"
start "query" cmd /c ".\start query 8901"
start "execute" cmd /c ".\start execute 8902"
