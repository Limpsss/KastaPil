package com.example.kastapil.models

class AppearanceManager(langCode: String) {
    var onLanguageChanged: ((String) -> Unit)? = null
    var onThemeChanged: ((Boolean?) -> Unit)? = null
    val currLangCode = langCode

    fun changeLanguage(langCode: String) {
        onLanguageChanged?.invoke(langCode)
    }

    fun changeTheme(isDarkMode: Boolean?) {
        onThemeChanged?.invoke(isDarkMode)
    }
}