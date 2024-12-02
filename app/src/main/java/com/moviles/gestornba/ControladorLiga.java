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
import android.widget.Spinner;
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
    Spinner selector;

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

        selector=findViewById(R.id.selec);



        editEquipo=findViewById(R.id.editarEquipo);
        elimiEquipo=findViewById(R.id.eliminarEquipo);
        anaEquipo=findViewById(R.id.crearEquipo);

        editEquipo.setOnClickListener(this);
        elimiEquipo.setOnClickListener(this);
        anaEquipo.setOnClickListener(this);

        equiposLiga=findViewById(R.id.equiposLigaId);
        mostrarEquipos();
        selector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String itemSeleccionado = parent.getItemAtPosition(position).toString();
                // Ejecutar función según la selección del Spinner 1
                //ArrayAdapter<String> adapter= recuperar(itemSeleccionado);
                nombreEquipo=itemSeleccionado;
                creador.setText(itemSeleccionado);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });
        cargarSpinner();



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
        String nombre="Equipo nuevo "+conteo;

        while(existirEquipo(nombre)){
            conteo++;
            nombre="Equipo nuevo "+conteo;
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

    public void cargarSpinner(){
       ArrayList<String> nombres=new ArrayList<String>();
        for(Equipo clave:equipos){
            nombres.add(clave.getNombre());
        }

        ArrayAdapter<String> adaptador=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,nombres);

        selector.setAdapter(adaptador);
    }
    public void editarEquipo(){
        String nombre=String.valueOf(selector.getSelectedItem());
        String nombreModificado=String.valueOf(creador.getText());

        if(!existirEquipo(nombreModificado)){
            for(Equipo clave:equipos){
                if(clave.getNombre().equalsIgnoreCase(nombre)){
                    clave.setNombre(nombreModificado);
                }
            }
        }


    }


    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.eliminarEquipo){
            eliminarEquipo();
            cargarSpinner();
            mostrarEquipos();
        }else if(v.getId()==R.id.crearEquipo){
            crearEquipo();
            cargarSpinner();
            mostrarEquipos();
        }else if(v.getId()==R.id.editarEquipo){
            editarEquipo();
            cargarSpinner();
            mostrarEquipos();
        }
    }
}