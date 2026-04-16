package moe.matsuri.nb4a.blocklist

import android.content.Context
import android.graphics.drawable.Drawable
import io.nekohasekai.sagernet.utils.PackageCache

/** Источник записи блок-листа, к которой относится пакет. */
enum class BlocklistSource {
    /** Пакет из [AccreditedBlocklist.defaults] — защищённый паролем. */
    DEFAULT,

    /** Пакет добавлен пользователем вручную — можно удалять без пароля. */
    USER
}

/**
 * Снимок записи блок-листа. [installed] = `true`, если приложение найдено
 * на устройстве (тогда [label] и [icon] подтянуты из `PackageManager`).
 */
data class BlockedApp(
    val packageName: String,
    val label: String,
    val icon: Drawable?,
    val source: BlocklistSource,
    val installed: Boolean
)

object BlockedAppsScanner {

    /**
     * Возвращает **только установленные** приложения, пересекающиеся с
     * блок-листом. Подходит для сценария блокировки запуска VPN.
     */
    fun scanInstalled(
        context: Context,
        defaults: Set<String>,
        userAdded: Set<String> = emptySet()
    ): List<BlockedApp> = build(context, defaults, userAdded, onlyInstalled = true)

    /**
     * Возвращает **весь** блок-лист: и установленные, и нет. Подходит для
     * экрана управления списком.
     */
    fun listAll(
        context: Context,
        defaults: Set<String>,
        userAdded: Set<String> = emptySet()
    ): List<BlockedApp> = build(context, defaults, userAdded, onlyInstalled = false)

    /** Совместимость со старыми вызовами. */
    fun scan(context: Context, blocklist: Set<String>): List<BlockedApp> =
        scanInstalled(context, blocklist)

    private fun build(
        context: Context,
        defaults: Set<String>,
        userAdded: Set<String>,
        onlyInstalled: Boolean
    ): List<BlockedApp> {
        if (defaults.isEmpty() && userAdded.isEmpty()) return emptyList()

        PackageCache.awaitLoadSync()
        val pm = context.packageManager

        fun create(pkg: String, source: BlocklistSource): BlockedApp? {
            val appInfo = PackageCache.installedApps[pkg]
            val installed = appInfo != null
            if (onlyInstalled && !installed) return null

            val label = if (installed) {
                runCatching { appInfo!!.loadLabel(pm)?.toString() }.getOrNull()
                    ?.ifBlank { null }
                    ?: PackageCache.loadLabel(pkg)
            } else {
                AccreditedNames.resolve(pkg)
            }
            val icon = if (installed) {
                runCatching { appInfo!!.loadIcon(pm) }.getOrNull()
            } else null

            return BlockedApp(pkg, label, icon, source, installed)
        }

        val result = mutableListOf<BlockedApp>()
        for (pkg in defaults) create(pkg, BlocklistSource.DEFAULT)?.let(result::add)
        for (pkg in userAdded) {
            if (pkg in defaults) continue
            create(pkg, BlocklistSource.USER)?.let(result::add)
        }

        // В первую очередь — установленные, потом — по источнику, потом — по
        // отображаемому имени.
        result.sortWith(
            compareByDescending<BlockedApp> { it.installed }
                .thenBy { it.source.ordinal }
                .thenBy { it.label.lowercase() }
        )
        return result
    }
}
