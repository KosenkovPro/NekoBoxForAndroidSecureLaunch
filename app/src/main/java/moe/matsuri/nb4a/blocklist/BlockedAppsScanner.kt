package moe.matsuri.nb4a.blocklist

import android.content.Context
import android.graphics.drawable.Drawable
import io.nekohasekai.sagernet.utils.PackageCache

/**
 * Результат сверки: установленное приложение, попавшее в блок-лист.
 */
data class BlockedApp(
    val packageName: String,
    val label: String,
    val icon: Drawable?
)

/**
 * Сверяет [PackageCache.installedApps] с переданным списком пакетов.
 * Возвращает отсортированный по отображаемому имени список найденных
 * приложений вместе с их иконками и label-ами.
 */
object BlockedAppsScanner {

    fun scan(context: Context, blocklist: Set<String>): List<BlockedApp> {
        if (blocklist.isEmpty()) return emptyList()

        PackageCache.awaitLoadSync()
        val pm = context.packageManager
        val result = mutableListOf<BlockedApp>()

        for (pkg in blocklist) {
            val appInfo = PackageCache.installedApps[pkg] ?: continue
            val label = runCatching { appInfo.loadLabel(pm)?.toString() }.getOrNull()
                ?.ifBlank { null }
                ?: PackageCache.loadLabel(pkg)
            val icon = runCatching { appInfo.loadIcon(pm) }.getOrNull()

            result += BlockedApp(
                packageName = pkg,
                label = label,
                icon = icon
            )
        }

        result.sortBy { it.label.lowercase() }
        return result
    }
}
