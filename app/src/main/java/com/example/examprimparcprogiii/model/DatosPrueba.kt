package com.example.examprimparcprogiii.model

import java.time.LocalDate

/**
 * Datos de prueba realistas para "El Búho Lector".
 * [crearLibreria] arma una librería nueva cada vez con:
 * 6 autores, 6 socios, 15 libros (10 del catálogo + 5 recibidos por trueque),
 * 6 préstamos (uno ya devuelto) y 5 trueques.
 */
object DatosPrueba {

    /** Fecha fija de "hoy" para que las multas siempre den lo mismo. */
    val HOY: LocalDate = LocalDate.of(2026, 11, 19)

    fun crearLibreria(): BuhoLector {
        val libreria = BuhoLector()

        // ---------- Autores ----------
        val garciaMarquez = Autor("A-001", "Gabriel García Márquez", "Colombiana")
        val allende = Autor("A-002", "Isabel Allende", "Chilena")
        val cortazar = Autor("A-003", "Julio Cortázar", "Argentina")
        val vargasLlosa = Autor("A-004", "Mario Vargas Llosa", "Peruana")
        val esquivel = Autor("A-005", "Laura Esquivel", "Mexicana")
        val borges = Autor("A-006", "Jorge Luis Borges", "Argentina")
        listOf(garciaMarquez, allende, cortazar, vargasLlosa, esquivel, borges)
            .forEach(libreria::agregarAutor)

        // ---------- Socios ----------
        listOf(
            Socio("S-001", "Lucía Fernández", NivelMembresia.ORO, creditosIniciales = 15),
            Socio("S-002", "Mateo Rojas", NivelMembresia.PLATA),
            Socio("S-003", "Valentina Quispe", NivelMembresia.BRONCE, creditosIniciales = 5),
            Socio("S-004", "Diego Mamani", NivelMembresia.PLATA, creditosIniciales = 40),
            Socio("S-005", "Camila Vargas", NivelMembresia.ORO),
            Socio("S-006", "Sebastián Gutiérrez", NivelMembresia.BRONCE)
        ).forEach(libreria::agregarSocio)

        // ---------- Libros del catálogo ----------
        listOf(
            LibroUsado("L-001", "Cien años de soledad", garciaMarquez, "Novela", 1982, EstadoConservacion.BUENO),
            LibroUsado("L-002", "El amor en los tiempos del cólera", garciaMarquez, "Novela", 1985, EstadoConservacion.COMO_NUEVO),
            LibroUsado("L-003", "La casa de los espíritus", allende, "Novela", 1994, EstadoConservacion.BUENO),
            LibroUsado("L-004", "Eva Luna", allende, "Novela", 1989, EstadoConservacion.GASTADO),
            LibroUsado("L-005", "Rayuela", cortazar, "Novela", 1977, EstadoConservacion.GASTADO),
            LibroUsado("L-006", "Final del juego", cortazar, "Cuentos", 2004, EstadoConservacion.COMO_NUEVO),
            LibroUsado("L-007", "La tía Julia y el escribidor", vargasLlosa, "Novela", 1996, EstadoConservacion.BUENO),
            LibroUsado("L-008", "Conversación en La Catedral", vargasLlosa, "Novela", 2010, EstadoConservacion.COMO_NUEVO),
            LibroUsado("L-009", "Como agua para chocolate", esquivel, "Novela", 1999, EstadoConservacion.GASTADO),
            LibroUsado("L-010", "Ficciones", borges, "Cuentos", 2011, EstadoConservacion.COMO_NUEVO)
        ).forEach(libreria::agregarLibro)

        // ---------- Valoraciones de lectores ----------
        valorar(libreria, "L-001", 5, 5, 4)
        valorar(libreria, "L-002", 4, 5)
        valorar(libreria, "L-003", 4, 3, 5)
        valorar(libreria, "L-005", 5, 4)
        valorar(libreria, "L-007", 3)
        valorar(libreria, "L-009", 4, 4, 5)
        valorar(libreria, "L-010", 5, 5)

        // ---------- Trueques (los socios entregan libros a la tienda) ----------
        trueque(libreria, "S-001", LibroUsado("L-011", "Crónica de una muerte anunciada", garciaMarquez, "Novela", 1981, EstadoConservacion.BUENO), LocalDate.of(2026, 10, 2))
        trueque(libreria, "S-004", LibroUsado("L-012", "Paula", allende, "Memorias", 1995, EstadoConservacion.COMO_NUEVO), LocalDate.of(2026, 10, 6))
        trueque(libreria, "S-003", LibroUsado("L-013", "Bestiario", cortazar, "Cuentos", 1983, EstadoConservacion.GASTADO), LocalDate.of(2026, 10, 9))
        trueque(libreria, "S-002", LibroUsado("L-014", "La ciudad y los perros", vargasLlosa, "Novela", 2001, EstadoConservacion.BUENO), LocalDate.of(2026, 10, 14))
        trueque(libreria, "S-006", LibroUsado("L-015", "El Aleph", borges, "Cuentos", 1990, EstadoConservacion.GASTADO), LocalDate.of(2026, 10, 18))

        // ---------- Préstamos ----------
        prestar(libreria, "L-001", "S-001", LocalDate.of(2026, 11, 5))   // ORO: vence 26/11, al día
        prestar(libreria, "L-003", "S-002", LocalDate.of(2026, 10, 28))  // PLATA: venció 11/11, con retraso
        prestar(libreria, "L-005", "S-003", LocalDate.of(2026, 11, 1))   // BRONCE: venció 08/11, con retraso
        prestar(libreria, "L-008", "S-004", LocalDate.of(2026, 11, 15))  // PLATA: vence 29/11, al día
        prestar(libreria, "L-010", "S-005", LocalDate.of(2026, 10, 20))  // ORO: venció 10/11, con retraso
        val devuelto = prestar(libreria, "L-002", "S-006", LocalDate.of(2026, 11, 3))
        libreria.devolverLibro(devuelto.id, LocalDate.of(2026, 11, 12)) // devuelto 2 días tarde

        return libreria
    }

    // ---------- Ayudantes privados ----------

    private fun valorar(libreria: BuhoLector, libroId: String, vararg estrellas: Int) {
        val libro = libreria.listarLibros().first { it.id == libroId }
        estrellas.forEach(libro::valorar)
    }

    private fun prestar(libreria: BuhoLector, libroId: String, socioId: String, fecha: LocalDate): Prestamo =
        checkNotNull(libreria.prestarLibro(libroId, socioId, fecha)) {
            "No se pudo prestar $libroId a $socioId"
        }

    private fun trueque(libreria: BuhoLector, socioId: String, libro: LibroUsado, fecha: LocalDate): Trueque =
        checkNotNull(libreria.registrarTrueque(socioId, libro, fecha)) {
            "No se pudo registrar el trueque de ${libro.id}"
        }
}
