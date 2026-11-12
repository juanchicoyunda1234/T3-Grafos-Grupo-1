# Diagrama de clases — T3 Grafos, Grupo 1

[⬅ Volver al README](../README.md)

GitHub dibuja este diagrama automáticamente porque está escrito en Mermaid.

```mermaid
classDiagram
    direction LR

    namespace modelo {
        class NodoVertice {
            -String nombre
            -NodoAdyacencia primerAdyacente
            -NodoVertice siguiente
            -int grado
            -int indice
            +getNombre() String
            +getPrimerAdyacente() NodoAdyacencia
            +getSiguiente() NodoVertice
            +getGrado() int
            +getIndice() int
        }
        class NodoAdyacencia {
            -NodoVertice destino
            -NodoAdyacencia siguiente
            +getDestino() NodoVertice
            +getSiguiente() NodoAdyacencia
        }
    }

    namespace estructuras {
        class Grafo {
            -NodoVertice primero
            -NodoVertice ultimo
            -int cantidadVertices
            -int cantidadAristas
            +registrarVertice(String) boolean
            +buscarVertice(String) NodoVertice
            +crearConexion(String, String) boolean
            +eliminarConexion(String, String) boolean
            +eliminarVertice(String) boolean
            +existeConexion(String, String) boolean
            +mostrar() String
            +validarIntegridad() ArrayList~String~
            +estadisticas() String
            -insertarOrdenado(NodoVertice, NodoVertice) void
            -quitarDeLista(NodoVertice, NodoVertice) boolean
            -reindexar() void
        }
        class ColaVertices {
            -Nodo frente
            -Nodo fin
            +encolar(NodoVertice) void
            +desencolar() NodoVertice
            +estaVacia() boolean
        }
    }

    namespace algoritmos {
        class Recorridos {
            -Grafo grafo
            +bfs(String) ResultadoRecorrido
            +dfs(String) ResultadoRecorrido
            +componentesConectadas() ArrayList
            -dfsRecursivo(NodoVertice, int, boolean[], ResultadoRecorrido) void
        }
        class ResultadoRecorrido {
            -ArrayList~String~ orden
            -ArrayList~Integer~ niveles
            +ordenComoTexto() String
            +porNiveles() String
            +conProfundidad() String
        }
        class Recomendador {
            +int MAX_RECOMENDACIONES = 3
            -Grafo grafo
            +vecinosComunes(String, String) ArrayList~String~
            +candidatos(String) ArrayList~Candidato~
            +recomendar(String) ArrayList~Candidato~
            -ordenar(ArrayList~Candidato~) void
        }
        class Candidato {
            -String nombre
            -ArrayList~String~ amigosComunes
            +getPuntuacion() int
            +vaAntesQue(Candidato) boolean
        }
    }

    namespace app {
        class Main {
            +main(String[]) void
        }
        class DatosPrueba {
            +cargar(Grafo) void
        }
    }

    Grafo "1" o-- "*" NodoVertice : lista de vertices
    NodoVertice "1" o-- "*" NodoAdyacencia : lista de adyacencia
    NodoAdyacencia --> NodoVertice : destino
    Recorridos --> Grafo
    Recorridos ..> ColaVertices : usa en BFS
    Recorridos ..> ResultadoRecorrido : crea
    Recomendador --> Grafo
    Recomendador ..> Candidato : crea y ordena
    Main --> Grafo
    Main --> Recorridos
    Main --> Recomendador
    Main ..> DatosPrueba
```

## Lectura del diagrama

| Relación | Significado |
|---|---|
| `Grafo` → `NodoVertice` | El grafo es una lista enlazada de vértices (`primero` → `siguiente` → …). |
| `NodoVertice` → `NodoAdyacencia` | Cada vértice es dueño de su lista de adyacencia: sus amistades. |
| `NodoAdyacencia` → `NodoVertice` | Cada arista apunta al vértice amigo (`destino`), no a una copia. |
| `Recorridos` y `Recomendador` → `Grafo` | Los algoritmos están separados de la estructura y solo la leen. |
| `Main` | Capa de aplicación: el menú usa el grafo y los dos algoritmos. |

Capas: **modelo** (datos) → **estructuras** (grafo y cola) → **algoritmos** (BFS, DFS, recomendación) → **app** (consola).
