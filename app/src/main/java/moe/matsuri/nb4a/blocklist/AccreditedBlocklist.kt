package moe.matsuri.nb4a.blocklist

/**
 * Пакеты приложений аккредитованных ИТ-компаний (РФ), наличие которых на
 * устройстве блокирует запуск VPN (см. [README.md]).
 *
 * Тип [Set] автоматически устраняет повторы; явные дубли в исходнике также
 * удалены для читаемости.
 */
object AccreditedBlocklist {

    val defaults: Set<String> = setOf(
        // --- Fintech / neobanks
        "ru.ozon.bank",
        "ru.wildberries.bank",
        "ru.yandex.bank",
        "ru.sberbank.investor",
        "ru.sberbank.broker",
        "ru.sber.insurance",
        "ru.sber.leasing",

        // --- Investment / trading
        "ru.finam.trade",
        "ru.bcs.broker",
        "ru.openbroker",
        "ru.vtb.investments",
        "ru.tinkoff.invest.beta",
        "ru.alfa.invest",

        // --- Additional banks (regional / niche)
        "ru.uralsib.mobile",
        "ru.absolutbank",
        "ru.ubrr.mobile",
        "ru.tatfondbank",
        "ru.centerinvest.mobile",
        "ru.lockobank",
        "ru.genbank.mobile",
        "ru.sdm.mobile",

        // --- Digital / SME banks
        "ru.delobank",
        "ru.tochka.business",
        "ru.modulbank.business",
        "ru.sfera.bank",

        // --- Payment / wallets
        "ru.paykeeper",
        "ru.cloudpayments.app",
        "ru.paylate",
        "ru.kassa.app",
        "ru.uniteller",

        // --- MTS ecosystem
        "ru.mts.mymts",
        "ru.mts.android.apps.tracker",
        "ru.mts.android.apps.coordinator",
        "ru.mts.link",
        "ru.mts.urent",
        "ru.mts.music",
        "ru.mts.kion",
        "ru.mts.store",
        "ru.mts.tv",
        "ru.mts.pay",
        "ru.mts.cloud",
        "ru.mts.id",
        "ru.mts.market",
        "ru.mts.shop",

        // --- Beeline
        "ru.beeline.mykabinet",
        "ru.beeline.pay",
        "ru.beeline.music",
        "ru.beeline.tv",
        "ru.beeline.cloud",
        "ru.beeline.home",

        // --- MegaFon
        "ru.megafon.lk",
        "ru.megafon.app",
        "ru.megafon.pay",
        "ru.megafon.music",
        "ru.megafon.tv",
        "ru.megafon.cloud",
        "ru.megafon.business",
        "ru.megafon.veon",

        // --- Yota
        "ru.yota.app",
        "ru.yota.lk",
        "ru.yota.selfcare",
        "ru.yota.business",

        // --- VK Play / streaming / gaming
        "com.my.mygamesapp",
        "com.vk.vkplay",
        "com.vkontakte.live",
        "com.vk.live",
        "ru.vk.live",

        // --- MY.GAMES / VK gaming ecosystem
        "com.my.games.app",
        "com.my.games.launcher",
        "com.my.games.store",
        "com.my.games.hub",

        // --- VK ecosystem (rare / internal / side apps)
        "com.vkontakte.workspaces",
        "com.vkontakte.workspace",
        "com.vkontakte.services",
        "com.vkontakte.admin",
        "com.vkontakte.ads",
        "com.vkontakte.analytics",
        "com.vkontakte.store",

        // --- VK video / media experimental
        "com.vk.video.beta",
        "com.vkontakte.video",
        "ru.vk.video.player",

        // --- VK ID / auth ecosystem
        "com.vk.id",
        "com.vkontakte.account",
        "ru.vk.passport",

        // --- Yandex (редкие и внутренние)
        "ru.yandex.browser.dev",
        "ru.yandex.browser.alpha",
        "ru.yandex.search",
        "ru.yandex.services",
        "ru.yandex.superapp",
        "ru.yandex.mobile",
        "ru.yandex.partner",
        "ru.yandex.yango",
        "ru.yandex.ubercab",
        "ru.yandex.uber",
        "ru.yandex.courier",

        // --- Yandex experimental / less visible
        "ru.yandex.food",
        "ru.yandex.shop",
        "ru.yandex.client",
        "ru.yandex.mobile.exp",

        // --- Sber ecosystem (скрытые / сервисные)
        "ru.sberbank.sdk",
        "ru.sberbank.platform",
        "ru.sberbank.id",
        "ru.sberbank.auth",
        "ru.sber.devices",
        "ru.sber.auto",
        "ru.sber.health",
        "ru.sber.education",

        // --- Ростелеком / гос-сервисы
        "ru.rt.video",
        "ru.rt.portal",
        "ru.rt.service",
        "ru.rt.client",
        "ru.rt.cloud",
        "ru.rt.tv",

        // --- Tele2
        "ru.tele2.market",
        "ru.tele2.pay",
        "ru.tele2.tv",

        // --- Mail / VK Tech / enterprise
        "ru.mail.workspace",
        "ru.mail.team",
        "ru.mail.video",
        "ru.mail.biz.video",
        "ru.mail.biz.chat",
        "ru.mail.biz.mail",
        "ru.mail.biz.calendar",

        // --- Rambler / media hidden apps
        "ru.rambler.video",
        "ru.rambler.weather",
        "ru.rambler.horoscope",
        "ru.rambler.news",
        "ru.rambler.mail",
        "ru.rambler.games",

        // --- Streaming / media (менее очевидные)
        "ru.more.tv",
        "ru.start.ru",
        "ru.premier.one",
        "ru.kion.android",
        "ru.wink.rt",

        // --- Gaming platforms
        "ru.vk.play",
        "ru.my.games",
        "com.my.gamestore",
        "com.my.games.platform",

        // --- Cloud / identity / SDK
        "ru.yandex.oauth",
        "ru.vk.oauth",
        "ru.sber.oauth",
        "ru.mail.oauth",
        "com.vk.auth",
        "com.yandex.auth",

        // --- Yandex (extra)
        "ru.yandex.browser.beta",
        "ru.yandex.music.beta",
        "ru.yandex.maps.beta",
        "ru.yandex.disk.beta",

        // --- VK ecosystem (extra / less obvious)
        "com.vk.admin",
        "com.vk.ads",
        "com.vk.team",

        // --- Sber ecosystem (missing pieces)
        "ru.sberbank.sberpay",
        "ru.sberbank.sbermarket",
        "ru.sberbank.spasibo",
        "ru.sberbank.onlineassistant",
        "ru.sberbank.telemed",
        "ru.sberdevices.salute",

        // --- Tinkoff ecosystem
        "ru.tinkoff.invest",
        "ru.tinkoff.business",
        "ru.tinkoff.accounting",

        // --- Alfa ecosystem
        "ru.alfabank.mobile.business",
        "ru.alfabank.alfainvest",

        // --- VTB ecosystem
        "ru.vtb.invest",
        "ru.vtb.business",

        // --- Other fintech / payments
        "ru.payberry.app",
        "ru.paymaster.app",
        "ru.robokassa.app",

        // --- Marketplaces / retail
        "ru.kazanexpress.android",
        "ru.sima.land",
        "ru.kupivip",
        "ru.hoff.app",
        "ru.petrovich.app",
        "ru.leroymerlin.android",
        "ru.obi.app",
        "ru.globus.app",
        "ru.metro.ccc",
        "ru.auchan.app",
        "ru.okmarket.app",

        // --- Food / delivery
        "ru.deliveryclub.app",
        "ru.pizzahut.android",
        "ru.dodo.pizza",
        "ru.tanuki.app",
        "ru.kfc.mobile",
        "ru.burgerking",
        "ru.vkusnoitochka.app",

        // --- Pharma / health
        "ru.eapteka.app",
        "ru.rigla.app",
        "ru.asna.app",
        "ru.medsi",
        "ru.invitro.app",
        "ru.gemotest.app",

        // --- Transport / navigation
        "ru.aeroexpress",
        "ru.mosgortrans",
        "ru.troika.app",
        "ru.rzd.cargo",
        "ru.blablacar",
        "ru.poputi.app",

        // --- Taxi / micromobility
        "com.citymobil",
        "ru.drivee",
        "ru.yandex.scooters",

        // --- Logistics / delivery
        "ru.dpdonline",
        "ru.dellin",
        "ru.baikalservice",
        "ru.pecom.app",

        // --- Media / streaming
        "ru.ntvplus.app",
        "ru.tricolor.app",
        "ru.tvzavr",
        "ru.amediateka",

        // --- Work / productivity / SaaS
        "ru.bitrix24.app",
        "ru.amocrm.app",
        "ru.megaplan.app",
        "ru.yclients.mobile",
        "ru.docsvision",
        "ru.elma.app",

        // --- Education
        "ru.skillbox.app",
        "ru.geekbrains.app",
        "ru.skyeng.student",
        "ru.skyeng.teacher",
        "ru.foxford.app",
        "ru.netology.app",

        // --- Job / HR
        "ru.hh.android",
        "ru.superjob.app",
        "ru.zarplata.app",
        "ru.youdo.app",

        // --- Travel / booking
        "ru.tutu",
        "ru.onetwotrip",
        "ru.travelata.app",
        "ru.sletat.app",
        "ru.ostrovok.app",

        // --- Insurance
        "ru.ingos.mobile",
        "ru.alfains.mobile",
        "ru.renins.app",
        "ru.sogaz.app",

        // --- Smart home / IoT
        "ru.yandex.iot",
        "ru.sber.smart.home",
        "ru.rostelecom.smarthome",

        // --- Government / city
        "ru.pfr.app",
        "ru.gibdd.app",
        "ru.mos.transport",
        "ru.mos.culture",
        "ru.mos.school",
        "ru.mos.dnevnik",

        // --- Cloud / storage
        "ru.cloud.mailru",
        "ru.selectel.mobile",
        "ru.reg.ru.app",

        // --- Security / antivirus
        "com.kaspersky.kasperskyinternetsecurity",
        "com.kaspersky.passwordmanager",
        "ru.drweb",
        "com.eset.mobile.security.russia",

        // --- App stores (RU)
        "ru.rustore.client",
        "com.nashstore",

        // --- Grocery / retail chains
        "ru.pyaterochka.app",
        "com.semerochka",
        "ru.x5.retailgroup.x5bonus",
        "com.avoska.app",
        "ru.chizhik.app",
        "ru.perekrestok.app",
        "ru.magnit.app",
        "ru.lenta.app",
        "ru.vprok.app",
        "ru.utkonos.app",
        "ru.samokat.app",
        "ru.sbermarket",
        "ru.torgservis.svetofor",

        // --- DIY / hardware
        "ru.maxidom.app",
        "ru.vseinstrumenti.app",
        "ru.castorama.app",

        // --- Banks (major, core)
        "ru.raiffeisen.mobile",
        "ru.rosbank.android",
        "ru.gazprombank.android.mobilebank",
        "ru.psbank.mobile",
        "ru.akbars.mobile",

        // --- Yandex ecosystem (core)
        "ru.yandex.searchplugin",
        "ru.yandex.browser",
        "ru.yandex.maps",
        "ru.yandex.metro",
        "ru.yandex.taxi",
        "ru.yandex.eda",
        "ru.yandex.lavka",

        // --- Social / Media (core)
        "com.vkontakte.android",
        "ru.ok.android",
        "ru.mail.mailapp",

        // --- Telecom (core)
        "ru.mts",
        "ru.megafon.mlk",
        "ru.beeline.services",
        "ru.tele2.mytele2",

        // --- Government / Classifieds
        "ru.gosuslugi.android",
        "ru.avito.android",

        // --- Additional popular (RKS Global VPN-detection list)
        "ru.sberbank.android",
        "ru.tinkoff.bank",
        "com.vk.video",
        "com.wildberries.ru",
        "ru.kinopoisk",
        "ru.ozon.app",
        "ru.dublgis.dgismobile",
        "com.vkontakte.music"
    )
}
