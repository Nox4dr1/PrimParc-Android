package com.example.examprimparcprogiii.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

/** Esquema de colores "papel y café". Sin dynamic color y solo modo claro. */
private val PapelYCafe = lightColorScheme(
    primary = Cafe,
    onPrimary = Crema,
    primaryContainer = CafeClaro,
    onPrimaryContainer = CafeOscuro,
    secondary = Oliva,
    onSecondary = Crema,
    secondaryContainer = OlivaClaro,
    onSecondaryContainer = OlivaOscuro,
    tertiary = Ambar,
    onTertiary = Crema,
    tertiaryContainer = AmbarClaro,
    onTertiaryContainer = AmbarOscuro,
    background = Papel,
    onBackground = TintaCafe,
    surface = Crema,
    onSurface = TintaCafe,
    surfaceVariant = PapelVariante,
    onSurfaceVariant = TintaSuave,
    outline = Borde,
    outlineVariant = BordeSuave,
    surfaceContainerLowest = PapelMuyClaro,
    surfaceContainerLow = PapelBajo,
    surfaceContainer = PapelMedio,
    surfaceContainerHigh = PapelAlto,
    surfaceContainerHighest = PapelMuyAlto,
    error = Ladrillo,
    onError = Crema,
    errorContainer = LadrilloClaro,
    onErrorContainer = LadrilloOscuro
)

@Composable
fun ExamPrimParcProgIIITheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = PapelYCafe,
        typography = Typography,
        content = content
    )
}
