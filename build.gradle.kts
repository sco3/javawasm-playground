plugins {
    java
    application
    eclipse
    id("com.gradleup.shadow") version "8.3.7"
    id("com.github.ben-manes.versions") version "0.51.0"
    id("org.teavm") version "0.13.0"
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
    implementation("io.javalin:javalin:7.2.2")
    implementation("org.slf4j:slf4j-simple:2.0.13")
    implementation("org.teavm:teavm-classlib:0.13.0")
    implementation("org.teavm:teavm-tooling:0.13.0")
    implementation("org.teavm:teavm-jso-apis:0.13.0")
    implementation("org.teavm:teavm-jso:0.13.0")
}

application {
    mainClass.set("scorta.Backend")
}

tasks.withType<JavaExec>().configureEach {
    jvmArgs("--enable-preview")
}

teavm {
    all {
        mainClass = "scorta.Frontend"
    }
    js {
        targetFileName = "client.js"
        outputDir = file("src/main/resources/public")
    }
    wasmGC {
        targetFileName = "client.wasm"
        outputDir = file("src/main/resources/public")
    }
}
