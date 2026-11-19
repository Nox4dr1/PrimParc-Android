package com.example.examprimparcprogiii.model

import java.time.LocalDate
import java.time.temporal.ChronoUnit

/**
 * Préstamo de un libro a un socio (hereda de [Operacion]).
 * La fecha límite depende del nivel del socio y la multa depende
 * del estado de conservación del libro.
 * Al crearse, el libro queda marcado como prestado.
 */
class Prestamo(
    id: String,
    libro: LibroUsado,
    socio: Socio,
    fecha: LocalDate
) : Operacion(id, libro, socio, fecha) {

    val fechaLimite: LocalDate = fecha.plusDays(socio.nivel.diasPrestamo.toLong())

    /** null mientras el libro no se haya devuelto. */
    private var fechaDevolucion: LocalDate? = null

    init {
        libro.marcarPrestado()
    }

    fun estaActivo(): Boolean = fechaDevolucion == null

    /** Días de retraso hasta hoy (o hasta la devolución, si ya se devolvió). */
    fun diasRetraso(hoy: LocalDate): Int {
        val fin = fechaDevolucion ?: hoy
        val dias = ChronoUnit.DAYS.between(fechaLimite, fin)
        return if (dias > 0) dias.toInt() else 0
    }

    /** Multa = días de retraso × multa diaria según el estado del libro. */
    fun calcularMulta(hoy: LocalDate): Double = diasRetraso(hoy) * libro.estado.multaDiaria

    /** Cierra el préstamo y deja el libro disponible otra vez. */
    fun registrarDevolucion(fecha: LocalDate) {
        check(estaActivo()) { "El préstamo $id ya fue devuelto" }
        require(!fecha.isBefore(this.fecha)) { "La devolución no puede ser antes del préstamo" }
        fechaDevolucion = fecha
        libro.marcarDevuelto()
    }

    override fun descripcion(): String {
        val estadoTexto = if (estaActivo()) "hasta $fechaLimite" else "devuelto el $fechaDevolucion"
        return "Préstamo de «${libro.titulo}» a ${socio.nombre} $estadoTexto"
    }
}
