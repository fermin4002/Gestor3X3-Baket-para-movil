package com.moviles.gestornba;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

public class MainActivity extends AppCompatActivity {

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
            Toolbar toolbar = findViewById(R.id.barra);
            setSupportActionBar(toolbar);
            crearDatos();

            return insets;
        });
    }

   /* public void setSupportActionBar(Toolbar toolbar) {
    }*/


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(R.id.pap==item.getItemId()){
            Toast.makeText(this, "boton pap", Toast.LENGTH_SHORT).show();
        }else if(R.id.pep==item.getItemId()){
            Toast.makeText(this, "boton pep", Toast.LENGTH_SHORT).show();
        }else if(R.id.pip==item.getItemId()){
            Toast.makeText(this, "boton pip", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_equipos, menu);
        return true;
    }

    //Metodos propios

    public void crearDatos(){
        equipos.clear();

    }


}//fin Class