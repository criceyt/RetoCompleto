@echo off
echo Iniciando ambos JAR en paralelo...
start java -jar ".\..\RetoServer\dist\RetoServer.jar"
start java -jar ".\dist\RetoCliente.jar"


