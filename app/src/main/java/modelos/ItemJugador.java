package modelos;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class ItemJugador implements Parcelable {
    private String nombre;
    private String pos;
    private int dorsal;

    public ItemJugador(String nombre, String pos, int dorsal) {
        this.nombre = nombre;
        this.pos = pos;
        this.dorsal = dorsal;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPos() {
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }

    public int getDorsal() {
        return dorsal;
    }

    public void setDorsal(int dorsal) {
        this.dorsal = dorsal;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {

    }
}
