package modelos;

public class Jugador {

    private String nombre;
    private String pos;
    private int dorsal;
    private String img;

    public Jugador(String nombre,String pos,int dorsal) {
        this.nombre=nombre;
        this.pos = pos;
        this.dorsal=dorsal;
        this.img=null;
    }

    public Jugador(String nombre,String pos,int dorsal,String img) {
        this.nombre=nombre;
        this.pos = pos;
        this.dorsal=dorsal;
        this.img=img;
    }

    public String getPos() {
        return pos;
    }

    public int getDorsal() {
        return dorsal;
    }

    public String getImg() {
        return img;
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

    public void setImg(String img) {
        this.img = img;
    }
}
