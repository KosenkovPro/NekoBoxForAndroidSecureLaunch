# NekoBoxForAndroidSecureLaunch

Форк [NekoBoxForAndroid](https://github.com/MatsuriDayo/NekoBoxForAndroid) — универсального Android-клиента на базе `sing-box` (VMess/VLESS/Trojan/Shadowsocks/Hysteria/WireGuard и др.).

Особенность форка: перед запуском VPN проверяется наличие на устройстве приложений аккредитованных ИТ-компаний (РФ), и при их обнаружении запуск блокируется. На момент фиксации репозитория (HEAD `be7bdc0`) логика заявлена только в `README.md`; в Kotlin/Java-коде реализация ещё не появилась.

## Структура репозитория

| Путь | Назначение |
| --- | --- |
| [app/](app) | Единственный Gradle-модуль (`:app`), Android-приложение. См. [app/CLAUDE.md](app/CLAUDE.md). |
| [libcore/](libcore) | Отдельный Go-модуль, собирается в `libcore.aar` через `gomobile` и кладётся в `app/libs`. См. [libcore/CLAUDE.md](libcore/CLAUDE.md). |
| [buildSrc/](buildSrc) | Kotlin DSL helpers для Gradle (`setupApp`, `setupCommon`, подпись, flavors). См. [buildSrc/CLAUDE.md](buildSrc/CLAUDE.md). |
| [buildScript/](buildScript) | Shell-скрипты: инициализация NDK/Go, сборка `libcore`, ассеты, F-Droid, nkmr. См. [buildScript/CLAUDE.md](buildScript/CLAUDE.md). |
| [run](run) | Обёртки над частыми операциями сборки (используется CI/локально). |
| [nb4a.properties](nb4a.properties) | Источник правды по версиям: `PACKAGE_NAME`, `VERSION_NAME`, `VERSION_CODE`, `PRE_VERSION_NAME`. |
| [settings.gradle.kts](settings.gradle.kts) | Подключает только `:app` (`rootProject.name = "NB4A"`). |
| [release.keystore](release.keystore) | Релизный keystore (пароли берутся из `local.properties` или env `KEYSTORE_PASS`/`ALIAS_NAME`/`ALIAS_PASS`). |

## Ключевые факты для работы с кодом

- Gradle-модуль ровно один — `:app`. `libcore`, `buildSrc`, `buildScript` не включены в `settings.gradle.kts`, их сборка идёт отдельно и результат (`libcore.aar`) попадает в `app/libs` до Gradle-сборки APK.
- Версия приложения поднимается правкой `nb4a.properties` (итоговый `versionCode = VERSION_CODE * 5` — см. `buildSrc`).
- Product flavors: `oss`, `fdroid`, `play`, `preview` (измерение `vendor`). ABI-splits: `armeabi-v7a`, `arm64-v8a`, `x86`, `x86_64`.
- `libcore` зависит от родственных репо `matsuridayo/libneko` и `sagernet/sing-box` через `replace ../../libneko` и `replace ../../sing-box` — ожидается соседний layout директорий.
- Перед задачей «обновить ядро / пересобрать libcore» см. [libcore/CLAUDE.md](libcore/CLAUDE.md) и [buildScript/CLAUDE.md](buildScript/CLAUDE.md).
- Перед задачей про заявленную функциональность SecureLaunch: реализации ещё нет, начинать с выбора места внедрения (разумнее всего — `VpnRequestActivity` / `BaseService.Interface.startRunner()` в `app`).

## Типовые команды

```bash
# Сборка libcore (требует Go + Android NDK + gomobile-matsuri)
cd libcore && ./build.sh

# Сборка APK (после того, как libcore.aar лежит в app/libs)
./gradlew :app:assembleOssDebug
./gradlew :app:assembleOssRelease
```

Подробности сборочной среды — в `buildScript/init/env.sh` и `buildScript/init/env_ndk.sh`.
