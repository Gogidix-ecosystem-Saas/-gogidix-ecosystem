# Foundation Domain Integration Blueprint
## Zero-Assumption Management Domain Integration Guide

> **Document Version:** 1.0.0
> **Last Updated:** November 26, 2025
> **Status:** Production Ready
> **Target Audience:** Management Domain Architects & Development Teams

---

## Executive Summary

This document provides a comprehensive, zero-assumption integration guide for the Gogidix Foundation Domain. It outlines all necessary components, configurations, and protocols required for seamless integration with the Management Domain and other business domains.

### Key Integration Points
- **Service Discovery:** Netflix Eureka (Port 8761)
- **Configuration Management:** Spring Cloud Config (Port 8404)
- **API Gateway:** Spring Cloud Gateway (Port 8401)
- **Message Broker:** RabbitMQ Integration (Port 8405)
- **Database Management:** Centralized DB Service (Port 8406)
- **Service Discovery Service:** Enhanced Discovery (Port 8407)

---

## Architecture Overview

### Foundation Domain Components

```
┌─────────────────────────────────────────────────────────────┐
│                    FOUNDATION DOMAIN                        │
├─────────────────────────────────────────────────────────────┤
│  Service Registry (8761)                                    │
│  ┌─────────────────┐  ┌─────────────────┐                   │
│  │   Eureka Server │  │   Service Mesh  │                   │
│  │   Registration  │  │   Integration   │                   │
│  └─────────────────┘  └─────────────────┘                   │
├─────────────────────────────────────────────────────────────┤
│  Config Server (8404)  ┌─────────────────────────────────┐  │
│  ┌─────────────────┐  │  API Gateway (8401)            │  │
│  │  Centralized    │  │  ┌─────────────────────────────┐ │  │
│  │  Configuration  │  │  │  Spring Cloud Gateway       │ │  │
│  │  Management     │  │  │  - Routing                   │ │  │
│  └─────────────────┘  │  │  - Load Balancing            │ │  │
│                        │  │  - Security                 │ │  │
│  Message Broker (8405)│  │  - Rate Limiting            │ │  │
│  ┌─────────────────┐  │  └─────────────────────────────┘ │  │
│  │  RabbitMQ       │  └─────────────────────────────────┘  │
│  │  Integration    │                                       │
│  └─────────────────┘  Database Management (8406)          │
├─────────────────────────────────────────────────────────────┤
│  Service Discovery (8407)                                   │
│  ┌─────────────────┐  ┌─────────────────────────────────┐  │
│  │  Enhanced       │  │  Supporting Services            │  │
│  │  Discovery      │  │  - Monitoring                   │  │
│  │  Mechanisms     │  │  - Logging                      │  │
│  └─────────────────┘  │  - Metrics                      │  │
│                        │  - Health Checks                │  │
│                        └─────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────┘
```

---

## Service Registry Integration

### Eureka Server Configuration
**Endpoint:** `http://localhost:8761/eureka/`

#### Management Domain Service Registration
```yaml
# application.yml for Management Services
eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/
    register-with-eureka: true
    fetch-registry: true
    healthcheck:
      enabled: true
  instance:
    prefer-ip-address: true
    lease-renewal-interval-in-seconds: 30
    lease-expiration-duration-in-seconds: 90
    metadata-map:
      domain: management
      version: 1.0.0
      environment: production
```

#### Registration Template
```java
@EnableEurekaClient
@SpringBootApplication
public class ManagementServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ManagementServiceApplication.class, args);
    }
}
```

### Service Health Monitoring
- **Health Check Endpoint:** `/actuator/health`
- **Metrics Endpoint:** `/actuator/metrics`
- **Prometheus Endpoint:** `/actuator/prometheus`

---

## Configuration Management Integration

### Config Server Access
**Endpoint:** `http://localhost:8404`

#### Configuration Structure
```
config-repo/
├── management/
│   ├── user-management.yml
│   ├── role-management.yml
│   ├── permission-management.yml
│   └── notification-service.yml
├── business/
│   ├── social-commerce.yml
│   ├── haulage-logistics.yml
│   ├── fintech.yml
│   └── video-streaming.yml
└── global/
    ├── database.yml
    ├── security.yml
    └── messaging.yml
```

#### Bootstrap Configuration
```yaml
# bootstrap.yml for Management Services
spring:
  application:
    name: user-management-service
  cloud:
    config:
      uri: http://localhost:8404
      label: main
      profile: production
      fail-fast: true
      retry:
        initial-interval: 1000
        max-attempts: 6
```

---

## API Gateway Integration

### Gateway Configuration
**Base URL:** `http://localhost:8401`

#### Route Configuration Template
```yaml
spring:
  cloud:
    gateway:
      routes:
        # Management Domain Routes
        - id: user-management-service
          uri: lb://user-management-service
          predicates:
            - Path=/api/users/**
          filters:
            - StripPrefix=1
            - name: RequestRateLimiter
              args:
                redis-rate-limiter.replenishRate: 10
                redis-rate-limiter.burstCapacity: 20
                key-resolver: "#{@userKeyResolver}"

        - id: role-management-service
          uri: lb://role-management-service
          predicates:
            - Path=/api/roles/**
          filters:
            - StripPrefix=1
            - name: CircuitBreaker
              args:
                name: roleCircuitBreaker
                fallbackUri: forward:/fallback/roles
```

#### Cross-Origin Resource Sharing (CORS)
```yaml
spring:
  cloud:
    gateway:
      globalcors:
        cors-configurations:
          '[/**]':
            allowedOrigins:
              - "http://management-ui.gogidix.com"
              - "http://localhost:3000"
            allowedMethods:
              - GET
              - POST
              - PUT
              - DELETE
              - OPTIONS
            allowedHeaders: "*"
            allowCredentials: true
```

---

## Message Broker Integration

### RabbitMQ Configuration
**Management URL:** `http://localhost:8405`
**Queue Exchange:** `foundation.direct`

#### Message Producer Configuration
```java
@Configuration
@EnableRabbit
public class ManagementMessagingConfig {

    @Bean
    public TopicExchange managementExchange() {
        return new TopicExchange("management.exchange", true, false);
    }

    @Bean
    public Queue userManagementQueue() {
        return QueueBuilder.durable("management.user.queue").build();
    }

    @Bean
    public Binding userManagementBinding() {
        return BindingBuilder
                .bind(userManagementQueue())
                .to(managementExchange())
                .with("management.user.created");
    }
}
```

#### Event Publishing Template
```java
@Service
public class UserEventPublisher {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void publishUserCreatedEvent(User user) {
        UserCreatedEvent event = UserCreatedEvent.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .timestamp(Instant.now())
                .build();

        rabbitTemplate.convertAndSend(
            "management.exchange",
            "management.user.created",
            event
        );
    }
}
```

#### Event Consumer Template
```java
@RabbitListener(queues = "management.user.queue")
public class UserEventConsumer {

    @RabbitHandler
    public void handleUserCreated(UserCreatedEvent event) {
        log.info("Received user created event: {}", event);
        // Process event for other domains
        notifyRelevantServices(event);
    }
}
```

---

## Database Management Integration

### Database Service Configuration
**Endpoint:** `http://localhost:8406`
**Database Name:** `management_domain`

#### Connection Configuration
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/management_domain
    username: ${DB_USERNAME:management_user}
    password: ${DB_PASSWORD:secure_password}
    hikari:
      pool-name: ManagementHikariPool
      minimum-idle: 5
      maximum-pool-size: 20
      connection-timeout: 20000
      idle-timeout: 300000
      max-lifetime: 1200000
```

#### Database Initialization
```sql
-- Management Domain Schema
CREATE SCHEMA IF NOT EXISTS management;

-- User Management Tables
CREATE TABLE management.users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    status VARCHAR(20) DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Role Management Tables
CREATE TABLE management.roles (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) UNIQUE NOT NULL,
    description TEXT,
    permissions JSONB,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- User-Role Junction Table
CREATE TABLE management.user_roles (
    user_id BIGINT REFERENCES management.users(id),
    role_id BIGINT REFERENCES management.roles(id),
    assigned_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id, role_id)
);
```

---

## Security Integration

### OAuth2 Configuration
```yaml
spring:
  security:
    oauth2:
      resourceserver:
        jwt:
          issuer-uri: http://keycloak.gogidix.com/auth/realms/gogidix
          jwk-set-uri: http://keycloak.gogidix.com/auth/realms/gogidix/protocol/openid-connect/certs
```

### Inter-Service Authentication
```java
@Configuration
@EnableWebSecurity
public class InterServiceSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/actuator/**").permitAll()
                .requestMatchers("/api/internal/**").hasRole("SERVICE")
                .anyRequest().authenticated()
            )
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt.jwtDecoder(jwtDecoder()))
            );
        return http.build();
    }
}
```

---

## Monitoring & Observability

### Metrics Collection
```yaml
management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics,prometheus
  endpoint:
    health:
      show-details: always
    metrics:
      enabled: true
  metrics:
    export:
      prometheus:
        enabled: true
    distribution:
      percentiles-histogram:
        http.server.requests: true
      percentiles:
        http.server.requests: 0.5, 0.75, 0.95, 0.99
```

### Distributed Tracing
```yaml
spring:
  sleuth:
    zipkin:
      base-url: http://zipkin.gogidix.com:9411
    sampler:
      probability: 1.0
```

---

## Deployment Checklist

### Pre-Integration Requirements
- [ ] Foundation services are running and healthy
- [ ] Eureka registry is accessible
- [ ] Config Server is configured
- [ ] Database connections are established
- [ ] Message broker is operational
- [ ] SSL certificates are configured
- [ ] Network policies allow inter-service communication

### Integration Steps
1. **Service Registration**
   ```bash
   curl -X POST http://localhost:8761/eureka/apps \
     -H "Content-Type: application/json" \
     -d @service-registration.json
   ```

2. **Configuration Validation**
   ```bash
   curl http://localhost:8404/management/user-management-service-production
   ```

3. **Gateway Route Testing**
   ```bash
   curl -H "Authorization: Bearer <token>" \
        http://localhost:8401/api/users/health
   ```

4. **Message Queue Testing**
   ```bash
   curl -X POST http://localhost:8405/actuator/health
   ```

### Post-Integration Validation
- [ ] All services registered in Eureka
- [ ] Configuration is properly loaded
- [ ] API routes are accessible
- [ ] Message queues are processing
- [ ] Database connections are healthy
- [ ] Metrics are being collected

---

## Troubleshooting Guide

### Common Issues & Solutions

#### Service Registration Failures
**Problem:** Service not appearing in Eureka dashboard
**Solution:**
```bash
# Check Eureka logs
docker logs eureka-server

# Verify network connectivity
curl http://localhost:8761/eureka/apps

# Check service configuration
grep -r "eureka" src/main/resources/
```

#### Configuration Loading Issues
**Problem:** Configuration not loading from Config Server
**Solution:**
```bash
# Test Config Server endpoint
curl http://localhost:8404/management/user-management-service-production

# Check bootstrap configuration
cat src/main/resources/bootstrap.yml

# Verify Git repository configuration
curl http://localhost:8404/actuator/env
```

#### Gateway Routing Failures
**Problem:** Requests not reaching target services
**Solution:**
```bash
# Check Gateway routes
curl http://localhost:8401/actuator/gateway/routes

# Test specific route
curl -v http://localhost:8401/api/users/health

# Check service availability
curl http://localhost:8761/eureka/apps/USER-MANAGEMENT-SERVICE
```

---

## Performance Optimization

### Connection Pooling
```yaml
spring:
  datasource:
    hikari:
      maximum-pool-size: 20
      minimum-idle: 5
      connection-timeout: 20000
      idle-timeout: 300000
      max-lifetime: 1200000
```

### Caching Strategy
```java
@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        return CaffeineCacheManagerBuilder
                .newCaffeineCacheManagerBuilder()
                .withCache("users", Caffeine.newBuilder()
                        .expireAfterWrite(Duration.ofMinutes(10))
                        .maximumSize(1000))
                .build();
    }
}
```

---

## Scaling Guidelines

### Horizontal Scaling
- **Minimum replicas:** 2 per service
- **Maximum replicas:** 10 per service
- **Autoscaling thresholds:**
  - CPU: 70%
  - Memory: 80%
  - Response time: 500ms

### Load Balancing
```yaml
eureka:
  instance:
    prefer-ip-address: true
    lease-renewal-interval-in-seconds: 10
    lease-expiration-duration-in-seconds: 30
```

---

## Security Best Practices

### API Security
```java
@RestController
@RequestMapping("/api/users")
public class UserController {

    @PreAuthorize("hasRole('USER_MANAGER')")
    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        // Implementation
    }
}
```

### Data Encryption
```yaml
spring:
  datasource:
    password: ENC(encrypted_password)
jasypt:
  encryptor:
    password: ${JASYPT_ENCRYPTOR_PASSWORD}
```

---

## Contact & Support

### Foundation Domain Team
- **Architecture Team:** architecture@gogidix.com
- **DevOps Team:** devops@gogidix.com
- **Security Team:** security@gogidix.com

### Emergency Contacts
- **On-Call Engineer:** +1-555-ONCALL
- **Incident Response:** incident@gogidix.com

### Documentation
- **API Documentation:** https://docs.gogidix.com/foundation-api
- **Wiki:** https://wiki.gogidix.com/foundation-domain
- **Slack Channel:** #foundation-domain

---

## Version History

| Version | Date | Changes | Author |
|---------|------|---------|--------|
| 1.0.0 | 2025-11-26 | Initial Production Release | Foundation Team |

---

> **Note:** This document is version-controlled and automatically updated. For the latest version, always refer to the master copy in the repository.