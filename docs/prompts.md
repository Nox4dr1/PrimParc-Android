# Prompts usados con IA — El Búho Lector (Checkpoint 3)

**Autora:** Adriana Milenka Torrico Cardoso
**Herramienta:** Claude (asistente de IA dentro de Android Studio)
**Proyecto:** `com.example.examprimparcprogiii`

Este archivo guarda, en orden, **todos los prompts** que usé durante el examen, copiados tal como
los escribí. Debajo de cada uno hay un resumen de lo que respondió o hizo la IA y de las decisiones
que se tomaron.

---

## Prompt 1 — Conectar el proyecto con GitHub

> conecta este proyecto con mi repositorio https://github.com/Nox4dr1/PrimParc-Android.git

**Lo que hizo la IA:**
- Ejecutó `git init` con la rama `main` y agregó el remoto `origin`.
- Comprobó que el repositorio de GitHub estaba vacío.
- Revisó el `.gitignore`, de modo que `local.properties` y las carpetas `build` quedan fuera.
- Creó el commit "Initial commit" y lo subió con `git push -u origin main`.

---

## Prompt 2 — Reglas del examen y preguntas iniciales

> Eres mi asistente en un examen de Programación III (Android, Kotlin, Jetpack Compose, Material 3). Está permitido usar IA.
> Mi caso de estudio es una LIBRERÍA. Proyecto: paquete com.example.examprimparcprogiii, ya conectado a GitHub.
> Checkpoints del examen:
> 1. Diseño de clases (atributos, métodos y relaciones) – 15 pts
> 2. Implementación en Kotlin aplicando POO – 10 pts
> 3. Subir los scripts/prompts usados (tercer commit) – 10 pts
> 4. Diseño de dos interfaces que listen al menos dos de los objetos, detallando los componentes de Jetpack Compose – 25 pts
> 5. Dos pantallas en Jetpack Compose que muestren al menos dos de los objetos – 20 pts
> 6. Que compile y corra en el emulador – 20 pts
> Reglas: trabajaremos UN checkpoint a la vez y no avances hasta que yo te lo diga. Explica con palabras simples.
> Mi diseño debe ser ORIGINAL: no uses el diseño típico de "interfaz Producto + Libro + Revista + Venta".
> Antes de diseñar nada, hazme entre 6 y 8 preguntas de opción múltiple (con una opción recomendada)
> para definir la librería: nombre, qué tipos de publicaciones vende o presta, atributos importantes,
> qué operaciones necesita (venta, préstamo, reservas, inventario), y el estilo y los colores de las pantallas.
> Espera mis respuestas.

**Lo que hizo la IA:** intentó mostrar las preguntas en un formulario, pero no se podía desplazar
dentro de él.

## Prompt 3 — Preguntas como texto

> No puedo desplazarme en el formulario. Hazme las mismas preguntas como texto normal en el chat, numeradas, con las opciones en letras (a, b, c, d) y la recomendada marcada. Te respondo así: 1a, 2b, 3c...

**Lo que hizo la IA:** escribió 8 preguntas de opción múltiple, cada una con una opción recomendada:
1. Nombre de la librería.
2. Concepto (segunda mano y trueque, cómics, libros antiguos o infantil).
3. Tipos de publicaciones.
4. Operaciones (préstamo, venta, trueque, reservas).
5. Atributos especiales (estado de conservación, ubicación, créditos, valoraciones).
6. Personas del sistema.
7. Estilo y colores.
8. Qué mostraría cada una de las dos pantallas.

## Prompt 4 — Mis respuestas

> 1 otro El búho lector
> 2a)
> 3a)
> 4a)
> 5a y d)
> 6a)
> 7a)
> 8a)

**Decisiones resultantes:**

| Pregunta | Respuesta |
|---|---|
| Nombre | **El Búho Lector** |
| Concepto | Libros de segunda mano y trueque |
| Publicaciones | Libros usados |
| Operaciones | Préstamo con fecha de devolución y multa por retraso |
| Atributos | Estado de conservación y valoración de los lectores (1 a 5 estrellas) |
| Personas | Socio lector con nivel de membresía y librero |
| Estilo | Cálido "papel y café": beige, marrón y verde oliva |
| Pantallas | Catálogo de publicaciones + socios con sus préstamos activos |

**Lo que hizo la IA:** propuso un primer diseño de clases en texto, con diagrama Mermaid y relaciones.

---

## Prompt 5 — Checkpoint 1: diagrama de clases

> Con mis respuestas, diseña el modelo de clases del checkpoint 1 en un diagrama Mermaid (classDiagram).
> Aplica POO: una clase abstracta o interfaz para lo común, herencia para los tipos concretos,
> un enum o sealed class para las categorías, composición/asociación (por ejemplo Autor o Editorial)
> y una clase gestora con listas privadas y métodos para agregar, listar, buscar y filtrar.
> Entre 6 y 9 clases. Indica visibilidad (+ / -), tipos de los atributos y multiplicidad de las relaciones.
> Ponlo en un archivo docs/diagrama_clases.md y debajo explica en 5 viñetas cada relación y qué pilar de POO aplica. de llamara docs/diseno_clases.md. añade trueque. el commit lo hare yo cuando me indiquers

**Lo que hizo la IA:**
- Creó `docs/diseno_clases.md` con un diagrama Mermaid de 9 clases:
  - `Operacion` (abstracta), con sus hijas `Prestamo` y `Trueque`
  - `LibroUsado`, `Autor` y `Socio`
  - los enums `EstadoConservacion` y `NivelMembresia`
  - la gestora `BuhoLector`
- Añadió el trueque: el socio entrega un libro y recibe créditos según el estado del libro.
- Para no pasar de 9 clases, quitó `Librero` y convirtió las valoraciones en una lista privada
  dentro de `LibroUsado`.

---

## Prompt 6 — Checkpoint 2: implementación en Kotlin

> Implementa en Kotlin las clases del diagrama, SIN interfaz gráfica.
> Cada clase en su propio archivo, dentro del paquete com.example.examprimparcprogiii.model.
> Aplica encapsulamiento (private + métodos), herencia, polimorfismo (override) y data class donde convenga.
> Crea también model/DatosPrueba.kt con al menos 5 objetos de cada tipo, con datos realistas en español.
> Comenta brevemente cada clase. Verifica que compile. avisame par aque haga el commit

**Lo que hizo la IA:**
- Creó las 9 clases en `model/`, cada una en su archivo, y además `DatosPrueba.kt` con
  6 autores, 6 socios, 15 libros, 6 préstamos y 5 trueques.
- Subió `minSdk` de 24 a 26, porque `java.time.LocalDate` necesita API 26.
- Agregó una regla nueva: al devolver un libro, la multa se paga con créditos si el socio tiene
  suficientes.
- Compilación: **BUILD SUCCESSFUL**.

## Prompt 7 — Verificar compilación

> antes, asegurate de que odo compila bien

**Lo que hizo la IA:** compiló el proyecto desde cero con `gradlew clean :app:assembleDebug`.
Resultado: **BUILD SUCCESSFUL**, sin errores ni advertencias de Kotlin.

---

## Prompt 8 — Checkpoint 4: diseño de pantallas

> ants del punto , vamos con el cheout 4. Checkpoint 4: diseña DOS pantallas que listen al menos dos de mis tipos de objetos.
> Para cada pantalla, dibuja un mockup en texto (ASCII) y al lado de cada parte escribe el componente de Jetpack Compose
> que usarías (Scaffold, TopAppBar, NavigationBar o Tabs, LazyColumn, LazyRow, Card, ListItem, FAB, Badge, FilterChip, etc.),
> usando la guía https://developer.android.com/develop/ui/compose/components?hl=es-419
> Después, en una tabla, explica por qué elegiste cada componente.
> Ponlo en docs/diseno_pantallas.md. Pregúntame cuál estilo prefiero antes de implementar. avisame para que haga el push yo

**Lo que hizo la IA:**
- Creó `docs/diseno_pantallas.md` con los mockups ASCII de dos pantallas:
  - **Catálogo:** libros, autores y trueques.
  - **Socios:** socios con sus préstamos, y una pestaña de movimientos que muestra el polimorfismo.
- Añadió la tabla de "por qué cada componente" y la paleta de colores.
- Revisó que los números del mockup coincidieran con los datos de prueba.
- Preguntó por el estilo: tarjetas y letras, modo oscuro, intensidad de color.

## Prompt 9 — Elección de estilo

> 1. una mezcla de a y b. 2a) 3a)
>
> con esto solo hago push del checkpoint 4? o falta algo?

**Decisiones resultantes:**
- Títulos en serif y textos en sans-serif.
- Tarjetas con borde fino y sombra suave.
- Solo modo claro.
- Barra superior marrón con texto crema.

La IA lo documentó en `docs/diseno_pantallas.md`.

---

## Prompt 10 — Checkpoint 5: pantallas en Jetpack Compose

> vamos al pinto 5
>
> Implementa las dos pantallas con Jetpack Compose y Material 3, en el paquete com.example.examprimparcprogiii.ui:
> - Pantalla1.kt y Pantalla2.kt, cada una con su @Preview(showBackground = true, showSystemUi = true).
> - Usa los datos de DatosPrueba.kt y muestra al menos dos tipos de objetos.
> - Una NavigationBar inferior que cambie entre las dos pantallas con un estado (remember), sin librería de navegación.
> - Solo material3, colores de MaterialTheme.colorScheme, contentDescription en los íconos y aplica el innerPadding del Scaffold.
> - Muéstrame cómo queda MainActivity (setContent con mi tema) para que arranque en esta app.
> Hazlo paso a paso: primero Pantalla1, espera a que compile y luego Pantalla2.
>
> avisame cualquer decision apra que yo haga los push

**Lo que hizo la IA:**
1. Cambió el tema "papel y café": colores, tipografía serif, sin modo oscuro y sin dynamic color.
2. Creó `Componentes.kt`, `BarraNavegacion.kt` y `BuhoLectorApp.kt`.
3. **Pantalla1** (catálogo): la primera compilación falló por un smart cast sobre un estado
   delegado. Lo corrigió y compiló. También corrigió un redondeo del promedio de estrellas.
4. **Pantalla2** (socios y movimientos): compiló a la primera.
5. Actualizó `MainActivity` para que use `ExamPrimParcProgIIITheme { BuhoLectorApp() }`.
6. Renderizó los dos previews y compiló el APK: **BUILD SUCCESSFUL**.

---

## Prompt 11 — Checkpoint 6: README, diagrama y capturas

> vamos con l punto 6
>
> Crea un README.md con: título del proyecto, autora Adriana Milenka Torrico Cardoso, caso de estudio,
> lista de clases, lista de componentes Compose usados y una sección "Capturas" con los nombres docs/captura1.png y docs/captura2.png. donde esten la spantallas añade tambie lla foto del diagrama de clases que te stpy pasando. Avisame apra revisar y hacer los commits

*(Adjunté la imagen del diagrama de clases.)*

**Lo que hizo la IA:**
- Guardó la imagen del diagrama como `docs/diagrama_clases.png`.
- Instaló el APK en el emulador, abrió la app y sacó las dos capturas reales
  (`docs/captura1.png` y `docs/captura2.png`). Así comprobó que **la app corre en el emulador**.
- Creó el `README.md` con todas las secciones pedidas.

---

## Prompt 12 — Este archivo

> ahora si gurtada toda la conversacion uy los prompts en un med me avisas para pushear

**Lo que hizo la IA:** creó este archivo `docs/prompts.md`.

---

## Archivos generados con ayuda de la IA

| Checkpoint | Archivos |
|---|---|
| 1 | `docs/diseno_clases.md` |
| 2 | `app/build.gradle.kts` (minSdk 26), `model/*.kt` |
| 3 | `docs/prompts.md` |
| 4 | `docs/diseno_pantallas.md` |
| 5 | `ui/Pantalla1.kt`, `ui/Pantalla2.kt`, `ui/BarraNavegacion.kt`, `ui/BuhoLectorApp.kt`, `ui/Componentes.kt`, `ui/theme/*`, `MainActivity.kt` |
| 6 | `README.md`, `docs/diagrama_clases.png`, `docs/captura1.png`, `docs/captura2.png` |
