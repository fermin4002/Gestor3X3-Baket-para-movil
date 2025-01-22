package modelos;

import java.io.Serializable;

public class Jugador implements Serializable {

    private String nombre;
    private String pos;
    private int dorsal;
    private int id_jugador;
    private int equipo_fk;



    public Jugador(String nombre,String pos,int dorsal,int id_jugador,int equipo_fk) {
        this.nombre=nombre;
        this.pos = pos;
        this.dorsal=dorsal;
        this.id_jugador=id_jugador;
        this.equipo_fk=equipo_fk;

    }

    public String getPos() {
        return pos;
    }

    public int getDorsal() {
        return dorsal;
    }


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }

    public void setDorsal(int dorsal) {
        this.dorsal = dorsal;
    }

    public int getId_jugador() {
        return id_jugador;
    }

    public void setId_jugador(int id_jugador) {
        this.id_jugador = id_jugador;
    }

    public int getEquipo_fk() {
        return equipo_fk;
    }

    public void setEquipo_fk(int equipo_fk) {
        this.equipo_fk = equipo_fk;
    }
}
