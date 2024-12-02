package com.moviles.gestornba;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

public class ControladorEquipos extends AppCompatActivity implements View.OnClickListener{

    //elementos
    Button jugadorplus;
    Toolbar toolbar;
    ListView listaEquipos;
    Spinner selector;
    //EQUIPOS
    public ArrayList<Equipo> equipos=new ArrayList<Equipo>();
    private String equip,jugador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.vista_equipos);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
           //Menu desplegable
            toolbar = findViewById(R.id.barra);
            setSupportActionBar(toolbar);

            listaEquipos=findViewById(R.id.equipo);

            selector=findViewById(R.id.selector);
            jugadorplus=findViewById(R.id.masJugador);
            jugadorplus.setOnClickListener(this);

            //Recibir Equipos
            equipos = (ArrayList<Equipo>) getIntent().getSerializableExtra("equipos");
            cargarSpinner();
            //Seleccion jugador
            listaEquipos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //elemento seleccionado
                    String itemSeleccionado = parent.getItemAtPosition(position).toString();

                    modificarJugador(itemSeleccionado);
                    //

                }
            });


            selector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String itemSeleccionado = parent.getItemAtPosition(position).toString();
                    // Ejecutar función según la selección del Spinner 1
                    equip=itemSeleccionado;
                    recuperar(itemSeleccionado);

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }

            });


            return insets;
        });

    }
    //A partir de aqui
    @Override
    protected void onSaveInstanceState(@NonNull Bundle datos) {

        datos.putSerializable("equipos",equipos);
        super.onSaveInstanceState(datos);

    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle datos) {
        super.onRestoreInstanceState(datos);
        equipos= (ArrayList<Equipo>) datos.getSerializable("equipos");

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(R.id.pap==item.getItemId()){
            volverInicio();
            //Toast.makeText(this, "boton pap", Toast.LENGTH_SHORT).show();
        }else if(R.id.pep==item.getItemId()){
            irEquipos();
            // Toast.makeText(this, "Ya estas en equipos", Toast.LENGTH_SHORT).show();
        }else if(R.id.pip==item.getItemId()){
            Toast.makeText(this, "Ya estas en equipos", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_equipos, menu);
        return true;
    }


    //Metodos propios
    public void volverInicio() {
        Intent i=new Intent(this, MainActivity.class);
        i.putExtra("equipos", equipos);
        startActivity(i);
    }

    public void irEquipos(){
        Intent i=new Intent(this, ControladorLiga.class);
        i.putExtra("equipos", equipos);
        startActivity(i);
    }

    public void cargarSpinner(){

        ArrayList<String> equipos=new ArrayList<String>();

        for(Equipo clave:this.equipos){
            equipos.add(clave.getNombre());
        }
        ArrayAdapter<String> adaptador=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,equipos);

        selector.setAdapter(adaptador);

    }

    public Equipo buscarEquipo(String nombre){
        Equipo salida=null;
        for(Equipo clave:equipos){
            if(nombre.equalsIgnoreCase(clave.getNombre())){
                salida=clave;
            }
        }
        return salida;
    }

    //LISTENNER SPINNER
    public void recuperar(String equipo){
        Equipo equi=buscarEquipo(equipo);
        ArrayList<String> item=new ArrayList<String>();
        for(Jugador clave:equi.getJugadores()){
            item.add(clave.getNombre());
        }

        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,item);

        listaEquipos.setAdapter(adapter);

    }

    public void crearJugador(){
        Equipo equipo=buscarEquipo(String.valueOf(this.equip));

        int conteo=equipo.getJugadores().size();
        conteo++;
        String nombre="Jugador nuevo "+conteo;

        while(existirJugador(nombre,equipo)){
            conteo++;
            nombre="Jugador nuevo "+conteo;
        }
        equipo.getJugadores().add(new Jugador(nombre,"C","-1"));
        recuperar(String.valueOf(this.equip));

    }

    public boolean existirJugador(String nombre,Equipo equipo){
        boolean salida=false;
        for(Jugador clave: equipo.getJugadores()){
            if (clave.getNombre().equalsIgnoreCase(nombre)) {
                salida = true;

            }
        }
        return salida;
    }

    //Visualizacion fin
    public void modificarJugador(String jugador) {
        Intent i=new Intent(this, JugadorControlador.class);
        i.putExtra("equipos", equipos);
        i.putExtra("nombreEquipo",equip);
        i.putExtra("jugador",jugador);
        startActivity(i);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.masJugador){
            crearJugador();
            Toast.makeText(this, "Jugador Creado asignale un dorsal para poder usarlo", Toast.LENGTH_SHORT).show();
        }
    }
}