FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app

# Copiar el archivo Java desde la raíz
COPY HelloWorld.java .

# Compilar el archivo Java
RUN javac HelloWorld.java

FROM eclipse-temurin:21-jre-alpine AS runtime
WORKDIR /app

# Copiar la clase compilada
COPY --from=build /app/HelloWorld.class .

# Ejecutar la aplicación
CMD ["java", "HelloWorld"]