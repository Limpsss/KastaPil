package com.example.kastapil.viewModels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.kastapil.models.AppearanceManager

class SettingsViewModelFactory(private val appearanceManager: AppearanceManager) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = SettingsViewModel(appearanceManager) as T
}

class SettingsViewModel(private val appearanceManager: AppearanceManager): ViewModel() {
    val username = mutableStateOf("")
    var currLangCode = appearanceManager.currLangCode

    fun onUsernameChange(newString: String) {
        username.value = newString
    }

    fun changeLanguage(langCode: String) {
        appearanceManager.changeLanguage(langCode)
        currLangCode = langCode
    }

    fun changeTheme(isDarkMode: Boolean?) {
        appearanceManager.changeTheme(isDarkMode)
    }
}