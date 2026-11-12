package algoritmos;

import estructuras.Grafo;
import java.util.ArrayList;
import modelo.NodoAdyacencia;
import modelo.NodoVertice;

/**
 * RF08 y RF09: vecinos comunes y recomendacion de amistades.
 *
 * Idea: un "amigo de mi amigo" que todavia no es mi amigo es un candidato.
 * Cada amigo que compartimos le suma 1 punto.
 */
public class Recomendador {

    public static final int MAX_RECOMENDACIONES = 3;

    private final Grafo grafo;

    public Recomendador(Grafo grafo) {
        this.grafo = grafo;
    }

    /**
     * RF08: amigos que a y b tienen en comun. Como las dos listas de
     * adyacencia estan ordenadas, se intersecan avanzando dos punteros
     * (como en el merge de MergeSort): O(grado(a) + grado(b)).
     * Devuelve null si alguno no existe.
     */
    public ArrayList<String> vecinosComunes(String a, String b) {
        NodoVertice va = grafo.buscarVertice(a);
        NodoVertice vb = grafo.buscarVertice(b);
        if (va == null || vb == null) {
            return null;
        }
        ArrayList<String> comunes = new ArrayList<>();
        NodoAdyacencia x = va.getPrimerAdyacente();
        NodoAdyacencia y = vb.getPrimerAdyacente();
        while (x != null && y != null) {
            int cmp = x.getDestino().getNombre().compareToIgnoreCase(y.getDestino().getNombre());
            if (cmp == 0) {
                comunes.add(x.getDestino().getNombre());
                x = x.getSiguiente();
                y = y.getSiguiente();
            } else if (cmp < 0) {
                x = x.getSiguiente();
            } else {
                y = y.getSiguiente();
            }
        }
        return comunes;
    }

    /**
     * Todos los candidatos para "nombre", ya ordenados.
     * Devuelve null si el usuario no existe.
     *
     * Reglas (requisito 6):
     *  - nunca el propio usuario (autorrecomendacion),
     *  - nunca un amigo directo (conexion existente),
     *  - nunca el mismo candidato dos veces (un Candidato por indice).
     *
     * Complejidad: suma de los grados de los amigos del usuario, mas el
     * ordenamiento por insercion de los candidatos.
     */
    public ArrayList<Candidato> candidatos(String nombre) {
        NodoVertice usuario = grafo.buscarVertice(nombre);
        if (usuario == null) {
            return null;
        }
        int n = grafo.getCantidadVertices();
        boolean[] esAmigo = new boolean[n];
        Candidato[] porIndice = new Candidato[n];
        ArrayList<Candidato> lista = new ArrayList<>();

        NodoAdyacencia a = usuario.getPrimerAdyacente();
        while (a != null) {
            esAmigo[a.getDestino().getIndice()] = true;
            a = a.getSiguiente();
        }

        a = usuario.getPrimerAdyacente();
        while (a != null) {
            NodoVertice amigo = a.getDestino();
            NodoAdyacencia b = amigo.getPrimerAdyacente();
            while (b != null) {
                NodoVertice posible = b.getDestino();
                int i = posible.getIndice();
                if (posible != usuario && !esAmigo[i]) {
                    if (porIndice[i] == null) {
                        porIndice[i] = new Candidato(posible.getNombre());
                        lista.add(porIndice[i]);
                    }
                    porIndice[i].agregarAmigoComun(amigo.getNombre());
                }
                b = b.getSiguiente();
            }
            a = a.getSiguiente();
        }

        ordenar(lista);
        return lista;
    }

    /** RF09: los tres mejores candidatos (o menos, si no hay tantos). */
    public ArrayList<Candidato> recomendar(String nombre) {
        ArrayList<Candidato> todos = candidatos(nombre);
        if (todos == null) {
            return null;
        }
        ArrayList<Candidato> mejores = new ArrayList<>();
        for (int i = 0; i < todos.size() && i < MAX_RECOMENDACIONES; i++) {
            mejores.add(todos.get(i));
        }
        return mejores;
    }

    /** Ordenamiento por insercion con el criterio de Candidato.vaAntesQue. */
    private void ordenar(ArrayList<Candidato> lista) {
        for (int i = 1; i < lista.size(); i++) {
            Candidato clave = lista.get(i);
            int j = i - 1;
            while (j >= 0 && clave.vaAntesQue(lista.get(j))) {
                lista.set(j + 1, lista.get(j));
                j--;
            }
            lista.set(j + 1, clave);
        }
    }
}
