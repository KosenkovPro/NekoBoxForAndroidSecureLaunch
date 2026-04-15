# `libcore` — Go-модуль ядра

Отдельный Go-модуль (`go.mod`: `module libcore`, Go 1.23). Собирается через `gomobile-matsuri bind` в `libcore.aar` и подключается в `:app` как локальная зависимость из `app/libs`. В `settings.gradle.kts` не присутствует — это не Gradle-модуль.

## Состав

| Файл/каталог | Назначение |
| --- | --- |
| `nb4a.go` | Публичный API, экспортируемый в Java (через gomobile). Основные функции для приложения. |
| `platform_java.go`, `platform_box.go` | Мосты между sing-box и Android-платформой (сеть, DNS, уведомления). |
| `box.go`, `box_include.go` | Запуск/остановка sing-box instance с тегами компиляции. |
| `dns_box.go`, `dns_android.go` | DNS-резолвер, использующий `LocalResolverImpl` из Kotlin. |
| `assets.go`, `assets_android.go`, `assets_other.go` | Работа с geoip/geosite/ассетами; отдельная реализация под Android. |
| `geoip.go`, `geosite.go` | Форматы MaxMind / sing-box geosite. |
| `interface_monitor.go` | Мониторинг сетевых интерфейсов. |
| `http.go`, `io.go`, `crypto.go`, `fix.go`, `certs.go` | Утилиты HTTP/IO/крипто, патчи и встроенные сертификаты. |
| `stun.go`, `stun/` | STUN-клиент для Tools → STUN. |
| `procfs/` | Парсер `/proc` для Android. |
| `ech/` | ECH (Encrypted Client Hello) утилиты. |
| `device/` | Платформенные обвязки. |
| `build.sh`, `init.sh` | Сборка (см. ниже). |
| `go.mod`, `go.sum` | Зависимости. |

## Зависимости (важное)

`go.mod` использует `replace` на соседние директории:

```
replace github.com/matsuridayo/libneko => ../../libneko
replace github.com/sagernet/sing-box => ../../sing-box
```

То есть ожидается layout:

```
<workspace>/
  libneko/
  sing-box/
  NekoBoxForAndroidSecureLaunch/
    libcore/
```

Без соседних `libneko` и `sing-box` сборка не стартует. Их версии указаны в `require` как `v1.0.0` (placeholder для replace).

Протокольные зависимости подтягиваются из форков SagerNet/Matsuri: `sing-box`, `sing-tun`, `sing-quic`, `sing-shadowsocks(2)`, `sing-vmess`, `sing-shadowtls`, `quic-go`, `wireguard-go`, `anytls/sing-anytls`, `gvisor`, `utls` и т.д.

## Сборка

```bash
cd libcore
./build.sh
```

Скрипт:
1. Подключает `env_java.sh` (если есть) и `../buildScript/init/env_ndk.sh`.
2. Устанавливает `GOBIND=gobind-matsuri` и вызывает `gomobile-matsuri bind -androidapi 21 -trimpath -ldflags='-s -w'`.
3. Build tags: `with_conntrack,with_gvisor,with_quic,with_wireguard,with_utls,with_clash_api`.
4. Результирующий `libcore.aar` копируется в `../app/libs/`.

Перед первым запуском нужен `init.sh` (установка `gomobile-matsuri`, NDK, Go), см. также [`../buildScript/CLAUDE.md`](../buildScript/CLAUDE.md).

## API для Android

Всё, что будет вызываться из Kotlin, должно экспортироваться как публичные функции/типы на уровне пакета `libcore` и иметь типы, поддерживаемые gomobile (`string`, `[]byte`, `int`, `bool`, структуры с примитивами, интерфейсы с методами возвращающими скаляр+error).

Обратные вызовы из Go в Kotlin делаются через интерфейсы (`platform_java.go`) — их реализует `moe.matsuri.nb4a.NativeInterface` и `moe.matsuri.nb4a.net.LocalResolverImpl`. При изменении сигнатур — менять синхронно в Kotlin.

## Подсказки при изменениях

- После правок Go всегда пересобирать `.aar` и коммитить обновлённый бинарник только если так принято в этом потоке (обычно `.aar` не коммитится — смотри `.gitignore`).
- Build tags управляют набором протоколов: убирая тег, можно уменьшить размер APK.
- `go.sum` обновлять `go mod tidy` в директории `libcore`, при этом `replace` на `../../libneko` и `../../sing-box` должен указывать на существующие каталоги — иначе `tidy` упадёт.
- Для отладки крашей в нативном коде проверяйте `logcat` по тегу `libcore`/`GoLog` и стек через `ndk-stack` (нужен debug-вариант сборки без `-s -w`).
