package modelos;

import java.io.Serializable;
import java.util.ArrayList;

public class Equipo implements Serializable {
    String nombre;
    ArrayList<Jugador> jugadores;


    public Equipo(String nombre, ArrayList<Jugador> jugadores) {
        this.nombre = nombre;
        this.jugadores = jugadores;

    }

    public Equipo(String nombre) {
        this.nombre = nombre;

        this.jugadores=new ArrayList<Jugador>();
    }

    public String getNombre() {
        return nombre;
    }

    public ArrayList<Jugador> getJugadores() {
        return jugadores;
    }



    public void setJugadores(ArrayList<Jugador> jugadores) {
        this.jugadores = jugadores;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
