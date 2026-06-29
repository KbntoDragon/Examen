@echo off

echo Iniciando Eureka...
cd eureka
start cmd /k "mvnw.cmd spring-boot:run"

echo Esperando 12 segundos...
timeout /t 12 /nobreak > null

echo Iniciando Gateway...
cd ../gateway
start cmd /k "mvnw.cmd spring-boot:run"

echo Iniciando Inventario...
cd ../ms/ms
start cmd /k "mvnw.cmd spring-boot:run"

echo Iniciando Ventas...
cd ../../ms2/ms2
start cmd /k "mvnw.cmd  spring-boot:run"

echo Iniciando Bicicletas...
cd ../../ms3/ms3
start cmd /k "mvnw.cmd spring-boot:run"

echo Iniciando Personas...
cd ../../ms4/ms4
start cmd /k "mvnw.cmd spring-boot:run"

echo Todo listo! Dashboard: http://localhost:8761/
pause