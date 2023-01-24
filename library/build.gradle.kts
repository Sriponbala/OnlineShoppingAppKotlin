plugins {
    id("java")
    id("kotlin")
    `maven-publish`
}

group = "org.sri"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

publishing{
    publications{
        create<MavenPublication>("library"){
            groupId = "org.sri"
            artifactId = "library"
            version = "1.0-SNAPSHOT"

            from(components["java"])
        }
    }
}