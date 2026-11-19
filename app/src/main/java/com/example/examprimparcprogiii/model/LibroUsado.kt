package com.example.examprimparcprogiii.model

/**
 * Un ejemplar físico de un libro usado de la tienda.
 * Encapsulamiento: la disponibilidad y las valoraciones son privadas;
 * se consultan y modifican solo con métodos que validan las reglas.
 * Asociación: cada libro está escrito por un [Autor].
 */
class LibroUsado(
    val id: String,
    val titulo: String,
    val autor: Autor,
    val genero: String,
    val anioEdicion: Int,
    val estado: EstadoConservacion
) {
    private var disponible: Boolean = true
    private val valoraciones: MutableList<Int> = mutableListOf()

    init {
        require(titulo.isNotBlank()) { "El libro debe tener título" }
        require(anioEdicion in 1450..2100) { "Año de edición inválido: $anioEdicion" }
    }

    fun estaDisponible(): Boolean = disponible

    /** Se llama al prestar el libro. Falla si ya estaba prestado. */
    fun marcarPrestado() {
        check(disponible) { "«$titulo» ya está prestado" }
        disponible = false
    }

    /** Se llama cuando el socio devuelve el libro. */
    fun marcarDevuelto() {
        disponible = true
    }

    /** Agrega una valoración de 1 a 5 estrellas. */
    fun valorar(estrellas: Int) {
        require(estrellas in 1..5) { "Las estrellas deben ir de 1 a 5" }
        valoraciones.add(estrellas)
    }

    /** Promedio de estrellas; 0.0 si nadie lo ha valorado todavía. */
    fun promedioEstrellas(): Double =
        if (valoraciones.isEmpty()) 0.0 else valoraciones.average()

    override fun toString(): String = "«$titulo» de ${autor.nombre} (${estado.etiqueta})"
}
