package modelo;

/**
 * Nodo de la lista principal de vertices (un estudiante de la red UTA).
 *
 * Cada vertice guarda:
 *  - su nombre (identificador unico, sin distinguir mayusculas),
 *  - la cabeza de SU lista de adyacencia (sus amistades),
 *  - el enlace al siguiente vertice de la lista principal,
 *  - su grado (cantidad de amigos) para no recorrer la lista cada vez,
 *  - un indice 0..n-1 que usan BFS, DFS y el recomendador para marcar
 *    visitados en arreglos (sin HashMap ni HashSet).
 */
public class NodoVertice {

    private String nombre;
    private NodoAdyacencia primerAdyacente;
    private NodoVertice siguiente;
    private int grado;
    private int indice;

    public NodoVertice(String nombre, int indice) {
        this.nombre = nombre;
        this.indice = indice;
        this.primerAdyacente = null;
        this.siguiente = null;
        this.grado = 0;
    }

    public String getNombre() {
        return nombre;
    }

    public NodoAdyacencia getPrimerAdyacente() {
        return primerAdyacente;
    }

    public void setPrimerAdyacente(NodoAdyacencia primerAdyacente) {
        this.primerAdyacente = primerAdyacente;
    }

    public NodoVertice getSiguiente() {
        return siguiente;
    }

    public void setSiguiente(NodoVertice siguiente) {
        this.siguiente = siguiente;
    }

    public int getGrado() {
        return grado;
    }

    public void setGrado(int grado) {
        this.grado = grado;
    }

    public int getIndice() {
        return indice;
    }

    public void setIndice(int indice) {
        this.indice = indice;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
