package modelos;

import java.util.ArrayList;

public class Equipo {
    String nombre;
    ArrayList<Jugador> jugadores;
    String img;

    public Equipo(String nombre, ArrayList<Jugador> jugadores, String img) {
        this.nombre = nombre;
        this.jugadores = jugadores;
        this.img = img;
    }

    public Equipo(String nombre, String img) {
        this.nombre = nombre;
        this.img = img;
        this.jugadores=new ArrayList<Jugador>();
    }

    public String getNombre() {
        return nombre;
    }

    public ArrayList<Jugador> getJugadores() {
        return jugadores;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public void setJugadores(ArrayList<Jugador> jugadores) {
        this.jugadores = jugadores;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
