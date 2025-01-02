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
    // Ktor Core
    implementation("io.ktor:ktor-server-core-jvm:$ktorVersion")
    // ネットワーク実装に Netty を使用する場合
    implementation("io.ktor:ktor-server-netty-jvm:$ktorVersion")
    // ログ出力 (Logback)
    implementation("ch.qos.logback:logback-classic:1.4.11")

    testImplementation(kotlin("test"))
}

application {
    mainClass.set("com.github.daitasu.ApplicationKt")
}
