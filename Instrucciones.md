# Instrucciones

## Requisitos previos

- Tener instalado **Docker** y **Docker Compose** en tu máquina.
- Clonar este repositorio o descargar el código fuente.

---

## Levantar el proyecto con Docker

1. Abre una terminal en la raíz del proyecto (donde está el archivo `docker-compose.yml`).
2. Ejecuta el siguiente comando para construir y levantar los contenedores:
   ```sh
   docker-compose up --build
   ```
   Esto descargará las imágenes necesarias, construirá la imagen de la aplicación y levantará los servicios:
   - **db**: Base de datos PostgreSQL (puerto 5432, usuario: `postgres`, base: `supportdb`)
   - **app**: Aplicación Spring Boot (puerto 8080)

3. Espera a que ambos servicios estén corriendo. Puedes verificarlo en los logs de la terminal.
4. Accede a la aplicación en: [http://localhost:8080](http://localhost:8080)

---

## Usuarios de prueba

Puedes autenticarte usando los siguientes usuarios de ejemplo:

**Usuarios con rol USER:**
- Correo: juan.perez@example.com | Contraseña: password123
- Correo: maria.garcia@example.com | Contraseña: password123
- Correo: carlos.lopez@example.com | Contraseña: password123

**Usuarios con rol TECH:**
- Correo: ana.rodriguez@example.com | Contraseña: admin123
- Correo: luis.martinez@example.com | Contraseña: admin123

---

## Probar los endpoints

1. Realiza un login en `/auth/login` para obtener un token JWT.
2. Usa el token en el header `Authorization` para acceder a los demás endpoints:
   ```
   Authorization: Bearer <tu_token>
   ```
3. Puedes importar la colección de Insomnia/Postman incluida en `src/main/resources/` para facilitar las pruebas.

---

## Comandos útiles

- **Detener los contenedores:**
  ```sh
  docker-compose down
  ```
- **Ver los logs en tiempo real:**
  ```sh
  docker-compose logs -f
  ```
- **Reconstruir la imagen (si cambiaste el código):**
  ```sh
  docker-compose up --build
  ```

---

## Notas adicionales

- Si necesitas ejecutar comandos dentro del contenedor de la app:
  ```sh
  docker-compose exec app bash
  ```
- Puedes modificar los puertos en el archivo `docker-compose.yml` si es necesario.
- La base de datos se inicializa automáticamente con los usuarios de prueba.
