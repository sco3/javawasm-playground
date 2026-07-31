plugins {
    java
    application
    eclipse
    id("com.gradleup.shadow") version "8.3.7"
    id("com.github.ben-manes.versions") version "0.51.0"
    id("org.teavm") version "0.9.2"
}

group = "com.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
    }
}

dependencies {
    implementation("io.javalin:javalin:6.3.0")
    implementation("org.slf4j:slf4j-simple:2.0.13")
    implementation("org.teavm:teavm-classlib:0.9.2")
    implementation("org.teavm:teavm-tooling:0.9.2")
}

application {
    mainClass.set("scorta.Backend")
}

tasks.withType<JavaCompile>().configureEach {
    options.compilerArgs.add("--enable-preview")
}

tasks.withType<JavaExec>().configureEach {
    jvmArgs("--enable-preview")
}

teavm {
    all {
        mainClass = "scorta.Frontend"
    }
    wasm {
        targetFileName = "client.wasm"
    }
}
