# 🏠 Victus Residencias  

<p align="center">
  <img src="https://raw.githubusercontent.com/andrias01/UcoChallengeVictus/master/imagenes/logo.png" alt="Logo Victus Residencias" width="400"/>
</p>

## 📘 Presentación General

**Victus Residencias** es una plataforma integral para la **gestión de conjuntos residenciales**, desarrollada por estudiantes de la **Universidad Católica de Oriente (UCO)**.  
El proyecto implementa una **arquitectura distribuida basada en microservicios** desplegada en **Microsoft Azure**, con un enfoque en **seguridad, disponibilidad y escalabilidad**.

### 🌐 Arquitectura General

- **Frontend:** React (Vite + Tailwind + ShadCN/UI)
- **Backend:** Spring Boot 3.5.x (Java 21)
- **Base de Datos:** PostgreSQL (persistencia principal)
- **Infraestructura:** Azure App Service, Azure Database for PostgreSQL
- **Seguridad:** OAuth2 + JWT, Azure Key Vault, Web Application Firewall
- **Observabilidad:** Azure Monitor + Application Insights
- **Control de versiones:** GitHub
- **Despliegue:** CI/CD con GitHub Actions

---

## 🧩 Modelo de Clases

<p align="center">
  <img src="https://raw.githubusercontent.com/andrias01/UcoChallengeVictus/master/imagenes/modeloClases.png" alt="Modelo de Clases" width="800"/>
</p>

Representa las entidades principales del dominio: `ConjuntoResidencial`, `Residente`, `Reserva`, `Administrador`, `ZonaComún`, y sus relaciones. Define la base estructural del sistema y sus asociaciones.

---

## 🧮 Modelo MER (Entidad–Relación)

<p align="center">
  <img src="https://raw.githubusercontent.com/andrias01/UcoChallengeVictus/master/imagenes/modeloMER.png" alt="Modelo MER" width="800"/>
</p>

Este modelo refleja el esquema físico de la base de datos en PostgreSQL, definiendo llaves primarias, foráneas y relaciones 1:N y N:M entre las tablas del sistema.

---

## 🔄 Modelo de Actividades

<p align="center">
  <img src="https://raw.githubusercontent.com/andrias01/UcoChallengeVictus/master/imagenes/DiagramaActividades.png" alt="Modelo de Actividades" width="800"/>
</p>

El diagrama de actividades representa el flujo de negocio para los procesos clave:
- Registro de conjuntos residenciales
- Registro y validación de residentes
- Reservas en zonas comunes
- Confirmación y seguimiento de turnos

---

## 🧱 Modelo de Objetos

<p align="center">
  <img src="https://raw.githubusercontent.com/andrias01/UcoChallengeVictus/master/imagenes/ModeloObjetos.png" alt="Modelo de Objetos" width="800"/>
</p>

Describe instancias específicas de clases y cómo interactúan en tiempo de ejecución dentro de un escenario concreto de gestión residencial.

---

## ⚙️ Modelo de Estados

<p align="center">
  <img src="https://raw.githubusercontent.com/andrias01/UcoChallengeVictus/master/imagenes/DiagramaEstados.png" alt="Modelo de Estados" width="800"/>
</p>

Muestra los estados posibles de una **Reserva**:
- Pendiente  
- Confirmada  
- En uso  
- Finalizada  
- Cancelada  

---

## ☁️ Modelo de Despliegue

### 🧭 Arquitectura de referencia

<p align="center">
  <img src="https://raw.githubusercontent.com/andrias01/UcoChallengeVictus/master/imagenes/Arquitectura.png" alt="Arquitectura" width="900"/>
</p>

### 🧭 Arquetipo de referencia

<p align="center">
  <img src="https://raw.githubusercontent.com/andrias01/UcoChallengeVictus/master/imagenes/Arquetipo.png" alt="Arquetipo" width="900"/>
</p>

### 🧭 Arquitectura Distribuida
Victus Residencias adopta una **arquitectura distribuida con microservicios**, donde cada módulo (Usuarios, Reservas, Administración) se despliega en contenedores independientes dentro del entorno **Azure App Service**.

### 🧱 Bloques de Construcción Adoptados
- Spring Boot Framework  
- PostgreSQL JDBC Driver  
- Azure Key Vault Connector  
- React Frontend SPA  
- OAuth2 / JWT Authentication  

### 🧩 Bloques de Construcción Desarrollados

**Backend:**

<p align="center">
  <img src="https://raw.githubusercontent.com/andrias01/UcoChallengeVictus/master/imagenes/bloqConstDesaBack.png" alt="Backend Components" width="800"/>
</p>

**Frontend:**

<p align="center">
  <img src="https://raw.githubusercontent.com/andrias01/UcoChallengeVictus/master/imagenes/bloqConstDesaFront.png" alt="Frontend Components" width="800"/>
</p>

---

## 🧮 Modelo de Paquetes

<p align="center">
  <img src="https://raw.githubusercontent.com/andrias01/UcoChallengeVictus/master/imagenes/ModeloPaquetes.png" alt="Modelo de Paquetes" width="800"/>
</p>

### 📄 Documentación del Modelo de Paquetes

<p align="center">
  <img src="https://raw.githubusercontent.com/andrias01/UcoChallengeVictus/master/imagenes/DocPaquetes.png" alt="Documentación de Paquetes" width="800"/>
</p>

El modelo de paquetes organiza la aplicación siguiendo una arquitectura **Hexagonal (Ports & Adapters)**, garantizando separación clara entre capas:
- **Domain:** lógica de negocio
- **Application:** casos de uso
- **Infrastructure:** persistencia, API REST, seguridad

---

## ⚙️ Modelo de Componentes

<p align="center">
  <img src="https://raw.githubusercontent.com/andrias01/UcoChallengeVictus/master/imagenes/ModeloComponentes.png" alt="Modelo de Componentes" width="800"/>
</p>

### 📄 Documentación de Componentes

<p align="center">
  <img src="https://raw.githubusercontent.com/andrias01/UcoChallengeVictus/master/imagenes/DocComponentes.png" alt="Documentación de Componentes" width="800"/>
</p>

Se identifican:
- Componentes **externos**: Java 21, Spring Boot, PostgreSQL JDBC.  
- Componentes **propios**: Microservicio VictusResidencias, CrossCutting, ApplicationCore.

---

## 🔁 Modelo de Secuencia

<p align="center">
  <img src="https://raw.githubusercontent.com/andrias01/UcoChallengeVictus/master/imagenes/ModeloSecuencias.png" alt="Modelo de Secuencia" width="900"/>
</p>

El flujo describe la interacción entre los actores del sistema para el caso de uso: **Registrar Conjunto Residencial**

1. El administrador ingresa los datos (nombre, dirección, ciudad, administrador).  
2. El frontend React envía un `POST /api/conjuntos-residenciales`.  
3. El backend verifica la existencia de ciudad y administrador.  
4. Se persiste el nuevo conjunto residencial en PostgreSQL.  
5. Se retorna una respuesta HTTP `200 OK`.

### 📄 Documentación del Modelo de Secuencia

<p align="center">
  <img src="https://raw.githubusercontent.com/andrias01/UcoChallengeVictus/master/imagenes/DocSecuencia.png" alt="Documentación de Secuencia" width="800"/>
</p>

---

## ⚖️ Trade-Off

<p align="center">
  <img src="https://raw.githubusercontent.com/andrias01/UcoChallengeVictus/master/imagenes/trade-Off.png" alt="Trade-Off" width="800"/>
</p>

Este modelo compara las decisiones arquitectónicas en términos de **seguridad, rendimiento, disponibilidad y mantenibilidad**, priorizando la integración en Azure y la modularidad del código.

---

## 🧠 Mapa de Empatía

<p align="center">
  <img src="https://raw.githubusercontent.com/andrias01/UcoChallengeVictus/master/imagenes/MapaEmpatia.png" alt="Mapa de Empatía" width="800"/>
</p>

El mapa identifica las percepciones, frustraciones y necesidades de los **residentes**, **administradores** y **personal de portería**, garantizando una experiencia centrada en el usuario.

---

## 🎯 Escenarios de Calidad

> **Nota:** Para mejor visualización de la tabla completa, consulta el [documento completo de escenarios](https://shorturl.at/IEAS5)

### Seguridad

#### SEG-CAL_0001: Control de Acceso Basado en Roles
- **Tipo:** Preventivo
- **Objetivo:** Garantizar que solo los usuarios tengan los privilegios apropiados para acceder a funciones administrativas críticas
- **Táctica:** Uso de JWT + OAuth 2.0
- **Criterio de éxito:** Solo usuarios con roles apropiados pueden ejecutar acciones administrativas críticas
- **Medida:** Tiempo de validación < 200 ms; tasa de error = 0%

#### SEG-CAL_0002: Validación Continua de Cumplimiento Regulatorio
- **Tipo:** Detectivo
- **Objetivo:** Asegurar el cumplimiento continuo de regulaciones y normas de protección de datos
- **Táctica:** Bitácoras de Auditoría
- **Criterio de éxito:** Cumplimiento regulatorio ≥ 100%
- **Medida:** Frecuencia de verificación automática cada hora

#### SEG-CAL_0003: Protección de Información Personal de Residentes
- **Tipo:** Preventivo
- **Objetivo:** Garantizar que los datos de los residentes estén cifrados y protegidos contra accesos no autorizados
- **Táctica:** Cifrado de Tránsito
- **Criterio de éxito:** Los intentos de acceso no autorizados se registran y bloquean
- **Medida:** Tiempo de detección ≤ 2 s; tasa de bloqueos 100%

#### SEG-CAL_0004: Gestión Segura de Sesiones de Usuario
- **Tipo:** Detectivo
- **Objetivo:** Proteger las sesiones activas ante comportamientos sospechosos
- **Táctica:** Detección de Anomalías
- **Criterio de éxito:** Sesiones comprometidas se terminan y notifican
- **Medida:** Tiempo de detección < 10 s; tasa de falsos positivos < 2%

#### SEG-CAL_0005: Identificación Temprana de Amenazas Emergentes
- **Tipo:** Proactivo
- **Objetivo:** Detectar amenazas nuevas y responder automáticamente
- **Táctica:** Web Application Firewall
- **Criterio de éxito:** Sistema mitiga amenaza antes de impacto
- **Medida:** Precisión ≥ 85%

### Disponibilidad

#### DISP-CAL_0013: Estabilidad bajo Carga Máxima de Reservas
- **Tipo:** Preventivo
- **Objetivo:** Garantizar rendimiento y respuesta bajo alta demanda
- **Táctica:** Gestión de Rendimiento
- **Criterio de éxito:** 99,5% de solicitudes procesadas sin error
- **Medida:** Latencia < 4 s; disponibilidad del 100%

#### DISP-CAL_0014: Resiliencia de la Sesión ante Inestabilidad de Red
- **Tipo:** Detectivo
- **Objetivo:** Asegurar continuidad del servicio ante fallos de red
- **Táctica:** Tolerancia a Fallos
- **Criterio de éxito:** Reanudación automática
- **Medida:** Reanudación en < 30 s

#### DISP-CAL_0015: Gestión de Agotamiento del Pool de Conexiones
- **Tipo:** Reactivo
- **Objetivo:** Evitar bloqueos por agotamiento de recursos
- **Táctica:** Tolerancia a Fallos
- **Criterio de éxito:** Sin bloqueos HTTP 503
- **Medida:** Tiempo de recuperación ≤ 5 s

#### DISP-CAL_0016: Disponibilidad durante Eliminación Forzada de Entidad
- **Tipo:** Preventivo
- **Objetivo:** Mantener integridad durante operaciones críticas
- **Táctica:** Concurrencia
- **Criterio de éxito:** No se pierden datos ni registros
- **Medida:** Duración < 5 s

#### DISP-CAL_0017: Impacto del Mantenimiento Programado
- **Tipo:** Reactivo
- **Objetivo:** Minimizar tiempo fuera de servicio
- **Táctica:** Mantenimiento con Mínima Interrupción
- **Criterio de éxito:** Disponibilidad ≥ 99%
- **Medida:** Tiempo total de parada < 15 min

---

## 🧾 Autoría

**Universidad Católica de Oriente (UCO)**  
Proyecto desarrollado por estudiantes del programa **Ingeniería de Sistemas**

**Especificaciones Técnicas:**
- **Año:** 2025  
- **Arquitectura:** Microservicios distribuidos en Azure  
- **Lenguaje principal:** Java 21  
- **Framework:** Spring Boot 3.5.x  
- **Base de datos:** PostgreSQL  
- **Frontend:** React + Vite  
- **Infraestructura:** Azure Cloud Services  

---

## 📞 Contacto

Para más información sobre el proyecto, visita el repositorio oficial:  
🔗 [GitHub - UcoChallengeVictus](https://github.com/andrias01/UcoChallengeVictus)

---

<p align="center">
  <sub>Desarrollado con ❤️ por estudiantes de la Universidad Católica de Oriente</sub>
</p>