# README — Proyecto Web Veterinaria Patitas

Este README explica **paso a paso** cómo ejecutar, configurar y trabajar con el proyecto **Veterinaria Patitas**, desarrollado en **Java + Spring Boot**, usando **VS Code o NetBeans**, con **MySQL**, **Git** y manejo básico de GitHub.

Repositorio oficial del proyecto: **github.com/miagv/veterinariaPatitas**

---

## 1. Requisitos previos

Instala y verifica los siguientes componentes en tu equipo:

### 🔹 Software necesario

* **Java JDK 17 o superior** (Spring Boot requiere versiones modernas)
* **Apache Maven 3.8+** (VS Code lo instala automáticamente si usas extensiones de Java)
* **MySQL Server 8.x**
* **Git** (para clonar y actualizar el repositorio)
* **VS Code** *(recomendado)* o **NetBeans**

### 🔹 Extensiones necesarias (si usas VS Code)

* Extension Pack for Java
* Spring Boot Tools
* Spring Initializr
* Maven for Java

---

## 2. Clonar el repositorio

Abre una terminal y ejecuta:

```bash
git clone https://github.com/miagv/veterinariaPatitas.git
cd veterinariaPatitas
```

Si usas GitHub Desktop, puedes clonarlo desde la interfaz gráfica.

---



## 3. Configurar el archivo `application.properties`

Ubicación:

```
src/main/resources/application.properties
```

Configura la conexión MySQL:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/patitas_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
spring.datasource.username="usuario de mysql"
spring.datasource.password="contraseña de mysql
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.defer-datasource-initialization=true
spring.sql.init.mode=always
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
server.port=8080




## 4. Ejecutar el proyecto

### 🔹 En VS Code

1. Abre la carpeta del repositorio.
2. VS Code detectará automáticamente el proyecto Maven.
3. En la barra lateral, abre **Spring Boot Dashboard**.
4. Da clic en **Run** sobre el proyecto `veterinariaPatitas`.

### 🔹 En NetBeans

1. Archivo → Abrir proyecto
2. Selecciona la carpeta del repositorio
3. Clic derecho sobre el proyecto → **Run**

---

## 5. Acceso al sistema

Una vez iniciado el servidor, abre en tu navegador:

```
http://localhost:8080
```

### 🔹 Credenciales de prueba incluídas

| Rol        | Usuario      | Contraseña |
| ---------- | ------------ | ---------- |
| Trabajador | `trabajador` | `123456`   |
| Cliente    | `cliente1`   | `123456`   |

Usa estas cuentas para entrar al sistema, validar roles o probar funcionalidades.

---

## 6. Estructura del proyecto (Spring Boot)

```
/src
├─ main/java/com/patitas
│  ├─ controller/
│  ├─ entity/
│  ├─ repository/
│  ├─ service/
│  └─ VeterinariaPatitasApplication.java
├─ main/resources/
│  ├─ templates/ (Thymeleaf)
│  ├─ static/ (CSS, JS, imágenes)
│  └─ application.properties
└─ test/
```

---

## 8. Generar el build del proyecto

Si necesitas empaquetarlo:

```bash
mvn clean package
```

Esto genera un archivo `.jar` en:

```
target/veterinariaPatitas.jar
```

Ejecutarlo manualmente:

```bash
java -jar target/veterinariaPatitas.jar
```

---

## 8. Comandos útiles de Git

### Primer commit después de clonar

```bash
git add .
git commit -m "Primer commit local"
git push origin main
```

### Actualizar proyecto desde GitHub

```bash
git pull origin main
```

---

## 9. Errores comunes y solución

###  Error: *Cannot connect to MySQL*

* Verifica usuario/contraseña en `application.properties`.
* Asegúrate de que MySQL está corriendo.
* Ejecuta `mysql -u root -p` para conectarte.

###  Error: *Port 8080 already in use*

Cambiar en `application.properties`:

```properties
server.port=8081
```

###  Tablas no se crean

Asegúrate que `ddl-auto=update` está activo.

---

## 10. Contribuciones

1. Crea una rama:

```bash
git checkout -b feature/nueva-funcion
```

2. Haz tus cambios y luego:

```bash
git push origin feature/nueva-funcion
```

3. Abre un Pull Request en GitHub.

---

## 11. Contacto

Proyecto desarrollado por el equipo de **Veterinaria Patitas**.

Si necesitas soporte técnico o quieres agregar nuevas secciones, notificaciones, reportes PDF u otro módulo, solo escribenos.
