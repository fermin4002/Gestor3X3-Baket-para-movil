package modelos;

import java.io.Serializable;

public class Jugador implements Serializable {

    private String nombre;
    private String pos;
    private String dorsal;




    public Jugador(String nombre,String pos,String dorsal) {
        this.nombre=nombre;
        this.pos = pos;
        this.dorsal=dorsal;

    }

    public String getPos() {
        return pos;
    }

    public String getDorsal() {
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

    public void setDorsal(String dorsal) {
        this.dorsal = dorsal;
    }


}
