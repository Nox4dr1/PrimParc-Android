@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.examprimparcprogiii.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.StarHalf
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterAltOff
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.AssistChip
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.examprimparcprogiii.model.BuhoLector
import com.example.examprimparcprogiii.model.DatosPrueba
import com.example.examprimparcprogiii.model.EstadoConservacion
import com.example.examprimparcprogiii.model.LibroUsado
import com.example.examprimparcprogiii.model.Trueque
import com.example.examprimparcprogiii.ui.theme.ExamPrimParcProgIIITheme

/** Opciones de la fila de FilterChip del catálogo. */
private enum class FiltroCatalogo(val etiqueta: String, val estado: EstadoConservacion?) {
    TODOS("Todos", null),
    DISPONIBLES("Disponibles", null),
    COMO_NUEVO(EstadoConservacion.COMO_NUEVO.etiqueta, EstadoConservacion.COMO_NUEVO),
    BUENO(EstadoConservacion.BUENO.etiqueta, EstadoConservacion.BUENO),
    GASTADO(EstadoConservacion.GASTADO.etiqueta, EstadoConservacion.GASTADO)
}

/**
 * Pantalla 1 — Catálogo.
 * Lista objetos LibroUsado (tarjetas), Autor (chips) y Trueque (hoja inferior).
 */
@Composable
fun Pantalla1(
    libreria: BuhoLector,
    modifier: Modifier = Modifier,
    barraInferior: @Composable () -> Unit = {}
) {
    // Estado de la pantalla (sobrevive a rotaciones gracias a rememberSaveable)
    var texto by rememberSaveable { mutableStateOf("") }
    var filtro by rememberSaveable { mutableStateOf(FiltroCatalogo.TODOS) }
    var autorId by rememberSaveable { mutableStateOf<String?>(null) }
    var mostrarTrueques by rememberSaveable { mutableStateOf(false) }

    val autores = libreria.listarAutores()
    val autorSeleccionado = autores.find { it.id == autorId }

    // Combina búsqueda + filtro + autor usando los métodos de la gestora
    val libros = run {
        val porTitulo = libreria.buscarLibroPorTitulo(texto)
        val estado = filtro.estado
        val porFiltro = when {
            filtro == FiltroCatalogo.DISPONIBLES -> libreria.filtrarDisponibles()
            estado != null -> libreria.filtrarPorEstado(estado)
            else -> libreria.listarLibros()
        }
        val porAutor = autorSeleccionado?.let { libreria.filtrarPorAutor(it) }
        porTitulo.filter { it in porFiltro && (porAutor == null || it in porAutor) }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                navigationIcon = {
                    Icon(
                        imageVector = Icons.Filled.AutoStories,
                        contentDescription = "Logo de El Búho Lector",
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )
                },
                title = {
                    Column {
                        Text("El Búho Lector", style = MaterialTheme.typography.titleLarge)
                        Text(
                            "Libros usados · ${libreria.listarLibros().size} ejemplares",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        texto = ""
                        filtro = FiltroCatalogo.TODOS
                        autorId = null
                    }) {
                        Icon(Icons.Filled.FilterAltOff, contentDescription = "Quitar filtros")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        bottomBar = barraInferior,
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { mostrarTrueques = true },
                icon = { Icon(Icons.Filled.SwapHoriz, contentDescription = "Ver trueques") },
                text = { Text("Trueques") }
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 88.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Búsqueda
            item {
                OutlinedTextField(
                    value = texto,
                    onValueChange = { texto = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Buscar por título") },
                    singleLine = true,
                    leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "Buscar") },
                    trailingIcon = {
                        if (texto.isNotEmpty()) {
                            IconButton(onClick = { texto = "" }) {
                                Icon(Icons.Filled.Clear, contentDescription = "Borrar búsqueda")
                            }
                        }
                    }
                )
            }

            // Filtros por disponibilidad y estado
            item {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(FiltroCatalogo.entries) { opcion ->
                        FilterChip(
                            selected = filtro == opcion,
                            onClick = { filtro = opcion },
                            label = { Text(opcion.etiqueta) },
                            leadingIcon = if (filtro == opcion) {
                                { Icon(Icons.Filled.Check, contentDescription = "Seleccionado") }
                            } else null
                        )
                    }
                }
            }

            // Autores (objetos Autor)
            item {
                Text(
                    "Autores",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.height(4.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(autores, key = { it.id }) { autor ->
                        InputChip(
                            selected = autor.id == autorId,
                            onClick = { autorId = if (autorId == autor.id) null else autor.id },
                            label = { Text(autor.nombre) },
                            avatar = {
                                AvatarIniciales(
                                    nombre = autor.nombre,
                                    tamano = InputChipDefaults.AvatarSize,
                                    estilo = MaterialTheme.typography.labelSmall
                                )
                            }
                        )
                    }
                }
            }

            // Libros (objetos LibroUsado)
            if (libros.isEmpty()) {
                item {
                    Text(
                        "No hay libros con esos filtros.",
                        modifier = Modifier.padding(vertical = 24.dp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                items(libros, key = { it.id }) { libro ->
                    TarjetaLibro(libro)
                }
            }
        }

        if (mostrarTrueques) {
            HojaTrueques(
                trueques = libreria.listarOperaciones()
                    .filterIsInstance<Trueque>()
                    .sortedByDescending { it.fecha },
                onCerrar = { mostrarTrueques = false }
            )
        }
    }
}

/** Tarjeta de un LibroUsado: borde fino + sombra suave (estilo mixto). */
@Composable
private fun TarjetaLibro(libro: LibroUsado) {
    val forma = MaterialTheme.shapes.medium
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, forma),
        shape = forma,
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 1.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(libro.titulo, style = MaterialTheme.typography.titleMedium)
            Text(
                libro.autor.nombre,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                "${libro.genero} · edición ${libro.anioEdicion}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(8.dp))
            Estrellas(promedio = libro.promedioEstrellas())
            Spacer(Modifier.height(4.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                AssistChip(
                    onClick = {},
                    label = { Text(libro.estado.etiqueta) }
                )
                val disponible = libro.estaDisponible()
                SuggestionChip(
                    onClick = {},
                    label = { Text(if (disponible) "Disponible" else "Prestado") },
                    colors = SuggestionChipDefaults.suggestionChipColors(
                        containerColor = if (disponible) MaterialTheme.colorScheme.secondaryContainer
                        else MaterialTheme.colorScheme.surfaceVariant,
                        labelColor = if (disponible) MaterialTheme.colorScheme.onSecondaryContainer
                        else MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    border = null
                )
            }
        }
    }
}

/** Fila de 5 estrellas (llena, media o vacía) + promedio numérico. */
@Composable
private fun Estrellas(promedio: Double) {
    val resumen = if (promedio == 0.0) "Sin valoraciones"
    else "Valoración ${formatoDecimal(promedio)} de 5"

    Row(
        verticalAlignment = Alignment.CenterVertically,
        // TalkBack lee una sola frase en vez de 5 íconos sueltos
        modifier = Modifier.clearAndSetSemantics { contentDescription = resumen }
    ) {
        if (promedio == 0.0) {
            Text(resumen, style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
            return@Row
        }
        repeat(5) { i ->
            val (icono, descripcion) = when {
                promedio >= i + 0.75 -> Icons.Filled.Star to "Estrella llena"
                promedio >= i + 0.25 -> Icons.AutoMirrored.Filled.StarHalf to "Media estrella"
                else -> Icons.Filled.StarBorder to "Estrella vacía"
            }
            Icon(
                imageVector = icono,
                contentDescription = descripcion,
                tint = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier.size(18.dp)
            )
        }
        Spacer(Modifier.width(6.dp))
        Text(formatoDecimal(promedio), style = MaterialTheme.typography.labelLarge)
    }
}

/** Hoja inferior con la lista de objetos Trueque. */
@Composable
private fun HojaTrueques(trueques: List<Trueque>, onCerrar: () -> Unit) {
    ModalBottomSheet(
        onDismissRequest = onCerrar,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Text(
            "Últimos trueques",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
        LazyColumn(contentPadding = PaddingValues(bottom = 24.dp)) {
            items(trueques, key = { it.id }) { trueque ->
                ListItem(
                    leadingContent = {
                        Icon(
                            Icons.Filled.SwapHoriz,
                            contentDescription = "Trueque",
                            tint = MaterialTheme.colorScheme.secondary
                        )
                    },
                    headlineContent = { Text(trueque.socio.nombre) },
                    supportingContent = {
                        Text(
                            "${trueque.libro.titulo} · ${trueque.libro.estado.etiqueta} · ${trueque.fecha.aTexto()}",
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    trailingContent = {
                        Text(
                            "+${trueque.creditosOtorgados}",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.tertiary
                        )
                    },
                    colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.surface)
                )
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun Pantalla1Preview() {
    ExamPrimParcProgIIITheme {
        val libreria = remember { DatosPrueba.crearLibreria() }
        Pantalla1(
            libreria = libreria,
            barraInferior = {
                BarraNavegacion(
                    actual = Destino.CATALOGO,
                    onCambiar = {},
                    prestamosConRetraso = libreria.prestamosConRetraso(DatosPrueba.HOY).size
                )
            }
        )
    }
}
