package com.example.examprimparcprogiii.model

/**
 * Categoría del estado físico de un libro usado.
 * Cada estado lleva sus propias reglas: la multa diaria por retraso
 * y los créditos que recibe un socio si entrega un libro en trueque.
 */
enum class EstadoConservacion(
    val etiqueta: String,
    val multaDiaria: Double,
    val creditosTrueque: Int
) {
    COMO_NUEVO("Como nuevo", multaDiaria = 2.0, creditosTrueque = 30),
    BUENO("Bueno", multaDiaria = 1.5, creditosTrueque = 20),
    GASTADO("Gastado", multaDiaria = 1.0, creditosTrueque = 10)
}
