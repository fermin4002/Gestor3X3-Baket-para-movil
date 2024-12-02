package com.moviles.gestornba;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

import modelos.*;

public class JugadorControlador extends AppCompatActivity implements View.OnClickListener {

    private Spinner pos;
    private EditText nombre,posi;
    private Button eliminaJugador,modificarJugador;
    Toolbar toolbar;
    private ArrayList<Equipo> equipos=new ArrayList<Equipo>();
    private String nombreEquipo,nombreJugador;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.vista_jugador);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        toolbar = findViewById(R.id.barra);
        setSupportActionBar(toolbar);


        equipos = (ArrayList<Equipo>) getIntent().getSerializableExtra("equipos");
        nombreEquipo=getIntent().getStringExtra("nombreEquipo");
        nombreJugador=getIntent().getStringExtra("jugador");

        pos=findViewById(R.id.posicion);
        nombre=findViewById(R.id.editNombre);
        posi=findViewById(R.id.editNumero);

        eliminaJugador=findViewById(R.id.eliminar);
        eliminaJugador.setOnClickListener(this);
        modificarJugador=findViewById(R.id.editar);
        modificarJugador.setOnClickListener(this);

        cargarPos();
        cargarJugador();

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(R.id.pap==item.getItemId()){
            irSimular();

        }else if(R.id.pep==item.getItemId()){
            irEquipos();

        }else if(R.id.pip==item.getItemId()){
            irPlantilla();
            Toast.makeText(this, "boton pip", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_equipos, menu);
        return true;
    }

    //Metodos propios
    public void cargarPos(){

        ArrayList<String> equipos=new ArrayList<String>();

        equipos.add("B");
        equipos.add("E");
        equipos.add("Al");
        equipos.add("Ap");
        equipos.add("C");

        ArrayAdapter<String> adaptador=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,equipos);

        pos.setAdapter(adaptador);

    }

    public Equipo buscarEquipo(String nombre){
        Equipo salida=null;
        for(Equipo clave:equipos){
            if(clave.getNombre().equalsIgnoreCase(nombre)){
                salida=clave;
            }
        }
        return salida;
    }

    public Jugador buscarJugador(Equipo equipo,String jugador){
        Jugador salida=null;
        for(Jugador clave:equipo.getJugadores()){
            if(clave.getNombre().equalsIgnoreCase(jugador)){
                salida=clave;
            }
        }
        return salida;
    }

    public void cargarJugador(){
        Equipo equipo=buscarEquipo(nombreEquipo);
        Jugador jugador=buscarJugador(equipo,nombreJugador);
        int posicion=0;
        switch(jugador.getPos()){
            case "B":
                posicion=0;
                break;
            case "E":
                posicion=1;
                break;
            case "Al":
                posicion=2;
                break;
            case "Ap":
                posicion=3;
                break;
            case "C":
                posicion=4;
                break;
        }
        pos.setSelection(posicion);
        nombre.setText(jugador.getNombre());
        posi.setText(jugador.getDorsal());
    }

    public void eliminarJugador(){
        Equipo equipo=buscarEquipo(nombreEquipo);
        Jugador jugador=buscarJugador(equipo,nombreJugador);
        equipo.getJugadores().remove(jugador);
        irPlantilla();

    }

    public void modificarJugador(){
        Equipo equipo=buscarEquipo(nombreEquipo);
        Jugador jugador=buscarJugador(equipo,nombreJugador);
        String nombre= String.valueOf(this.nombre.getText());
        if(comprobarDorsal(equipo)&& comprobarNombre(nombre,equipo)){
            jugador.setNombre(String.valueOf(this.nombre.getText()));
            jugador.setDorsal(String.valueOf(posi.getText()));
            jugador.setPos(String.valueOf(pos.getSelectedItem()));
            irPlantilla();
        }else if(!comprobarDorsal(equipo)) {
            Toast.makeText(this,"El dorsal ha de ser un numero mayor a 0 y no lo puede tener otro en el equipo",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this,"Ese jugador ya existe",Toast.LENGTH_SHORT).show();
        }

    }
    public boolean comprobarNombre(String nombre, Equipo equipo){
        boolean salida=true;

        for(Jugador clave:equipo.getJugadores()){
            if(clave.getNombre().equalsIgnoreCase(nombre) && !nombreJugador.equalsIgnoreCase(nombre)){
               salida=false;
            }
        }

        return salida;
    }

    public boolean comprobarDorsal(Equipo equipo){
        boolean salida=true;
        int dorsal;
        String nombre=nombreJugador;
        try{
           dorsal=Integer.parseInt(String.valueOf(posi.getText())) ;
           for(Jugador clave:equipo.getJugadores()){
               if(Integer.parseInt(clave.getDorsal())==dorsal){
                   if(!clave.getNombre().equalsIgnoreCase(nombre)){
                       salida=false;
                   }

               }
           }
        }catch(Exception e){
            e.printStackTrace();
            salida=false;
        }
        return salida;
    }

    public void irPlantilla() {
        Intent i=new Intent(this, ControladorEquipos.class);
        i.putExtra("equipos", equipos);
        startActivity(i);
    }
    public void irEquipos() {
        Intent i=new Intent(this, ControladorLiga.class);
        i.putExtra("equipos", equipos);
        startActivity(i);
    }
    public void irSimular() {
        Intent i=new Intent(this, MainActivity.class);
        i.putExtra("equipos", equipos);
        startActivity(i);
    }
    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.eliminar){
            eliminarJugador();
        }else if(v.getId()==R.id.editar){
            modificarJugador();
        }
    }
}