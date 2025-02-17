package com.moviles.gestornba;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import sql.DbHelper;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //elementos
    Toolbar toolbar;
    ListView equipo1,equipo2;
    Spinner local,visitante;
    Button simular;
    TextView nombre1,nombre2,localWin,localLose,visitanteWin,visitanteLose;

    //BBDD

    String usuario;
    //EQUIPOS
   // public ArrayList<Equipo> equipos=new ArrayList<Equipo>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;

        });
        equipo1=findViewById(R.id.plantilla1);
        equipo2=findViewById(R.id.plantilla2);
        local=findViewById(R.id.equipo1);
        visitante=findViewById(R.id.equipo2);
        localWin=findViewById(R.id.localWin);
        localLose=findViewById(R.id.localLose);
        visitanteWin=findViewById(R.id.visitanteWin);
        visitanteLose=findViewById(R.id.visitanteLose);

        usuario=getIntent().getStringExtra("usuario");

        local.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String itemSeleccionado = parent.getItemAtPosition(position).toString();
                // Ejecutar función según la selección del Spinner 1
                AdaptadorJugador adapter= recuperar(itemSeleccionado);
                Equipo temp=buscarEquipo(itemSeleccionado);
                localWin.setText(String.valueOf(temp.getVictorias()));
                localLose.setText(String.valueOf(temp.getDerrotas()));
                nombre1.setText(itemSeleccionado);
                equipo1.setAdapter(adapter);
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
                AdaptadorJugador adapter= recuperar(itemSeleccionado);
                Equipo temp=buscarEquipo(itemSeleccionado);
                visitanteWin.setText(String.valueOf(temp.getVictorias()));
                visitanteLose.setText(String.valueOf(temp.getDerrotas()));
                nombre2.setText(itemSeleccionado);
                equipo2.setAdapter(adapter);
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
        cargarSpinner();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(R.id.pap==item.getItemId()){
            Toast.makeText(this, "Ya estas en simular", Toast.LENGTH_SHORT).show();
        }else if(R.id.pep==item.getItemId()){
            irEquipos();

        }else if(R.id.pip ==item.getItemId()){
            irPlantilla();

        }else if(R.id.pop ==item.getItemId()){
            irClasificacion();

        }else if(R.id.pup==item.getItemId()){
            irAjustes();
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

        datos.putString("equipol", String.valueOf(local.getSelectedItem()));
        datos.putString("equipov", String.valueOf(visitante.getSelectedItem()));

        super.onSaveInstanceState(datos);

    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle datos) {
        super.onRestoreInstanceState(datos);
        String equipol=datos.getString("equipol");
        String equipov=datos.getString("equipov");

        recuperarEquipo(local,equipol);
        recuperarEquipo(visitante,equipov);


    }
    //
    public int recuperarEquipo(Spinner selector,String equipo){
        int salida=0;
        ArrayAdapter<String> adapter=(ArrayAdapter<String>) selector.getAdapter();
        int pos=0;
        for(int i=0;i<adapter.getCount();i++){
            if(equipo.equals(adapter.getItem(i))){
                pos=i;
            }
        }

        selector.setSelection(pos);
        return salida;
    }

    //Metodos propios
    public void cargarSpinner(){
        SQLiteDatabase db=null;
        DbHelper dbOpen=null;

        Cursor c=null;

        String query="select nombre from equipo where usuario_fk=?";
        String[] variables={usuario};
        ArrayList<String> equipos=new ArrayList<String>();
        try{
            dbOpen=new DbHelper(this);
            db=dbOpen.getReadableDatabase();
            c=db.rawQuery(query,variables);
            while(c.moveToNext()){
                equipos.add(c.getString(c.getColumnIndex("nombre")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
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

       ArrayAdapter<String> adaptador=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,equipos);

       local.setAdapter(adaptador);
       visitante.setAdapter(adaptador);
    }

    //Consultas Extraer equipos

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
                salida.setVictorias(c.getInt(c.getColumnIndex("victorias")));
                salida.setDerrotas(c.getInt(c.getColumnIndex("derrotas")));
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

    //LISTENNER SPINNER

    public AdaptadorJugador recuperar(String equipo){
        Equipo equi=buscarEquipo(equipo);
        ArrayList<ItemJugador> item=new ArrayList<ItemJugador>();
        for(Jugador clave:equi.getJugadores()){
            if(clave.getDorsal()!=-1){
                item.add(new ItemJugador(clave.getNombre(),clave.getPos(),clave.getDorsal()));
            }
        }

        AdaptadorJugador adapter=new AdaptadorJugador(this,item);
        //nombre1.setText(equipo);

        return adapter;
    }

    //Desplazarse por las actividades
    public void siumular(){
        Intent i=new Intent(this, Simulacion.class);
        //i.putExtra("equipos", equipos);
        i.putExtra("local",String.valueOf(local.getSelectedItem()));
        i.putExtra("visitante",String.valueOf(visitante.getSelectedItem()));
        i.putExtra("usuario", usuario);
        startActivity(i);

    }

    public void irPlantilla() {
        Intent i=new Intent(this, ControladorEquipos.class);
        i.putExtra("usuario", usuario);
        startActivity(i);
    }

    public void irClasificacion() {
        Intent i=new Intent(this, Clasificacion.class);
        i.putExtra("usuario", usuario);
        startActivity(i);
    }

    public void irEquipos(){
        Intent i=new Intent(this, ControladorLiga.class);
        i.putExtra("usuario", usuario);
        startActivity(i);
    }
    public void irAjustes(){
        Intent i=new Intent(this, Ajustes.class);
        i.putExtra("usuario", usuario);
        startActivity(i);
    }

    //
    public boolean sePuedeJugar(){
        boolean salida=false;

        if(!String.valueOf(this.local.getSelectedItem()).equalsIgnoreCase(String.valueOf(this.visitante.getSelectedItem()))){
            Equipo local=buscarEquipo(String.valueOf(this.local.getSelectedItem()));
            Equipo visitante=buscarEquipo(String.valueOf(this.visitante.getSelectedItem()));
            if(local.jugadoresUsables()>=3 && visitante.jugadoresUsables()>=3){
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