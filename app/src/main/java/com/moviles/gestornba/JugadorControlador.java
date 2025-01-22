package com.moviles.gestornba;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import sql.DbHelper;

public class JugadorControlador extends AppCompatActivity implements View.OnClickListener {

    private Spinner pos;
    private EditText nombre,posi;
    private Button eliminaJugador,modificarJugador;
    Toolbar toolbar;
    //private ArrayList<Equipo> equipos=new ArrayList<Equipo>();
    private String usuario;
    private int pkJugador;
    private int equipo;
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


        usuario=getIntent().getStringExtra("usuario");
        pkJugador=getIntent().getIntExtra("jugador",0);
        equipo=getIntent().getIntExtra("pkEquipo",0);

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

   /* public Equipo buscarEquipo(String nombre){
        Equipo salida=null;
        for(Equipo clave:equipos){
            if(clave.getNombre().equalsIgnoreCase(nombre)){
                salida=clave;
            }
        }
        return salida;
    }*/

    public Jugador buscarJugador(){
        Jugador salida=null;

        return salida;
    }

    public void cargarJugador(){
        SQLiteDatabase db=null;
        DbHelper dbOpen=null;
        Cursor c=null;
        int posicion=0;
        String nombre=null;
        String posicionLetra=null;
        String dorsal=null;
        String query="select nombre,dorsal,posicion " +
                     "from jugador j " +
                     "where id_jugador=?";
        String[] values={String.valueOf(pkJugador)};
        try{
            dbOpen=new DbHelper(this);
            db=dbOpen.getReadableDatabase();
            c=db.rawQuery(query,values);
            if(c.moveToFirst()){
                nombre=c.getString(c.getColumnIndex("nombre"));
                posicionLetra=c.getString(c.getColumnIndex("posicion"));
                dorsal=String.valueOf(c.getInt(c.getColumnIndex("dorsal")));
            }
        }catch(Exception e){
            e.printStackTrace();
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
        switch(posicionLetra){
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
        this.nombre.setText(nombre);
        posi.setText(dorsal);
    }

    public void eliminarJugador(){
        SQLiteDatabase db=null;
        DbHelper dbOpen=null;

        String query="delete from jugador where id_jugador=?";
        String[] values={String.valueOf(pkJugador)};
        try{
            dbOpen=new DbHelper(this);
            db=dbOpen.getWritableDatabase();
            db.execSQL(query,values);

        }catch(Exception e){
            e.printStackTrace();
        }finally {
            if(null!=db){
                db.close();
            }
            if(null!=dbOpen){
                dbOpen.close();
            }
        }

        irPlantilla();

    }

    public void modificarJugador(){
        String nombre=String.valueOf(this.nombre.getText());
        String dorsal=String.valueOf(posi.getText());
        String posicion= String.valueOf(pos.getSelectedItem());
        SQLiteDatabase db=null;
        DbHelper dbOpen=null;

        String query="update  jugador set nombre=?,dorsal=?,posicion=? where id_jugador=?";
        String[] values={nombre,dorsal,posicion,String.valueOf(pkJugador)};
        try{
            dbOpen=new DbHelper(this);
            db=dbOpen.getWritableDatabase();
            db.execSQL(query,values);

        }catch(Exception e){
            e.printStackTrace();
        }finally {
            if(null!=db){
                db.close();
            }
            if(null!=dbOpen){
                dbOpen.close();
            }
        }

    }
    public boolean existeNombre(){
        boolean salida=false;
        String nombre=String.valueOf(this.nombre.getText());
        SQLiteDatabase db=null;
        DbHelper dbOpen=null;
        Cursor c=null;
        String query="select nombre from jugador " +
                "where equipo_fk=? and id_jugador!=?";
        String[] values={String.valueOf(equipo),String.valueOf(pkJugador)};

            try {
                dbOpen=new DbHelper(this);
                db=dbOpen.getReadableDatabase();
                c = db.rawQuery(query, values);
                while (c.moveToNext()) {
                    if (c.getString(c.getColumnIndex("nombre")).equals(nombre)) {
                        salida = true;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (null != db) {
                    db.close();
                }
                if (null != dbOpen) {
                    dbOpen.close();
                }
                if (null != c) {
                    c.close();
                }
            }

        return salida;
    }

    public boolean existeDorsal(){
        boolean salida=false;
        int dorsal=-1;
        SQLiteDatabase db=null;
        DbHelper dbOpen=null;
        Cursor c=null;
        String query="select dorsal from jugador " +
                     "where equipo_fk=? and id_jugador!=?";
        String[] values={String.valueOf(equipo),String.valueOf(pkJugador)};
        try{
             dorsal=Integer.parseInt(String.valueOf(posi.getText()));
        }catch(Exception e){
            e.printStackTrace();
        }

        if(dorsal>0) {
            try {
                dbOpen=new DbHelper(this);
                db=dbOpen.getReadableDatabase();
                c = db.rawQuery(query, values);
                while (c.moveToNext()) {
                    if (c.getInt(c.getColumnIndex("dorsal")) == dorsal) {
                        salida = true;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (null != db) {
                    db.close();
                }
                if (null != dbOpen) {
                    dbOpen.close();
                }
                if (null != c) {
                    c.close();
                }
            }
        }else{
            salida=true;
        }
        return salida;
    }

    public void irPlantilla() {
        Intent i=new Intent(this, ControladorEquipos.class);
        i.putExtra("usuario", usuario);
        startActivity(i);
    }
    public void irEquipos() {
        Intent i=new Intent(this, ControladorLiga.class);
        i.putExtra("usuario", usuario);
        startActivity(i);
    }
    public void irSimular() {
        Intent i=new Intent(this, MainActivity.class);
        i.putExtra("usuario", usuario);
        startActivity(i);
    }




    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.eliminar){
            eliminarJugador();
        }else if(v.getId()==R.id.editar){
            if(!existeDorsal()&& !existeNombre()){
                modificarJugador();
                irPlantilla();
            }else if(existeDorsal()) {
                Toast.makeText(this,"El dorsal ha de ser un numero mayor a 0 y no lo puede tener otro en el equipo",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this,"Ese jugador ya existe",Toast.LENGTH_SHORT).show();
            }

        }
    }
}