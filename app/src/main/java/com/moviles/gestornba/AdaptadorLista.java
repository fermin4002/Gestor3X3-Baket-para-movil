package com.moviles.gestornba;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import modelos.ItemList;

public class AdaptadorLista extends BaseAdapter {

    private Context context;
    private List<ItemList> list;

    public AdaptadorLista(Context context, List<ItemList> list){
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
            convertView = inflater.inflate(R.layout.item_list, parent, false);
        }

        // Obtén el elemento actual
        ItemList currentItem = (ItemList) getItem(position);


        // Configura los datos en la vista
        TextView pos = convertView.findViewById(R.id.txPosicion);
        TextView nombre = convertView.findViewById(R.id.txNombre);
        TextView victorias = convertView.findViewById(R.id.txVictorias);
        TextView derrotas = convertView.findViewById(R.id.txDerrotas);

        pos.setText(String.valueOf(currentItem.getPos()));
        nombre.setText(String.valueOf(currentItem.getNombre()));
        victorias.setText(String.valueOf(currentItem.getVictorias()));
        derrotas.setText(String.valueOf(currentItem.getDerrotas()));

        return convertView;
    }

}
