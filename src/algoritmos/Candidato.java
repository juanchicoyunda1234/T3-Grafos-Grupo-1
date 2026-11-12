package algoritmos;

import java.util.ArrayList;

/**
 * Candidato a nueva amistad. Su puntuacion es la cantidad de amigos en
 * comun con el usuario consultado (reto especial del Grupo 1: decir
 * cuantos y CUALES son).
 */
public class Candidato {

    private final String nombre;
    private final ArrayList<String> amigosComunes = new ArrayList<>();

    public Candidato(String nombre) {
        this.nombre = nombre;
    }

    public void agregarAmigoComun(String amigo) {
        amigosComunes.add(amigo);
    }

    public String getNombre() {
        return nombre;
    }

    public ArrayList<String> getAmigosComunes() {
        return amigosComunes;
    }

    public int getPuntuacion() {
        return amigosComunes.size();
    }

    /**
     * Criterio de orden: mayor puntuacion primero; si empatan, orden
     * alfabetico del nombre (desempate determinista).
     */
    public boolean vaAntesQue(Candidato otro) {
        if (getPuntuacion() != otro.getPuntuacion()) {
            return getPuntuacion() > otro.getPuntuacion();
        }
        return nombre.compareToIgnoreCase(otro.nombre) < 0;
    }

    @Override
    public String toString() {
        return nombre + " - " + getPuntuacion() + " punto(s) - amigos en comun: "
                + String.join(", ", amigosComunes);
    }
}
