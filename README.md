[![Build Status](https://drone.bethibande.com/api/badges/Bethibande/YADI/status.svg)](https://drone.bethibande.com/Bethibande/YADI)
[![Maven](https://maven.bethibande.com/api/badge/latest/releases/com/yadi/core?name=Reposilite&prefix=v)](https://maven.bethibande.com)

# YADI
**Y**et **A**nother **D**ependency **I**njector

## Usage
```kotlin
val database = module {
    bind<DatabaseConnector> {
        bindSingleton { MariadbConnector(config) } tagged "mariadb"
        bindSingleton { SqlLightConnector(config) }
    }
}

val repository = module(database) {
    bindSingleton { UserRepository(instance()) }
}

val app = module {
    inject(database)
    inject(repository)
    
    val userRepo = instance<UserRepository>()
    val mariadb = instance<DatabaseConnector>("mariadb")
}
```

## Javadocs
- [Core (latest)](https://maven.bethibande.com/javadoc/releases/com/yadi/core/latest)

## Import
Note: replace 'VERSION' with the version you want to use
#### Maven
```xml
<repository>
    <id>bethibande-releases</id>
    <url>https://maven.bethibande.com/releases</url>
</repository>

<dependency>
  <groupId>com.yadi</groupId>
  <artifactId>core</artifactId>
  <version>VERSION</version>
</dependency>
```

#### Gradle
```kotlin
maven {
    name = "bethibandeReleases"
    url = uri("https://maven.bethibande.com/releases")
}

implementation("com.yadi:core:VERSION")
```
```groovy
maven {
    name "bethibandeReleases"
    url "https://maven.bethibande.com/releases"
}

implementation "com.yadi:core:VERSION"
```
