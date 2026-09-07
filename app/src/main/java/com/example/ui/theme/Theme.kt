package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme =
  darkColorScheme(
    primary = KhadamatiNeonCyan,
    onPrimary = Color(0xFF001F2B),
    primaryContainer = Color(0xFF00384D),
    onPrimaryContainer = Color(0xFFBBE9FF),
    secondary = KhadamatiSecondaryTeal,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFF004D40),
    onSecondaryContainer = KhadamatiSecondaryContainer,
    tertiary = KhadamatiAmberTertiary,
    background = KhadamatiSurfaceDark,
    surface = KhadamatiCardDark,
    surfaceVariant = Color(0xFF1E293B),
    onSurface = Color(0xFFF8FAFC),
    onSurfaceVariant = Color(0xFFCBD5E1),
    outline = KhadamatiOutlineDark
  )

private val LightColorScheme =
  lightColorScheme(
    primary = KhadamatiBluePrimary,
    onPrimary = Color.White,
    primaryContainer = KhadamatiBlueContainer,
    onPrimaryContainer = KhadamatiOnBlueContainer,
    secondary = KhadamatiSecondaryTeal,
    onSecondary = Color.White,
    secondaryContainer = KhadamatiSecondaryContainer,
    onSecondaryContainer = KhadamatiOnSecondaryContainer,
    tertiary = KhadamatiAmberTertiary,
    background = KhadamatiSurfaceLight,
    surface = KhadamatiCardLight,
    surfaceVariant = Color(0xFFF1F5F9),
    onSurface = Color(0xFF0F172A),
    onSurfaceVariant = Color(0xFF475569),
    outline = KhadamatiOutlineLight
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  // Disable dynamic color so custom rich brand styling & glowing neon accents remain consistent
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  val colorScheme =
    when {
      dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
      }

      darkTheme -> DarkColorScheme
      else -> LightColorScheme
    }

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
