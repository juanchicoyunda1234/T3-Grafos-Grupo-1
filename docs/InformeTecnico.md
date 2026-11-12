# Informe técnico — T3 Grafos, Grupo 1

**Caso:** Red social universitaria UTA · **Asignatura:** Estructura de Datos · **Valor:** 3,00 puntos

[⬅ Volver al README](../README.md) · [📐 Diagrama de clases](DiagramaClases.md) · [🧪 Casos de prueba](../pruebas/CasosPrueba.md) · [🖼️ Evidencias](../evidencias/)

## 1. Problema

La plataforma necesita sugerir nuevas amistades entre estudiantes a partir de las relaciones que ya existen. La red se modela como un **grafo no dirigido** G = (V, E): cada estudiante es un vértice y cada amistad es una arista. Como la amistad es mutua, una arista sirve en los dos sentidos.

## 2. ¿Por qué listas de adyacencia?

| | Matriz de adyacencia | Lista de adyacencia |
|---|---|---|
| Memoria | O(V²) | O(V + E) |
| ¿Son amigos A y B? | O(1) | O(grado(A)) |
| Recorrer los amigos de A | O(V) | O(grado(A)) |
| Agregar un vértice | hay que redimensionar la matriz | O(1) al final de la lista |

Una red social es **dispersa**: cada estudiante tiene pocos amigos comparado con el total de estudiantes. Una matriz desperdiciaría casi toda su memoria en ceros. Además, BFS, DFS y la recomendación lo que más hacen es **recorrer los amigos de un vértice**, y en eso la lista solo toca las aristas que existen.

La restricción del enunciado prohíbe `HashMap`, `HashSet`, `TreeMap` y `TreeSet`. Por eso el grafo usa dos listas enlazadas propias: la de vértices (`NodoVertice`) y, en cada vértice, su lista de adyacencia (`NodoAdyacencia`). Para marcar visitados sin un Set, cada vértice lleva un **índice 0..n-1** que sirve de posición en un `boolean[]`.

## 3. Diseño

El diagrama de clases completo está en [DiagramaClases.md](DiagramaClases.md).

```
modelo        NodoVertice, NodoAdyacencia        (datos)
estructuras   Grafo, ColaVertices                (estructura y CRUD)
algoritmos    Recorridos, Recomendador, ...      (lógica sobre el grafo)
app           Main, DatosPrueba                  (menú de consola)
pruebas       PruebasGrafo                       (verificación)
```

Las listas de adyacencia se mantienen **ordenadas alfabéticamente** al insertar. Esto tiene tres ventajas:

1. BFS y DFS son **deterministas**: el orden documentado siempre se puede reproducir.
2. Los **vecinos comunes** se calculan como el *merge* de MergeSort, avanzando dos punteros en O(grado(A) + grado(B)) en lugar de O(grado(A) · grado(B)).
3. La salida de la opción 5 se lee con facilidad.

El costo es que insertar una arista pasa de O(1) a O(grado). Se acepta porque los grados son pequeños.

## 4. Algoritmos

### BFS
Usa una cola FIFO propia. Visita el origen, después todos sus vecinos y después los vecinos de esos. El vértice se marca al **encolarlo**, no al sacarlo; así nunca entra dos veces a la cola. El nivel de cada vértice es su distancia, en número de amistades, desde el origen.

### DFS
Es recursivo, y la pila de llamadas de Java hace de pila. Entra al primer vecino no visitado y solo retrocede cuando ya no quedan vecinos nuevos. La profundidad que se muestra es la del árbol de llamadas, no la distancia más corta. Por eso Sofia aparece con profundidad 8 en el DFS y con nivel 1 en el BFS.

### Componentes conectadas
Se lanza un BFS desde cada vértice que todavía no ha sido visitado. Cada BFS descubre un componente completo. En el conjunto de prueba hay 3 componentes.

### Recomendación (reto del Grupo 1)
1. Se marcan los amigos directos de U en `esAmigo[]`.
2. Para cada amigo A de U, se recorre la lista de A. Cada C ≠ U que no es amigo de U es un candidato, y A se suma como amigo en común de C.
3. Con `Candidato[] porIndice` se crea un único objeto por candidato, así que no hay duplicados.
4. Los candidatos se ordenan por inserción: primero la puntuación de mayor a menor y, si empatan, el nombre de la A a la Z.
5. Se devuelven los 3 primeros, cada uno con la lista de amigos en común.

Un candidato con k amigos en común aparece exactamente k veces en el paso 2, porque llega una vez desde cada uno de esos amigos. Por eso la puntuación es igual al número de amigos en común.

## 5. Complejidad

V = vértices, E = aristas, d = grado de un vértice, c = número de candidatos.

| Operación | Complejidad | Motivo |
|---|---|---|
| Buscar vértice | O(V) | búsqueda lineal en la lista de vértices |
| Registrar vértice | O(V) | hay que descartar duplicados (la inserción en sí es O(1)) |
| Crear conexión | O(V + d) | buscar los dos vértices e insertar en orden |
| Eliminar conexión | O(V + d) | buscar los dos vértices y desenlazar en ambas listas |
| Eliminar vértice | O(V + Σ d(vecinos)) | quitarlo de la lista de cada vecino y renumerar los índices |
| Mostrar grafo | O(V + E) | se recorren todas las listas |
| BFS / DFS | O(V + E) | cada vértice entra una vez y cada lista se recorre una vez |
| Componentes conectadas | O(V + E) | los BFS juntos cubren el grafo una sola vez |
| Vecinos comunes | O(V + d(A) + d(B)) | intersección de dos listas ordenadas |
| Recomendar | O(V + Σ d(amigos de U) + c²) | amigos de amigos más el ordenamiento por inserción |
| Validar integridad | O(V² + V·E) | revisa duplicados, simetría y referencias |

## 6. Pruebas

Hay 44 pruebas automáticas en `src/pruebas/PruebasGrafo.java`, sin librerías externas, y una sesión real del menú. Todo está documentado en `pruebas/CasosPrueba.md`. Cubren altas y bajas de vértices y aristas, BFS y DFS con orden documentado, candidatos con 2 o más vecinos comunes, empates, usuarios aislados, grafos desconectados, la exclusión del propio usuario y de sus amigos, y la integridad después de eliminar.

**Resultado: 44 de 44 pruebas pasan.**

## 7. Conclusiones

- Las listas de adyacencia son la representación adecuada para una red social dispersa: ocupan O(V + E) y recorrer los amigos de un estudiante cuesta exactamente su grado.
- Mantener las listas ordenadas hace que los recorridos y las recomendaciones se puedan reproducir, y que la intersección de vecinos sea lineal.
- El BFS responde "¿a cuántas amistades de distancia está?". El DFS explora un camino completo antes de retroceder y sirve para alcanzabilidad y componentes.
- La puntuación por amigos en común es simple de explicar y de verificar a mano. Además cumple las reglas pedidas: no hay autorrecomendaciones, no se recomienda a amigos directos y no hay duplicados.
- **Mejoras posibles:** buscar vértices en O(1) con una tabla hash propia, ponderar la puntuación (por ejemplo por carrera o semestre) y guardar el grafo en un archivo.
