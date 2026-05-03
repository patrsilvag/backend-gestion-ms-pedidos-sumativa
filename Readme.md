# 📦 Microservicio de Pedidos - Sistema de Gestión de Tienda

Este proyecto es una implementación de grado ingeniería para la asignatura de Desarrollo de Software en **Duoc UC**. Se trata de un microservicio de alta disponibilidad especializado en la **gestión, procesamiento y persistencia de órdenes de compra**, integrado con **Oracle Cloud Infrastructure (OCI)** y diseñado bajo estándares de arquitectura limpia.

## 🚀 Especificaciones Técnicas
* **Lenguaje:** Java 21 (LTS)
* **Framework:** Spring Boot 3.4+
* **Base de Datos:** Oracle Autonomous Database (Cloud)
* **Gestión de Dependencias:** Maven
* **Puerto de Servicio:** 8082 (Puerto asignado para Pedidos)

## 🛠️ Características Principales

### 1. Orquestación de Transacciones
El microservicio de pedidos actúa como el motor transaccional del sistema:
* **Integración con Ms-Productos:** Al generar una orden, este servicio se comunica con el microservicio de productos (Puerto 8081) para validar existencias y gatillar el descuento de stock automático.
* **Persistencia en Tiempo Real:** Registra cada transacción en tablas dedicadas en **Oracle Cloud**, asegurando la trazabilidad de cada compra.

### 2. Flujo de Pedidos y Validación (Requerimientos #31 y #27)
Se implementó un flujo lógico que garantiza la integridad del negocio:
* **Estado del Pedido:** Maneja estados de orden (PENDIENTE, PAGADO, COMPLETADO).
* **Simulación de Pago:** Incluye la lógica para devolver confirmaciones de éxito ("Pago realizado con éxito") tras validar la coherencia de la orden, cumpliendo con la pauta de evaluación sin requerir pasarelas externas.
* **Validación Cruzada:** Verifica que el cliente y los productos existan antes de finalizar la transacción.

### 3. Seguridad y Control de Acceso (RBAC)
Control de acceso basado en roles mediante parámetros de consulta para proteger la integridad de las órdenes:
* **ADMIN:** Acceso a la visualización de todos los pedidos del sistema para gestión logística.
* **CLIENTE:** Acceso restringido a la creación de sus propios pedidos y consulta de su historial personal.

## 📑 API Endpoints y Códigos de Respuesta

| Método | Endpoint | Descripción | Código Éxito |
| :--- | :--- | :--- | :--- |
| **POST** | `/api/pedidos` | **Crear Pedido**: Genera orden y solicita descuento de stock | 201 Created |
| **GET** | `/api/pedidos` | Obtiene historial global (Requiere `?rol=ADMIN`) | 200 OK |
| **GET** | `/api/pedidos/{id}` | Consulta el estado de un pedido específico | 200 OK |
| **DELETE**| `/api/pedidos/{id}`| Cancela un pedido (Requiere `?rol=ADMIN`)| 204 No Content |

### Manejo de Errores Transaccionales
* **400 Bad Request:** Datos de pedido malformados o error en la lógica de negocio.
* **403 Forbidden:** Intento de acceso a historial de terceros sin rol de administrador.
* **404 Not Found:** Referencia a productos o usuarios que no existen en la nube.
* **500 Internal Server Error:** Error de conexión con la instancia de Oracle Cloud.

## 🧪 Calidad de Código (Semana 8)
* **SonarQube:** El proyecto ha sido escaneado para garantizar un código limpio, superando el 80% de aceptación.
* **Unit Testing:** Implementación de pruebas JUnit para validar que el cálculo de totales y la creación de órdenes sean exactos antes de la persistencia.

## 💻 Despliegue con Docker
Este microservicio está configurado para operar de forma independiente o dentro de una red de contenedores:

1. **Build:** `docker build -t ms-pedidos .`
2. **Run:** `docker run -p 8082:8082 ms-pedidos`

---
**Nota Técnica:** El microservicio cuenta con configuración de **CORS** habilitada para recibir peticiones desde el FrontEnd en Angular 21, facilitando la arquitectura de Microservicios desacoplados.