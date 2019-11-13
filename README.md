# SafeBox

[![Build Status](https://travis-ci.com/javierorbe/safebox.svg?token=fY7UcqZb8Kwca6juna3P&branch=master)](https://travis-ci.com/javierorbe/safebox)

Caja fuerte virtual para guardar tu información de forma segura.

## Requisitos del sistema del servidor

* JRE 12+
* Base de datos (MySQL o SQLite)

## Requisitos del sistema del cliente

* JRE 12+

## Estructura del proyecto

El código está separado en tres módulos:

* _client_: aplicación del cliente.
* _server_: aplicación del servidor.
* _common_: código compartido entre la aplicación cliente y servidor.

## Desarrollado con

* [Maven](https://maven.apache.org) - Gestión de dependencias
* [Travis CI](https://travis-ci.com) - Integración continua
* [JUnit](https://junit.org/junit5) - Pruebas unitarias
* [MySQL](https://www.mysql.com) - Base de datos
