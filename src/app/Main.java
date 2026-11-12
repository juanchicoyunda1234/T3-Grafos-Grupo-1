package app;

import algoritmos.Candidato;
import algoritmos.Recomendador;
import algoritmos.Recorridos;
import algoritmos.ResultadoRecorrido;
import estructuras.Grafo;
import java.io.Console;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * T3 - Laboratorio de grafos y prediccion de conexiones.
 * Grupo 1: Red social universitaria UTA.
 */
public class Main {

    private static final String PATRON_NOMBRE = "[\\p{L}][\\p{L} ]{1,39}";

    private static final Scanner ENTRADA = crearScanner();
    private static final Grafo GRAFO = new Grafo();
    private static final Recorridos RECORRIDOS = new Recorridos(GRAFO);
    private static final Recomendador RECOMENDADOR = new Recomendador(GRAFO);

    public static void main(String[] args) {
        System.out.println("==============================================");
        System.out.println("  RED SOCIAL UNIVERSITARIA UTA - Grupo 1");
        System.out.println("  Recomendacion de amistades con grafos");
        System.out.println("==============================================");

        String cargar = leerLinea("Cargar los datos de prueba (13 estudiantes, 17 amistades)? (s/n): ");
        if (cargar != null && cargar.trim().equalsIgnoreCase("s")) {
            DatosPrueba.cargar(GRAFO);
            System.out.println("Datos cargados.");
        }

        int opcion;
        do {
            mostrarMenu();
            opcion = leerEntero("Opcion: ", 0, 10);
            System.out.println();
            switch (opcion) {
                case 1 -> registrarEstudiante();
                case 2 -> crearConexion();
                case 3 -> eliminarConexion();
                case 4 -> eliminarEstudiante();
                case 5 -> visualizarGrafo();
                case 6 -> recorrerBfs();
                case 7 -> recorrerDfs();
                case 8 -> mostrarVecinosComunes();
                case 9 -> recomendarConexiones();
                case 10 -> mostrarEstadisticas();
                default -> System.out.println("Hasta luego.");
            }
        } while (opcion != 0);
    }

    private static void mostrarMenu() {
        System.out.println();
        System.out.println("---------------- MENU ----------------");
        System.out.println(" 1. Registrar usuario o entidad");
        System.out.println(" 2. Crear conexion");
        System.out.println(" 3. Eliminar conexion");
        System.out.println(" 4. Eliminar usuario o entidad");
        System.out.println(" 5. Visualizar grafo");
        System.out.println(" 6. Recorrer mediante BFS");
        System.out.println(" 7. Recorrer mediante DFS");
        System.out.println(" 8. Mostrar vecinos comunes");
        System.out.println(" 9. Recomendar conexiones");
        System.out.println("10. Mostrar estadisticas");
        System.out.println(" 0. Salir");
        System.out.println("--------------------------------------");
    }

    // ------------------------------------------------------------ opciones

    private static void registrarEstudiante() {
        String nombre = leerNombreNuevo("Nombre del estudiante: ");
        if (nombre == null) {
            return;
        }
        if (GRAFO.registrarVertice(nombre)) {
            System.out.println("Registrado: " + Grafo.normalizar(nombre));
        } else {
            System.out.println("Error: ya existe un estudiante con ese nombre.");
        }
    }

    private static void crearConexion() {
        String[] par = leerParExistente();
        if (par == null) {
            return;
        }
        if (GRAFO.existeConexion(par[0], par[1])) {
            System.out.println("Error: ya son amigos.");
        } else if (GRAFO.crearConexion(par[0], par[1])) {
            System.out.println("Amistad creada: " + nombreReal(par[0]) + " <-> " + nombreReal(par[1]));
        } else {
            System.out.println("Error: no se pudo crear la amistad.");
        }
    }

    private static void eliminarConexion() {
        String[] par = leerParExistente();
        if (par == null) {
            return;
        }
        if (GRAFO.eliminarConexion(par[0], par[1])) {
            System.out.println("Amistad eliminada en ambos sentidos.");
        } else {
            System.out.println("Error: esos estudiantes no son amigos.");
        }
    }

    private static void eliminarEstudiante() {
        if (avisarSiVacio()) {
            return;
        }
        String nombre = leerNombreExistente("Estudiante a eliminar: ");
        if (nombre == null) {
            return;
        }
        String real = nombreReal(nombre);
        int amistades = GRAFO.buscarVertice(nombre).getGrado();
        GRAFO.eliminarVertice(nombre);
        System.out.println("Eliminado " + real + " junto con " + amistades + " amistad(es).");
    }

    private static void visualizarGrafo() {
        System.out.println("Listas de adyacencia (estudiante -> amigos):");
        System.out.print(GRAFO.mostrar());
    }

    private static void recorrerBfs() {
        if (avisarSiVacio()) {
            return;
        }
        String origen = leerNombreExistente("Estudiante de origen: ");
        if (origen == null) {
            return;
        }
        ResultadoRecorrido r = RECORRIDOS.bfs(origen);
        System.out.println("BFS desde " + nombreReal(origen) + ":");
        System.out.println("  Orden: " + r.ordenComoTexto());
        System.out.println(r.porNiveles());
        avisarNoAlcanzados(r);
    }

    private static void recorrerDfs() {
        if (avisarSiVacio()) {
            return;
        }
        String origen = leerNombreExistente("Estudiante de origen: ");
        if (origen == null) {
            return;
        }
        ResultadoRecorrido r = RECORRIDOS.dfs(origen);
        System.out.println("DFS desde " + nombreReal(origen) + ":");
        System.out.println("  Orden: " + r.ordenComoTexto());
        System.out.println("  Profundidad: " + r.conProfundidad());
        avisarNoAlcanzados(r);
    }

    private static void mostrarVecinosComunes() {
        String[] par = leerParExistente();
        if (par == null) {
            return;
        }
        ArrayList<String> comunes = RECOMENDADOR.vecinosComunes(par[0], par[1]);
        System.out.println("Amigos en comun entre " + nombreReal(par[0]) + " y " + nombreReal(par[1])
                + ": " + comunes.size() + " punto(s)");
        System.out.println("  " + (comunes.isEmpty() ? "(ninguno)" : String.join(", ", comunes)));
    }

    private static void recomendarConexiones() {
        if (avisarSiVacio()) {
            return;
        }
        String nombre = leerNombreExistente("Recomendar amistades para: ");
        if (nombre == null) {
            return;
        }
        ArrayList<Candidato> mejores = RECOMENDADOR.recomendar(nombre);
        System.out.println("Top " + Recomendador.MAX_RECOMENDACIONES + " para " + nombreReal(nombre) + ":");
        if (mejores.isEmpty()) {
            System.out.println("  Sin candidatos (no tiene amigos de amigos que aun no sean sus amigos).");
            return;
        }
        for (int i = 0; i < mejores.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + mejores.get(i));
        }
    }

    private static void mostrarEstadisticas() {
        System.out.print(GRAFO.estadisticas());
        if (!GRAFO.estaVacio()) {
            ArrayList<ArrayList<String>> componentes = RECORRIDOS.componentesConectadas();
            System.out.println("Componentes conectadas:  " + componentes.size());
            for (int i = 0; i < componentes.size(); i++) {
                System.out.println("  " + (i + 1) + ") " + String.join(", ", componentes.get(i)));
            }
        }
        ArrayList<String> problemas = GRAFO.validarIntegridad();
        if (problemas.isEmpty()) {
            System.out.println("Integridad (RF10):       OK, sin duplicados ni referencias invalidas");
        } else {
            System.out.println("Integridad (RF10):       " + problemas.size() + " problema(s)");
            for (String p : problemas) {
                System.out.println("  - " + p);
            }
        }
    }

    // ---------------------------------------------------------- auxiliares

    private static void avisarNoAlcanzados(ResultadoRecorrido r) {
        int faltan = GRAFO.getCantidadVertices() - r.tamanio();
        if (faltan > 0) {
            System.out.println("  (" + faltan + " estudiante(s) no alcanzables: el grafo esta desconectado)");
        }
    }

    private static boolean avisarSiVacio() {
        if (GRAFO.estaVacio()) {
            System.out.println("El grafo esta vacio. Registre estudiantes primero.");
            return true;
        }
        return false;
    }

    private static String nombreReal(String nombre) {
        return GRAFO.buscarVertice(nombre).getNombre();
    }

    /** Pide dos estudiantes existentes y distintos. null si se cancela. */
    private static String[] leerParExistente() {
        if (GRAFO.getCantidadVertices() < 2) {
            System.out.println("Se necesitan al menos 2 estudiantes registrados.");
            return null;
        }
        String a = leerNombreExistente("Primer estudiante: ");
        if (a == null) {
            return null;
        }
        String b = leerNombreExistente("Segundo estudiante: ");
        if (b == null) {
            return null;
        }
        if (Grafo.normalizar(a).equalsIgnoreCase(Grafo.normalizar(b))) {
            System.out.println("Error: debe elegir dos estudiantes distintos.");
            return null;
        }
        return new String[] {a, b};
    }

    /** Nombre con formato valido. Enter vacio cancela. */
    private static String leerNombreNuevo(String mensaje) {
        while (true) {
            String linea = leerLinea(mensaje);
            if (linea == null) {
                return null;
            }
            String nombre = Grafo.normalizar(linea);
            if (nombre.isEmpty()) {
                System.out.println("Operacion cancelada.");
                return null;
            }
            if (nombre.matches(PATRON_NOMBRE)) {
                return nombre;
            }
            System.out.println("Nombre invalido: solo letras y espacios, de 2 a 40 caracteres.");
        }
    }

    /** Nombre que debe existir en el grafo. Enter vacio cancela. */
    private static String leerNombreExistente(String mensaje) {
        while (true) {
            String nombre = leerNombreNuevo(mensaje);
            if (nombre == null) {
                return null;
            }
            if (GRAFO.existeVertice(nombre)) {
                return nombre;
            }
            System.out.println("No existe el estudiante '" + nombre + "'. Intente de nuevo (Enter cancela).");
        }
    }

    private static int leerEntero(String mensaje, int min, int max) {
        while (true) {
            String linea = leerLinea(mensaje);
            if (linea == null) {
                return 0; // fin de la entrada: salir
            }
            try {
                int valor = Integer.parseInt(linea.trim());
                if (valor >= min && valor <= max) {
                    return valor;
                }
            } catch (NumberFormatException e) {
                // se informa abajo
            }
            System.out.println("Opcion invalida: ingrese un numero entre " + min + " y " + max + ".");
        }
    }

    private static String leerLinea(String mensaje) {
        System.out.print(mensaje);
        if (!ENTRADA.hasNextLine()) {
            return null;
        }
        return ENTRADA.nextLine().replace("﻿", ""); // quita el BOM si la entrada viene redirigida
    }

    private static Scanner crearScanner() {
        Console consola = System.console();
        return consola != null ? new Scanner(System.in, consola.charset()) : new Scanner(System.in);
    }
}
