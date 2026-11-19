package com.example.examprimparcprogiii.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.example.examprimparcprogiii.model.DatosPrueba

/**
 * Raíz de la app. Cambia entre Pantalla1 y Pantalla2 con un estado
 * (sin librería de navegación). Las dos pantallas comparten la misma
 * librería de datos y la misma barra inferior.
 */
@Composable
fun BuhoLectorApp() {
    val libreria = remember { DatosPrueba.crearLibreria() }
    var destino by rememberSaveable { mutableStateOf(Destino.CATALOGO) }

    val barraInferior: @Composable () -> Unit = {
        BarraNavegacion(
            actual = destino,
            onCambiar = { destino = it },
            prestamosConRetraso = libreria.prestamosConRetraso(DatosPrueba.HOY).size
        )
    }

    when (destino) {
        Destino.CATALOGO -> Pantalla1(libreria = libreria, barraInferior = barraInferior)
        Destino.SOCIOS -> Pantalla2(libreria = libreria, barraInferior = barraInferior)
    }
}
