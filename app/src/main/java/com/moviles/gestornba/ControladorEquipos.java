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
import android.widget.Toast;


import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

import modelos.ItemJugador;
import sql.DbHelper;

public class ControladorEquipos extends AppCompatActivity implements View.OnClickListener{

    //elementos
    Button jugadorplus;
    Toolbar toolbar;
    ListView listaEquipos;
    Spinner selector;
    //EQUIPOS
    //public ArrayList<Equipo> equipos=new ArrayList<Equipo>();
    private String usuario;
    private String nombreEquipo=null;
    private int pk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.vista_equipos);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //Menu desplegable
        toolbar = findViewById(R.id.barra);
        setSupportActionBar(toolbar);

        listaEquipos=findViewById(R.id.equipo);

        selector=findViewById(R.id.selector);
        jugadorplus=findViewById(R.id.masJugador);
        jugadorplus.setOnClickListener(this);
        usuario=getIntent().getStringExtra("usuario");
        nombreEquipo=getIntent().getStringExtra("nombreEquipo");
        cargarSpinner();
        //Seleccion jugador
        listaEquipos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //elemento seleccionado
                extraerPKequipo();
                ItemJugador otem= (ItemJugador) listaEquipos.getAdapter().getItem(position);
                String nombre=otem.getNombre();
                modificarJugador(nombre);
                //

            }
        });


        selector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String itemSeleccionado = parent.getItemAtPosition(position).toString();
                // Ejecutar función según la selección del Spinner 1
                //equip=itemSeleccionado;
                extraerPKequipo();
                listarJugadores(itemSeleccionado);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });
        if(null!=nombreEquipo){
            recuperarEquipo(nombreEquipo);
        }

    }
    //A partir de aqui

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(R.id.pap==item.getItemId()){
            volverInicio();
        }else if(R.id.pep==item.getItemId()){
            irEquipos();
        }else if(R.id.pip ==item.getItemId()){
            Toast.makeText(this, "Ya estas en equipos", Toast.LENGTH_SHORT).show();
        }else if(R.id.pop ==item.getItemId()){
            irClasificacion();
        }else if(R.id.pup==item.getItemId()){
            irAjustes();
        }
        return super.onOptionsItemSelected(item);
    }
    public void irAjustes(){
        Intent i=new Intent(this, Ajustes.class);
        i.putExtra("usuario", usuario);
        startActivity(i);
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_equipos, menu);
        return true;
    }


    //Metodos propios
    public void volverInicio() {
        Intent i=new Intent(this, MainActivity.class);
        i.putExtra("usuario", usuario);
        startActivity(i);
    }

    public void irEquipos(){
        Intent i=new Intent(this, ControladorLiga.class);
        i.putExtra("usuario", usuario);
        startActivity(i);
    }

    public void irClasificacion() {
        Intent i=new Intent(this, Clasificacion.class);
        i.putExtra("usuario", usuario);
        startActivity(i);
    }
    //aqui
    public void cargarSpinner(){
        SQLiteDatabase db=null;
        DbHelper dbOpen=null;
        Cursor c=null;
        ArrayList<String> item=new ArrayList<String>();

        String query="select nombre from equipo " +
                "where  usuario_fk=?";

        String[] variables={usuario};

        try{
            dbOpen=new DbHelper(this);
            db=dbOpen.getReadableDatabase();
            c=db.rawQuery(query,variables);

            while(c.moveToNext()){
                item.add(c.getString(c.getColumnIndex("nombre")));
            }

        }catch (Exception e){
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
        ArrayAdapter<String> adaptador=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,item);

        selector.setAdapter(adaptador);

    }


    //LISTENNER SPINNER
    public void extraerPKequipo(){
        SQLiteDatabase db=null;
        DbHelper dbOpen=null;
        Cursor c=null;
        String equipo=String.valueOf(selector.getSelectedItem());
        String query="select id_equipo from equipo where usuario_fk=? and nombre=?";
        String[] values={usuario,equipo};
        try{
            dbOpen=new DbHelper(this);
            db=dbOpen.getReadableDatabase();
            c=db.rawQuery(query,values);
            if(c.moveToFirst()){
                pk=c.getInt(c.getColumnIndex("id_equipo"));
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
    }

    public void listarJugadores(String equipo){
        SQLiteDatabase db=null;
        DbHelper dbOpen=null;
        Cursor c=null;
        ArrayList<ItemJugador> item=new ArrayList<ItemJugador>();

        String query="select j.* from jugador j " +
                "inner join equipo e on e.id_equipo=j.equipo_fk " +
                "where e.nombre=? and e.usuario_fk=?";

        String[] variables={equipo,usuario};

        try{
            dbOpen=new DbHelper(this);
            db=dbOpen.getReadableDatabase();
            c=db.rawQuery(query,variables);
            String nombre;
            String pos;
            int dorsal;
            while(c.moveToNext()){
                nombre=c.getString(c.getColumnIndex("nombre"));
                pos=c.getString(c.getColumnIndex("posicion"));
                dorsal=c.getInt(c.getColumnIndex("dorsal"));
                item.add(new ItemJugador(nombre,pos,dorsal));
            }

        }catch (Exception e){
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

        AdaptadorJugador adapter=new AdaptadorJugador(this,item);

        listaEquipos.setAdapter(adapter);

    }

    public void crearJugador(){
        String equipo=String.valueOf(selector.getSelectedItem());
        SQLiteDatabase db=null;
        DbHelper dbOpen=null;
        String nombre="Jugador nuevo 1";
        int cont=1;
        while(existirJugador(nombre,equipo)){
            cont++;
            nombre="Jugador nuevo "+cont;
        }

        String query="insert into jugador(nombre,dorsal,posicion,equipo_fk) values (?,-1,?,?)";
        String[] values={nombre,"C",String.valueOf(pk)};
        //equipo.getJugadores().add(new Jugador(nombre,"C","-1"));
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

        listarJugadores(equipo);
    }

    public boolean existirJugador(String nombre,String equipo){
        boolean salida=false;
        SQLiteDatabase db=null;

        DbHelper dbOpen=null;
        Cursor c=null;
        String query="select j.nombre from jugador j " +
                "inner join equipo e on j.equipo_fk=e.id_equipo " +
                "where e.usuario_fk=? and e.nombre=? and j.nombre=?";
        String[] values={usuario,equipo,nombre};
        try{
            dbOpen=new DbHelper(this);
            db=dbOpen.getReadableDatabase();
            c=db.rawQuery(query,values);
            if(c.moveToFirst()){
               salida=true;
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

    //Visualizacion fin plantear diferente
    public void modificarJugador(String jugador) {
        String equipo=String.valueOf(selector.getSelectedItem());
        int pk=0;
        SQLiteDatabase db=null;
        DbHelper dbOpen=null;
        Cursor c=null;
        Intent i=new Intent(this, JugadorControlador.class);
        String query="select j.id_jugador from jugador j " +
                "inner join equipo e on j.equipo_fk=e.id_equipo " +
                "where e.usuario_fk=? and e.nombre=? and j.nombre=?";
        String[] values={usuario,equipo,jugador};
       try{
           dbOpen=new DbHelper(this);
           db=dbOpen.getReadableDatabase();
           c=db.rawQuery(query,values);
           if(c.moveToFirst()){
               pk=c.getInt(c.getColumnIndex("j.id_jugador"));
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
        i.putExtra("usuario", usuario);
        i.putExtra("jugador",pk);
        i.putExtra("pkEquipo",this.pk);
        i.putExtra("nombreEquipo",String.valueOf(selector.getSelectedItem()));
        startActivity(i);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.masJugador){
            crearJugador();
            Toast.makeText(this, "Jugador Creado asignale un dorsal para poder usarlo", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle datos) {

        datos.putString("equipo", String.valueOf(selector.getSelectedItemId()));

        super.onSaveInstanceState(datos);

    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle datos) {
        super.onRestoreInstanceState(datos);
        String equipo=datos.getString("equipo");

        recuperarEquipo(equipo);

    }

    public void recuperarEquipo(String equipo){
        ArrayAdapter<String> adapter=(ArrayAdapter<String>) selector.getAdapter();
        int pos=0;
        for(int i=0;i<adapter.getCount();i++){
            if(equipo.equals(adapter.getItem(i))){
                pos=i;
            }
        }

        selector.setSelection(pos);
    }
}