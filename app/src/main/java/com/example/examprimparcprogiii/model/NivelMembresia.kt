package com.example.examprimparcprogiii.model

/**
 * Categoría de membresía de un socio.
 * Define cuántos libros puede tener prestados a la vez y por cuántos días.
 */
enum class NivelMembresia(
    val maxLibros: Int,
    val diasPrestamo: Int
) {
    BRONCE(maxLibros = 2, diasPrestamo = 7),
    PLATA(maxLibros = 3, diasPrestamo = 14),
    ORO(maxLibros = 5, diasPrestamo = 21)
}
