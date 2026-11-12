package algoritmos;

import java.util.ArrayList;

/**
 * Resultado de un recorrido: orden de visita y, para cada vertice visitado,
 * su nivel (BFS) o su profundidad en el arbol de llamadas (DFS).
 */
public class ResultadoRecorrido {

    private final ArrayList<String> orden = new ArrayList<>();
    private final ArrayList<Integer> niveles = new ArrayList<>();

    public void agregar(String nombre, int nivel) {
        orden.add(nombre);
        niveles.add(nivel);
    }

    public ArrayList<String> getOrden() {
        return orden;
    }

    public ArrayList<Integer> getNiveles() {
        return niveles;
    }

    public int tamanio() {
        return orden.size();
    }

    /** "Jose -> Ana -> Maria ..." */
    public String ordenComoTexto() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < orden.size(); i++) {
            if (i > 0) {
                sb.append(" -> ");
            }
            sb.append(orden.get(i));
        }
        return sb.toString();
    }

    /**
     * Agrupa por nivel. En BFS los niveles salen en orden no decreciente,
     * por eso basta con agrupar los consecutivos.
     */
    public String porNiveles() {
        StringBuilder sb = new StringBuilder();
        int nivelActual = -1;
        for (int i = 0; i < orden.size(); i++) {
            int nivel = niveles.get(i);
            if (nivel != nivelActual) {
                if (nivelActual != -1) {
                    sb.append('\n');
                }
                sb.append("  Nivel ").append(nivel).append(": ");
                nivelActual = nivel;
            } else {
                sb.append(", ");
            }
            sb.append(orden.get(i));
        }
        return sb.toString();
    }

    /** "Jose(0) Ana(1) Luis(2) ..." para el DFS. */
    public String conProfundidad() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < orden.size(); i++) {
            if (i > 0) {
                sb.append("  ");
            }
            sb.append(orden.get(i)).append('(').append(niveles.get(i)).append(')');
        }
        return sb.toString();
    }
}
