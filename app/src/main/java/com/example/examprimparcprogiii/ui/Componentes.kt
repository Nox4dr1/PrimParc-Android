package com.example.examprimparcprogiii.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.examprimparcprogiii.model.BuhoLector
import com.example.examprimparcprogiii.model.NivelMembresia
import com.example.examprimparcprogiii.model.Prestamo
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

// Ayudantes compartidos por Pantalla1 y Pantalla2.

private val FORMATO_FECHA: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

/** 19/11/2026 */
fun LocalDate.aTexto(): String = format(FORMATO_FECHA)

/** 12,00 */
fun formatoMonto(valor: Double): String =
    String.format(Locale.forLanguageTag("es"), "%.2f", valor)

/** 4,7 (un decimal, redondeado) */
fun formatoDecimal(valor: Double): String =
    String.format(Locale.forLanguageTag("es"), "%.1f", valor)

/** ORO -> "Oro" */
fun NivelMembresia.nombreVisible(): String =
    name.lowercase().replaceFirstChar { it.uppercase() }

/** Préstamos todavía activos que ya pasaron su fecha límite. */
fun BuhoLector.prestamosConRetraso(hoy: LocalDate): List<Prestamo> =
    listarOperaciones()
        .filterIsInstance<Prestamo>()
        .filter { it.estaActivo() && it.diasRetraso(hoy) > 0 }

/** Círculo con las iniciales de una persona (primer y último nombre). */
@Composable
fun AvatarIniciales(
    nombre: String,
    modifier: Modifier = Modifier,
    tamano: Dp = 40.dp,
    estilo: TextStyle = MaterialTheme.typography.labelLarge
) {
    val palabras = nombre.split(" ").filter { it.isNotBlank() }
    val iniciales = when {
        palabras.isEmpty() -> "?"
        palabras.size == 1 -> palabras.first().take(1)
        else -> "${palabras.first().first()}${palabras.last().first()}"
    }.uppercase()

    Box(
        modifier = modifier
            .size(tamano)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.secondaryContainer),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = iniciales,
            style = estilo,
            color = MaterialTheme.colorScheme.onSecondaryContainer
        )
    }
}
