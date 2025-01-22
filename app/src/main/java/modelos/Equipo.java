package modelos;

import java.io.Serializable;
import java.util.ArrayList;

public class Equipo implements Serializable {

    String nombre;
    ArrayList<Jugador> jugadores;
    int idEquipo;
    String idUsuario;
    int victorias;
    int derrotas;
    public Equipo(String nombre,int idEquipo,String idUsuario, ArrayList<Jugador> jugadores) {
        this.nombre = nombre;
        this.idEquipo=idEquipo;
        this.idUsuario=idUsuario;
        this.jugadores = jugadores;
        this.victorias=0;
        this.derrotas=0;
    }

    public Equipo(String nombre,int idEquipo,String idUsuario) {
        this.nombre = nombre;
        this.idEquipo=idEquipo;
        this.idUsuario=idUsuario;
        this.jugadores=new ArrayList<Jugador>();
        this.victorias=0;
        this.derrotas=0;
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

    public int getIdEquipo() {
        return idEquipo;
    }

    public void setIdEquipo(int idEquipo) {
        this.idEquipo = idEquipo;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getVictorias() {
        return victorias;
    }

    public void setVictorias(int victorias) {
        this.victorias = victorias;
    }

    public int getDerrotas() {
        return derrotas;
    }

    public void setDerrotas(int derrotas) {
        this.derrotas = derrotas;
    }

    public int jugadoresUsables(){
        int salida=0;

        for(Jugador clave:jugadores){
            if(clave.getDorsal()!=-1){
                salida++;
            }
        }

        return salida;
    }

}
