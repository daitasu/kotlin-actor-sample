plugins {
    // Kotlin JVM プラグイン
    kotlin("jvm") version "1.8.22"
    // gradle run タスクなどを利用可能にするアプリケーションプラグイン
    application
}

repositories {
    mavenCentral()
}

val ktorVersion = "3.0.3"

dependencies {
    testImplementation(kotlin("test"))

    implementation("org.apache.pekko:pekko-actor-typed_2.13:1.1.1")
    implementation("org.apache.pekko:pekko-cluster-typed_2.13:1.1.1")
    implementation("org.apache.pekko:pekko-serialization-jackson_2.13:1.1.1")
    implementation("org.apache.pekko:pekko-remote_2.13:1.1.1")

}

application {
    mainClass.set("com.github.daitasu.ApplicationKt")
}
