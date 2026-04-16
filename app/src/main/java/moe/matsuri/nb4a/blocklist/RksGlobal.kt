package moe.matsuri.nb4a.blocklist

import java.util.Locale

/**
 * Пакеты, которые по открытым данным используют RKS Global для
 * обнаружения VPN-подключения.
 */
object RksGlobal {

    /** Пакеты с RKS-детекцией VPN. */
    val packages: Set<String> = setOf(
        "ru.yandex.browser",
        "ru.yandex.maps",
        "com.vkontakte.android",
        "ru.mts",
        "ru.sberbank.android",
        "ru.tinkoff.bank",
        "com.vk.video",
        "com.wildberries.ru",
        "ru.kinopoisk",
        "ru.ozon.app",
        "ru.samokat.app",
        "ru.rustore.client",
        "ru.vtb24.mobilebanking.android",
        "ru.yandex.music",
        "ru.avito.android",
        "ru.alfabank.mobile.android",
        "ru.dublgis.dgismobile",
        "ru.sberbank.sbermarket",
        "ru.ok.android",
        "ru.max.app",
        "ru.rutube.app",
        "com.vkontakte.music"
    )

    fun isRks(pkg: String): Boolean = pkg in packages

    /** Локализованный бейдж для карточки. */
    val badge: String
        get() = if (Locale.getDefault().language == "ru")
            "Проверка сети (RKS Global)"
        else
            "RKS Global: VPN detection"
}
