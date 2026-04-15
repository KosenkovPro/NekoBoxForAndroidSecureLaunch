# `buildSrc` — Kotlin DSL helpers для Gradle

Стандартный Gradle `buildSrc`-проект. Содержит единственный файл [`src/main/kotlin/Helpers.kt`](src/main/kotlin/Helpers.kt), который экспортирует функции-расширения для `Project`, используемые в `app/build.gradle.kts`.

## Экспортируемые функции

| Функция | Что делает |
| --- | --- |
| `Project.requireMetadata()` | Ленивая загрузка `nb4a.properties` в корне репозитория (`PACKAGE_NAME`, `VERSION_NAME`, `VERSION_CODE`, `PRE_VERSION_NAME`). |
| `Project.requireLocalProperties()` | Загружает `local.properties` из корня либо base64-строку из env `LOCAL_PROPERTIES` (CI). |
| `Project.setupCommon()` | Общие настройки Android: `compileSdk=35`, `minSdk=21`, `targetSdk=35`, `buildToolsVersion=35.0.1`, JVM 1.8, lint (всё как warningsAsErrors, отчёты в `build/lint.*`), packaging excludes (META-INF, kotlin metadata, …), debug/release buildTypes, переименование APK (`<projectName>-<versionName>`). |
| `Project.setupAppCommon()` | `setupCommon()` + signingConfigs: читает `KEYSTORE_PASS` / `ALIAS_NAME` / `ALIAS_PASS` из `local.properties` или env, применяет `release.keystore` к `release` и `debug`. |
| `Project.setupApp()` | `setupAppCommon()` + applicationId/versionCode (=`VERSION_CODE × 5`)/versionName из `nb4a.properties`; ProGuard для release; ABI splits (`armeabi-v7a`, `arm64-v8a`, `x86`, `x86_64`, без universal); flavorDimension `vendor` с flavors `oss`/`fdroid`/`play`/`preview` (preview несёт `PRE_VERSION_NAME` в BuildConfig); генерация aliases `assemble{Arm64,Arm,X64,X86}FdroidRelease`; `jniLibs.srcDir("executableSo")`. Переименовывает итоговые APK в `NekoBox-<versionName>.apk`, preview — в `NekoBox-<PRE_VERSION_NAME>.apk`. |

## Использование

В [`app/build.gradle.kts`](../app/build.gradle.kts):

```kotlin
plugins { id("com.android.application"); /* ... */ }
setupApp()                  // вызывается до блока android { }
android { /* доп. настройки модуля */ }
```

## Правила работы

- `buildSrc/build.gradle.kts` подключает `kotlin-dsl` и AGP/Kotlin plugins, чтобы внутри можно было работать с `ApplicationExtension`, `AbstractAppExtension`, `BaseVariantOutputImpl`, `KotlinJvmOptions`.
- `exitProcess` импортирован «про запас» — не вызывается, но оставлен, см. `Helpers.kt:11`.
- `isMinifyEnabled`/`isShrinkResources` можно выключить переменной окружения `nkmr_minify=0` — это делается именно здесь.
- При изменении версии SDK/NDK/Java — править тут, а не в `app/build.gradle.kts`.
- Если нужен новый flavor — добавлять в `setupApp()` внутрь `productFlavors { ... }`.
- Для подписи CI кладёт `release.keystore` в корень + `LOCAL_PROPERTIES` (base64) в env.

## Смежные файлы

- [`../nb4a.properties`](../nb4a.properties) — читается `requireMetadata()`.
- [`../repositories.gradle.kts`](../repositories.gradle.kts) — применяется ко всем проектам из top-level `build.gradle.kts`.
- [`../release.keystore`](../release.keystore) — keystore, подставляется `setupAppCommon()`.
