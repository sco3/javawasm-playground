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

# Compile frontend to WebAssembly via TeaVM
build-frontend: wrapper
    ./gradlew generateWasmGC

# Build backend shadow jar
build-backend: wrapper
    ./gradlew shadowJar

# Build both frontend and backend
build: build-frontend build-backend

# Run Javalin backend server
run: wrapper
    ./gradlew run

# Clean build artifacts
clean: wrapper
    ./gradlew clean
    
    
    
# ./gradlew generateJavaScript generateWasmGC build
# ./gradlew generateJavaScript generateWasmGC run
