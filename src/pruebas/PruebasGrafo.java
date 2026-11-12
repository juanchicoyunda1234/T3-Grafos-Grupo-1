package pruebas;

import algoritmos.Candidato;
import algoritmos.Recomendador;
import algoritmos.Recorridos;
import app.DatosPrueba;
import estructuras.Grafo;
import java.util.ArrayList;

/**
 * Pruebas reproducibles sin librerias externas (sin JUnit).
 * Cada caso imprime PASA o FALLA con lo esperado y lo obtenido.
 * Ejecutar:  java -cp bin pruebas.PruebasGrafo
 */
public class PruebasGrafo {

    private static int pasadas = 0;
    private static int fallidas = 0;

    public static void main(String[] args) {
        System.out.println("=== Pruebas T3 Grafos - Grupo 1 ===\n");
        pruebasVertices();
        pruebasAristas();
        pruebasEliminacion();
        pruebasRecorridos();
        pruebasRecomendacion();
        pruebasIntegridadYEstadisticas();

        System.out.println("\nResultado: " + pasadas + " pasadas, " + fallidas + " fallidas.");
        if (fallidas > 0) {
            System.exit(1);
        }
    }

    private static Grafo grafoDePrueba() {
        Grafo g = new Grafo();
        DatosPrueba.cargar(g);
        return g;
    }

    // ------------------------------------------------------------- RF01

    private static void pruebasVertices() {
        Grafo g = new Grafo();
        verificar("CP01 Registrar vertice nuevo", true, g.registrarVertice("Ana"));
        verificar("CP02 Rechazar duplicado (mayusculas distintas)", false, g.registrarVertice("  ANA "));
        verificar("CP03 Rechazar nombre vacio", false, g.registrarVertice("   "));
        verificar("CP04 Buscar vertice existente", "Ana", g.buscarVertice("ana").getNombre());
        verificar("CP05 Buscar vertice inexistente", null, g.buscarVertice("Zoe"));
        verificar("CP06 Cantidad de vertices", 1, g.getCantidadVertices());
    }

    // ------------------------------------------------------------- RF02

    private static void pruebasAristas() {
        Grafo g = new Grafo();
        g.registrarVertice("Ana");
        g.registrarVertice("Jose");
        verificar("CP07 Crear conexion valida", true, g.crearConexion("Ana", "Jose"));
        verificar("CP08 Conexion bidireccional (Jose ve a Ana)", true, g.existeConexion("Jose", "Ana"));
        verificar("CP09 Rechazar conexion repetida (orden inverso)", false, g.crearConexion("Jose", "Ana"));
        verificar("CP10 Rechazar autoconexion", false, g.crearConexion("Ana", "Ana"));
        verificar("CP11 Rechazar conexion con inexistente", false, g.crearConexion("Ana", "Zoe"));
        verificar("CP12 Cantidad de aristas", 1, g.getCantidadAristas());

        Grafo d = grafoDePrueba();
        verificar("CP13 Datos de prueba: 13 vertices", 13, d.getCantidadVertices());
        verificar("CP14 Datos de prueba: 17 aristas", 17, d.getCantidadAristas());
    }

    // --------------------------------------------------------- RF03 y RF04

    private static void pruebasEliminacion() {
        Grafo g = grafoDePrueba();
        verificar("CP15 Eliminar conexion Jose-Ana", true, g.eliminarConexion("Jose", "Ana"));
        verificar("CP16 Ya no existe Jose->Ana", false, g.existeConexion("Jose", "Ana"));
        verificar("CP17 Ya no existe Ana->Jose", false, g.existeConexion("Ana", "Jose"));
        verificar("CP18 Grado de Ana baja a 3", 3, g.buscarVertice("Ana").getGrado());
        verificar("CP19 Eliminar conexion inexistente", false, g.eliminarConexion("Jose", "Ana"));

        verificar("CP20 Eliminar vertice Maria", true, g.eliminarVertice("Maria"));
        verificar("CP21 Maria ya no existe", false, g.existeVertice("Maria"));
        verificar("CP22 Aristas 17 - 1 - 4 = 12", 12, g.getCantidadAristas());
        verificar("CP23 Nadie apunta a Maria (integridad)", 0, g.validarIntegridad().size());
        verificar("CP24 Eliminar vertice inexistente", false, g.eliminarVertice("Maria"));
    }

    // --------------------------------------------------------- RF06 y RF07

    private static void pruebasRecorridos() {
        Grafo g = grafoDePrueba();
        Recorridos r = new Recorridos(g);
        verificar("CP25 BFS desde Jose",
                "Jose -> Ana -> Maria -> Sofia -> Luis -> Pedro -> Camila -> Carlos -> Lucia",
                r.bfs("Jose").ordenComoTexto());
        verificar("CP26 BFS niveles desde Jose", "[0, 1, 1, 1, 2, 2, 2, 2, 3]",
                r.bfs("Jose").getNiveles().toString());
        verificar("CP27 DFS desde Jose",
                "Jose -> Ana -> Luis -> Lucia -> Camila -> Maria -> Pedro -> Carlos -> Sofia",
                r.dfs("Jose").ordenComoTexto());
        verificar("CP28 BFS no cruza a otro componente", "Diego -> Andres -> Valeria",
                r.bfs("Diego").ordenComoTexto());
        verificar("CP29 BFS de aislado solo se visita a si mismo", "Mateo", r.bfs("Mateo").ordenComoTexto());
        verificar("CP30 Recorrido desde inexistente devuelve null", null, r.dfs("Zoe"));
        verificar("CP31 Tres componentes conectadas", 3, r.componentesConectadas().size());
    }

    // --------------------------------------------------------- RF08 y RF09

    private static void pruebasRecomendacion() {
        Grafo g = grafoDePrueba();
        Recomendador rec = new Recomendador(g);

        verificar("CP32 Vecinos comunes Jose y Pedro (ejemplo del enunciado)", "[Ana, Maria]",
                rec.vecinosComunes("Jose", "Pedro").toString());
        verificar("CP33 Vecinos comunes sin coincidencias", "[]",
                rec.vecinosComunes("Jose", "Diego").toString());

        ArrayList<Candidato> top = rec.recomendar("Jose");
        verificar("CP34 Top 3 de Jose (empate Luis/Pedro por orden alfabetico)",
                "Luis:2:[Ana, Maria] | Pedro:2:[Ana, Maria] | Camila:1:[Maria]", resumen(top));
        verificar("CP35 Jose no se recomienda a si mismo ni a sus amigos", true,
                noContiene(rec.candidatos("Jose"), "Jose", "Ana", "Maria", "Sofia"));

        g.crearConexion("Jose", "Pedro");
        ArrayList<Candidato> despues = rec.recomendar("Jose");
        verificar("CP36 Pedro desaparece tras crear Jose-Pedro", true, noContiene(despues, "Pedro"));
        verificar("CP37 Nuevo top 3 de Jose",
                "Carlos:2:[Pedro, Sofia] | Luis:2:[Ana, Maria] | Camila:1:[Maria]", resumen(despues));

        verificar("CP38 Usuario aislado sin candidatos", 0, rec.recomendar("Mateo").size());
        verificar("CP39 Triangulo cerrado sin candidatos", 0, rec.recomendar("Diego").size());
        verificar("CP40 Candidatos sin duplicados", true, sinDuplicados(rec.candidatos("Lucia")));
    }

    // --------------------------------------------------------------- RF10

    private static void pruebasIntegridadYEstadisticas() {
        Grafo g = grafoDePrueba();
        verificar("CP41 Integridad del grafo inicial", 0, g.validarIntegridad().size());
        g.eliminarConexion("Lucia", "Camila");
        g.eliminarVertice("Ana");
        g.registrarVertice("Nuevo Estudiante");
        g.crearConexion("Nuevo Estudiante", "Mateo");
        verificar("CP42 Integridad tras altas y bajas mezcladas", 0, g.validarIntegridad().size());
        verificar("CP43 Indices consecutivos tras eliminar (ultimo = n-1)",
                g.getCantidadVertices() - 1, g.buscarVertice("Nuevo Estudiante").getIndice());
        verificar("CP44 Estadisticas reportan aislados", true,
                grafoDePrueba().estadisticas().contains("Aislados:               Mateo"));
    }

    // ---------------------------------------------------------- utilidades

    private static String resumen(ArrayList<Candidato> lista) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < lista.size(); i++) {
            if (i > 0) {
                sb.append(" | ");
            }
            Candidato c = lista.get(i);
            sb.append(c.getNombre()).append(':').append(c.getPuntuacion()).append(':')
              .append(c.getAmigosComunes());
        }
        return sb.toString();
    }

    private static boolean noContiene(ArrayList<Candidato> lista, String... nombres) {
        for (Candidato c : lista) {
            for (String n : nombres) {
                if (c.getNombre().equalsIgnoreCase(n)) {
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean sinDuplicados(ArrayList<Candidato> lista) {
        for (int i = 0; i < lista.size(); i++) {
            for (int j = i + 1; j < lista.size(); j++) {
                if (lista.get(i).getNombre().equalsIgnoreCase(lista.get(j).getNombre())) {
                    return false;
                }
            }
        }
        return true;
    }

    private static void verificar(String caso, Object esperado, Object obtenido) {
        boolean ok = (esperado == null) ? obtenido == null : esperado.equals(obtenido);
        if (ok) {
            pasadas++;
            System.out.println("[PASA]  " + caso);
        } else {
            fallidas++;
            System.out.println("[FALLA] " + caso);
            System.out.println("        esperado: " + esperado);
            System.out.println("        obtenido: " + obtenido);
        }
    }
}
