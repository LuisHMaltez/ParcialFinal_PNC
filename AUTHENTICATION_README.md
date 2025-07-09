# Autenticación JWT - Sistema de Tickets

## Descripción

Se ha implementado un sistema de autenticación JWT para el sistema de tickets. El endpoint `/auth/login` permite a los usuarios autenticarse y obtener un token JWT que deben usar para acceder a los demás endpoints protegidos.

## Endpoints

### POST /auth/login

**Descripción:** Autentica un usuario y devuelve un token JWT.

**Request Body:**
```json
{
  "correo": "juan.perez@example.com",
  "password": "password123"
}
```

**Response (200 OK):**
```json
{
  "uri": "/auth/login",
  "message": "Login exitoso",
  "status": 200,
  "time": "2024-01-15",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiJ9...",
    "tipo": "Bearer",
    "id": 1,
    "nombre": "Juan Pérez",
    "correo": "juan.perez@example.com",
    "nombreRol": "USER"
  }
}
```

**Response (401 Unauthorized):**
```json
{
  "message": "Credenciales inválidas",
  "status": 401,
  "time": "2024-01-15",
  "uri": "/auth/login"
}
```

## Usuarios de Prueba

Se han creado los siguientes usuarios de prueba:

### Usuarios con rol USER:
- **Correo:** juan.perez@example.com
- **Contraseña:** password123

- **Correo:** maria.garcia@example.com
- **Contraseña:** password123

- **Correo:** carlos.lopez@example.com
- **Contraseña:** password123

### Usuarios con rol TECH:
- **Correo:** ana.rodriguez@example.com
- **Contraseña:** admin123

- **Correo:** luis.martinez@example.com
- **Contraseña:** admin123

## Uso del Token JWT

Una vez obtenido el token JWT, debe incluirse en el header `Authorization` de todas las solicitudes a endpoints protegidos:

```
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

## Configuración

### Propiedades JWT (application.yml):
```yaml
application:
  security:
    jwt:
      secret-key: 404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970
      expiration: 86400000 # 24 horas
      refresh-token:
        expiration: 604800000 # 7 días
```

### Endpoints Protegidos:
- Todos los endpoints excepto `/auth/**` y `/swagger-ui/**` requieren autenticación
- El sistema usa autenticación stateless (sin sesiones)

## Estructura del Proyecto

### Nuevos archivos creados:
- `AuthController.java` - Controlador de autenticación
- `AuthService.java` - Servicio de autenticación
- `CustomUserDetailsService.java` - Servicio personalizado de UserDetails
- `SecurityConfig.java` - Configuración de seguridad
- `JwtService.java` - Servicio para manejo de JWT
- `JwtAuthenticationFilter.java` - Filtro de autenticación JWT
- `LoginRequest.java` - DTO para solicitud de login
- `LoginResponse.java` - DTO para respuesta de login
- `AuthenticationException.java` - Excepción personalizada
- `data.sql` - Script con usuarios de prueba

### Dependencias agregadas:
- `spring-boot-starter-security`
- `jjwt-api`, `jjwt-impl`, `jjwt-jackson`

## Seguridad

- Las contraseñas se encriptan usando BCrypt
- Los tokens JWT tienen expiración configurable
- Se valida la autenticación en cada request
- Se manejan excepciones de autenticación de forma segura 