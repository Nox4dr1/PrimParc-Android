package com.example.examprimparcprogiii.model

/**
 * Lector inscrito en la librería.
 * Encapsulamiento: los créditos (ganados con trueques) son privados
 * y solo cambian mediante métodos que validan la cantidad.
 */
class Socio(
    val id: String,
    val nombre: String,
    val nivel: NivelMembresia,
    creditosIniciales: Int = 0
) {
    private var creditos: Int = creditosIniciales

    init {
        require(nombre.isNotBlank()) { "El socio debe tener nombre" }
        require(creditosIniciales >= 0) { "Los créditos no pueden ser negativos" }
    }

    /** Devuelve los créditos actuales (solo lectura). */
    fun verCreditos(): Int = creditos

    /** Suma créditos, por ejemplo al entregar un libro en trueque. */
    fun sumarCreditos(cantidad: Int) {
        require(cantidad > 0) { "La cantidad debe ser positiva" }
        creditos += cantidad
    }

    /** Intenta gastar créditos. Devuelve false si no le alcanzan. */
    fun usarCreditos(cantidad: Int): Boolean {
        if (cantidad <= 0 || cantidad > creditos) return false
        creditos -= cantidad
        return true
    }

    /** Indica si todavía puede pedir otro libro según su nivel. */
    fun puedePedirMas(prestamosActivos: Int): Boolean = prestamosActivos < nivel.maxLibros

    override fun toString(): String = "$nombre (${nivel.name}, $creditos créditos)"
}
