package com.moviles.gestornba;

import android.content.Intent;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import modelos.*;

public class ControladorLiga extends AppCompatActivity implements View.OnClickListener {
    Toolbar toolbar;
    ListView equiposLiga;
    Button editEquipo,elimiEquipo,anaEquipo;
    EditText creador;

    ArrayList<Equipo> equipos=new ArrayList<Equipo>();
    String nombreEquipo=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.vista_liga);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        toolbar = findViewById(R.id.barra);
        setSupportActionBar(toolbar);
        equipos = (ArrayList<Equipo>) getIntent().getSerializableExtra("equipos");

        creador=findViewById(R.id.creadorEquipos);

        editEquipo=findViewById(R.id.editarEquipo);
        elimiEquipo=findViewById(R.id.eliminarEquipo);
        anaEquipo=findViewById(R.id.crearEquipo);

        editEquipo.setOnClickListener(this);
        elimiEquipo.setOnClickListener(this);
        anaEquipo.setOnClickListener(this);

        equiposLiga=findViewById(R.id.equiposLigaId);
        mostrarEquipos();
        equiposLiga.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String itemSeleccionado = parent.getItemAtPosition(position).toString();
                nombreEquipo=itemSeleccionado;
                creador.setText(itemSeleccionado);
            }
        });


    }
    @Override

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(R.id.pap==item.getItemId()){
            irSimular();
        }else if(R.id.pep==item.getItemId()){
            Toast.makeText(this, "Ya estas en equipos", Toast.LENGTH_SHORT).show();
        }else if(R.id.pip==item.getItemId()){
            irPlantilla();
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_equipos, menu);
        return true;
    }

    public void irPlantilla() {
        Intent i=new Intent(this, ControladorEquipos.class);
        i.putExtra("equipos", equipos);
        startActivity(i);
    }
    public void irSimular() {
        Intent i=new Intent(this, MainActivity.class);
        i.putExtra("equipos", equipos);
        startActivity(i);
    }

    public void mostrarEquipos(){
        ArrayList<String> nombresEquipos=new ArrayList<String>();

        for(Equipo clave:equipos){
            nombresEquipos.add(clave.getNombre());
        }
        ArrayAdapter<String> adaptador=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,nombresEquipos);

        equiposLiga.setAdapter(adaptador);
    }

    public boolean existirEquipo(String nombre){
        boolean salida=false;
        for(Equipo clave: equipos){
            if (clave.getNombre().equalsIgnoreCase(nombre)) {
                salida = true;

            }
        }
        return salida;
    }

    public void crearEquipo(){
        int conteo=equipos.size();
        conteo++;
        String nombre="Equipo nuevo"+conteo;

        while(existirEquipo(nombre)){
            conteo++;
            nombre="Equipo nuevo"+conteo;
        }

        equipos.add(new Equipo(nombre));

    }

    public void eliminarEquipo(){
        String nombreEquipo= String.valueOf(creador.getText());
        Equipo equipo=null;
        for(Equipo clave:equipos){
            if(clave.getNombre().equalsIgnoreCase(nombreEquipo)){
                equipo=clave;
            }
        }
        if(null!=equipo){
            equipos.remove(equipo);
        }else{
            Toast.makeText(this, nombreEquipo+" no exixte", Toast.LENGTH_SHORT).show();
        }

    }

    public void editarEquipo(){
        if(nombreEquipo!=null){
            Equipo equipo=null;
            for(Equipo clave:equipos){
                if(clave.getNombre().equalsIgnoreCase(nombreEquipo)){
                    equipo=clave;
                }
            }
            equipo.setNombre(String.valueOf(creador.getText()));
            Toast.makeText(this,nombreEquipo+" ahora se llama "+String.valueOf(creador.getText()), Toast.LENGTH_SHORT).show();
            nombreEquipo=null;
        }else{
            Toast.makeText(this,"Seleccione un equipo a modificar", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.eliminarEquipo){
            eliminarEquipo();
            mostrarEquipos();
        }else if(v.getId()==R.id.crearEquipo){
            crearEquipo();
            mostrarEquipos();
        }else if(v.getId()==R.id.editarEquipo){
            editarEquipo();
            mostrarEquipos();
        }
    }
}