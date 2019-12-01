# SafeBox

[![Build Status](https://travis-ci.com/javierorbe/safebox.svg?token=fY7UcqZb8Kwca6juna3P&branch=master)](https://travis-ci.com/javierorbe/safebox)

Caja fuerte virtual para guardar tu información de forma segura.

## Uso del servidor

### Requisitos del sistema del servidor

* JRE 12+
* Base de datos (MySQL, PostgreSQL o SQLite)

### Configuración del servidor

Al iniciar el servidor por primera vez, se crea un fichero de configuración en la misma ruta desde la que se ejecuta la aplicación.

## Uso del cliente

### Requisitos del sistema del cliente

* JRE 12+

### Configuración del cliente

El fichero de configuración se genera en el directorio del usuario (en sistemas Windows en _\<user\>/AppData/Roaming/_).

## Estructura del proyecto

El código está separado en tres módulos:

* _client_: aplicación del cliente.
* _server_: aplicación del servidor.
* _common_: código compartido entre la aplicación cliente y servidor.

## Desarrollado con

* [Maven](https://maven.apache.org) - Gestión de dependencias
* [Travis CI](https://travis-ci.com) - Integración continua
* [JUnit](https://junit.org/junit5) - Pruebas unitarias
* [SLF4j](http://www.slf4j.org/) - Logger
* [HikariCP](https://github.com/brettwooldridge/HikariCP) - Pool de conexiones de JDBC
* Drivers de JDBC ([MySQL](https://dev.mysql.com/downloads/connector/j/), [PostgreSQL](https://jdbc.postgresql.org/) y [SQLite](https://bitbucket.org/xerial/sqlite-jdbc/downloads/))
* [GSON](https://github.com/google/gson) - Serialización JSON
* [Argon2](https://github.com/phxql/argon2-jvm) - Función de derivación de clave
* [LGoodDatePicker](https://github.com/LGoodDatePicker/LGoodDatePicker) - Selector de fechas para Swing
