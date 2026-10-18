package com.example.kastapil.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val darkColorScheme = darkColorScheme(
    primaryContainer = domd_theme_dark_primaryContainer,
    onPrimaryContainer = domd_theme_dark_onPrimaryContainer,
    background = domd_theme_dark_background,
    secondaryContainer = domd_theme_dark_secondaryContainer,
)

private val lightColorScheme = lightColorScheme(
    primaryContainer = domd_theme_light_primaryContainer,
    onPrimaryContainer = domd_theme_light_onPrimaryContainer,
    background = domd_theme_light_background,
    secondaryContainer = domd_theme_light_secondaryContainer,
)

@Composable
fun KastaPilTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
//    val colorScheme = when {
//        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
//            val context = LocalContext.current
//            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
//        }
//
//        darkTheme -> DarkColorScheme
//        else -> LightColorScheme
//    }
//    val view = LocalView.current
//    if (!view.isInEditMode) {
//        SideEffect {
//            val window = (view.context as Activity).window
//            window.statusBarColor = colorScheme.primary.toArgb()
//            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
//        }
//    }

    val colorScheme = if (!darkTheme) {
        lightColorScheme
    } else {
        darkColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}