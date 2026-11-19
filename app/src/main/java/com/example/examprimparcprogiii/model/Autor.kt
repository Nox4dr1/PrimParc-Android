package com.example.examprimparcprogiii.model

/**
 * Autor de uno o varios libros.
 * Es una data class porque solo guarda datos que no cambian:
 * Kotlin genera automáticamente equals, hashCode, toString y copy.
 */
data class Autor(
    val id: String,
    val nombre: String,
    val nacionalidad: String
) {
    init {
        require(nombre.isNotBlank()) { "El autor debe tener nombre" }
    }
}
