package com.example.myapplication

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import java.util.Locale

object LocaleHelper {

    fun setLocale(context: Context, languageCode: String) {
        val tag = when (languageCode.lowercase()) {
            "ingles", "en" -> "en"
            "espanhol", "es" -> "es"
            else -> "pt-BR"
        }

        val locale = when (tag) {
            "en" -> Locale("en")
            "es" -> Locale("es")
            else -> Locale("pt", "BR")
        }

        Locale.setDefault(locale)

        val resources: Resources = context.resources
        val config = Configuration(resources.configuration)
        config.setLocale(locale)

        @Suppress("DEPRECATION")
        resources.updateConfiguration(config, resources.displayMetrics)

        context.getSharedPreferences("configuracoes_unifit", Context.MODE_PRIVATE)
            .edit()
            .putString("idioma", languageCode)
            .apply()

        // Apply native AppCompat locales
        val localeList = LocaleListCompat.forLanguageTags(tag)
        AppCompatDelegate.setApplicationLocales(localeList)
    }

    fun applySavedLocale(context: Context) {
        val prefs = context.getSharedPreferences("configuracoes_unifit", Context.MODE_PRIVATE)
        val idioma = prefs.getString("idioma", "portugues") ?: "portugues"
        setLocale(context, idioma)
    }
}
