package io.itcorner.moodle.core.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val Brand = Color(0xFFF98012)
private val BrandDark = Color(0xFFB85F08)

private val LightScheme = lightColorScheme(
    primary = Brand,
    onPrimary = Color.White,
    secondary = BrandDark,
    onSecondary = Color.White,
)

private val DarkScheme = darkColorScheme(
    primary = Brand,
    onPrimary = Color.Black,
    secondary = BrandDark,
    onSecondary = Color.White,
)

@Composable
fun MoodleTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkScheme else LightScheme,
        content = content,
    )
}
