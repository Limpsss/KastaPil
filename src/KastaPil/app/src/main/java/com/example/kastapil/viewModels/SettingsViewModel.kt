package com.example.kastapil.viewModels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class SettingsViewModel: ViewModel() {
    val username = mutableStateOf("")
    val englishSelected = mutableStateOf(true)
    val swedishSelected = mutableStateOf(false)

    fun onUsernameChange(newString: String) {
        username.value = newString
    }

    fun selectLanguage(lang: String) {
        when (lang) {
            "ENG" -> {
                englishSelected.value = true
                swedishSelected.value = false
            }
            "SWE" -> {
                englishSelected.value = false
                swedishSelected.value = true
            }
        }
    }
}