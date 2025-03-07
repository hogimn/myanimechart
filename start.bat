@echo off
REM Usage: run_app.bat module-name port-number

if "%1"=="" (
    echo Module name is required.
    echo Usage: run_app.bat module-name port-number
    exit /b 1
)

if "%2"=="" (
    echo Port number is required.
    echo Usage: run_app.bat module-name port-number
    exit /b 1
)

set MODULE_NAME=%1
set PORT=%2

echo Running Spring Boot application: %MODULE_NAME% on port %PORT%
call .\mvnw spring-boot:run -pl myanimechart-%MODULE_NAME% -D"spring-boot.run.jvmArguments"="-Dserver.port=%PORT% -Djavax.net.ssl.trustStore=%truststore_path% -Djavax.net.ssl.trustStorePassword=%truststore_password%"
