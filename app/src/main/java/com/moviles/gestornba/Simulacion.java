package com.moviles.gestornba;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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

public class Simulacion extends AppCompatActivity {
    Toolbar toolbar;

    TextView marcadorL,marcadorV,localN,visitanteN;
    ListView plantillaL,plantillaV;



    ArrayList<Equipo> equipos=new ArrayList<Equipo>();
    String localS,visitanteS;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.vista_simulacion);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        toolbar = findViewById(R.id.barra);
        setSupportActionBar(toolbar);
        marcadorL=findViewById(R.id.marcadorL);
        marcadorV=findViewById(R.id.marcadorV);

        localN=findViewById(R.id.localN);
        visitanteN=findViewById(R.id.visitanteN);

        plantillaL=findViewById(R.id.plantillaL);
        plantillaV=findViewById(R.id.plantillaV);

        equipos=(ArrayList<Equipo>) getIntent().getSerializableExtra("equipos");
        localS=getIntent().getStringExtra("local");
        visitanteS=getIntent().getStringExtra("visitante");
        simular();

    }
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(R.id.pap==item.getItemId()){
            irSimular();
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

    public void irSimular(){
        Intent i=new Intent(this, MainActivity.class);
        i.putExtra("equipos", equipos);
        startActivity(i);
    }

    public void simular(){
        localN.setText(localS);
        visitanteN.setText(visitanteS);
        plantillaL.setAdapter(cargarEquipos(buscarEquipo(localS)));
        plantillaV.setAdapter(cargarEquipos(buscarEquipo(visitanteS)));
        int num1,num2;
        do{
            num1=(int)(Math.random()*22);
            num2=(int)(Math.random()*22);
        }while(num1==num2);

        marcadorL.setText(String.valueOf(num1));
        marcadorV.setText(String.valueOf(num2));
    }
    public Equipo buscarEquipo(String  nombre){
        Equipo salida=null;
        for(Equipo clave:equipos){
            if(clave.getNombre().equalsIgnoreCase(nombre)){
                salida=clave;
            }
        }
        return salida;
    }
    public ArrayAdapter<String> cargarEquipos(Equipo equipo){
        ArrayAdapter<String> adaptador=null;
        ArrayList<String> jugadores=new ArrayList<String>();
        if(equipo.getJugadores().size()==3){
           for(Jugador clave: equipo.getJugadores()){
               jugadores.add(clave.getNombre());
           }

        }else{
            int num;
            for(int i=0;i<3;i++){

                num=(int)(0+Math.random()*equipo.getJugadores().size());
                if(!jugadores.contains(equipo.getJugadores().get(num).getNombre())){
                    jugadores.add(equipo.getJugadores().get(num).getNombre());

                }else{
                    i--;
                }
            }

        }
        adaptador=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,jugadores);
        return adaptador;
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle datos) {

        datos.putString("local", String.valueOf(marcadorL.getText()));
        datos.putString("visitante", String.valueOf(marcadorV.getText()));
        super.onSaveInstanceState(datos);

    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle datos) {
        super.onRestoreInstanceState(datos);

        marcadorV.setText(datos.getString("visitante"));
        marcadorL.setText(datos.getString("local"));
    }

}