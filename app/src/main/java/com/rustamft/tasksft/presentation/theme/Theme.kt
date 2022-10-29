package com.rustamft.tasksft.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.rustamft.tasksft.domain.model.Preferences.Theme

private val DarkColorPalette = darkColors(
    primary = RedWisteria,
    primaryVariant = Sappanwood,
    secondary = DarkBlue,
    secondaryVariant = White,
    background = TraditionalBlack,
    onBackground = White,
    surface = PitchBlack,
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
)

/* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
*/

private var Colors = DarkColorPalette

object AppTheme {
    val colors get() = Colors
    val isDark get() = Colors == DarkColorPalette
    val taskColors = listOf(
        PureCrimson,
        Corn,
        Patina,
        LapisLazuli,
        WisteriaPurple
    )
}

@Composable
fun AppTheme(
    theme: Theme,
    content: @Composable () -> Unit
) {
    Colors = when (theme) {
        is Theme.Auto -> if (isSystemInDarkTheme()) DarkColorPalette else LightColorPalette
        is Theme.Light -> LightColorPalette
        is Theme.Dark -> DarkColorPalette
    }

    val systemUiController = rememberSystemUiController()

    systemUiController.setSystemBarsColor(color = Colors.background)

    MaterialTheme(
        colors = Colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
