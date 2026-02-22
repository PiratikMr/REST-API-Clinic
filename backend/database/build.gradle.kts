buildscript {
    dependencies {
        classpath("org.postgresql:postgresql:42.7.8")
    }
}

plugins {
    id("org.flywaydb.flyway") version "9.21.0"
}

flyway {
    url = "jdbc:postgresql://${System.getenv("PG_HOST")}:${System.getenv("PG_PORT")}/${System.getenv("PG_DATABASE")}"
    user = System.getenv("PG_USER")
    password = System.getenv("PG_PASSWORD")

    locations = arrayOf("filesystem:migrations")
}