package estructuras;

import java.util.ArrayList;
import modelo.NodoAdyacencia;
import modelo.NodoVertice;

/**
 * Grafo NO dirigido implementado con listas de adyacencia propias.
 *
 * Estructura:
 *   primero -> [Ana] -> [Jose] -> [Maria] -> ...      (lista de vertices)
 *                |
 *                +-> Jose -> Luis -> Pedro -> Sofia    (lista de adyacencia de Ana)
 *
 * Decisiones de diseno:
 *  - Los nombres son unicos sin distinguir mayusculas ("ana" == "Ana").
 *  - Cada lista de adyacencia se mantiene ORDENADA alfabeticamente. Asi BFS,
 *    DFS y las recomendaciones dan siempre el mismo resultado (deterministas)
 *    y los vecinos comunes se calculan intersecando dos listas ordenadas.
 *  - Cada vertice tiene un indice 0..n-1 para marcar visitados en arreglos.
 */
public class Grafo {

    private NodoVertice primero;
    private NodoVertice ultimo;
    private int cantidadVertices;
    private int cantidadAristas;

    public Grafo() {
        primero = null;
        ultimo = null;
        cantidadVertices = 0;
        cantidadAristas = 0;
    }

    // =====================================================================
    // PARTE 1 (feature/grafo): registrar, buscar, conectar y mostrar
    // =====================================================================

    /** Quita espacios de los extremos y deja un solo espacio entre palabras. */
    public static String normalizar(String nombre) {
        if (nombre == null) {
            return "";
        }
        return nombre.trim().replaceAll("\\s+", " ");
    }

    /** Busqueda lineal en la lista de vertices. O(V). */
    public NodoVertice buscarVertice(String nombre) {
        String buscado = normalizar(nombre);
        NodoVertice actual = primero;
        while (actual != null) {
            if (actual.getNombre().equalsIgnoreCase(buscado)) {
                return actual;
            }
            actual = actual.getSiguiente();
        }
        return null;
    }

    public boolean existeVertice(String nombre) {
        return buscarVertice(nombre) != null;
    }

    /**
     * RF01: registra un vertice al final de la lista. Rechaza vacios y
     * duplicados. O(V) por la busqueda del duplicado.
     */
    public boolean registrarVertice(String nombre) {
        String limpio = normalizar(nombre);
        if (limpio.isEmpty() || buscarVertice(limpio) != null) {
            return false;
        }
        NodoVertice nuevo = new NodoVertice(limpio, cantidadVertices);
        if (primero == null) {
            primero = nuevo;
        } else {
            ultimo.setSiguiente(nuevo);
        }
        ultimo = nuevo;
        cantidadVertices++;
        return true;
    }

    /** true si b aparece en la lista de adyacencia de a. O(grado(a)). */
    public boolean estanConectados(NodoVertice a, NodoVertice b) {
        NodoAdyacencia actual = a.getPrimerAdyacente();
        while (actual != null) {
            if (actual.getDestino() == b) {
                return true;
            }
            actual = actual.getSiguiente();
        }
        return false;
    }

    public boolean existeConexion(String a, String b) {
        NodoVertice va = buscarVertice(a);
        NodoVertice vb = buscarVertice(b);
        return va != null && vb != null && estanConectados(va, vb);
    }

    /**
     * RF02: crea la amistad a-b en ambos sentidos. Rechaza vertices
     * inexistentes, autoconexiones y conexiones repetidas.
     */
    public boolean crearConexion(String a, String b) {
        NodoVertice va = buscarVertice(a);
        NodoVertice vb = buscarVertice(b);
        if (va == null || vb == null || va == vb || estanConectados(va, vb)) {
            return false;
        }
        insertarOrdenado(va, vb);
        insertarOrdenado(vb, va);
        cantidadAristas++;
        return true;
    }

    /** Inserta "destino" en la lista de "origen" respetando el orden alfabetico. */
    private void insertarOrdenado(NodoVertice origen, NodoVertice destino) {
        NodoAdyacencia nuevo = new NodoAdyacencia(destino);
        NodoAdyacencia anterior = null;
        NodoAdyacencia actual = origen.getPrimerAdyacente();
        while (actual != null
                && actual.getDestino().getNombre().compareToIgnoreCase(destino.getNombre()) < 0) {
            anterior = actual;
            actual = actual.getSiguiente();
        }
        nuevo.setSiguiente(actual);
        if (anterior == null) {
            origen.setPrimerAdyacente(nuevo);
        } else {
            anterior.setSiguiente(nuevo);
        }
        origen.setGrado(origen.getGrado() + 1);
    }

    /** RF05: texto con la lista de adyacencia de cada vertice. */
    public String mostrar() {
        if (primero == null) {
            return "(el grafo esta vacio)\n";
        }
        StringBuilder sb = new StringBuilder();
        NodoVertice v = primero;
        while (v != null) {
            sb.append(String.format("%-10s (grado %d) -> ", v.getNombre(), v.getGrado()));
            NodoAdyacencia a = v.getPrimerAdyacente();
            if (a == null) {
                sb.append("sin conexiones");
            }
            while (a != null) {
                sb.append(a.getDestino().getNombre());
                if (a.getSiguiente() != null) {
                    sb.append(" -> ");
                }
                a = a.getSiguiente();
            }
            sb.append('\n');
            v = v.getSiguiente();
        }
        return sb.toString();
    }

    public NodoVertice getPrimero() {
        return primero;
    }

    public int getCantidadVertices() {
        return cantidadVertices;
    }

    public int getCantidadAristas() {
        return cantidadAristas;
    }

    public boolean estaVacio() {
        return cantidadVertices == 0;
    }

    // =====================================================================
    // PARTE 2 (feature/crud): eliminar, integridad y estadisticas
    // =====================================================================

    /** RF03: elimina la amistad a-b de AMBAS listas. */
    public boolean eliminarConexion(String a, String b) {
        NodoVertice va = buscarVertice(a);
        NodoVertice vb = buscarVertice(b);
        if (va == null || vb == null || !estanConectados(va, vb)) {
            return false;
        }
        quitarDeLista(va, vb);
        quitarDeLista(vb, va);
        cantidadAristas--;
        return true;
    }

    /** Quita "destino" de la lista de adyacencia de "origen". */
    private boolean quitarDeLista(NodoVertice origen, NodoVertice destino) {
        NodoAdyacencia anterior = null;
        NodoAdyacencia actual = origen.getPrimerAdyacente();
        while (actual != null && actual.getDestino() != destino) {
            anterior = actual;
            actual = actual.getSiguiente();
        }
        if (actual == null) {
            return false;
        }
        if (anterior == null) {
            origen.setPrimerAdyacente(actual.getSiguiente());
        } else {
            anterior.setSiguiente(actual.getSiguiente());
        }
        origen.setGrado(origen.getGrado() - 1);
        return true;
    }

    /**
     * RF04: elimina el vertice y TODAS sus conexiones.
     * 1) lo quita de la lista de cada vecino, 2) lo desenlaza de la lista
     * principal, 3) renumera los indices para que sigan siendo 0..n-1.
     */
    public boolean eliminarVertice(String nombre) {
        NodoVertice v = buscarVertice(nombre);
        if (v == null) {
            return false;
        }
        NodoAdyacencia a = v.getPrimerAdyacente();
        while (a != null) {
            quitarDeLista(a.getDestino(), v);
            cantidadAristas--;
            a = a.getSiguiente();
        }
        v.setPrimerAdyacente(null);
        v.setGrado(0);

        NodoVertice anterior = null;
        NodoVertice actual = primero;
        while (actual != v) {
            anterior = actual;
            actual = actual.getSiguiente();
        }
        if (anterior == null) {
            primero = v.getSiguiente();
        } else {
            anterior.setSiguiente(v.getSiguiente());
        }
        if (ultimo == v) {
            ultimo = anterior;
        }
        v.setSiguiente(null);
        cantidadVertices--;
        reindexar();
        return true;
    }

    private void reindexar() {
        int i = 0;
        NodoVertice actual = primero;
        while (actual != null) {
            actual.setIndice(i++);
            actual = actual.getSiguiente();
        }
    }

    /** true si el nodo es (por referencia) uno de los vertices de la lista. */
    private boolean perteneceAlGrafo(NodoVertice nodo) {
        NodoVertice actual = primero;
        while (actual != null) {
            if (actual == nodo) {
                return true;
            }
            actual = actual.getSiguiente();
        }
        return false;
    }

    /**
     * RF10: revisa la estructura completa. Devuelve la lista de problemas
     * encontrados; si esta vacia, el grafo esta integro.
     */
    public ArrayList<String> validarIntegridad() {
        ArrayList<String> problemas = new ArrayList<>();
        int contados = 0;
        int sumaGrados = 0;
        NodoVertice v = primero;
        while (v != null) {
            if (v.getIndice() != contados) {
                problemas.add("Indice incorrecto en " + v.getNombre());
            }
            contados++;

            NodoVertice w = v.getSiguiente();
            while (w != null) {
                if (w.getNombre().equalsIgnoreCase(v.getNombre())) {
                    problemas.add("Vertice duplicado: " + v.getNombre());
                }
                w = w.getSiguiente();
            }

            int largo = 0;
            NodoAdyacencia a = v.getPrimerAdyacente();
            while (a != null) {
                largo++;
                NodoVertice d = a.getDestino();
                if (d == v) {
                    problemas.add("Autoconexion en " + v.getNombre());
                } else if (!perteneceAlGrafo(d)) {
                    problemas.add("Referencia invalida en " + v.getNombre() + " -> " + d.getNombre());
                } else if (!estanConectados(d, v)) {
                    problemas.add("Arista no simetrica: " + v.getNombre() + " -> " + d.getNombre());
                }
                NodoAdyacencia b = a.getSiguiente();
                while (b != null) {
                    if (b.getDestino() == d) {
                        problemas.add("Arista duplicada: " + v.getNombre() + " -> " + d.getNombre());
                    }
                    b = b.getSiguiente();
                }
                a = a.getSiguiente();
            }
            if (largo != v.getGrado()) {
                problemas.add("Grado desactualizado en " + v.getNombre());
            }
            sumaGrados += largo;
            v = v.getSiguiente();
        }
        if (contados != cantidadVertices) {
            problemas.add("Contador de vertices incorrecto");
        }
        if (sumaGrados != 2 * cantidadAristas) {
            problemas.add("La suma de grados no es el doble de las aristas");
        }
        return problemas;
    }

    /** Parte numerica de la opcion 10 del menu. */
    public String estadisticas() {
        StringBuilder sb = new StringBuilder();
        sb.append("Vertices (estudiantes): ").append(cantidadVertices).append('\n');
        sb.append("Aristas (amistades):    ").append(cantidadAristas).append('\n');
        if (cantidadVertices == 0) {
            return sb.toString();
        }
        double promedio = (2.0 * cantidadAristas) / cantidadVertices;
        sb.append(String.format("Grado promedio:         %.2f%n", promedio));

        NodoVertice mayor = primero;
        StringBuilder aislados = new StringBuilder();
        NodoVertice v = primero;
        while (v != null) {
            if (v.getGrado() > mayor.getGrado()) {
                mayor = v;
            }
            if (v.getGrado() == 0) {
                if (aislados.length() > 0) {
                    aislados.append(", ");
                }
                aislados.append(v.getNombre());
            }
            v = v.getSiguiente();
        }
        sb.append("Mas conectado:          ").append(mayor.getNombre())
          .append(" (").append(mayor.getGrado()).append(" amigos)\n");
        sb.append("Aislados:               ")
          .append(aislados.length() == 0 ? "ninguno" : aislados.toString()).append('\n');
        return sb.toString();
    }
}
