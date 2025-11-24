# Checkstyle Violations Quick Fix Guide

This guide provides quick fixes for common checkstyle violations encountered in the Gogidix Ecosystem.

## Common Violations and Fixes

### 1. Final Parameters

**Violation**: `FinalParameter` - Method parameters should be final

**Fix**:
```java
// Before
public static void main(String[] args) { ... }
public boolean equals(Object o) { ... }
public void processData(String data) { ... }

// After
public static void main(final String[] args) { ... }
public boolean equals(final Object o) { ... }
public void processData(final String data) { ... }
```

### 2. Need Braces

**Violation**: `NeedBraces` - if/for/while statements must have braces

**Fix**:
```java
// Before
if (condition)
    doSomething();

// After
if (condition) {
    doSomething();
}

// Before
for (int i = 0; i < 10; i++)
    process(i);

// After
for (int i = 0; i < 10; i++) {
    process(i);
}
```

### 3. Line Length

**Violation**: `LineLength` - Line exceeds 80 characters

**Fix**:
```java
// Before
if (condition1 && condition2 && condition3 && condition4 && condition5) {
    doSomething();
}

// After
if (condition1
    && condition2
    && condition3
    && condition4
    && condition5) {
    doSomething();
}

// Or break method parameters
// Before
public void process(String param1, String param2, String param3, String param4, String param5) {
    // code
}

// After
public void process(
        final String param1,
        final String param2,
        final String param3,
        final String param4,
        final String param5) {
    // code
}
```

### 4. Operator Wrap

**Violation**: `OperatorWrap` - Operators should be at end of line

**Fix**:
```java
// Before
boolean result = condition1
    && condition2
    || condition3;

// After
boolean result = condition1 &&
    condition2 ||
    condition3;
```

### 5. Missing Javadoc

**Violation**: `MissingJavadocMethod` - Public methods need Javadoc

**Fix**:
```java
// Before
public String getValue() {
    return value;
}

// After
/**
 * Gets the value of this object.
 *
 * @return the value
 */
public String getValue() {
    return value;
}

// For equals method
/**
 * {@inheritDoc}
 * Implementation of equals method for comparison.
 * This implementation follows the contract of the equals method
 * and is consistent with the hashCode implementation.
 */
@Override
public boolean equals(final Object o) {
    // implementation
}

// For main method
/**
 * Main method for the application.
 *
 * @param args command line arguments
 */
public static void main(final String[] args) {
    // implementation
}
```

### 6. Javadoc Package

**Violation**: `JavadocPackage` - Package needs package-info.java

**Fix**:
Create `package-info.java` in each package:
```java
/**
 * com.gogidix.service package.
 *
 * <p>This package contains components for the service module
 * within the Gogidix ecosystem.</p>
 *
 * @since 1.0.0
 */
package com.gogidix.service;
```

### 7. @Override Annotation

**Violation**: `MissingOverride` - Methods overriding super should have @Override

**Fix**:
```java
// Before
public boolean equals(final Object o) {
    // implementation
}

public int hashCode() {
    // implementation
}

// After
@Override
public boolean equals(final Object o) {
    // implementation
}

@Override
public int hashCode() {
    // implementation
}
```

### 8. Left Curly

**Violation**: `LeftCurly` - Opening brace should be on same line

**Fix**:
```java
// Before
if (condition)
{
    doSomething();
}

// After
if (condition) {
    doSomething();
}
```

### 9. HideUtilityClassConstructor

**Violation**: `HideUtilityClassConstructor` - Utility classes need private constructor

**Fix**:
```java
// Before
public final class StringUtils {
    public static boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }
}

// After
public final class StringUtils {

    private StringUtils() {
        // Utility class - prevent instantiation
    }

    public static boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }
}

// For Spring Boot applications with @SpringBootApplication
@SpringBootApplication
@SuppressWarnings("HideUtilityClassConstructor")
public class Application {
    public static void main(final String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

### 10. Design for Extension

**Violation**: `DesignForExtension` - Classes should be designed for extension

**Fix**:
```java
// Before
public class Service {
    public void process() {
        // code that shouldn't be overridden
        System.out.println("Processing");
        // more code
    }
}

// After
public class Service {
    public final void process() {
        validate();
        doProcess();
        cleanup();
    }

    protected void validate() {
        // Default validation
    }

    protected void doProcess() {
        // Allow subclasses to override
        System.out.println("Processing");
    }

    protected void cleanup() {
        // Default cleanup
    }
}
```

## Automated Fix Script

Save this as `fix-checkstyle.sh` and run it from the service directory:

```bash
#!/bin/bash

echo "Fixing common checkstyle violations..."

# Fix final parameters
find . -name "*.java" -exec sed -i 's/public static void main(String\[\] args)/public static void main(final String[] args)/g' {} \;
find . -name "*.java" -exec sed -i 's/public boolean equals(Object o)/public boolean equals(final Object o)/g' {} \;

# Fix braces for simple if statements
find . -name "*.java" -exec sed -i 's/if (\(.*\))\s*$/if (\1) {\n    }/g' {} \;

# Add @Override before equals methods
find . -name "*.java" -exec grep -l "public boolean equals" {} \; | while read file; do
    if ! grep -B2 "public boolean equals" "$file" | grep -q "@Override"; then
        sed -i '/public boolean equals(/i\    @Override' "$file"
    fi
done

echo "Fixes applied. Review and commit the changes."
```

## Verification

After fixing violations, verify the fixes:

```bash
# Compile to check for syntax errors
mvn clean compile

# Run checkstyle to verify fixes
mvn checkstyle:check

# Run tests to ensure functionality
mvn test
```

## Best Practices

1. **Fix violations as you write code** - Don't let them accumulate
2. **Use IDE plugins** - Most IDEs have checkstyle plugins that highlight violations
3. **Pre-commit hooks** - Set up pre-commit hooks to catch violations early
4. **Regular reviews** - Review code for checkstyle compliance during PRs

## Common Pitfalls

1. **Over-formatting** - Don't make code less readable to satisfy checkstyle
2. **Ignoring warnings** - Address all violations, not just errors
3. **Manual fixes only** - Use automated tools where possible
4. **Inconsistent style** - Maintain consistency across the codebase

## Resources

- [Checkstyle Configuration](https://checkstyle.sourceforge.io/config.html)
- [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html)
- [Spring Boot Best Practices](https://spring.io/guides)

---

**Remember**: The goal of checkstyle is to maintain code quality and consistency, not to be overly restrictive. Use judgment and focus on writing clean, readable code.