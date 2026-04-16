package moe.matsuri.nb4a.blocklist

import android.content.Context

/**
 * Хранилище пакетов, которые пользователь добавил в блок-лист вручную.
 * В отличие от [AccreditedBlocklist.defaults] эти пакеты можно удалять
 * без пароля администратора.
 */
class UserPackagesStore(context: Context) {

    private val prefs = context.applicationContext
        .getSharedPreferences(PREFS, Context.MODE_PRIVATE)

    fun getAll(): Set<String> =
        prefs.getStringSet(KEY, emptySet())?.toSet() ?: emptySet()

    fun add(pkg: String) {
        val normalized = pkg.trim()
        if (normalized.isEmpty()) return
        val updated = getAll().toMutableSet().apply { add(normalized) }
        prefs.edit().putStringSet(KEY, updated).apply()
    }

    fun remove(pkg: String) {
        val updated = getAll().toMutableSet().apply { remove(pkg) }
        prefs.edit().putStringSet(KEY, updated).apply()
    }

    fun contains(pkg: String): Boolean = pkg in getAll()

    companion object {
        private const val PREFS = "user_blocked_packages"
        private const val KEY = "packages"
    }
}
