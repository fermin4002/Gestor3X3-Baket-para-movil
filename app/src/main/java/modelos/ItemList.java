package modelos;

public class ItemList {
    private String nombre;
    private int victorias;
    private int derrotas;
    private int pos;

    public ItemList(String nombre, int victorias, int derrotas, int pos) {
        this.nombre = nombre;
        this.victorias = victorias;
        this.derrotas = derrotas;
        this.pos = pos;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }
}
