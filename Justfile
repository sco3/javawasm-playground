# Justfile for javawasm-playground

set shell := ["bash", "-uc"]

# Show available recipes
default:
    @just --list

# Generate Gradle wrapper if missing
wrapper:
    @if [ ! -f ./gradlew ]; then \
        echo "Gradle wrapper not found, generating..."; \
        gradle wrapper --gradle-version 9.6.1; \
    fi

# Build frontend and backend, then run Javalin server
run: wrapper
    ./gradlew generateJavaScript generateWasmGC run

# Compile frontend to JS and WebAssembly
build-frontend: wrapper
    ./gradlew generateJavaScript generateWasmGC

# Build backend shadow jar
build-backend: wrapper
    ./gradlew shadowJar

# Build all targets
build: build-frontend build-backend

# Clean build artifacts
clean: wrapper
    ./gradlew clean
