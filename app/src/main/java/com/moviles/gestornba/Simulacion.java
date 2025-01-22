package com.moviles.gestornba;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import sql.DbHelper;

public class Simulacion extends AppCompatActivity {
    Toolbar toolbar;

    TextView marcadorL,marcadorV,localN,visitanteN;
    ListView plantillaL,plantillaV;



    //ArrayList<Equipo> equipos=new ArrayList<Equipo>();
    String usuario;
    String localS;
    String visitanteS;
    int tempL,tempV;
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

        usuario=getIntent().getStringExtra("usuario");
        localS=getIntent().getStringExtra("local");
        visitanteS=getIntent().getStringExtra("visitante");
        //jugado=false;

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
        tempL=num1;
        tempV=num2;
        if(num1>num2){
            updateWinRate(localS,visitanteS);
        }else{
            updateWinRate(visitanteS,localS);
        }


    }


    public Equipo buscarEquipo(String nombre){
        Cursor c=null;
        SQLiteDatabase db=null;
        DbHelper dbOpen=null;
        Equipo salida=null;
        //Equipo
        String club;
        String idUsuario;
        int idEquipo;

        String recuperarEquipoSQL="select * from equipo where usuario_fk=? and nombre=?";
        String[] variables={usuario,nombre};
        try{
            dbOpen=new DbHelper(this);
            db=dbOpen.getReadableDatabase();
            c= db.rawQuery(recuperarEquipoSQL,variables);
            if(c.moveToFirst()){
                club=c.getString(c.getColumnIndex("nombre"));
                idUsuario=c.getString(c.getColumnIndex("usuario_fk"));
                idEquipo=c.getInt(c.getColumnIndex("id_equipo"));
                salida=new Equipo(club,idEquipo,idUsuario);
                salida.setJugadores(buscarJugadores(idEquipo));
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

        return salida;
    }

    public ArrayList<Jugador> buscarJugadores(int fkEquipo){
        SQLiteDatabase db=null;
        DbHelper dbOpen=null;
        Cursor c=null;

        ArrayList<Jugador> salida=new ArrayList<Jugador>();

        //jugador
        String nombre;
        String pos;
        String dorsal;
        int id_jugador;
        int equipo_fk;

        String sqlJugadores="select * from jugador where equipo_fk=?";
        String[] variables={String.valueOf(fkEquipo)};
        try{
            dbOpen=new DbHelper(this);
            db=dbOpen.getReadableDatabase();
            c=db.rawQuery(sqlJugadores,variables);
            while (c.moveToNext()){
                nombre=c.getString(c.getColumnIndex("nombre"));
                pos=c.getString(c.getColumnIndex("posicion"));
                dorsal=c.getString(c.getColumnIndex("dorsal"));
                id_jugador=c.getInt(c.getColumnIndex("id_jugador"));
                equipo_fk=c.getInt(c.getColumnIndex("equipo_fk"));
                salida.add(new Jugador(nombre,pos,Integer.parseInt(dorsal),id_jugador,equipo_fk));
            }

        }catch (Exception e){
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

        ArrayList<String> localE=new ArrayList<String>();
        ArrayList<String> visitanteE=new ArrayList<String>();
        for (int i = 0; i < plantillaL.getAdapter().getCount(); i++) {
            localE.add(String.valueOf(plantillaL.getAdapter().getItem(i)));
        }
        for (int i = 0; i < plantillaV.getAdapter().getCount(); i++) {
            visitanteE.add(String.valueOf(plantillaV.getAdapter().getItem(i)));
        }
        datos.putStringArrayList("lEquipo",localE);
        datos.putStringArrayList("vEquipo",visitanteE);

        datos.putString("nombreL",String.valueOf(localN.getText()));
        datos.putString("nombreV",String.valueOf(visitanteN.getText()));


        super.onSaveInstanceState(datos);

    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle datos) {
        super.onRestoreInstanceState(datos);
        plantillaL.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,
                datos.getStringArrayList("lEquipo")));
        plantillaV.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,
                datos.getStringArrayList("vEquipo")));
        marcadorV.setText(datos.getString("visitante"));
        marcadorL.setText(datos.getString("local"));
        if(tempV>tempL){
            corregir(datos.getString("nombreV"),datos.getString("nombreL"));
        }else{
            corregir(datos.getString("nombreL"),datos.getString("nombreV"));
        }

    }

    public void corregir(String win,String lose){
        SQLiteDatabase db=null;
        DbHelper dbOpen=null;

        String upWin="update  equipo set victorias=victorias-1 where nombre=? and usuario_fk=?";
        String upLose="update  equipo set derrotas=derrotas-1 where nombre=? and usuario_fk=?";
        String[] variablesW={win,usuario};
        String[] variablesL={lose,usuario};

        try{
            dbOpen=new DbHelper(this);
            db=dbOpen.getWritableDatabase();
            db.execSQL(upWin,variablesW);
            db.execSQL(upLose,variablesL);


        }catch (Exception e){
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

    public void updateWinRate(String win,String lose){
        SQLiteDatabase db=null;
        DbHelper dbOpen=null;

        String upWin="update  equipo set victorias=victorias+1 where nombre=? and usuario_fk=?";
        String upLose="update  equipo set derrotas=derrotas+1 where nombre=? and usuario_fk=?";
        String[] variablesW={win,usuario};
        String[] variablesL={lose,usuario};

        try{
            dbOpen=new DbHelper(this);
            db=dbOpen.getWritableDatabase();
            db.execSQL(upWin,variablesW);
            db.execSQL(upLose,variablesL);


        }catch (Exception e){
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

}