package com.example.kastapil

import android.app.UiModeManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.runtime.rememberCoroutineScope
import androidx.core.os.LocaleListCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.kastapil.models.GoogleAuthClient
import com.example.kastapil.models.AppearanceManager
import com.example.kastapil.navigation.SetUpNavHost
import com.example.kastapil.ui.theme.KastaPilTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.android.gms.auth.api.identity.Identity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth


class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val googleAuthClient = GoogleAuthClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )

        // get curr lang on app system
        val currLangCode = AppCompatDelegate.getApplicationLocales().toLanguageTags()
        val appearanceManager = AppearanceManager(currLangCode)

        // Subscribe to event to change the apps language
        appearanceManager.onLanguageChanged = { langCode ->
            AppCompatDelegate.setApplicationLocales(
                LocaleListCompat.forLanguageTags(langCode))
        }

        // Subscribe to event to change apps theme
        appearanceManager.onThemeChanged = { isDarkMode ->
            val mode: Int
            if (isDarkMode == true) {
                mode = AppCompatDelegate.MODE_NIGHT_YES
            } else if (isDarkMode == false) {
                mode = AppCompatDelegate.MODE_NIGHT_NO
            } else {
                mode = getSystemDefaultTheme(applicationContext)
            }
            AppCompatDelegate.setDefaultNightMode(mode)
        }

        setContent {
            KastaPilTheme {
                SetStatusBarColor(color = Color.Black)

                navController = rememberNavController()
                SetUpNavHost(navController, googleAuthClient, appearanceManager)
            }
        }
    }
}

fun getSystemDefaultTheme(context: Context): Int {
    val uiModeManager = context.getSystemService(Context.UI_MODE_SERVICE) as UiModeManager
    return when (uiModeManager.nightMode) {
        UiModeManager.MODE_NIGHT_YES -> AppCompatDelegate.MODE_NIGHT_YES
        UiModeManager.MODE_NIGHT_NO -> AppCompatDelegate.MODE_NIGHT_NO
        UiModeManager.MODE_NIGHT_AUTO -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        else -> AppCompatDelegate.MODE_NIGHT_UNSPECIFIED
    }
}

@Composable
fun SetStatusBarColor(color: Color) {
    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setSystemBarsColor(color)
    }
}

