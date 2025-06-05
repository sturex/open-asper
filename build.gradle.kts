plugins {
    java
    application
    id("org.springframework.boot") version "3.1.7"
    id("io.spring.dependency-management") version "1.1.0"
    id("maven-publish")
}

group = "dev.asper"
version = "0.0.1"


java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
    withSourcesJar()
}

configurations {
    all {
        exclude("org.springframework.boot", "spring-boot-starter-logging")
        exclude("jakarta.servlet", "jakarta.servlet-api")
        resolutionStrategy {
            force("org.antlr:antlr4-runtime:4.9.3")
            force("org.antlr:antlr4:4.9.3")
            force("org.antlr:antlr4-tool:4.9.3")
        }
    }
    compileClasspath {
        resolutionStrategy.force("org.glassfish.jersey.core:jersey-client:2.36")
        resolutionStrategy.force("org.glassfish.jersey.core:jersey-common:2.36")
        resolutionStrategy.force("org.glassfish.jersey.core:jersey-server:2.36")
        resolutionStrategy.force("org.glassfish.jersey.containers:jersey-container-servlet:2.36")
        resolutionStrategy.force("org.glassfish.jersey.containers:jersey-container-servlet-core:2.36")
        resolutionStrategy.force("org.glassfish.jersey.inject:jersey-hk2:2.36")
    }
}

tasks.withType<JavaCompile> {
    val compilerArgs = options.compilerArgs
    compilerArgs.addAll(listOf( "-Xlint:unchecked", "-Xlint:deprecation"))
}

repositories {
    mavenCentral()
    mavenLocal()
}

dependencyManagement {
    dependencies {
        dependency("org.antlr:antlr4-runtime:4.9.3")
        dependency("org.antlr:antlr4:4.9.3")
        dependency("org.antlr:antlr4-tool:4.9.3")
    }
}

dependencies {
    annotationProcessor("org.projectlombok:lombok")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    implementation("org.apache.logging.log4j:log4j-core:2.20.0")
    implementation("org.apache.logging.log4j:log4j-api:2.20.0")
    implementation("org.apache.hadoop:hadoop-client-runtime:3.0.3")
    implementation("org.apache.hadoop:hadoop-client-api:3.0.3")
    implementation("org.apache.hadoop:hadoop-common:3.0.3") {
        exclude("com.google.guava", "guava")
    }
    implementation("net.lingala.zip4j:zip4j:2.11.5")

    implementation("io.github.lognet:grpc-spring-boot-starter:5.1.0") {
        exclude("com.google.guava", "guava")
    }
    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-graphql")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    implementation("org.scala-lang.modules:scala-collection-compat_2.13:2.1.0")
    implementation("org.scala-lang:scala-library:2.13.8")
    implementation("org.scala-lang.modules:scala-java8-compat_2.13:1.0.2")
    implementation("org.scala-lang.modules:scala-parallel-collections_2.13:1.0.3")
    implementation("org.scala-lang.modules:scala-parser-combinators_2.13:1.1.2")
    implementation("org.scala-lang:scala-reflect:2.13.8")
    implementation("org.scala-lang.modules:scala-xml_2.13:1.2.0")
    implementation("org.scalanlp:breeze-macros_2.13:1.2")
    implementation("org.scalanlp:breeze_2.13:1.2")

    implementation("com.fasterxml.jackson.core:jackson-core:2.14.2")
    implementation("org.apache.commons:commons-lang3:3.12.0")

    implementation("com.clickhouse:clickhouse-http-client:0.3.2-patch11")
    implementation("com.clickhouse:clickhouse-jdbc:0.3.2-patch11")

    implementation("org.apache.spark:spark-core_2.13:3.5.4"){
        exclude("org.antlr", "antlr4-runtime")
    }
    implementation("org.apache.spark:spark-mllib-local_2.13:3.5.4")
    implementation("org.apache.spark:spark-mllib_2.13:3.5.4")
    implementation("org.apache.spark:spark-catalyst_2.13:3.5.4")
    implementation("org.apache.spark:spark-graphx_2.13:3.5.4")
    implementation("org.apache.spark:spark-kvstore_2.13:3.5.4")
    implementation("org.apache.spark:spark-launcher_2.13:3.5.4")
    implementation("org.apache.spark:spark-network-common_2.13:3.5.4")
    implementation("org.apache.spark:spark-network-shuffle_2.13:3.5.4")
    implementation("org.apache.spark:spark-sketch_2.13:3.5.4")
    implementation("org.apache.spark:spark-sql_2.13:3.5.4")
    implementation("org.apache.spark:spark-streaming_2.13:3.5.4")
    implementation("org.apache.spark:spark-tags_2.13:3.5.4")
    implementation("org.apache.spark:spark-unsafe_2.13:3.5.4")

    implementation("jakarta.validation:jakarta.validation-api:3.0.2")
    implementation("org.codehaus.janino:commons-compiler")
    implementation("org.codehaus.janino:janino")
    runtimeOnly("org.antlr:antlr4-runtime") {
        version {
            strictly("4.9.3")
        }
    }
    implementation("io.grpc:grpc-all:1.25.0") {
        exclude("com.google.guava:guava")
    }
    implementation("com.google.guava:guava") {
        version {
            strictly("32.1.2-jre")
        }
    }
    implementation("org.glassfish.jersey.core:jersey-client") { version { strictly("2.36") } }
    implementation("org.glassfish.jersey.core:jersey-common") { version { strictly("2.36") } }
    implementation("org.glassfish.jersey.core:jersey-server") { version { strictly("2.36") } }
    implementation("org.glassfish.jersey.containers:jersey-container-servlet") { version { strictly("2.36") } }
    implementation("org.glassfish.jersey.containers:jersey-container-servlet-core") { version { strictly("2.36") } }
    implementation("org.glassfish.jersey.inject:jersey-hk2") { version { strictly("2.36") } }
    implementation("com.github.avro-kotlin.avro4k:avro4k-core:1.6.0") // solver requires it

    compileOnly("org.projectlombok:lombok")
    runtimeOnly("org.postgresql:postgresql")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework:spring-webflux")

    implementation("org.telegram:telegrambots-springboot-longpolling-starter:7.10.0")
    implementation("org.telegram:telegrambots-client:8.0.0")
    implementation("org.liquibase:liquibase-core")
}




application {
    mainClass.set("dev.asper.AsperApplication")
    applicationDefaultJvmArgs = listOf(
        "-Xms96g",
        "-Xmx96g",
        "–XX:+PrintCodeCache",
        "-XX:ReservedCodeCacheSize=2048M",
        "-XX:InitialCodeCacheSize=2048M",
        "-XX:+UseCodeCacheFlushing",
        "--add-opens=java.base/java.lang=ALL-UNNAMED",
        "--add-opens=java.base/java.lang.invoke=ALL-UNNAMED",
        "--add-opens=java.base/java.lang.reflect=ALL-UNNAMED",
        "--add-opens=java.base/java.io=ALL-UNNAMED",
        "--add-opens=java.base/java.net=ALL-UNNAMED",
        "--add-opens=java.base/java.nio=ALL-UNNAMED",
        "--add-opens=java.base/java.util=ALL-UNNAMED",
        "--add-opens=java.base/java.util.concurrent=ALL-UNNAMED",
        "--add-opens=java.base/java.util.concurrent.atomic=ALL-UNNAMED",
        "--add-opens=java.base/sun.nio.ch=ALL-UNNAMED",
        "--add-opens=java.base/sun.nio.cs=ALL-UNNAMED",
        "--add-opens=java.base/sun.security.action=ALL-UNNAMED",
        "--add-opens=java.base/sun.util.calendar=ALL-UNNAMED"
    )
}

tasks {
    withType<Tar> {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }
    withType<Zip> {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }
    bootJar {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        archiveFileName.set("app.jar")
    }
    jar {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        enabled = false
    }
    distTar {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        enabled = false
    }
}

tasks.bootJar {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    archiveFileName.set("app.jar")
}