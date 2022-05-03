package com.mabahmani.instasave.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val DarkColorPalette = darkColors(
    primary = BlueGrey900,
    secondary = BlueGrey200,
    primaryVariant = Grey50,
    secondaryVariant = Grey300,
    background = White

)

private val LightColorPalette = lightColors(
    primary = BlueGrey50,
    secondary = BlueGrey200,
    primaryVariant = Grey900,
    secondaryVariant = Grey300,
    background = Black
)

@Composable
fun InstaSaveTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}