package com.example.examprimparcprogiii.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

/** Los dos destinos de la app. */
enum class Destino(val etiqueta: String, val icono: ImageVector) {
    CATALOGO("Catálogo", Icons.AutoMirrored.Filled.MenuBook),
    SOCIOS("Socios", Icons.Filled.People)
}

/**
 * Barra inferior compartida por las dos pantallas.
 * En "Socios" muestra un Badge con los préstamos que tienen retraso.
 */
@Composable
fun BarraNavegacion(
    actual: Destino,
    onCambiar: (Destino) -> Unit,
    prestamosConRetraso: Int
) {
    NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
        Destino.entries.forEach { destino ->
            NavigationBarItem(
                selected = destino == actual,
                onClick = { onCambiar(destino) },
                icon = {
                    if (destino == Destino.SOCIOS && prestamosConRetraso > 0) {
                        BadgedBox(badge = { Badge { Text("$prestamosConRetraso") } }) {
                            Icon(
                                imageVector = destino.icono,
                                contentDescription = "${destino.etiqueta}: $prestamosConRetraso préstamos con retraso"
                            )
                        }
                    } else {
                        Icon(imageVector = destino.icono, contentDescription = destino.etiqueta)
                    }
                },
                label = { Text(destino.etiqueta) },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = MaterialTheme.colorScheme.secondaryContainer,
                    selectedIconColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    selectedTextColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    }
}
