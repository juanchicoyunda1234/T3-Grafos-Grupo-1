package estructuras;

import modelo.NodoVertice;

/**
 * Cola FIFO propia (lista enlazada con frente y fin) que usa el BFS.
 * encolar y desencolar son O(1).
 */
public class ColaVertices {

    private static class Nodo {
        private final NodoVertice dato;
        private Nodo siguiente;

        Nodo(NodoVertice dato) {
            this.dato = dato;
        }
    }

    private Nodo frente;
    private Nodo fin;

    public void encolar(NodoVertice vertice) {
        Nodo nuevo = new Nodo(vertice);
        if (fin == null) {
            frente = nuevo;
        } else {
            fin.siguiente = nuevo;
        }
        fin = nuevo;
    }

    public NodoVertice desencolar() {
        if (frente == null) {
            return null;
        }
        NodoVertice dato = frente.dato;
        frente = frente.siguiente;
        if (frente == null) {
            fin = null;
        }
        return dato;
    }

    public boolean estaVacia() {
        return frente == null;
    }
}
