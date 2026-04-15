# `:app` — Android-приложение NekoBox

Единственный Gradle-модуль в проекте (`com.android.application`, namespace `io.nekohasekai.sagernet`, applicationId задаётся из `nb4a.properties` → `moe.nb4a`). Kotlin + Java, AndroidX, Room, ViewBinding, AIDL, KSP, coroutines.

## Сборка

- Плагины: `com.android.application`, `kotlin-android`, `com.google.devtools.ksp`, `kotlin-parcelize`.
- Конфигурация вынесена в [buildSrc](../buildSrc/CLAUDE.md): `setupApp()` задаёт compileSdk=35, minSdk=21, targetSdk=35, JVM 1.8 + coreLibraryDesugaring, flavors (`oss`/`fdroid`/`play`/`preview`), ABI splits, подпись.
- `executableSo/` добавляется как `jniLibs.srcDir` — туда кладутся бинарники плагинов, упаковываемые как `lib*.so` (`useLegacyPackaging = true`).
- KSP-параметры Room: `room.incremental=true`, схемы сохраняются в [schemas/](schemas).
- ProGuard: `proguard-android-optimize.txt` + [proguard-rules.pro](proguard-rules.pro). В debug — `applicationIdSuffix = "debug"`, `jniDebuggable = true`.
- `libcore.aar` попадает через `implementation(fileTree("libs"))` — собирается отдельно ([../libcore/CLAUDE.md](../libcore/CLAUDE.md)).

## Исходники

### `src/main`

| Путь | Содержит |
| --- | --- |
| [AndroidManifest.xml](src/main/AndroidManifest.xml) | Манифест, сервисы VPN/Proxy/Tile, shortcuts, разрешения. |
| [aidl/](src/main/aidl) | AIDL-интерфейсы для IPC между UI и фоновой службой. |
| [assets/](src/main/assets) | Бандлы (geoip/geosite по необходимости, yacd). |
| [res/](src/main/res) | XML-ресурсы: layouts, preferences, drawables, locales (локали объединяются — `enableSplit = false`). |
| [java/](src/main/java) | Весь Kotlin/Java-код. Три корневых пакета — см. ниже. |

### Основные пакеты Kotlin/Java

#### `io.nekohasekai.sagernet` (`app/src/main/java/io/nekohasekai/sagernet`)

Исторический корень приложения (унаследован от SagerNet).

- [`SagerNet.kt`](src/main/java/io/nekohasekai/sagernet/SagerNet.kt) — `Application`-класс, глобальные инстансы (`PackageManager`, `ConnectivityManager`, NotificationChannels).
- [`Constants.kt`](src/main/java/io/nekohasekai/sagernet/Constants.kt) — action-strings, настройки по умолчанию, ключи preferences.
- [`BootReceiver.kt`](src/main/java/io/nekohasekai/sagernet/BootReceiver.kt), [`QuickToggleShortcut.kt`](src/main/java/io/nekohasekai/sagernet/QuickToggleShortcut.kt) — автозапуск и быстрые переключатели.
- `bg/` — фоновый сервис (`BaseService`, `VpnService`, `ProxyService`, `TileService`, `GuardedProcessPool`, `SubscriptionUpdater`, `SagerConnection`). Точка старта/остановки VPN.
- `database/` — Room-база (`SagerDatabase`), сущности (`ProxyEntity`, `ProxyGroup`, `RuleEntity`), `DataStore`, `ProfileManager`, `GroupManager`.
- `fmt/` — конвертеры/билдеры конфигов для каждого протокола (shadowsocks, v2ray, trojan, hysteria, tuic, wireguard, ssh, socks, http, naive, mieru, trojan_go, internal). `ConfigBuilder.kt` собирает sing-box JSON. `gson/` — адаптеры JSON.
- `group/` — обновление групп/подписок (`GroupUpdater`, `RawUpdater`, `GroupInterfaceAdapter`).
- `ktx/` — kotlin-расширения (`Dialogs`, `Nets`, `Formats`, `Preferences`, `Logs`, `Asyncs`, `Kryos`, `Layouts`, `Dimens`, `Browsers`, `Utils`).
- `plugin/PluginManager.kt` — обнаружение/запуск плагинов-обёрток (trojan-go, naive, mieru).
- `ui/` — экраны (Main/Configuration/Route/Group/Settings/Backup/Logcat/Scanner/Stun/Network/About/Tools/Assets/Webview) и их `profile/`.
- `utils/` — `PackageCache`, `Theme`, `CrashHandler`, `DefaultNetworkListener`, `Subnet`, `Commandline`.
- `widget/` — кастомные Preference/View для UI (AppList/Group/Outbound/UserAgent/Url/Stats/FabProgressBehavior и т.п.).
- `aidl/` — DTO (`TrafficData`, `SpeedDisplayData`), сериализуемые через Parcelable.

#### `moe.matsuri.nb4a` (`app/src/main/java/moe/matsuri/nb4a`)

Код, добавленный в рамках NB4A поверх SagerNet.

- [`SingBoxOptions.java`](src/main/java/moe/matsuri/nb4a/SingBoxOptions.java), [`SingBoxOptionsUtil.kt`](src/main/java/moe/matsuri/nb4a/SingBoxOptionsUtil.kt) — типизированные опции sing-box JSON.
- [`NativeInterface.kt`](src/main/java/moe/matsuri/nb4a/NativeInterface.kt) — обратные вызовы из Go (`libcore`) в Kotlin.
- [`Protocols.kt`](src/main/java/moe/matsuri/nb4a/Protocols.kt), [`TempDatabase.kt`](src/main/java/moe/matsuri/nb4a/TempDatabase.kt) — вспомогательные утилиты/временная БД.
- `net/LocalResolverImpl.kt` — реализация DNS-резолвера, передаваемая в libcore.
- `plugin/Plugins.kt` — обёртка над PluginManager.
- `proxy/` — конкретные протоколы уровня NB4A: `anytls/`, `config/` (custom sing-box JSON-профили), `neko/` (Neko-плагины), `shadowtls/` + общий `PreferenceBinding*.kt`.
- `ui/` — расширенные `Preference`-классы (`ColorPickerPreference`, `EditConfigPreference`, `MTUPreference`, `SimpleMenuPreference`, `LongClickListPreference`, `UrlTestPreference`, `ConnectionTestNotification`, `ExtendedKeyboard`, `Dialogs`).
- `utils/` — `JavaUtil`, `KotlinUtil`, `NGUtil`, `Util`, `SendLog`, `WebViewUtil`.

#### `com.github.shadowsocks.plugin` (`app/src/main/java/com/github/shadowsocks/plugin`)

Плагин-API совместимый с shadowsocks-android — используется для внешних плагинов-обработчиков протоколов.

## Точки входа

- `Application` — `io.nekohasekai.sagernet.SagerNet`.
- Launcher — `ui.MainActivity`.
- VPN — `bg.VpnService` (Service) + `bg.VpnRequestActivity` (запрос разрешения `VpnService.prepare()`).
- Прокси без VPN — `bg.ProxyService`.
- Quick Settings tile — `bg.TileService`.
- Shortcuts — `QuickToggleShortcut`, `ui.QuickEnableShortcut`, `ui.QuickDisableShortcut`.

Для работы фонового сервиса через `workmanager` подключены `androidx.work:work-runtime-ktx` и `work-multiprocess`.

## Подсказки при изменениях

- Любая правка схемы Room → новая версия БД + миграция (используется Roomigrant, схемы коммитятся в `schemas/`).
- Добавление протокола: новый пакет в `fmt/`, ветка в `ConfigBuilder`, DTO с `@Parcelize`, UI в `ui/profile/`, регистрация в `Protocols.kt` и `PluginEntry.kt` при необходимости.
- Добавление preference — унаследуйтесь от готовых `widget/` или `moe.matsuri.nb4a.ui` вариантов, а не от голых AndroidX, чтобы попасть в общий стиль.
- При работе с Native (libcore): тип/сигнатуры методов должны совпадать с экспортом из Go — см. [`libcore/nb4a.go`](../libcore/nb4a.go) и [`libcore/platform_java.go`](../libcore/platform_java.go).
- Для задач «SecureLaunch/блокировка по списку приложений»: разумная точка — перед `BaseService.Interface.startRunner()` или в `VpnRequestActivity` до `VpnService.prepare()`; список установленных пакетов уже кэшируется в `utils/PackageCache.kt`.
