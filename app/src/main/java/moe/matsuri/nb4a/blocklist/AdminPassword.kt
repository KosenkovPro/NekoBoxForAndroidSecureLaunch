package moe.matsuri.nb4a.blocklist

/**
 * Проверка административного пароля для разрешения удаления пакетов
 * из списка «по умолчанию» ([AccreditedBlocklist.defaults]).
 *
 * Принимаются варианты (без учёта регистра и с взаимозаменой `ё`/`е`):
 *   - `ЧёКакМаркет` (`ЧеКакМаркет`)
 *   - `CheKakStore`
 */
object AdminPassword {

    private val ACCEPTED: Set<String> = setOf(
        normalize("ЧёКакМаркет"),
        normalize("CheKakStore")
    )

    fun matches(input: String?): Boolean {
        if (input.isNullOrBlank()) return false
        return normalize(input) in ACCEPTED
    }

    private fun normalize(raw: String): String =
        raw.trim().lowercase().replace('ё', 'е')
}
