# Nueva Esperanza MS

Sistema distribuido para la gestión de un taller de bicicletas, desarrollado con
Spring Boot 4.1.0 / Java 21, comunicación REST entre servicios, API Gateway,
documentación Swagger/OpenAPI pruebas unitarias

## Integrantes
- (Nombres: Erick, Sebastian, Nicolas Maulen — Nº de equipo)_

## Microservicios implementados

| Servicio | Puerto | Dominio | Base de datos |
|---|---|---|---|
| **gateway** | 8080 | API Gateway (Spring Cloud Gateway) | — |
| **ms** (inventario) | 0 | Productos, Repuestos, Servicios | `db_tallerbici` |
| **ms2** (ventas) | 0 | Boletas, Tipos de pago | `db_tallerbici` |
| **ms3** (bicicleta) | 0 | Bicicletas, Marcas, Modelos, Colores | `db_tallerbici` |
| **ms4** (persona) | 0 | Clientes, Empleados | `db_tallerbici` |

## Ejecución local

Requisitos: JDK 21, Maven 3.9+, MySQL 8 corriendo en `localhost:3306` (usuario `root`, sin
contraseña). El perfil por defecto es `dev` y crea las bases automáticamente
(`createDatabaseIfNotExist=true`).

## Pruebas unitarias y cobertura

Tests con JUnit 5 + Mockito, usando H2 en memoria
(no requieren MySQL). Cobertura medida con JaCoCo (>80% en los 4 microservicios).

## Stack técnico
- Spring Boot 4.1.0, Java 21
- Spring Web, Spring Data JPA, Spring Validation
- Spring Cloud Gateway (MVC) 2025.1.1
- WebClient
- MySQL 8 + Flyway
- springdoc-openapi 3.0.0 (Swagger UI)
- Mockito
