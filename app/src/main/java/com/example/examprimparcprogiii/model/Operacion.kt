package com.example.examprimparcprogiii.model

import java.time.LocalDate

/**
 * Clase abstracta con lo común de cualquier movimiento de un libro:
 * quién (socio), qué (libro) y cuándo (fecha).
 * Abstracción: no se puede crear una "operación" genérica, solo sus hijas
 * [Prestamo] y [Trueque] (herencia).
 * Polimorfismo: cada hija sobrescribe [descripcion] a su manera.
 */
abstract class Operacion(
    val id: String,
    val libro: LibroUsado,
    val socio: Socio,
    val fecha: LocalDate
) {
    /** Texto que resume la operación; cada tipo lo implementa distinto. */
    abstract fun descripcion(): String

    override fun toString(): String = descripcion()
}
