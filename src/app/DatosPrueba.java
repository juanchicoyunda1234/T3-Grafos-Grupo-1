package app;

import estructuras.Grafo;

/**
 * Conjunto de datos de la red social universitaria UTA:
 * 13 estudiantes y 17 amistades.
 *
 * Esta pensado para probar:
 *  - el ejemplo del enunciado (Jose y Pedro comparten a Ana y Maria),
 *  - empates de puntuacion (Luis y Pedro tienen 2 puntos para Jose),
 *  - un grafo desconectado (Diego, Valeria y Andres forman otro grupo),
 *  - un usuario aislado (Mateo).
 *
 * Se escriben sin tildes para evitar problemas de codificacion en consola.
 */
public final class DatosPrueba {

    public static final String[] ESTUDIANTES = {
        "Ana", "Jose", "Maria", "Pedro", "Luis", "Sofia", "Carlos",
        "Lucia", "Camila", "Diego", "Valeria", "Andres", "Mateo"
    };

    public static final String[][] AMISTADES = {
        {"Jose", "Ana"}, {"Jose", "Maria"}, {"Pedro", "Ana"}, {"Pedro", "Maria"},
        {"Ana", "Luis"}, {"Maria", "Luis"}, {"Jose", "Sofia"}, {"Sofia", "Carlos"},
        {"Carlos", "Lucia"}, {"Luis", "Lucia"}, {"Ana", "Sofia"}, {"Pedro", "Carlos"},
        {"Maria", "Camila"}, {"Lucia", "Camila"},
        {"Diego", "Valeria"}, {"Valeria", "Andres"}, {"Andres", "Diego"}
    };

    private DatosPrueba() {
    }

    public static void cargar(Grafo grafo) {
        for (String estudiante : ESTUDIANTES) {
            grafo.registrarVertice(estudiante);
        }
        for (String[] amistad : AMISTADES) {
            grafo.crearConexion(amistad[0], amistad[1]);
        }
    }
}
