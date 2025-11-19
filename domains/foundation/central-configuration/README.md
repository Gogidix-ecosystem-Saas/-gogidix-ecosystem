# Central Configuration Domain

## Overview

The Central Configuration Domain is the first domain in the Gogidix Ecosystem that provides configuration management services for all microservices. This domain handles database configurations, feature toggles, environment settings, deployment scripts, and security configurations.

## Architecture

### Domain Services

1. **Database Services**
   - `database-config`: Database connection and configuration management
   - `database-migrations`: Database schema migration management

2. **Deployment Services**
   - `deployment-scripts`: Deployment automation scripts
   - `regional-deployment`: Multi-region deployment management

3. **Feature Services**
   - `environment-config`: Environment-specific configurations
   - `feature-toggle-config`: Feature flag management

4. **Infrastructure Services**
   - `infrastructure-as-code`: Infrastructure configuration templates
   - `kubernetes-manifests`: Kubernetes deployment manifests

5. **Security Services**
   - `secrets-management`: Secure credential storage and retrieval
   - `disaster-recovery`: Backup and recovery configurations

6. **Server Services**
   - `config-server`: Centralized configuration server

## Service Details

### Database Services

#### database-config
- **Port**: 8081
- **Purpose**: Provides centralized database configuration management
- **Endpoints**:
  - GET `/actuator/health` - Health check
  - GET `/config/databases` - List all database configurations
  - GET `/config/databases/{id}` - Get specific database config
  - POST `/config/databases` - Create new database config
  - PUT `/config/databases/{id}` - Update database config

#### database-migrations
- **Port**: 8082
- **Purpose**: Manages database schema migrations
- **Endpoints**:
  - GET `/actuator/health` - Health check
  - POST `/migrations/run` - Execute pending migrations
  - GET `/migrations/status` - Check migration status
  - GET `/migrations/history` - View migration history

### Deployment Services

#### deployment-scripts
- **Port**: 8083
- **Purpose**: Stores and manages deployment automation scripts
- **Endpoints**:
  - GET `/actuator/health` - Health check
  - GET `/scripts` - List all deployment scripts
  - GET `/scripts/{id}` - Get specific script
  - POST `/scripts/execute` - Execute deployment script

#### regional-deployment
- **Port**: 8084
- **Purpose**: Manages multi-region deployment strategies
- **Endpoints**:
  - GET `/actuator/health` - Health check
  - GET `/regions` - List configured regions
  - GET `/deployments/{region}` - Get regional deployment status
  - POST `/deployments/{region}` - Deploy to specific region

### Feature Services

#### environment-config
- **Port**: 8085
- **Purpose**: Manages environment-specific configurations
- **Endpoints**:
  - GET `/actuator/health` - Health check
  - GET `/environments` - List all environments
  - GET `/environments/{name}/config` - Get environment config
  - PUT `/environments/{name}/config` - Update environment config

#### feature-toggle-config
- **Port**: 8086
- **Purpose**: Feature flag and toggle management
- **Endpoints**:
  - GET `/actuator/health` - Health check
  - GET `/features` - List all feature flags
  - GET `/features/{name}` - Get specific feature status
  - PUT `/features/{name}` - Update feature status

### Security Services

#### secrets-management
- **Port**: 8087
- **Purpose**: Secure storage and retrieval of secrets and credentials
- **Endpoints**:
  - GET `/actuator/health` - Health check
  - POST `/secrets` - Store new secret
  - GET `/secrets/{id}` - Retrieve secret
  - DELETE `/secrets/{id}` - Delete secret
  - POST `/secrets/rotate` - Rotate secret

#### disaster-recovery
- **Port**: 8088
- **Purpose**: Disaster recovery and backup configurations
- **Endpoints**:
  - GET `/actuator/health` - Health check
  - POST `/backup/create` - Create backup
  - POST `/backup/restore` - Restore from backup
  - GET `/backup/status` - Check backup status

### Server Services

#### config-server
- **Port**: 8888
- **Purpose**: Central configuration server for Spring Cloud Config
- **Endpoints**:
  - GET `/actuator/health` - Health check
  - GET `/{application}/{profile}` - Get configuration for application
  - GET `/{application}/{profile}/{label}` - Get configuration with label

## Development

### Prerequisites

- Java 17
- Maven 3.8+
- Docker (for containerization)
- Git

### Building Services

Each service can be built independently:

```bash
# Navigate to service directory
cd domains/foundation/central-configuration/backend/java/[service-type]/[service-name]

# Compile
mvn clean compile

# Run tests
mvn test

# Package
mvn package -DskipTests

# Run locally
java -jar target/[service-name]-1.0.0.jar
```

### Building All Services

To build all services in the domain:

```bash
# From the domain root
cd domains/foundation/central-configuration/backend/java

# Build all services
find . -name "pom.xml" -exec dirname {} \; | while read service; do
    echo "Building $service"
    cd "$service"
    mvn clean package -DskipTests -q
    cd - > /dev/null
done
```

### Code Quality

All services must pass:
- Checkstyle validation (enterprise-grade standards)
- Unit tests
- Integration tests
- Security scans

#### Common Checkstyle Fixes

```bash
# Fix final parameters
find . -name "*.java" -exec sed -i 's/public static void main(String\[\] args)/public static void main(final String[] args)/g' {} \;
find . -name "*.java" -exec sed -i 's/public boolean equals(Object o)/public boolean equals(final Object o)/g' {} \;

# Add @Override annotations
find . -name "*.java" -exec grep -l "public boolean equals" {} \; | while read file; do
    sed -i '/public boolean equals(/i\    @Override' "$file"
done
```

## Configuration

### Environment Variables

Each service supports the following environment variables:

| Variable | Default | Description |
|----------|---------|-------------|
| `SPRING_PROFILES_ACTIVE` | `dev` | Active Spring profile |
| `SERVER_PORT` | Varies | Server port |
| `LOG_LEVEL` | `INFO` | Logging level |
| `CONFIG_SERVER_URL` | `http://localhost:8888` | Config server URL |

### Database Configuration

Services that require database connections use the following configuration:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://${DB_HOST}:${DB_PORT}/${DB_NAME}
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
    driver-class-name: org.postgresql.Driver
  jpa:
    hibernate:
      ddl-auto: validate
    show-sql: false
```

## Deployment

### Docker Deployment

Each service includes a Dockerfile. Build with:

```bash
# Build Docker image
docker build -t gogidix/[service-name]:latest .

# Run container
docker run -d \
  --name [service-name] \
  -p [port]:[port] \
  -e SPRING_PROFILES_ACTIVE=prod \
  gogidix/[service-name]:latest
```

### Kubernetes Deployment

Kubernetes manifests are provided in the `infrastructure-services/kubernetes-manifests` directory.

Apply with:

```bash
kubectl apply -f k8s/
```

## Monitoring

### Health Endpoints

All services expose Spring Boot Actuator endpoints:

- `/actuator/health` - Service health
- `/actuator/info` - Service information
- `/actuator/metrics` - Application metrics

### Logging

Services use structured logging with the following levels:
- ERROR: Application errors
- WARN: Warning conditions
- INFO: Important events
- DEBUG: Detailed debugging

## Security

### Authentication

Services use OAuth 2.0 for authentication. Configure:

```yaml
spring:
  security:
    oauth2:
      client:
        provider:
          oidc:
            issuer-uri: ${OAUTH_ISSUER_URI}
        registration:
          oidc:
            client-id: ${OAUTH_CLIENT_ID}
            client-secret: ${OAUTH_CLIENT_SECRET}
```

### Authorization

Role-based access control (RBAC) is implemented using Spring Security:

- `ADMIN`: Full access
- `USER`: Read-only access
- `SERVICE`: Service-to-service access

## Testing

### Unit Tests

Run unit tests for a service:

```bash
cd [service-directory]
mvn test
```

### Integration Tests

Run integration tests:

```bash
cd [service-directory]
mvn verify -P integration-test
```

### Contract Tests

API contracts are tested using Spring Cloud Contract:

```bash
mvn contract:test
```

## Troubleshooting

### Common Issues

1. **Port Conflicts**
   - Check if port is in use: `netstat -tulpn | grep [port]`
   - Change port in application.yml

2. **Database Connection Issues**
   - Verify database is running
   - Check connection string
   - Validate credentials

3. **Configuration Issues**
   - Check Config Server is accessible
   - Verify configuration files
   - Check environment variables

### Debug Mode

Enable debug logging:

```bash
java -jar -Dlogging.level.com.gogidix=DEBUG [service-name].jar
```

## API Documentation

API documentation is available via Swagger UI:

- Development: http://localhost:[port]/swagger-ui.html
- Staging: https://staging.gogidix.com/[service-name]/swagger-ui.html
- Production: https://api.gogidix.com/[service-name]/swagger-ui.html

## Support

For support:
1. Check this documentation
2. Review service logs
3. Check monitoring dashboards
4. Create an issue in the repository

## Version History

| Version | Date | Changes |
|---------|------|---------|
| 1.0.0 | 2025-11-19 | Initial release with all domain services |

---

**Last Updated**: 2025-11-19
**Maintainers**: Gogidix Platform Team