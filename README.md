# MakotoDecor Backend

Spring Boot REST API for MakotoDecor application.

## Features

- REST API endpoints for Product management
- H2 Database (in-memory for development)
- JPA/Hibernate for data persistence
- Docker Compose support (disabled by default)

## API Endpoints

- `GET /api/products` - Get product information

## Local Development

1. Clone the repository
2. Run the application:
   ```bash
   ./mvnw spring-boot:run
   ```
3. Access the API at: `http://localhost:8080/api/products`
4. H2 Console (development only): `http://localhost:8080/h2-console`

## Deployment on Render.com

1. Connect your GitHub repository to Render
2. Create a new Web Service
3. Use the following settings:
   - **Build Command**: `./mvnw clean package -DskipTests`
   - **Start Command**: `java -jar target/makotodecor-0.0.1-SNAPSHOT.jar`
   - **Environment**: Java
4. Set environment variables (optional):
   - `SPRING_PROFILES_ACTIVE=production`
   - `SPRING_DATASOURCE_URL=jdbc:h2:mem:testdb`

## Environment Variables

- `SPRING_PROFILES_ACTIVE` - Spring profile (default: default)
- `SPRING_DATASOURCE_URL` - Database URL
- `SPRING_DATASOURCE_USERNAME` - Database username
- `SPRING_DATASOURCE_PASSWORD` - Database password
- `SPRING_JPA_HIBERNATE_DDL_AUTO` - Hibernate DDL mode
- `SPRING_JPA_SHOW_SQL` - Show SQL queries (true/false)
- `SPRING_H2_CONSOLE_ENABLED` - Enable H2 console (true/false)
