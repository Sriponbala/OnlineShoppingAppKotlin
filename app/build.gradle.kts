plugins {
    id("java")
    id("kotlin")
    `maven-publish`
}

group = "org.bala"
version = "1.0-SNAPSHOT"

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    implementation("org.sri:library:1.0-SNAPSHOT")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

publishing{
    publications{
        create<MavenPublication>("app"){
            groupId = "org.bala"
            artifactId = "app"
            version = "1.0-SNAPSHOT"

            from(components["java"])
        }
    }
}