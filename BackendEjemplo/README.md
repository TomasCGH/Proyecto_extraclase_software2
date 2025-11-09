# VictusLittleBackend

## Configuración del Entorno

### Variables de Entorno Requeridas

Para ejecutar esta aplicación, necesitas configurar las siguientes variables de entorno:

- `VAULT_TOKEN`: Token de autenticación para HashiCorp Vault

### Configuración Local

1. Copia el archivo `.env.example` a `.env`:
   ```bash
   cp .env.example .env
   ```

2. Edita el archivo `.env` y añade tu token de Vault:
   ```
   VAULT_TOKEN=tu-token-aqui
   ```

3. Para ejecutar la aplicación en PowerShell:
   ```powershell
   $env:VAULT_TOKEN = (Get-Content .env | Select-String '^VAULT_TOKEN=(.*)$').Matches.Groups[1].Value
   .\mvnw spring-boot:run
   ```

### Configuración en Producción

En el entorno de producción (Railway), configura la variable de entorno `VAULT_TOKEN` en la configuración del servicio.

## Notas de Seguridad

- NUNCA subas el archivo `.env` al repositorio
- NUNCA compartas tu token de Vault
- NUNCA hardcodees el token en el código fuente
- Usa siempre variables de entorno para las credenciales