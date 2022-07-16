package com.rustamft.tasksft.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val DarkColorPalette = darkColors(
    primary = RedWisteria,
    primaryVariant = Sappanwood,
    secondary = DarkBlue,
    secondaryVariant = White,
    background = PitchBlack,
    onBackground = White,
    surface = TraditionalBlack,
    onSurface = IndigoWhite
)

private val LightColorPalette = lightColors(
    primary = Sappanwood,
    primaryVariant = Sappanwood,
    secondary = IndigoWhite,
    secondaryVariant = White,
    background = White,
    onBackground = PitchBlack,
    surface = IndigoWhite,
    onSurface = DarkBlue

    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

private var Colors = DarkColorPalette

object AppTheme {
    val colors get() = Colors
}

@Composable
fun AppTheme(
    darkTheme: Boolean?,
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme ?: isSystemInDarkTheme()) {
        DarkColorPalette
    } else {
        LightColorPalette
    }
    val systemUiController = rememberSystemUiController()

    systemUiController.setSystemBarsColor(color = colors.background)

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )

    Colors = colors
}
