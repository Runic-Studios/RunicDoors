val artifactName = "doors"
val rrGroup: String by rootProject.extra
val rrVersion: String by rootProject.extra

plugins {
    `java-library`
    `maven-publish`
}

group = rrGroup
version = rrVersion

dependencies {
    compileOnly(commonLibs.paper)
    compileOnly(commonLibs.acf)
    compileOnly(commonLibs.worldguardcore)
    compileOnly(commonLibs.worldguardlegacy)
    compileOnly(commonLibs.worldguardevents)
    compileOnly(project(":Projects:Core"))
    compileOnly(project(":Projects:Common"))
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = rrGroup
            artifactId = artifactName
            version = rrVersion
            from(components["java"])
        }
    }
}