import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    alias(libs.plugins.dokka)
    alias(libs.plugins.gitSemVer)
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.qa)
    alias(libs.plugins.publishOnCentral)
    alias(libs.plugins.multiJvmTesting)
    alias(libs.plugins.taskTree)
}

buildscript {
    configurations.classpath {
        resolutionStrategy.activateDependencyLocking()
    }
}

group = "org.danilopianini"

repositories {
    mavenCentral()
}

gitSemVer {
    minimumVersion.set("5.4.0")
}

multiJvm {
    jvmVersionForCompilation.set(8)
}

dependencies {
    implementation(kotlin("stdlib"))
    testImplementation(libs.bundles.kotlin.testing)
}

dependencyLocking {
    lockAllConfigurations()
}

kotlin {
    compilerOptions {
        allWarningsAsErrors = true
        freeCompilerArgs = listOf("-opt-in=kotlin.RequiresOptIn")
    }
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        showStandardStreams = true
        showCauses = true
        showStackTraces = true
        events(*TestLogEvent.entries.toTypedArray())
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
    }
}

signing {
    val signingKey: String? by project
    val signingPassword: String? by project
    useInMemoryPgpKeys(signingKey, signingPassword)
}

publishOnCentral {
    repoOwner = "DanySK"
    projectLongName.set("Unidata jj2000")
    projectDescription.set(
        "Fork of jpeg2k code from https://code.google.com/p/jj2000/. " +
            "This is a dependency for support of compression in Grib2 files in netCDF-java and TDS. " +
            "This repo is NOT under active development other than updates for bug fixes. " +
            "Implementing new features and functionality for this code base is beyond the current scope " +
            "of our development and support for the THREDDS project. " +
            "We welcome bug fixes and other contributions to this code.",
    )
    projectUrl.set("https://github.com/DanySK/jj2000")
    licenseName.set("jj2000 License")
    licenseUrl.set("https://github.com/DanySK/jj2000/blob/main/COPYRIGHT")
    scmConnection.set("scm:git:https://github.com/DanySK/jj2000.git")
    repository("https://maven.pkg.github.com/danysk/${rootProject.name}".lowercase()) {
        user.set("DanySK")
        password.set(System.getenv("GITHUB_TOKEN"))
    }
    publishing {
        publications {
            withType<MavenPublication> {
                pom {
                    developers {
                        developer {
                            id.set("unidata")
                            name.set("Unidata")
                            email.set("support-netcdf-java@unidata.ucar.edu")
                            organization.set("UCAR/Unidata")
                            organizationUrl.set("http://www.unidata.ucar.edu/")
                        }
                        developer {
                            name.set("Danilo Pianini")
                            email.set("danilo.pianini@gmail.com")
                        }
                    }
                }
            }
        }
    }
}
