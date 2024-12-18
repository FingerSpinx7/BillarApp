package com.example.billarapp.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

// Colores personalizados para la aplicación
private val DarkColorScheme = darkColorScheme(
    primary = VerdePrimario,
    secondary = VerdeSecundario,
    background = FondoOscuro,
    surface = FondoTarjeta,
    onPrimary = TextoBlanco,
    onSecondary = TextoBlanco,
    onBackground = GrisClaro,
    onSurface = GrisClaro
)

private val LightColorScheme = lightColorScheme(
    primary = VerdePrimario,
    secondary = VerdeSecundario,
    background = TextoBlanco,
    surface = FondoTarjeta,
    onPrimary = FondoOscuro,
    onSecondary = FondoOscuro,
    onBackground = FondoOscuro,
    onSurface = FondoOscuro
)

@Composable
fun BillarAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
