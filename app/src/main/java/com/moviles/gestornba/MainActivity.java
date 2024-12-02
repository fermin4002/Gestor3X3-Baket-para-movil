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
import android.widget.TextView;
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

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //elementos
    Toolbar toolbar;
    ListView equipo1,equipo2;
    Spinner local,visitante;
    Button simular;
    TextView nombre1,nombre2;

    //EQUIPOS
    public ArrayList<Equipo> equipos=new ArrayList<Equipo>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            equipo1=findViewById(R.id.plantilla1);
            equipo2=findViewById(R.id.plantilla2);
            local=findViewById(R.id.equipo1);
            visitante=findViewById(R.id.equipo2);

            local.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String itemSeleccionado = parent.getItemAtPosition(position).toString();
                    // Ejecutar función según la selección del Spinner 1
                    recuperar1(itemSeleccionado);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }

                });
            visitante.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String itemSeleccionado = parent.getItemAtPosition(position).toString();
                    // Ejecutar función según la selección del Spinner 2
                    recuperar2(itemSeleccionado);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // Manejar el caso cuando no se selecciona nada (opcional)
                }

                });

            nombre1=findViewById(R.id.equipo1nombre);
            nombre2=findViewById(R.id.equipo2nombre);
            toolbar = findViewById(R.id.barra);
            setSupportActionBar(toolbar);
            simular=findViewById(R.id.simularB);
            simular.setOnClickListener(this);
            equipos = (ArrayList<Equipo>) getIntent().getSerializableExtra("equipos");

            if(null==equipos){
                equipos=crearPrueba();
            }
            cargarSpinner();
            return insets;

        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(R.id.pap==item.getItemId()){
            Toast.makeText(this, "Ya estas en simular", Toast.LENGTH_SHORT).show();
        }else if(R.id.pep==item.getItemId()){
            irEquipos();

        }else if(R.id.pip==item.getItemId()){
            irPlantilla();

        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_equipos, menu);
        return true;
    }


    //
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
    //


    //Metodos propios
    public void cargarSpinner(){

       ArrayList<String> equipos=new ArrayList<String>();

       for(Equipo clave:this.equipos){
           equipos.add(clave.getNombre());
       }
       ArrayAdapter<String> adaptador=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,equipos);

       local.setAdapter(adaptador);
       visitante.setAdapter(adaptador);
    }


    public void irPlantilla() {
        Intent i=new Intent(this, ControladorEquipos.class);
        i.putExtra("equipos", equipos);
        startActivity(i);
    }

    public void irEquipos(){
        Intent i=new Intent(this, ControladorLiga.class);
        i.putExtra("equipos", equipos);
        startActivity(i);
    }

    public ArrayList<Equipo> crearPrueba(){
        ArrayList<Equipo> equipos=new ArrayList<Equipo>();
        equipos.add(new Equipo("Boston"));
        equipos.add(new Equipo("Lakers"));
        equipos.add(new Equipo("Denver"));


        ArrayList<Jugador> jugadores=new ArrayList<Jugador>();
        jugadores.add(new Jugador("Lebron","Al","1"));
        jugadores.add(new Jugador("Antony Davis","Ap","2"));
        jugadores.add(new Jugador("Bronny","E","3"));
        jugadores.add(new Jugador("Lonzo Ball","B","4"));
        equipos.get(1).setJugadores(jugadores);
        jugadores=new ArrayList<Jugador>();
        jugadores.add(new Jugador("Kevin Garnet","Al","1"));
        jugadores.add(new Jugador(" Paul pierce","Ap","2"));
        jugadores.add(new Jugador("Smart","E","3"));
        jugadores.add(new Jugador("Jason Tatum","B","4"));
        equipos.get(0).setJugadores(jugadores);
        jugadores=new ArrayList<Jugador>();
        jugadores.add(new Jugador("Nikola Jokic","C","1"));
        jugadores.add(new Jugador("Michel Porter Jr","Ap","2"));
        jugadores.add(new Jugador("Jamal Murray","E","3"));
        jugadores.add(new Jugador("Rusell Westbrok","E","4"));
        equipos.get(2).setJugadores(jugadores);


        return equipos;
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
    public void recuperar1(String equipo){
        Equipo equi=buscarEquipo(equipo);
        ArrayList<String> item=new ArrayList<String>();
        for(Jugador clave:equi.getJugadores()){
            item.add(clave.getNombre());
        }

        ArrayAdapter<String> adapter=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,item);
        nombre1.setText(equipo);
        equipo1.setAdapter(adapter);

    }

    public void recuperar2(String equipo){
        Equipo equi=buscarEquipo(equipo);
        ArrayList<String> item=new ArrayList<String>();
        for(Jugador clave:equi.getJugadores()){
            item.add(clave.getNombre());
        }

        ArrayAdapter<String> adapter=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,item);
        nombre2.setText(equipo);
        equipo2.setAdapter(adapter);
    }

    public void siumular(){
        Intent i=new Intent(this, Simulacion.class);
        i.putExtra("equipos", equipos);
        i.putExtra("local",String.valueOf(local.getSelectedItem()));
        i.putExtra("visitante",String.valueOf(visitante.getSelectedItem()));

        startActivity(i);

    }
    public boolean sePuedeJugar(){
        boolean salida=false;

        if(!String.valueOf(this.local.getSelectedItem()).equalsIgnoreCase(String.valueOf(this.visitante.getSelectedItem()))){
            Equipo local=buscarEquipo(String.valueOf(this.local.getSelectedItem()));
            Equipo visitante=buscarEquipo(String.valueOf(this.visitante.getSelectedItem()));
            if(local.getJugadores().size()>=3 && visitante.getJugadores().size()>=3){
                salida=true;
            }
        }
        return salida;
    }
    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.simularB){
            if(sePuedeJugar()){
                siumular();
            }else{
                Toast.makeText(this, "Ambos equipos han de tener minimo 3 jugadores y no pueden ser el mismo", Toast.LENGTH_SHORT).show();
            }
        }
    }
}//fin Class