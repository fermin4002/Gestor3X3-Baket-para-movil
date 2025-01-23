package com.moviles.gestornba;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import modelos.ItemJugador;

public class AdaptadorJugador extends BaseAdapter {

    private Context context;
    private List<ItemJugador> list;

    public AdaptadorJugador(Context context, List<ItemJugador> list){
        this.context=context;
        this.list=list;

    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.item_vista_equipos, parent, false);
        }

        // Obtén el elemento actual
        ItemJugador currentItem = (ItemJugador) getItem(position);


        // Configura los datos en la vista
        TextView pos = convertView.findViewById(R.id.txPos);
        TextView nombre = convertView.findViewById(R.id.txNombre);
        TextView dorsal = convertView.findViewById(R.id.txDorsal);


        pos.setText(String.valueOf(currentItem.getPos()));
        nombre.setText(String.valueOf(currentItem.getNombre()));
        dorsal.setText(String.valueOf(currentItem.getDorsal()));


        return convertView;
    }
}
