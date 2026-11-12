package algoritmos;

import estructuras.ColaVertices;
import estructuras.Grafo;
import java.util.ArrayList;
import modelo.NodoAdyacencia;
import modelo.NodoVertice;

/**
 * RF06 y RF07: recorridos BFS y DFS, y deteccion de componentes conectadas.
 *
 * Los vecinos se exploran en el orden de la lista de adyacencia, que el
 * grafo mantiene alfabetico. Por eso el orden de visita es determinista.
 * Los visitados se marcan en un boolean[] usando el indice de cada vertice.
 * Complejidad de ambos recorridos: O(V + E).
 */
public class Recorridos {

    private final Grafo grafo;

    public Recorridos(Grafo grafo) {
        this.grafo = grafo;
    }

    /** BFS desde "origen". Devuelve null si el origen no existe. */
    public ResultadoRecorrido bfs(String origen) {
        NodoVertice inicio = grafo.buscarVertice(origen);
        if (inicio == null) {
            return null;
        }
        boolean[] visitado = new boolean[grafo.getCantidadVertices()];
        return bfsDesde(inicio, visitado);
    }

    /**
     * BFS por niveles con cola propia: primero todos los vecinos directos
     * (nivel 1), luego los vecinos de esos (nivel 2), y asi.
     */
    private ResultadoRecorrido bfsDesde(NodoVertice inicio, boolean[] visitado) {
        int[] nivel = new int[visitado.length];
        ResultadoRecorrido resultado = new ResultadoRecorrido();
        ColaVertices cola = new ColaVertices();

        visitado[inicio.getIndice()] = true;
        nivel[inicio.getIndice()] = 0;
        cola.encolar(inicio);

        while (!cola.estaVacia()) {
            NodoVertice actual = cola.desencolar();
            resultado.agregar(actual.getNombre(), nivel[actual.getIndice()]);

            NodoAdyacencia a = actual.getPrimerAdyacente();
            while (a != null) {
                NodoVertice vecino = a.getDestino();
                if (!visitado[vecino.getIndice()]) {
                    visitado[vecino.getIndice()] = true; // se marca al ENCOLAR, no al sacar
                    nivel[vecino.getIndice()] = nivel[actual.getIndice()] + 1;
                    cola.encolar(vecino);
                }
                a = a.getSiguiente();
            }
        }
        return resultado;
    }

    /** DFS recursivo desde "origen". Devuelve null si el origen no existe. */
    public ResultadoRecorrido dfs(String origen) {
        NodoVertice inicio = grafo.buscarVertice(origen);
        if (inicio == null) {
            return null;
        }
        boolean[] visitado = new boolean[grafo.getCantidadVertices()];
        ResultadoRecorrido resultado = new ResultadoRecorrido();
        dfsRecursivo(inicio, 0, visitado, resultado);
        return resultado;
    }

    /**
     * Visita el vertice y baja por el PRIMER vecino no visitado hasta el
     * fondo antes de probar el siguiente. La pila es la pila de llamadas.
     */
    private void dfsRecursivo(NodoVertice actual, int profundidad,
                              boolean[] visitado, ResultadoRecorrido resultado) {
        visitado[actual.getIndice()] = true;
        resultado.agregar(actual.getNombre(), profundidad);

        NodoAdyacencia a = actual.getPrimerAdyacente();
        while (a != null) {
            NodoVertice vecino = a.getDestino();
            if (!visitado[vecino.getIndice()]) {
                dfsRecursivo(vecino, profundidad + 1, visitado, resultado);
            }
            a = a.getSiguiente();
        }
    }

    /**
     * Componentes conectadas: se lanza un BFS desde cada vertice que aun no
     * fue visitado; cada BFS descubre un componente completo. O(V + E).
     */
    public ArrayList<ArrayList<String>> componentesConectadas() {
        ArrayList<ArrayList<String>> componentes = new ArrayList<>();
        boolean[] visitado = new boolean[grafo.getCantidadVertices()];
        NodoVertice v = grafo.getPrimero();
        while (v != null) {
            if (!visitado[v.getIndice()]) {
                componentes.add(bfsDesde(v, visitado).getOrden());
            }
            v = v.getSiguiente();
        }
        return componentes;
    }
}
