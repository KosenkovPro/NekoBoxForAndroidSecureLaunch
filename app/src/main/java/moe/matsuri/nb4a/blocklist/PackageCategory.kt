@file:Suppress("SpellCheckingInspection")

package moe.matsuri.nb4a.blocklist

import java.util.Locale

/**
 * Категории пакетов для фильтрации на экране «Пакеты».
 * [labelEn] / [labelRu] — отображаемые названия для Spinner.
 */
enum class PackageCategory(val labelEn: String, val labelRu: String) {
    ALL("All categories", "Все категории"),
    BROWSERS("Browsers", "Браузеры"),
    SOCIAL("Social / Messaging", "Социальные сети / Мессенджеры"),
    MEDIA("Media / Streaming", "Медиа / Стриминг"),
    MUSIC("Music", "Музыка"),
    VIDEO("Video", "Видео"),
    NEWS("News", "Новости"),
    MAPS("Maps / Navigation", "Карты / Навигация"),
    TRANSPORT("Transport / Travel", "Транспорт / Путешествия"),
    TAXI("Taxi / Mobility", "Такси / Мобильность"),
    DELIVERY("Delivery / Food", "Доставка / Еда"),
    MARKETPLACE("Marketplaces / E-commerce", "Маркетплейсы / Онлайн-магазины"),
    RETAIL("Retail / Grocery", "Ритейл / Продукты"),
    FINANCE("Finance / Banking", "Финансы / Банки"),
    PAYMENTS("Payments", "Платежи"),
    TELECOM("Telecom / Operators", "Телеком / Операторы связи"),
    GOVERNMENT("Government / Public Services", "Госуслуги / Гос. сервисы"),
    WORK("Work / Productivity", "Работа / Продуктивность"),
    BUSINESS("Business / CRM / SaaS", "Бизнес / CRM / SaaS"),
    EDUCATION("Education", "Образование"),
    JOBS("Jobs / HR", "Работа / Вакансии"),
    HEALTH("Health / Medical", "Здоровье / Медицина"),
    INSURANCE("Insurance", "Страхование"),
    SMART_HOME("Smart Home / IoT", "Умный дом / IoT"),
    CLOUD("Cloud / Storage / Hosting", "Облако / Хранилище / Хостинг"),
    SECURITY("Security / Antivirus", "Безопасность / Антивирус"),
    APP_STORES("App Stores", "Магазины приложений"),
    LOGISTICS("Logistics / Delivery Services", "Логистика / Доставка"),
    AUTOMOTIVE("Automotive", "Авто / Транспорт"),
    GAMING("Gaming", "Игры"),
    MISC("Misc / Other", "Разное / Прочее");

    val label: String
        get() = if (Locale.getDefault().language == "ru") labelRu else labelEn

    companion object {
        /** Возвращает категорию пакета. Для неизвестных — [MISC]. */
        fun of(pkg: String): PackageCategory = categoryMap[pkg] ?: MISC
    }
}

/**
 * Маппинг пакет → категория. Поддерживает все 288 записей
 * [AccreditedBlocklist.defaults]. Пользовательские пакеты (не из defaults)
 * по умолчанию попадают в [PackageCategory.MISC].
 */
private val categoryMap: Map<String, PackageCategory> = buildMap {
    fun put(cat: PackageCategory, vararg pkgs: String) {
        for (p in pkgs) put(p, cat)
    }

    put(PackageCategory.BROWSERS,
        "ru.yandex.browser", "ru.yandex.browser.beta", "ru.yandex.browser.dev",
        "ru.yandex.browser.alpha"
    )

    put(PackageCategory.SOCIAL,
        "com.vkontakte.android", "com.vk.im", "com.vk.calls",
        "ru.ok.android", "ru.ok.messages", "ru.mail.mailapp",
        "ru.max.app", "com.icq.mobile.client", "ru.myteam.messenger",
        "com.vkontakte.workspaces", "com.vkontakte.workspace",
        "com.vkontakte.services", "com.vkontakte.admin", "com.vkontakte.ads",
        "com.vkontakte.analytics", "com.vkontakte.store", "com.vkontakte.account",
        "com.vk.id", "ru.vk.passport", "com.vk.auth", "com.yandex.auth",
        "com.vk.admin", "com.vk.ads", "com.vk.team",
        "ru.mail.workspace", "ru.mail.team"
    )

    put(PackageCategory.MEDIA,
        "ru.rutube.app", "ru.ivi.client", "ru.more.play",
        "ru.start.ru", "ru.premier.one", "ru.kion.android", "ru.wink.rt",
        "ru.more.tv", "ru.ntvplus.app", "ru.tricolor.app", "ru.tvzavr",
        "ru.amediateka", "ru.vgtrk.smotrim", "ru.okko", "ru.kion",
        "ru.litres.android", "ru.yappy.app"
    )

    put(PackageCategory.MUSIC,
        "ru.yandex.music", "ru.yandex.music.beta",
        "ru.mts.music", "ru.beeline.music", "ru.megafon.music"
    )

    put(PackageCategory.VIDEO,
        "com.vk.video.beta", "com.vkontakte.video", "ru.vk.video.player",
        "com.vkontakte.live", "com.vk.live", "ru.vk.live",
        "ru.mail.video", "ru.mail.biz.video",
        "ru.rt.video", "ru.rambler.video",
        "com.vk.clips"
    )

    put(PackageCategory.NEWS,
        "ru.yandex.zen", "ru.yandex.news",
        "ru.rambler.news"
    )

    put(PackageCategory.MAPS,
        "ru.yandex.maps", "ru.yandex.maps.beta",
        "ru.yandex.yandexmaps", "ru.yandex.yandexnavi",
        "ru.yandex.metro", "ru.2gis.mobile"
    )

    put(PackageCategory.TRANSPORT,
        "ru.rzd.pass", "ru.rzd.cargo", "ru.aeroflot.app.android",
        "ru.s7.android", "ru.utair.android", "ru.pobeda.android",
        "ru.aeroexpress", "ru.mosgortrans", "ru.troika.app",
        "ru.mos.transport",
        "ru.tutu", "ru.onetwotrip", "ru.travelata.app",
        "ru.sletat.app", "ru.ostrovok.app",
        "ru.blablacar", "ru.poputi.app"
    )

    put(PackageCategory.TAXI,
        "ru.yandex.taxi", "ru.yandex.go", "ru.yandex.taximeter",
        "com.citymobil", "ru.drivee", "ru.maxim", "ru.taxsee.taxsee",
        "ru.delimobil", "ru.belkacar", "ru.whoosh",
        "ru.yandex.scooters", "ru.mts.urent",
        "ru.yandex.drive", "ru.yandex.yango",
        "ru.yandex.ubercab", "ru.yandex.uber"
    )

    put(PackageCategory.DELIVERY,
        "ru.yandex.eda", "ru.yandex.lavka", "ru.yandex.food",
        "ru.deliveryclub.app", "ru.samokat.app",
        "ru.dodo.pizza", "ru.pizzahut.android", "ru.tanuki.app",
        "ru.kfc.mobile", "ru.burgerking", "ru.vkusnoitochka.app",
        "ru.vprok.app", "ru.utkonos.app",
        "ru.cdek.app", "ru.boxberry"
    )

    put(PackageCategory.MARKETPLACE,
        "ru.ozon.app.android", "ru.wildberries.ru",
        "com.lamoda.android", "ru.avito", "ru.youla",
        "com.drom.auto", "ru.cian.main", "ru.domclick",
        "ru.kazanexpress.android", "ru.sima.land", "ru.kupivip",
        "ru.yandex.market", "ru.yandex.shop",
        "ru.avito.android"
    )

    put(PackageCategory.RETAIL,
        "ru.pyaterochka.app", "com.semerochka",
        "ru.x5.retailgroup.x5bonus", "com.avoska.app",
        "ru.chizhik.app", "ru.perekrestok.app",
        "ru.magnit.app", "ru.lenta.app",
        "ru.globus.app", "ru.metro.ccc", "ru.auchan.app", "ru.okmarket.app",
        "ru.hoff.app", "ru.petrovich.app", "ru.leroymerlin.android",
        "ru.obi.app", "ru.maxidom.app", "ru.vseinstrumenti.app",
        "ru.castorama.app",
        "ru.mvideo.android", "ru.eldorado.app", "ru.citilink.mobile",
        "ru.sportmaster.app", "ru.detmir.dmbonus",
        "ru.vkusvill", "ru.sbermarket",
        "ru.torgservis.svetofor"
    )

    put(PackageCategory.FINANCE,
        "ru.sberbankmobile", "ru.sberbankbusinessonline",
        "ru.vtb24.mobilebanking.android",
        "ru.alfabank.mobile.android", "ru.tinkoff.android",
        "ru.raiffeisen.mobile", "ru.rosbank.android",
        "ru.homecredit.bank", "ru.openbank",
        "ru.gazprombank.android.mobilebank", "ru.psbank.mobile",
        "ru.rshb", "ru.akbars.mobile", "ru.sovcombank.mobile",
        "ru.mkb.mobile", "ru.pochtabank.app", "ru.mtsbank",
        "ru.tochka", "ru.modulbank.android",
        "ru.sberbank.investor", "ru.sberbank.broker",
        "ru.sber.insurance", "ru.sber.leasing",
        "ru.finam.trade", "ru.bcs.broker", "ru.openbroker",
        "ru.vtb.investments", "ru.tinkoff.invest.beta", "ru.alfa.invest",
        "ru.uralsib.mobile", "ru.absolutbank", "ru.ubrr.mobile",
        "ru.tatfondbank", "ru.centerinvest.mobile", "ru.lockobank",
        "ru.genbank.mobile", "ru.sdm.mobile",
        "ru.delobank", "ru.tochka.business", "ru.modulbank.business", "ru.sfera.bank",
        "ru.ozon.bank", "ru.wildberries.bank", "ru.yandex.bank",
        "ru.tinkoff.invest", "ru.tinkoff.business", "ru.tinkoff.accounting",
        "ru.alfabank.mobile.business", "ru.alfabank.alfainvest",
        "ru.vtb.invest", "ru.vtb.business",
        "ru.sberbank.sberpay", "ru.sberbank.sbermarket",
        "ru.sberbank.spasibo", "ru.sberbank.onlineassistant",
        "ru.sberbank.telemed", "ru.sberdevices.salute"
    )

    put(PackageCategory.PAYMENTS,
        "ru.yoomoney", "ru.qiwi.wallet", "com.webmoney.my",
        "ru.nspk.mirpay", "ru.yandex.pay",
        "ru.paykeeper", "ru.cloudpayments.app", "ru.paylate",
        "ru.kassa.app", "ru.uniteller",
        "ru.payberry.app", "ru.paymaster.app", "ru.robokassa.app",
        "ru.mts.pay", "ru.beeline.pay", "ru.megafon.pay",
        "ru.tele2.pay"
    )

    put(PackageCategory.TELECOM,
        "ru.mts", "ru.mts.mymts", "ru.mts.kion", "ru.mts.store",
        "ru.mts.tv", "ru.mts.cloud", "ru.mts.id", "ru.mts.market", "ru.mts.shop",
        "ru.mts.link", "ru.mts.android.apps.tracker", "ru.mts.android.apps.coordinator",
        "ru.megafon.mlk", "ru.megafon.app", "ru.megafon.tv",
        "ru.megafon.cloud", "ru.megafon.business", "ru.megafon.veon", "ru.megafon.lk",
        "ru.beeline.services", "ru.beeline.mykabinet", "ru.beeline.tv",
        "ru.beeline.cloud", "ru.beeline.home",
        "ru.tele2", "ru.tele2.mytele2", "ru.tele2.market", "ru.tele2.tv",
        "ru.yota.app", "ru.yota.lk", "ru.yota.selfcare", "ru.yota.business",
        "com.rostelecom.mobile", "ru.sbermobile", "ru.tinkoff.mobile",
        "ru.motiv.telecom", "ru.skylink.mobile", "ru.domru.mobile",
        "ru.rt.video", "ru.rt.portal", "ru.rt.service", "ru.rt.client",
        "ru.rt.cloud", "ru.rt.tv"
    )

    put(PackageCategory.GOVERNMENT,
        "ru.gosuslugi.android", "ru.gosuslugi.stopcoronavirus",
        "ru.mos.app", "ru.mos.parking", "ru.mos.culture",
        "ru.mos.school", "ru.mos.dnevnik",
        "ru.fns.lkfl", "ru.emias.app", "ru.pochta.app", "ru.mfc.app",
        "ru.pfr.app", "ru.gibdd.app"
    )

    put(PackageCategory.WORK,
        "ru.mail.biz.mail", "ru.mail.biz.calendar", "ru.mail.biz.chat",
        "ru.yandex.telemost", "ru.yandex.rasp",
        "ru.yandex.translate"
    )

    put(PackageCategory.BUSINESS,
        "ru.bitrix24.app", "ru.amocrm.app", "ru.megaplan.app",
        "ru.yclients.mobile", "ru.docsvision", "ru.elma.app"
    )

    put(PackageCategory.EDUCATION,
        "ru.skillbox.app", "ru.geekbrains.app",
        "ru.skyeng.student", "ru.skyeng.teacher",
        "ru.foxford.app", "ru.netology.app",
        "ru.sber.education"
    )

    put(PackageCategory.JOBS,
        "ru.hh.android", "ru.superjob.app", "ru.zarplata.app", "ru.youdo.app"
    )

    put(PackageCategory.HEALTH,
        "ru.eapteka.app", "ru.rigla.app", "ru.asna.app",
        "ru.medsi", "ru.invitro.app", "ru.gemotest.app",
        "ru.apteka.ru", "ru.zdravcity.app", "ru.uteka.app",
        "ru.sber.health"
    )

    put(PackageCategory.INSURANCE,
        "ru.ingos.mobile", "ru.alfains.mobile", "ru.renins.app", "ru.sogaz.app"
    )

    put(PackageCategory.SMART_HOME,
        "ru.yandex.iot", "ru.sber.smart.home", "ru.rostelecom.smarthome",
        "ru.sber.devices"
    )

    put(PackageCategory.CLOUD,
        "ru.cloud.mailru", "ru.selectel.mobile", "ru.reg.ru.app",
        "ru.yandex.disk", "ru.yandex.disk.beta", "ru.mail.cloud",
        "ru.mts.cloud", "ru.beeline.cloud", "ru.megafon.cloud", "ru.rt.cloud"
    )

    put(PackageCategory.SECURITY,
        "com.kaspersky.kasperskyinternetsecurity", "com.kaspersky.passwordmanager",
        "ru.drweb", "com.eset.mobile.security.russia"
    )

    put(PackageCategory.APP_STORES,
        "ru.rustore.client", "com.nashstore", "ru.vk.store"
    )

    put(PackageCategory.LOGISTICS,
        "ru.dpdonline", "ru.dellin", "ru.baikalservice", "ru.pecom.app",
        "ru.yandex.delivery", "ru.yandex.courier"
    )

    put(PackageCategory.GAMING,
        "com.my.mygamesapp", "com.vk.vkplay",
        "com.my.games.app", "com.my.games.launcher",
        "com.my.games.store", "com.my.games.hub",
        "com.my.gamestore", "com.my.games.platform",
        "ru.vk.play", "ru.my.games",
        "ru.rambler.games"
    )

    put(PackageCategory.FINANCE,
        "ru.sberbank.android", "ru.tinkoff.bank"
    )

    put(PackageCategory.MARKETPLACE,
        "com.wildberries.ru", "ru.ozon.app"
    )

    put(PackageCategory.MEDIA,
        "ru.kinopoisk", "com.vk.video", "ru.rutube.app", "com.vkontakte.music"
    )

    put(PackageCategory.MAPS,
        "ru.dublgis.dgismobile"
    )

    put(PackageCategory.MISC,
        "ru.yandex.searchplugin", "ru.yandex.search",
        "ru.yandex.services", "ru.yandex.superapp",
        "ru.yandex.mobile", "ru.yandex.partner",
        "ru.yandex.client", "ru.yandex.mobile.exp",
        "ru.yandex.weatherplugin", "ru.yandex.kinopoisk",
        "ru.yandex.oauth", "ru.vk.oauth", "ru.sber.oauth", "ru.mail.oauth",
        "ru.sberbank.sdk", "ru.sberbank.platform", "ru.sberbank.id", "ru.sberbank.auth",
        "ru.sber.auto",
        "ru.rambler.weather", "ru.rambler.horoscope", "ru.rambler.mail",
        "com.yandex.launcher", "ru.yandex.news"
    )
}
