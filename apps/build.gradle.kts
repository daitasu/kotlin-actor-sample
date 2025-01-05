plugins {
    kotlin("jvm") version "1.8.22"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    application
}

repositories {
    mavenCentral()
}

val ktorVersion = "3.0.3"

dependencies {
    testImplementation(kotlin("test"))

    implementation("org.apache.pekko:pekko-actor-typed_2.13:1.1.2")
    implementation("org.apache.pekko:pekko-cluster-typed_2.13:1.1.2")
    implementation("org.apache.pekko:pekko-cluster-tools_2.13:1.1.2")
    implementation("org.apache.pekko:pekko-remote_2.13:1.1.2")
    implementation("org.apache.pekko:pekko-stream_2.13:1.1.2")
    implementation("org.apache.pekko:pekko-slf4j_2.13:1.1.2")

    implementation("org.slf4j:slf4j-simple:2.0.16")

}

tasks.withType<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar> {
    mergeServiceFiles("reference.conf")
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
    from(sourceSets.main.get().resources)
}

application {
    mainClass.set("com.github.daitasu.ApplicationKt")
}
