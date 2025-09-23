# API SOAP de Personajes de Diablo IV

# Si quiere ver el pullrequest siga el siguiente link
https://github.com/Asked133/SistemasDistribuidos_DCGS/pull/16

## Prerequisitos

Antes de comenzar, asegúrate de tener instalado el siguiente software en tu sistema:

Java 21: El proyecto está configurado para usar esta versión de Java.

Docker y Docker Compose (podman y python 3.13): Para construir y ejecutar la aplicación y la base de datos en contenedores.

Un cliente SOAP: Una herramienta como Insomnia para enviar peticiones al servicio.

(Opcional)Github CLI: Para que sea mas sencillo clonar el pull request

## Levantar el proyecto

### Primero hay que clonar el repositorio
git clone https://github.com/Asked133/SistemasDistribuidos_DCGS

cd SistemasDistrbuidos_DCGS

### Descarga la PR
gh pr checkout 16

### Opcion B: Sin Descargar gh   (pr-16 puede ser cambiado por otro nombre para mejor entendimiento)
git fetch origin pull/16/head:pr-16

git checkout pr-16

## Se levantan los contenedores estando en la raiz del proyecto (DiabloApi/)
### Docker
docker-compose up --build -d

### Podman   (Si tienes podman compose como PATH es otro comando)
python -m podman_compose up --build -d

(PATH)

podman-compose up --build -d

## Abra Insomnia e importe el contrato http://localhost:8055/ws/characters.wsdl

## Elija la operacion que requiera y llene los campos

## Si termino de usar la API use

### Docker
docker-compose down

### Podman
python -m podman_compose down

(PATH)

podman_compose down

## Si quiere eliminar los volumenes es

### Docker
docker-compose down -v

### Podman
python -m podman_compose down -v

(PATH)

podman-compose down -v

