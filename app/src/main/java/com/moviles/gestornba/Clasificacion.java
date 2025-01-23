package com.moviles.gestornba;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
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

import modelos.ItemList;
import sql.DbHelper;

public class Clasificacion extends AppCompatActivity {

    Toolbar toolbar;
    ListView clasi;
    private String usuario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.vista_clasificacion);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        toolbar = findViewById(R.id.barra);
        setSupportActionBar(toolbar);
        clasi=findViewById(R.id.clasificacion);
        usuario=getIntent().getStringExtra("usuario");
        recuperarEquipos();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_equipos, menu);
        return true;
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(R.id.pap==item.getItemId()){
            irSimular();
        }else if(R.id.pep==item.getItemId()){
            irEquipos();
        }else if(R.id.pip ==item.getItemId()){
            irPlantilla();
        }else if(R.id.pop ==item.getItemId()){
            Toast.makeText(this, "Ya estas en clasificacion", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    public void irPlantilla() {
        Intent i=new Intent(this, ControladorEquipos.class);
        i.putExtra("usuario", usuario);
        startActivity(i);
    }

    public void irEquipos(){
        Intent i=new Intent(this, ControladorLiga.class);
        i.putExtra("usuario", usuario);
        startActivity(i);
    }

    public void irSimular(){
        Intent i=new Intent(this, MainActivity.class);
        i.putExtra("usuario", usuario);
        startActivity(i);
    }

    public void recuperarEquipos(){
        SQLiteDatabase db=null;
        DbHelper dbOpen=null;
        Cursor c=null;
        ArrayList<ItemList> list=new ArrayList<ItemList>();
        String sqlQuery="select * from equipo where usuario_fk=? order by victorias-derrotas desc";
        String[] values={usuario};

        try{
            dbOpen=new DbHelper(this);
            db=dbOpen.getReadableDatabase();

            c=db.rawQuery(sqlQuery,values);

            String nombre;
            int vic,der,pos=0;
            while(c.moveToNext()){
                nombre=c.getString(c.getColumnIndex("nombre"));
                vic=c.getInt(c.getColumnIndex("victorias"));
                der=c.getInt(c.getColumnIndex("derrotas"));
                pos++;
                list.add(new ItemList(nombre,vic,der,pos));
            }

        }catch(Exception e){

        }finally {
            if(null!=db){
                db.close();
            }
            if(null!=dbOpen){
                dbOpen.close();
            }
            if(null!=c){
                c.close();
            }
        }

        AdaptadorLista adap=new AdaptadorLista(this,list);
        clasi.setAdapter(adap);

    }





}