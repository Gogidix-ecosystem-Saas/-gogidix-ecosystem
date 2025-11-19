# Gogidix Ecosystem Deployment Blueprint

## Overview

This document provides a comprehensive blueprint for deploying Gogidix Ecosystem domain services to production using the enterprise-grade CI/CD pipeline. The process ensures code quality, security, and reliability standards are met before deployment.

## Prerequisites

### 1. Repository Setup
- Ensure all domain code is committed to the repository
- Verify CI/CD workflow is properly configured
- Confirm all required files are tracked (no untracked files)

### 2. Branch Strategy
- `dev` - Development environment for testing
- `staging` - Pre-production environment
- `production` - Production environment
- `main/master` - Protected main branch
- `hotfix` - Emergency fixes branch

### 3. Required Files for Each Service
For each microservice in a domain, ensure the following structure:
```
service-name/
├── src/
│   ├── main/
│   │   ├── java/
│   │   └── resources/
│   └── test/
│       ├── java/
│       └── resources/
├── pom.xml
├── Dockerfile (required for containerization)
└── README.md
```

## Quality Gates

### 1. Code Quality Standards (Checkstyle)

All Java code must pass the following checkstyle rules:

#### Critical Rules (Must Pass):
- **LineLength**: Lines must not exceed 80 characters
- **NeedBraces**: All if/for/while statements must have braces
- **FinalParameters**: Method parameters must be final
- **LeftCurly**: Opening braces must be on the same line
- **OperatorWrap**: Operators must be at end of line
- **MissingJavadocMethod**: Public methods need Javadoc
- **JavadocPackage**: All packages need package-info.java
- **DesignForExtension**: Classes must be designed for extension
- **HideUtilityClassConstructor**: Utility classes need private constructors

#### Recommended Fixes:
```java
// 1. Add final to parameters
public static void main(final String[] args) { ... }
public boolean equals(final Object o) { ... }

// 2. Add braces to if statements
if (condition) {
    // code here
}

// 3. Add @Override annotations
@Override
public boolean equals(final Object o) { ... }

// 4. Create package-info.java for each package
/**
 * com.example.service package.
 *
 * <p>This package contains components for...</p>
 *
 * @since 1.0.0
 */
package com.example.service;
```

### 2. Build Requirements
- All services must compile successfully with Maven
- Unit tests must pass
- Integration tests must pass (if present)
- Code must package into executable JAR

### 3. Security Requirements
- No hardcoded secrets or credentials
- Dependencies must be up-to-date
- Security scans must pass

## CI/CD Pipeline Process

### Stage 1: Build and Test
```yaml
- Set up JDK 17
- Cache Maven dependencies
- Run checkstyle validation
- Compile all Java services
- Run unit tests
- Run integration tests
- Generate test reports
```

### Stage 2: Package
```yaml
- Package applications into JAR files
- Upload JAR artifacts
```

### Stage 3: Build Docker Images
```yaml
- Build Docker images for each service
- Push to container registry
- Tag images with commit SHA
```

### Stage 4: Validate
```yaml
- Validate Docker images
- Run security scans on images
- Verify image integrity
```

### Stage 5: Deploy to Staging
```yaml
- Deploy to staging environment
- Run smoke tests
- Verify health endpoints
```

### Stage 6: Deploy to Production
```yaml
- Manual approval required
- Deploy to production
- Run post-deployment verification
```

## Deployment Procedure

### Step 1: Pre-Deployment Checks

1. **Local Testing**
   ```bash
   # Navigate to service directory
   cd domains/[domain]/[service-type]/[service-name]

   # Compile and test locally
   mvn clean compile
   mvn test
   mvn package -DskipTests
   ```

2. **Code Quality Check**
   ```bash
   # Run checkstyle manually if needed
   mvn checkstyle:check
   ```

3. **Verify All Files Tracked**
   ```bash
   git status
   # Ensure no untracked files that should be committed
   ```

### Step 2: Commit and Push

1. **Stage Files**
   ```bash
   git add .
   ```

2. **Commit with Proper Message**
   ```bash
   git commit -m "feat: [Description of changes]

   - [Change details]
   - [Impact description]

   🤖 Generated with [Claude Code](https://claude.com/claude-code)

   Co-Authored-By: Claude <noreply@anthropic.com>"
   ```

3. **Push to Dev Branch**
   ```bash
   git push origin dev
   ```

### Step 3: Monitor Pipeline

1. **Check Pipeline Status**
   ```bash
   gh run list --workflow=[domain]-ci-cd.yml --limit=5
   ```

2. **View Pipeline Details**
   ```bash
   gh run view [run-id]
   ```

3. **Check Failed Jobs**
   ```bash
   gh run view [run-id] --log-failed
   ```

### Step 4: Troubleshooting

#### Checkstyle Violations
If checkstyle fails:

1. **Identify Violation Type**
   - Check the error logs for specific rule violations
   - Note the file and line numbers

2. **Apply Fixes**
   ```bash
   # Use the checkstyle fix script
   bash fix-checkstyle-violations.sh [service-path]
   ```

3. **Common Fixes**
   ```java
   // Add final parameters
   public void method(final String param) { ... }

   // Add braces
   if (condition) {
       doSomething();
   }

   // Add @Override
   @Override
   public boolean equals(final Object o) { ... }
   ```

#### Compilation Errors
If compilation fails:

1. **Check Syntax**
   - Verify no syntax errors
   - Check for missing imports
   - Ensure all dependencies are in pom.xml

2. **Local Build**
   ```bash
   mvn clean compile
   ```

#### Docker Build Failures
If Docker build fails:

1. **Check Dockerfile Exists**
   ```bash
   ls -la Dockerfile
   ```

2. **Verify Dockerfile Content**
   ```dockerfile
   FROM openjdk:17-jre-slim
   COPY target/*.jar app.jar
   EXPOSE 8080
   ENTRYPOINT ["java", "-jar", "/app.jar"]
   ```

## Service Templates

### 1. Standard Java Service Structure
```
service-name/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/gogidix/
│       │       └── [domain]/
│       │           └── [service]/
│       │               ├── [ServiceName]Application.java
│       │               ├── domain/
│       │               │   ├── model/
│       │               │   ├── repository/
│       │               │   └── service/
│       │               └── ...
│       └── resources/
│           └── application.yml
├── pom.xml
├── Dockerfile
└── README.md
```

### 2. Standard Dockerfile Template
```dockerfile
# Build stage
FROM maven:3.8.6-openjdk-17-slim AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Runtime stage
FROM openjdk:17-jre-slim
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### 3. Standard pom.xml Template
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.1.0</version>
        <relativePath/>
    </parent>

    <groupId>com.gogidix</groupId>
    <artifactId>[service-name]</artifactId>
    <version>1.0.0</version>
    <name>[Service Name]</name>
    <description>[Service Description]</description>

    <properties>
        <java.version>17</java.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-checkstyle-plugin</artifactId>
                <version>3.2.1</version>
                <configuration>
                    <configLocation>checkstyle.xml</configLocation>
                    <encoding>UTF-8</encoding>
                    <consoleOutput>true</consoleOutput>
                    <failsOnError>true</failsOnError>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
```

## Automated Scripts

### 1. Pre-Commit Hook Script
```bash
#!/bin/bash
# pre-commit-checks.sh

echo "Running pre-commit checks..."

# 1. Check for compilation errors
echo "Checking compilation..."
mvn clean compile -q
if [ $? -ne 0 ]; then
    echo "ERROR: Compilation failed. Fix before committing."
    exit 1
fi

# 2. Run checkstyle
echo "Running checkstyle..."
mvn checkstyle:check -q
if [ $? -ne 0 ]; then
    echo "ERROR: Checkstyle violations found. Fix before committing."
    exit 1
fi

# 3. Run tests
echo "Running tests..."
mvn test -q
if [ $? -ne 0 ]; then
    echo "ERROR: Tests failed. Fix before committing."
    exit 1
fi

echo "All checks passed. Ready to commit."
```

### 2. Checkstyle Fix Script
```bash
#!/bin/bash
# fix-checkstyle.sh

echo "Fixing checkstyle violations..."

# Add final parameters
find . -name "*.java" -exec sed -i 's/public static void main(String\[\] args)/public static void main(final String[] args)/g' {} \;
find . -name "*.java" -exec sed -i 's/public boolean equals(Object o)/public boolean equals(final Object o)/g' {} \;

# Add braces to if statements
find . -name "*.java" -exec sed -i 's/if (\(.*\)) {$/if (\1) {\n        /g' {} \;

# Add @Override annotations
find . -name "*.java" -exec grep -l "public boolean equals" {} \; | while read file; do
    sed -i '/public boolean equals(/i\    @Override' "$file"
done

echo "Checkstyle fixes applied."
```

## Best Practices

### 1. Code Organization
- Follow domain-driven design principles
- Keep services small and focused
- Use clear naming conventions
- Document all public APIs

### 2. Testing
- Write unit tests for all business logic
- Test edge cases
- Maintain test coverage > 80%
- Use descriptive test names

### 3. Security
- Never commit secrets or credentials
- Use environment variables for configuration
- Keep dependencies updated
- Run security scans regularly

### 4. Performance
- Optimize database queries
- Use caching appropriately
- Monitor application performance
- Set up alerting for critical metrics

## Rollback Procedure

If deployment fails:

1. **Identify Issue**
   - Check pipeline logs
   - Verify error messages
   - Assess impact

2. **Rollback**
   ```bash
   # Revert to previous commit
   git revert HEAD

   # Or reset to known good commit
   git reset --hard [commit-sha]

   # Push rollback
   git push origin dev --force
   ```

3. **Communicate**
   - Notify stakeholders
   - Document the issue
   - Plan fix deployment

## Monitoring and Alerting

### 1. Health Endpoints
All services must expose:
- `/actuator/health` - Health check
- `/actuator/info` - Service information
- `/actuator/metrics` - Application metrics

### 2. Logging
- Use structured logging
- Include correlation IDs
- Log at appropriate levels
- Avoid logging sensitive data

### 3. Metrics to Monitor
- Response time
- Error rate
- Throughput
- Resource utilization
- Database connections

## Contact and Support

For questions or issues with deployment:
- Check this documentation first
- Review pipeline logs
- Contact the DevOps team
- Create an issue in the repository

---

**Version**: 1.0.0
**Last Updated**: 2025-11-19
**Status**: Active