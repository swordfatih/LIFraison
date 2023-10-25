plugins {
    id("java")
    id("application")
    id("org.openjfx.javafxplugin") version "0.1.0"
    id("com.github.jacobono.jaxb") version "1.3.5"
}

group = "com.insa.lifraison"
version = "1.0-SNAPSHOT"

application {
    mainClass = "com.insa.lifraison.MainApplication"
}

javafx {
    version = "12"
    modules("javafx.controls", "javafx.fxml")
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}