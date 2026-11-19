package com.example.examprimparcprogiii.model

import java.time.LocalDate

/**
 * Trueque (hereda de [Operacion]): el socio entrega un libro usado a la tienda
 * y recibe créditos según el estado de conservación del libro.
 */
class Trueque(
    id: String,
    libro: LibroUsado,
    socio: Socio,
    fecha: LocalDate
) : Operacion(id, libro, socio, fecha) {

    val creditosOtorgados: Int = libro.estado.creditosTrueque

    override fun descripcion(): String =
        "Trueque: ${socio.nombre} entregó «${libro.titulo}» (+$creditosOtorgados créditos)"
}
