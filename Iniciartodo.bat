@echo off
REM Capturamos la ruta raiz del script
set "ROOT_DIR=%~dp0"

echo Iniciando Eureka...
cd "%ROOT_DIR%eureka\eureka"
start "Eureka" cmd /k "mvnw.cmd spring-boot:run"

echo Esperando 12 segundos...
timeout /t 12 /nobreak > nul

echo Iniciando Gateway...
cd "%ROOT_DIR%gateway\gateway"
start "Gateway" cmd /k "mvnw.cmd spring-boot:run"

echo Iniciando Inventario...
cd "%ROOT_DIR%ms\ms"
start "Inventario" cmd /k "mvnw.cmd spring-boot:run"

echo Iniciando Ventas...
cd "%ROOT_DIR%ms2\ms2"
start "Ventas" cmd /k "mvnw.cmd spring-boot:run"

echo Iniciando Bicicletas...
cd "%ROOT_DIR%ms3\ms3"
start "Bicicletas" cmd /k "mvnw.cmd spring-boot:run"

echo Iniciando Personas...
cd "%ROOT_DIR%ms4\ms4"
start "Personas" cmd /k "mvnw.cmd spring-boot:run"

echo Todo listo! Dashboard: http://localhost:8761/
pause