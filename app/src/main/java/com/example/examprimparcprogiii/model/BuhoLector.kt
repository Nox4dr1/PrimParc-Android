package com.example.examprimparcprogiii.model

import java.time.LocalDate
import kotlin.math.ceil

/**
 * Clase gestora de la librería "El Búho Lector".
 * Encapsulamiento: las listas son privadas; desde fuera solo se obtienen
 * copias de solo lectura y se modifican con métodos que validan las reglas.
 * Composición: es dueña de libros, socios y operaciones.
 * Agregación: también guarda los autores, que existen por sí mismos.
 */
class BuhoLector {

    private val libros: MutableList<LibroUsado> = mutableListOf()
    private val socios: MutableList<Socio> = mutableListOf()
    private val autores: MutableList<Autor> = mutableListOf()
    private val operaciones: MutableList<Operacion> = mutableListOf()

    // ---------- Agregar ----------

    /** Agrega un libro al catálogo y registra a su autor si aún no está en la lista. */
    fun agregarLibro(libro: LibroUsado) {
        require(libros.none { it.id == libro.id }) { "Ya existe un libro con id ${libro.id}" }
        agregarAutor(libro.autor)
        libros.add(libro)
    }

    fun agregarSocio(socio: Socio) {
        require(socios.none { it.id == socio.id }) { "Ya existe un socio con id ${socio.id}" }
        socios.add(socio)
    }

    /** Agrega un autor; si ya existe, no lo duplica. */
    fun agregarAutor(autor: Autor) {
        if (autores.none { it.id == autor.id }) autores.add(autor)
    }

    // ---------- Listar (copias de solo lectura) ----------

    fun listarLibros(): List<LibroUsado> = libros.toList()
    fun listarSocios(): List<Socio> = socios.toList()
    fun listarAutores(): List<Autor> = autores.toList()
    fun listarOperaciones(): List<Operacion> = operaciones.toList()

    // ---------- Buscar ----------

    fun buscarLibroPorTitulo(texto: String): List<LibroUsado> =
        libros.filter { it.titulo.contains(texto.trim(), ignoreCase = true) }

    fun buscarSocioPorId(id: String): Socio? = socios.find { it.id == id }

    // ---------- Filtrar ----------

    fun filtrarDisponibles(): List<LibroUsado> = libros.filter { it.estaDisponible() }

    fun filtrarPorEstado(estado: EstadoConservacion): List<LibroUsado> =
        libros.filter { it.estado == estado }

    fun filtrarPorAutor(autor: Autor): List<LibroUsado> =
        libros.filter { it.autor == autor }

    fun prestamosActivosDe(socio: Socio): List<Prestamo> =
        operaciones.filterIsInstance<Prestamo>().filter { it.socio.id == socio.id && it.estaActivo() }

    // ---------- Operaciones ----------

    /**
     * Presta un libro si existe, está disponible y el socio no llegó a su límite.
     * Devuelve null si no se puede prestar.
     */
    fun prestarLibro(libroId: String, socioId: String, hoy: LocalDate): Prestamo? {
        val libro = libros.find { it.id == libroId } ?: return null
        val socio = buscarSocioPorId(socioId) ?: return null
        if (!libro.estaDisponible()) return null
        if (!socio.puedePedirMas(prestamosActivosDe(socio).size)) return null

        val prestamo = Prestamo(nuevoId("P"), libro, socio, hoy)
        operaciones.add(prestamo)
        return prestamo
    }

    /**
     * Registra la devolución y devuelve la multa pendiente.
     * Si el socio tiene créditos suficientes, la multa se paga con ellos
     * (1 crédito = 1 unidad de multa, redondeando hacia arriba) y se devuelve 0.0.
     */
    fun devolverLibro(prestamoId: String, hoy: LocalDate): Double {
        val prestamo = requireNotNull(
            operaciones.filterIsInstance<Prestamo>().find { it.id == prestamoId && it.estaActivo() }
        ) { "No existe un préstamo activo con id $prestamoId" }

        prestamo.registrarDevolucion(hoy)
        val multa = prestamo.calcularMulta(hoy)
        if (multa > 0 && prestamo.socio.usarCreditos(ceil(multa).toInt())) return 0.0
        return multa
    }

    /**
     * El socio entrega un libro nuevo para la tienda: entra al catálogo
     * y el socio recibe créditos según el estado del libro.
     * Devuelve null si el socio no existe o el libro ya estaba en el catálogo.
     */
    fun registrarTrueque(socioId: String, libro: LibroUsado, hoy: LocalDate): Trueque? {
        val socio = buscarSocioPorId(socioId) ?: return null
        if (libros.any { it.id == libro.id }) return null

        agregarLibro(libro)
        val trueque = Trueque(nuevoId("T"), libro, socio, hoy)
        socio.sumarCreditos(trueque.creditosOtorgados)
        operaciones.add(trueque)
        return trueque
    }

    /** Genera ids como P-001 o T-007 usando el total de operaciones. */
    private fun nuevoId(prefijo: String): String =
        "$prefijo-${(operaciones.size + 1).toString().padStart(3, '0')}"
}
