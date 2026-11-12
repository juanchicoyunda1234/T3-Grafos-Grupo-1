# Casos de prueba — T3 Grafos, Grupo 1

[⬅ Volver al README](../README.md) · Código de las pruebas: [PruebasGrafo.java](../src/pruebas/PruebasGrafo.java) · Datos: [DatosPrueba.java](../src/app/DatosPrueba.java) · Capturas: [evidencias/](../evidencias/)

Todas las salidas de este documento son de **ejecuciones reales** del programa (JDK 21, Windows).

## Conjunto de datos

13 estudiantes y 17 amistades (`src/app/DatosPrueba.java`):

| # | Amistad | # | Amistad | # | Amistad |
|---|---|---|---|---|---|
| 1 | Jose – Ana | 7 | Jose – Sofia | 13 | Maria – Camila |
| 2 | Jose – Maria | 8 | Sofia – Carlos | 14 | Lucia – Camila |
| 3 | Pedro – Ana | 9 | Carlos – Lucia | 15 | Diego – Valeria |
| 4 | Pedro – Maria | 10 | Luis – Lucia | 16 | Valeria – Andres |
| 5 | Ana – Luis | 11 | Ana – Sofia | 17 | Andres – Diego |
| 6 | Maria – Luis | 12 | Pedro – Carlos | | Mateo: aislado |

Este conjunto cubre el ejemplo del enunciado (José y Pedro comparten a Ana y María), un **empate** (Luis y Pedro tienen 2 puntos para José), un **grafo desconectado** (Diego, Valeria y Andrés) y un **usuario aislado** (Mateo).

## 1. Pruebas automáticas

```bash
java -cp bin pruebas.PruebasGrafo
```

| Caso | Operación | Entrada | Esperado | Resultado |
|---|---|---|---|---|
| CP01 | Registrar vértice | "Ana" en grafo vacío | true | PASA |
| CP02 | Duplicado | "  ANA " | false | PASA |
| CP03 | Nombre vacío | "   " | false | PASA |
| CP04 | Buscar existente | "ana" | Ana | PASA |
| CP05 | Buscar inexistente | "Zoe" | null | PASA |
| CP06 | Contador de vértices | — | 1 | PASA |
| CP07 | Crear conexión | Ana–Jose | true | PASA |
| CP08 | Bidireccional | ¿Jose→Ana? | true | PASA |
| CP09 | Conexión repetida | Jose–Ana | false | PASA |
| CP10 | Autoconexión | Ana–Ana | false | PASA |
| CP11 | Con inexistente | Ana–Zoe | false | PASA |
| CP12 | Contador de aristas | — | 1 | PASA |
| CP13 | Datos de prueba | — | 13 vértices | PASA |
| CP14 | Datos de prueba | — | 17 aristas | PASA |
| CP15 | Eliminar conexión | Jose–Ana | true | PASA |
| CP16 | Extremo 1 actualizado | ¿Jose→Ana? | false | PASA |
| CP17 | Extremo 2 actualizado | ¿Ana→Jose? | false | PASA |
| CP18 | Grado actualizado | grado(Ana) | 3 | PASA |
| CP19 | Eliminar inexistente | Jose–Ana otra vez | false | PASA |
| CP20 | Eliminar vértice | Maria | true | PASA |
| CP21 | Vértice borrado | ¿existe Maria? | false | PASA |
| CP22 | Aristas asociadas borradas | 17 − 1 − 4 | 12 | PASA |
| CP23 | Integridad tras borrar | validarIntegridad() | 0 problemas | PASA |
| CP24 | Eliminar vértice inexistente | Maria otra vez | false | PASA |
| CP25 | BFS | desde Jose | Jose, Ana, Maria, Sofia, Luis, Pedro, Camila, Carlos, Lucia | PASA |
| CP26 | Niveles BFS | desde Jose | 0,1,1,1,2,2,2,2,3 | PASA |
| CP27 | DFS | desde Jose | Jose, Ana, Luis, Lucia, Camila, Maria, Pedro, Carlos, Sofia | PASA |
| CP28 | Grafo desconectado | BFS desde Diego | Diego, Andres, Valeria | PASA |
| CP29 | Usuario aislado | BFS desde Mateo | Mateo | PASA |
| CP30 | Origen inexistente | DFS desde Zoe | null | PASA |
| CP31 | Componentes | — | 3 | PASA |
| CP32 | Vecinos comunes | Jose, Pedro | [Ana, Maria] | PASA |
| CP33 | Sin vecinos comunes | Jose, Diego | [] | PASA |
| CP34 | Top 3 con empate | Jose | Luis 2, Pedro 2, Camila 1 | PASA |
| CP35 | Sin autorrecomendación ni amigos | Jose | no aparecen Jose, Ana, Maria ni Sofia | PASA |
| CP36 | Recomendación tras conectar | crear Jose–Pedro | Pedro desaparece | PASA |
| CP37 | Nuevo top 3 | Jose | Carlos 2, Luis 2, Camila 1 | PASA |
| CP38 | Aislado | Mateo | 0 candidatos | PASA |
| CP39 | Triángulo cerrado | Diego | 0 candidatos | PASA |
| CP40 | Sin duplicados | Lucia | sin nombres repetidos | PASA |
| CP41 | Integridad inicial | — | 0 problemas | PASA |
| CP42 | Integridad tras altas y bajas | mezcla de operaciones | 0 problemas | PASA |
| CP43 | Índices tras eliminar | último vértice | índice n−1 | PASA |
| CP44 | Estadísticas | — | reporta a Mateo como aislado | PASA |

**Resultado real: 44 pasadas, 0 fallidas.**

## 2. Sesión real del menú

Entradas, en orden: `s`, 5, 6 Jose, 7 Jose, 8 Jose Pedro, 9 Jose, 2 Jose Pedro, 9 Jose, 2 Jose Pedro, 2 Jose jose, Enter vacío, 1 ana, 1 J0se → Mateo Ruiz, abc, 99, 9 Mateo, 6 Diego, 3 Lucia Camila, 4 Maria, 10, 0.

### Visualizar grafo (opción 5)
```
Ana        (grado 4) -> Jose -> Luis -> Pedro -> Sofia
Jose       (grado 3) -> Ana -> Maria -> Sofia
Maria      (grado 4) -> Camila -> Jose -> Luis -> Pedro
Pedro      (grado 3) -> Ana -> Carlos -> Maria
Luis       (grado 3) -> Ana -> Lucia -> Maria
Sofia      (grado 3) -> Ana -> Carlos -> Jose
Carlos     (grado 3) -> Lucia -> Pedro -> Sofia
Lucia      (grado 3) -> Camila -> Carlos -> Luis
Camila     (grado 2) -> Lucia -> Maria
Diego      (grado 2) -> Andres -> Valeria
Valeria    (grado 2) -> Andres -> Diego
Andres     (grado 2) -> Diego -> Valeria
Mateo      (grado 0) -> sin conexiones
```

### BFS y DFS desde Jose (opciones 6 y 7)
```
BFS desde Jose:
  Orden: Jose -> Ana -> Maria -> Sofia -> Luis -> Pedro -> Camila -> Carlos -> Lucia
  Nivel 0: Jose
  Nivel 1: Ana, Maria, Sofia
  Nivel 2: Luis, Pedro, Camila, Carlos
  Nivel 3: Lucia
  (4 estudiante(s) no alcanzables: el grafo esta desconectado)

DFS desde Jose:
  Orden: Jose -> Ana -> Luis -> Lucia -> Camila -> Maria -> Pedro -> Carlos -> Sofia
  Profundidad: Jose(0)  Ana(1)  Luis(2)  Lucia(3)  Camila(4)  Maria(5)  Pedro(6)  Carlos(7)  Sofia(8)
  (4 estudiante(s) no alcanzables: el grafo esta desconectado)
```

**Cómo se explica el BFS:** los vecinos de Jose son Ana, Maria y Sofia (nivel 1). Ana aporta a Luis y Pedro; Maria aporta a Camila, porque Luis y Pedro ya estaban marcados; Sofia aporta a Carlos (nivel 2). Lucia solo se alcanza desde Luis (nivel 3).

**Cómo se explica el DFS:** Jose baja a Ana, su primer vecino. El primer vecino no visitado de Ana es Luis, el de Luis es Lucia, el de Lucia es Camila, el de Camila es Maria y el de Maria es Pedro. Pedro baja a Carlos, y Carlos a Sofia. Ahí ya no queda nadie sin visitar.

### Vecinos comunes y recomendaciones (opciones 8 y 9): ejemplo del enunciado
```
Amigos en comun entre Jose y Pedro: 2 punto(s)
  Ana, Maria

Top 3 para Jose:
  1. Luis - 2 punto(s) - amigos en comun: Ana, Maria
  2. Pedro - 2 punto(s) - amigos en comun: Ana, Maria
  3. Camila - 1 punto(s) - amigos en comun: Maria

Amistad creada: Jose <-> Pedro

Top 3 para Jose:
  1. Carlos - 2 punto(s) - amigos en comun: Pedro, Sofia
  2. Luis - 2 punto(s) - amigos en comun: Ana, Maria
  3. Camila - 1 punto(s) - amigos en comun: Maria
```
- **Empate:** Luis y Pedro tienen 2 puntos cada uno, y Luis sale primero por orden alfabético.
- **Después de conectar José–Pedro**, Pedro desaparece de la lista. Además, Carlos sube a 2 puntos, porque ahora comparte a Pedro y a Sofia con José.

### Validaciones
```
Error: ya son amigos.                                    (2: Jose - Pedro otra vez)
Error: debe elegir dos estudiantes distintos.            (2: Jose - jose)
Opcion invalida: ingrese un numero entre 0 y 10.         (Enter vacío, abc, 99)
Error: ya existe un estudiante con ese nombre.           (1: ana)
Nombre invalido: solo letras y espacios, de 2 a 40 caracteres.   (1: J0se)
Registrado: Mateo Ruiz
```

### Usuario aislado, grafo desconectado y bajas
```
Top 3 para Mateo:
  Sin candidatos (no tiene amigos de amigos que aun no sean sus amigos).

BFS desde Diego:
  Orden: Diego -> Andres -> Valeria
  Nivel 0: Diego
  Nivel 1: Andres, Valeria
  (11 estudiante(s) no alcanzables: el grafo esta desconectado)

Amistad eliminada en ambos sentidos.          (3: Lucia - Camila)
Eliminado Maria junto con 4 amistad(es).      (4: Maria)
```

### Estadísticas e integridad después de las bajas (opción 10)
```
Vertices (estudiantes): 13
Aristas (amistades):    13
Grado promedio:         2,00
Mas conectado:          Ana (4 amigos)
Aislados:               Camila, Mateo, Mateo Ruiz
Componentes conectadas:  5
  1) Ana, Jose, Luis, Pedro, Sofia, Lucia, Carlos
  2) Camila
  3) Diego, Andres, Valeria
  4) Mateo
  5) Mateo Ruiz
Integridad (RF10):       OK, sin duplicados ni referencias invalidas
```
Comprobación de las aristas: 17 + 1 (José–Pedro) − 1 (Lucía–Camila) − 4 (las de María) = **13**. Camila queda aislada porque perdió a sus dos amigas, Lucía y María.
