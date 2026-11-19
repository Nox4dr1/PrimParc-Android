# El Búho Lector — Diseño de pantallas (Checkpoint 4)

La app tiene **dos pantallas**. Se cambia de una a otra con una barra de navegación inferior.

| Pantalla | Objetos que lista | Métodos del modelo que usa |
|---|---|---|
| **1. Catálogo** | `LibroUsado`, `Autor` y `Trueque` (en una hoja inferior) | `listarLibros()`, `buscarLibroPorTitulo()`, `filtrarDisponibles()`, `filtrarPorEstado()`, `filtrarPorAutor()`, `listarAutores()`, `listarOperaciones()` |
| **2. Socios** | `Socio`, `Prestamo` y `Operacion` (préstamos y trueques mezclados) | `listarSocios()`, `prestamosActivosDe()`, `calcularMulta()`, `diasRetraso()`, `listarOperaciones()` con `descripcion()` polimórfico |

Guía de referencia: [Componentes de Compose (Material 3)](https://developer.android.com/develop/ui/compose/components?hl=es-419)

---

## Estructura común a las dos pantallas

```
┌──────────────────────────────────────┐
│                                      │ ◄── Scaffold (esqueleto: topBar, bottomBar, FAB, snackbar)
│        ( contenido de la pantalla )  │
│                                      │
├──────────────────────────────────────┤
│   📚 Catálogo        👥 Socios (3)   │ ◄── NavigationBar + 2 NavigationBarItem
└──────────────────────────────────────┘     └─ BadgedBox + Badge: préstamos con retraso
```

---

## Pantalla 1 — Catálogo

```
┌──────────────────────────────────────┐
│ 🦉 El Búho Lector              🔍    │ ◄── TopAppBar (title + actions: IconButton)
│    Libros usados · 15 ejemplares     │      └─ Text (subtítulo, bodySmall)
├──────────────────────────────────────┤
│ ┌──────────────────────────────────┐ │
│ │ 🔍 Buscar por título…            │ │ ◄── OutlinedTextField (leadingIcon = Icon Search)
│ └──────────────────────────────────┘ │      └─ llama a buscarLibroPorTitulo()
│                                      │
│ [✓Todos] [Disponibles] [Como nuevo]→ │ ◄── LazyRow de FilterChip (selección única)
│                                      │      └─ filtrarDisponibles() / filtrarPorEstado()
│ Autores                              │ ◄── Text (titleSmall)
│ (GGM) (Allende) (Cortázar) (Borges)→ │ ◄── LazyRow de InputChip con avatar de iniciales
│                                      │      └─ lista objetos Autor → filtrarPorAutor()
│ ┌──────────────────────────────────┐ │
│ │ Cien años de soledad             │ │ ◄── LazyColumn de ElevatedCard (una por LibroUsado)
│ │ Gabriel García Márquez           │ │      ├─ Text (titleMedium / bodyMedium)
│ │ Novela · edición 1982            │ │      ├─ Text (bodySmall)
│ │ ★★★★☆ 4.7     [Bueno] [Prestado] │ │      ├─ Row de Icon(Star) + Text promedio
│ └──────────────────────────────────┘ │      └─ AssistChip (estado) + SuggestionChip (disponible)
│ ┌──────────────────────────────────┐ │
│ │ Como agua para chocolate         │ │
│ │ Laura Esquivel                   │ │
│ │ Novela · edición 1999            │ │
│ │ ★★★★☆ 4.3  [Gastado][Disponible] │ │
│ └──────────────────────────────────┘ │
│                    ┌───────────────┐ │
│                    │ ⇄  Trueques   │ │ ◄── ExtendedFloatingActionButton
│                    └───────────────┘ │      └─ abre la hoja de trueques
├──────────────────────────────────────┤
│  📚 Catálogo        👥 Socios (3)    │ ◄── NavigationBar
└──────────────────────────────────────┘
```

**Hoja inferior que se abre al tocar "Trueques":**

```
┌──────────────────────────────────────┐
│               ────                   │ ◄── ModalBottomSheet (con su DragHandle)
│ Últimos trueques                     │ ◄── Text (titleLarge)
│ ⇄ Lucía Fernández                    │ ◄── LazyColumn de ListItem (una por Trueque)
│   Crónica de una muerte… · Bueno +20 │      ├─ leadingContent: Icon
│ ─────────────────────────────────────│      ├─ headlineContent / supportingContent
│ ⇄ Diego Mamani                       │      └─ trailingContent: Text "+20"
│   Paula · Como nuevo           +30   │ ◄── HorizontalDivider entre filas
└──────────────────────────────────────┘
```

---

## Pantalla 2 — Socios

```
┌──────────────────────────────────────┐
│ Socios y préstamos                   │ ◄── TopAppBar
├──────────────────────────────────────┤
│   Socios (6)    │   Movimientos      │ ◄── PrimaryTabRow + 2 Tab
│ ════════════                         │      └─ indicador bajo la pestaña activa
│                                      │
│ ┌────────┐ ┌────────┐ ┌────────┐     │ ◄── Row de 3 Card pequeñas (resumen)
│ │   5    │ │   3    │ │  41.0  │     │      └─ préstamos activos / con retraso / multas
│ │activos │ │retraso │ │ multas │     │
│ └────────┘ └────────┘ └────────┘     │
│                                      │
│ [Todos][Bronce][Plata][ Oro ]        │ ◄── SingleChoiceSegmentedButtonRow
│                                      │      └─ SegmentedButton por NivelMembresia
│ ┌──────────────────────────────────┐ │
│ │ (MR) Mateo Rojas            (1)  │ │ ◄── LazyColumn de OutlinedCard (una por Socio)
│ │      Plata · 20 créditos         │ │      └─ ListItem: leading = avatar (Box + Text)
│ │ ──────────────────────────────── │ │               trailing = Badge (préstamos activos)
│ │ 📕 La casa de los espíritus      │ │ ◄── HorizontalDivider
│ │    Venció 11/11 · 8 días tarde   │ │ ◄── ListItem por cada Prestamo activo
│ │    ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓  100 %     │ │      ├─ LinearProgressIndicator (tiempo usado)
│ │                   [⚠ Multa 12.0] │ │      └─ AssistChip con color de error
│ └──────────────────────────────────┘ │
│ ┌──────────────────────────────────┐ │
│ │ (LF) Lucía Fernández        (1)  │ │
│ │      Oro · 35 créditos           │ │
│ │ 📗 Cien años de soledad          │ │
│ │    Vence 26/11 · faltan 7 días   │ │
│ │    ▓▓▓▓▓▓▓▓▓▓▓░░░░░░░   67 %     │ │
│ └──────────────────────────────────┘ │
├──────────────────────────────────────┤
│  📚 Catálogo        👥 Socios (3)    │ ◄── NavigationBar
└──────────────────────────────────────┘
```

**Pestaña "Movimientos"** (aquí se ve el polimorfismo en la interfaz):

```
┌──────────────────────────────────────┐
│   Socios (6)    │   Movimientos      │ ◄── PrimaryTabRow (pestaña 2 activa)
│                   ═══════════        │
│ 📕 P-011  12/11/2026                 │ ◄── LazyColumn de ListItem (una por Operacion)
│    Préstamo de «El amor en los…»     │      ├─ leadingContent: Icon distinto con `when (op)`
│    a Sebastián Gutiérrez, devuelto   │      │   is Prestamo → libro · is Trueque → flechas
│ ─────────────────────────────────────│      ├─ supportingContent: op.descripcion()
│ ⇄ T-005  18/10/2026                  │      └─ overlineContent: id y fecha
│    Trueque: Sebastián entregó        │ ◄── HorizontalDivider
│    «El Aleph» (+10 créditos)         │
└──────────────────────────────────────┘
```

---

## Por qué elegí cada componente

| Componente | Dónde | Por qué |
|---|---|---|
| `Scaffold` | Las dos pantallas | Es el "esqueleto" de Material 3: coloca la barra superior, la inferior y el FAB en su sitio y respeta los bordes del sistema (*insets*). |
| `TopAppBar` | Las dos pantallas | Muestra el nombre de la librería y de la sección. Así el usuario siempre sabe dónde está. |
| `NavigationBar` + `NavigationBarItem` | Abajo | Hay solo 2 destinos principales; la guía recomienda la barra inferior para 3 a 5 destinos en teléfono y también funciona bien con 2. Se cambia de pantalla con un toque y sin librería de navegación extra. |
| `BadgedBox` + `Badge` | Ícono "Socios" y tarjeta de socio | Un número pequeño avisa de algo pendiente (préstamos con retraso o activos) sin ocupar espacio. |
| `OutlinedTextField` | Catálogo | Campo de búsqueda simple que llama a `buscarLibroPorTitulo()` en cada letra. |
| `FilterChip` en `LazyRow` | Catálogo | Los chips de filtro son el componente pensado para "mostrar solo…". La fila se desliza si no caben todos. |
| `InputChip` en `LazyRow` | Catálogo, autores | **Lista los objetos `Autor`** como chips con iniciales. Al tocar uno se filtran sus libros (`filtrarPorAutor()`). |
| `LazyColumn` | Las dos pantallas | Solo dibuja lo que está en pantalla. Es eficiente aunque haya cientos de libros o socios. |
| `ElevatedCard` | Catálogo | Cada libro es un objeto con mucha información. La tarjeta con sombra la agrupa y recuerda a un libro sobre la mesa. |
| `AssistChip` / `SuggestionChip` | Tarjeta de libro y de préstamo | Etiquetas cortas para el estado de conservación, la disponibilidad y la multa. Se leen de un vistazo. |
| `Icon` (Star) | Tarjeta de libro | Muestra `promedioEstrellas()` de forma visual, igual que cualquier tienda. |
| `ExtendedFloatingActionButton` | Catálogo | La acción especial de esta librería es el **trueque**. Un FAB con texto la destaca. |
| `ModalBottomSheet` | Catálogo | **Lista los objetos `Trueque`** sin salir del catálogo. Se cierra deslizando hacia abajo. |
| `PrimaryTabRow` + `Tab` | Socios | Separa dos vistas del mismo tema (socios / movimientos) dentro de una sola pantalla. |
| `Card` (resumen) | Socios | Tres números clave (activos, con retraso, multas) que se ven de inmediato. |
| `SingleChoiceSegmentedButtonRow` | Socios | Hay pocas opciones fijas (los niveles del enum) y solo una puede estar activa. Para eso está el botón segmentado. |
| `OutlinedCard` | Socios | Agrupa un **`Socio`** con sus **`Prestamo`** activos. El borde fino lo diferencia de las tarjetas del catálogo. |
| `ListItem` | Socios, movimientos, trueques | Es la fila estándar de Material 3, con espacios para ícono, título, subtítulo y dato a la derecha. Todo queda alineado y es accesible. |
| `LinearProgressIndicator` | Préstamo | Muestra cuánto del plazo ya se usó. Al 100 % y en rojo se nota el retraso. |
| `HorizontalDivider` | Listas | Separa filas sin recargar la pantalla. |

---

## Estilo visual propuesto: "papel y café"

| Rol de color (Material 3) | Color | Uso |
|---|---|---|
| `primary` | Marrón café `#6D4C41` | Barra superior, FAB, pestaña activa |
| `secondary` | Verde oliva `#6B7A3A` | Chips seleccionados, "Disponible" |
| `tertiary` | Ámbar `#C98A2B` | Estrellas, créditos |
| `background` / `surface` | Beige papel `#FBF3E4` / `#FFF8EE` | Fondo y tarjetas |
| `error` | Rojo ladrillo `#B3261E` | Retrasos y multas |

- Sin *dynamic color*, para que la app siempre tenga la paleta de la librería.

### Estilo elegido (mezcla de "clásico de librería" y "moderno minimalista")

| Decisión | Elección |
|---|---|
| Tipografía | **Serif** (`FontFamily.Serif`) en títulos de pantalla y de libros (clásico). **Sans-serif** en textos, chips y botones (moderno). |
| Tarjetas | Borde fino de 1 dp color `outlineVariant` con sombra muy suave (1 dp) y esquinas medianas de 12 dp. Así quedan limpias pero con un toque de "papel". |
| Espaciado | 16 dp de margen lateral y 12 dp entre tarjetas. |
| Modo | **Solo modo claro**: el "papel" se ve siempre igual. |
| Barra superior | **Marrón café** (`primary`) con título y íconos **crema** (`onPrimary`). |
| Barra inferior | Fondo beige (`surface`). El ítem activo lleva un indicador verde oliva (`secondaryContainer`). |
