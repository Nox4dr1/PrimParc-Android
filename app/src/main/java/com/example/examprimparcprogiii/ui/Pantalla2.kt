@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.examprimparcprogiii.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Badge
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.examprimparcprogiii.model.BuhoLector
import com.example.examprimparcprogiii.model.DatosPrueba
import com.example.examprimparcprogiii.model.NivelMembresia
import com.example.examprimparcprogiii.model.Operacion
import com.example.examprimparcprogiii.model.Prestamo
import com.example.examprimparcprogiii.model.Socio
import com.example.examprimparcprogiii.model.Trueque
import com.example.examprimparcprogiii.ui.theme.ExamPrimParcProgIIITheme
import java.time.LocalDate
import java.time.temporal.ChronoUnit

/**
 * Pantalla 2 — Socios.
 * Pestaña "Socios": lista objetos Socio con sus Prestamo activos.
 * Pestaña "Movimientos": lista todas las Operacion (Prestamo y Trueque)
 * usando descripcion(), así se ve el polimorfismo en la interfaz.
 */
@Composable
fun Pantalla2(
    libreria: BuhoLector,
    modifier: Modifier = Modifier,
    hoy: LocalDate = DatosPrueba.HOY,
    barraInferior: @Composable () -> Unit = {}
) {
    var pestana by rememberSaveable { mutableIntStateOf(0) }
    val socios = libreria.listarSocios()
    val operaciones = libreria.listarOperaciones()

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                navigationIcon = {
                    Icon(
                        imageVector = Icons.Filled.Groups,
                        contentDescription = "Socios de El Búho Lector",
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )
                },
                title = { Text("Socios y préstamos", style = MaterialTheme.typography.titleLarge) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        bottomBar = barraInferior,
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            PrimaryTabRow(
                selectedTabIndex = pestana,
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                Tab(
                    selected = pestana == 0,
                    onClick = { pestana = 0 },
                    text = { Text("Socios (${socios.size})") }
                )
                Tab(
                    selected = pestana == 1,
                    onClick = { pestana = 1 },
                    text = { Text("Movimientos (${operaciones.size})") }
                )
            }
            when (pestana) {
                0 -> ListaSocios(libreria, socios, hoy)
                else -> ListaMovimientos(operaciones)
            }
        }
    }
}

// ---------------- Pestaña 1: socios y préstamos activos ----------------

@Composable
private fun ListaSocios(libreria: BuhoLector, socios: List<Socio>, hoy: LocalDate) {
    var nivel by rememberSaveable { mutableStateOf<NivelMembresia?>(null) }
    val activos = libreria.listarOperaciones().filterIsInstance<Prestamo>().filter { it.estaActivo() }
    val conRetraso = libreria.prestamosConRetraso(hoy)
    val totalMultas = conRetraso.sumOf { it.calcularMulta(hoy) }
    val visibles = socios.filter { nivel == null || it.nivel == nivel }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Resumen con 3 tarjetas
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                TarjetaResumen("${activos.size}", "activos", MaterialTheme.colorScheme.secondary, Modifier.weight(1f))
                TarjetaResumen("${conRetraso.size}", "con retraso", MaterialTheme.colorScheme.error, Modifier.weight(1f))
                TarjetaResumen(formatoMonto(totalMultas), "en multas", MaterialTheme.colorScheme.error, Modifier.weight(1f))
            }
        }

        // Filtro por nivel de membresía (enum)
        item {
            val opciones: List<NivelMembresia?> = listOf(null) + NivelMembresia.entries
            SingleChoiceSegmentedButtonRow(Modifier.fillMaxWidth()) {
                opciones.forEachIndexed { indice, opcion ->
                    SegmentedButton(
                        selected = nivel == opcion,
                        onClick = { nivel = opcion },
                        shape = SegmentedButtonDefaults.itemShape(indice, opciones.size),
                        icon = {},
                        colors = SegmentedButtonDefaults.colors(
                            activeContainerColor = MaterialTheme.colorScheme.secondaryContainer
                        ),
                        label = { Text(opcion?.nombreVisible() ?: "Todos", maxLines = 1) }
                    )
                }
            }
        }

        if (visibles.isEmpty()) {
            item {
                Text(
                    "No hay socios con ese nivel.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(vertical = 24.dp)
                )
            }
        } else {
            items(visibles, key = { it.id }) { socio ->
                TarjetaSocio(socio, libreria.prestamosActivosDe(socio), hoy)
            }
        }
    }
}

@Composable
private fun TarjetaResumen(valor: String, etiqueta: String, color: Color, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(valor, style = MaterialTheme.typography.headlineSmall, color = color, maxLines = 1)
            Text(etiqueta, style = MaterialTheme.typography.labelMedium, textAlign = TextAlign.Center)
        }
    }
}

/** Tarjeta de un Socio con la lista de sus préstamos activos. */
@Composable
private fun TarjetaSocio(socio: Socio, prestamos: List<Prestamo>, hoy: LocalDate) {
    val tieneRetraso = prestamos.any { it.diasRetraso(hoy) > 0 }
    OutlinedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.outlinedCardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.outlinedCardElevation(defaultElevation = 1.dp)
    ) {
        ListItem(
            leadingContent = { AvatarIniciales(socio.nombre) },
            headlineContent = { Text(socio.nombre, style = MaterialTheme.typography.titleMedium) },
            supportingContent = {
                Text("${socio.nivel.nombreVisible()} · ${socio.verCreditos()} créditos")
            },
            trailingContent = {
                if (prestamos.isNotEmpty()) {
                    Badge(
                        containerColor = if (tieneRetraso) MaterialTheme.colorScheme.error
                        else MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.onSecondary,
                        modifier = Modifier.semantics {
                            contentDescription = "${prestamos.size} préstamos activos"
                        }
                    ) { Text("${prestamos.size}") }
                }
            },
            colors = ListItemDefaults.colors(containerColor = Color.Transparent)
        )

        if (prestamos.isEmpty()) {
            Text(
                "Sin préstamos activos",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
            )
        } else {
            prestamos.forEach { prestamo ->
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    color = MaterialTheme.colorScheme.outlineVariant
                )
                FilaPrestamo(prestamo, hoy)
            }
        }
    }
}

/** Un Prestamo: fecha límite, barra de tiempo usado y multa si hay retraso. */
@Composable
private fun FilaPrestamo(prestamo: Prestamo, hoy: LocalDate) {
    val retraso = prestamo.diasRetraso(hoy)
    val multa = prestamo.calcularMulta(hoy)
    val diasTotales = ChronoUnit.DAYS.between(prestamo.fecha, prestamo.fechaLimite).coerceAtLeast(1)
    val diasUsados = ChronoUnit.DAYS.between(prestamo.fecha, hoy)
    val progreso = (diasUsados.toFloat() / diasTotales).coerceIn(0f, 1f)
    val faltan = ChronoUnit.DAYS.between(hoy, prestamo.fechaLimite)
    val colorEstado = if (retraso > 0) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.secondary

    val textoPlazo = when {
        retraso > 0 -> "Venció el ${prestamo.fechaLimite.aTexto()} · $retraso días tarde"
        faltan == 0L -> "Vence hoy"
        faltan == 1L -> "Vence mañana · falta 1 día"
        else -> "Vence el ${prestamo.fechaLimite.aTexto()} · faltan $faltan días"
    }

    ListItem(
        leadingContent = {
            Icon(
                Icons.AutoMirrored.Filled.MenuBook,
                contentDescription = if (retraso > 0) "Libro con retraso" else "Libro prestado",
                tint = colorEstado
            )
        },
        headlineContent = {
            Text(prestamo.libro.titulo, maxLines = 1, overflow = TextOverflow.Ellipsis)
        },
        supportingContent = {
            Column {
                Text(textoPlazo, color = if (retraso > 0) colorEstado else Color.Unspecified)
                Spacer(Modifier.height(6.dp))
                LinearProgressIndicator(
                    progress = { progreso },
                    modifier = Modifier.fillMaxWidth(),
                    color = colorEstado,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant
                )
                if (multa > 0) {
                    Spacer(Modifier.height(6.dp))
                    AssistChip(
                        onClick = {},
                        label = { Text("Multa ${formatoMonto(multa)}") },
                        leadingIcon = {
                            Icon(Icons.Filled.Warning, contentDescription = "Multa por retraso")
                        },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer,
                            labelColor = MaterialTheme.colorScheme.onErrorContainer,
                            leadingIconContentColor = MaterialTheme.colorScheme.error
                        ),
                        border = null
                    )
                }
            }
        },
        colors = ListItemDefaults.colors(containerColor = Color.Transparent)
    )
}

// ---------------- Pestaña 2: movimientos (polimorfismo) ----------------

@Composable
private fun ListaMovimientos(operaciones: List<Operacion>) {
    val ordenadas = operaciones.sortedWith(compareByDescending<Operacion> { it.fecha }.thenByDescending { it.id })
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        items(ordenadas, key = { it.id }) { operacion ->
            // `when` con `is` decide el ícono según el tipo real del objeto
            val (icono, tipo, color) = when (operacion) {
                is Prestamo -> Triple(Icons.AutoMirrored.Filled.MenuBook, "Préstamo", MaterialTheme.colorScheme.primary)
                is Trueque -> Triple(Icons.Filled.SwapHoriz, "Trueque", MaterialTheme.colorScheme.secondary)
                else -> Triple(Icons.Filled.SwapHoriz, "Operación", MaterialTheme.colorScheme.outline)
            }
            ListItem(
                overlineContent = { Text("${operacion.id} · ${operacion.fecha.aTexto()}") },
                headlineContent = { Text(tipo) },
                // descripcion() es abstracto en Operacion: cada hija lo responde a su manera
                supportingContent = { Text(operacion.descripcion()) },
                leadingContent = { Icon(icono, contentDescription = tipo, tint = color) },
                colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.background)
            )
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp),
                color = MaterialTheme.colorScheme.outlineVariant
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun Pantalla2Preview() {
    ExamPrimParcProgIIITheme {
        val libreria = remember { DatosPrueba.crearLibreria() }
        Pantalla2(
            libreria = libreria,
            barraInferior = {
                BarraNavegacion(
                    actual = Destino.SOCIOS,
                    onCambiar = {},
                    prestamosConRetraso = libreria.prestamosConRetraso(DatosPrueba.HOY).size
                )
            }
        )
    }
}
