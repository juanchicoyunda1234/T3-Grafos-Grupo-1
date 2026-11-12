package modelo;

/**
 * Nodo de una lista de adyacencia.
 *
 * Cada NodoAdyacencia representa UNA arista que sale de un vertice hacia
 * "destino". Como el grafo es no dirigido, la amistad A-B se guarda dos
 * veces: un NodoAdyacencia en la lista de A (destino B) y otro en la lista
 * de B (destino A).
 */
public class NodoAdyacencia {

    private NodoVertice destino;
    private NodoAdyacencia siguiente;

    public NodoAdyacencia(NodoVertice destino) {
        this.destino = destino;
        this.siguiente = null;
    }

    public NodoVertice getDestino() {
        return destino;
    }

    public void setDestino(NodoVertice destino) {
        this.destino = destino;
    }

    public NodoAdyacencia getSiguiente() {
        return siguiente;
    }

    public void setSiguiente(NodoAdyacencia siguiente) {
        this.siguiente = siguiente;
    }
}
