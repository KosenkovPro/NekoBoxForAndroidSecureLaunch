# `buildScript/` — shell-скрипты сборочного конвейера

Это **не** Gradle-модуль — набор bash-скриптов, используемых локально и в CI для подготовки окружения, сборки нативных артефактов и ассетов. Значительная часть логики приезжает из родственного репо [`matsuridayo/nkmr`](https://github.com/matsuridayo/nkmr-public) через подмодуль в `nkmr/`.

## Структура

| Путь | Назначение |
| --- | --- |
| `copyLocal.sh` | Копирование локальных артефактов между соседними репо (`../libneko`, `../sing-box`). |
| `init/env.sh` | Экспорт `SRC_ROOT` и `ANDROID_*_CC/CXX/STRIP` для всех ABI (`armv7a`, `aarch64`, `i686`, `x86_64`) из toolchain NDK. Должен быть вызван `source`-ом из `run`-команд. |
| `init/env_ndk.sh` | Находит NDK (`ANDROID_HOME/ndk/25.0.8775105` → `ANDROID_NDK_HOME` → `NDK` → `ndk-bundle`) и экспортирует `ANDROID_NDK_HOME`, `NDK`. |
| `init/action/gradle.sh` | Обёртка Gradle для GitHub Actions. |
| `lib/core/build.sh`, `init.sh`, `get_source.sh`, `get_source_env.sh` | Подготовка и сборка Go-ядра (`libcore`): клонирование `sing-box`/`libneko` в соседние директории, установка `gomobile-matsuri`, прокидывание тегов сборки. |
| `lib/core.sh`, `lib/assets.sh` | Высокоуровневые обёртки: `core.sh` вызывает `libcore/build.sh`, `assets.sh` готовит geoip/geosite для `app/src/main/assets`. |
| `fdroid/prebuild.sh` | Prebuild-этап для F-Droid (без проприетарных зависимостей). |
| `nkmr/` | Содержимое submodule'а `nkmr` (общие скрипты релизного пайплайна Matsuri). |

## Ожидания к окружению

- **NDK 25.0.8775105** (строгая версия — ищется первой). Можно переопределить через `ANDROID_NDK_HOME` / `NDK`.
- **Android SDK**: `ANDROID_HOME` или стандартные пути (`~/Android/Sdk`, `~/.local/lib/android/sdk`, `~/Library/Android/sdk`).
- **Go 1.23** с `gomobile-matsuri` (ставит `buildScript/lib/core/init.sh`).
- **Соседний layout директорий** для replace-директив `libcore/go.mod`:
  ```
  <workspace>/libneko
  <workspace>/sing-box
  <workspace>/NekoBoxForAndroidSecureLaunch
  ```
- На Linux toolchain NDK ожидается в `toolchains/llvm/prebuilt/linux-x86_64/bin` — под macOS/Windows путь другой, `env.sh` в таком виде не сработает без правок.

## Типовой порядок команд

```bash
# 1. Один раз: поднять окружение и установить gomobile
source buildScript/init/env.sh
bash buildScript/lib/core/init.sh

# 2. Получить исходники sing-box / libneko в соседние папки
bash buildScript/lib/core/get_source.sh

# 3. Собрать libcore.aar
bash buildScript/lib/core.sh

# 4. (опц.) Обновить ассеты
bash buildScript/lib/assets.sh

# 5. Собрать APK
./gradlew :app:assembleOssRelease
```

## Подсказки

- Не коммитить изменения в `nkmr/` напрямую — это чужой submodule.
- Любая правка путей NDK/SDK идёт в `init/env_ndk.sh` и `init/env.sh`, не в Gradle.
- `fdroid/prebuild.sh` используется инфраструктурой F-Droid — при изменениях сохраняйте совместимость с их билд-сервером (без сети во время билда, только воспроизводимые шаги).
- Для Windows-разработчика эти скрипты рассчитаны на bash (Git Bash/WSL). Из обычного `cmd` они не запустятся.
