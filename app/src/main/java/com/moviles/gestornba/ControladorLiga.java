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
import android.widget.EditText;
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

import sql.DbHelper;

public class ControladorLiga extends AppCompatActivity implements View.OnClickListener {
    Toolbar toolbar;
    ListView equiposLiga;
    Button editEquipo,elimiEquipo,anaEquipo;
    EditText creador;
    Spinner selector;

    //ArrayList<Equipo> equipos=new ArrayList<Equipo>();
    String usuario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.vista_liga);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        toolbar = findViewById(R.id.barra);
        setSupportActionBar(toolbar);
       // equipos = (ArrayList<Equipo>) getIntent().getSerializableExtra("equipos");
        usuario=getIntent().getStringExtra("usuario");
        creador=findViewById(R.id.creadorEquipos);

        selector=findViewById(R.id.selec);



        editEquipo=findViewById(R.id.editarEquipo);
        elimiEquipo=findViewById(R.id.eliminarEquipo);
        anaEquipo=findViewById(R.id.crearEquipo);

        editEquipo.setOnClickListener(this);
        elimiEquipo.setOnClickListener(this);
        anaEquipo.setOnClickListener(this);

        equiposLiga=findViewById(R.id.equiposLigaId);

        selector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String itemSeleccionado = parent.getItemAtPosition(position).toString();
                // Ejecutar función según la selección del Spinner 1
                //ArrayAdapter<String> adapter= recuperar(itemSeleccionado);

                creador.setText(itemSeleccionado);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });
        selector.setAdapter(cargarEquipos());
        equiposLiga.setAdapter(cargarEquipos());



    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(R.id.pap==item.getItemId()){
            irSimular();
        }else if(R.id.pep==item.getItemId()){
            Toast.makeText(this, "Ya estas en equipos", Toast.LENGTH_SHORT).show();
        }else if(R.id.pip ==item.getItemId()){
            irPlantilla();
        }else if(R.id.pop ==item.getItemId()){
            irClasificacion();
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

    public void irClasificacion() {
        Intent i=new Intent(this, Clasificacion.class);
        i.putExtra("usuario", usuario);
        startActivity(i);
    }

    public void irSimular() {
        Intent i=new Intent(this, MainActivity.class);
        i.putExtra("usuario", usuario);
        startActivity(i);
    }

    public boolean existirEquipo(String nombre){
        boolean salida=false;
        //String nombreEquipo= String.valueOf(selector.getSelectedItem());
        SQLiteDatabase db=null;
        DbHelper dbOpen=null;
        Cursor c=null;
        String consulta="select nombre from equipo where usuario_fk=? and nombre=?;";

        String[] variables={usuario,nombre};

        try{
            dbOpen=new DbHelper(this);
            db=dbOpen.getReadableDatabase();
            c=db.rawQuery(consulta,variables);

            if(c.moveToFirst()){
                salida=true;
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
        return salida;
    }

    public void crearEquipo(){
        String nombre=String.valueOf(creador.getText());
        SQLiteDatabase db=null;
        DbHelper dbOpen=null;

        if(!existirEquipo(nombre)&&!nombre.equals("")){
            String crearEquipo="insert into equipo (nombre,usuario_fk) values (?,?)";

            String[] variables={nombre,usuario};

            try{
                dbOpen=new DbHelper(this);
                db=dbOpen.getWritableDatabase();
                db.execSQL(crearEquipo,variables);
            }catch(Exception e){
                e.printStackTrace();
            }finally{
                if(null!=db){
                    db.close();
                }
                if(null!=dbOpen){
                    dbOpen.close();
                }

            }
        }

    }

    public void eliminarEquipo(){
        SQLiteDatabase db=null;
        DbHelper dbOpen=null;
        String nombreEquipo= String.valueOf(selector.getSelectedItem());


        String elimnarjugadores="delete from jugador where equipo_fk in(" +
                "select id_equipo from equipo where usuario_fk=? and nombre=?)";

        String[] variables={usuario,nombreEquipo};

        String eliminarEquipo="delete from equipo where usuario_fk=? and nombre=?";
        try{
            dbOpen=new DbHelper(this);
            db=dbOpen.getWritableDatabase();
            db.execSQL(elimnarjugadores,variables);
            db.execSQL(eliminarEquipo,variables);
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            if(null!=db){
                db.close();
            }
            if(null!=dbOpen){
                dbOpen.close();
            }

        }
    }

    public  ArrayAdapter<String>  cargarEquipos(){
        SQLiteDatabase db=null;
        DbHelper dbOpen=null;
        Cursor c=null;
        ArrayAdapter<String> adaptador=null;
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

        adaptador=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,equipos);

        return adaptador;

    }

    public void editarEquipo(){
        SQLiteDatabase db=null;
        DbHelper dbOpen=null;
        String nombre=String.valueOf(selector.getSelectedItem());
        String nombreModificado=String.valueOf(creador.getText());
        String updateNombre="update equipo set nombre=? where nombre=? and usuario_fk=?;";

        String[] variables={nombreModificado,nombre,usuario,};

        if(!existirEquipo(nombreModificado)){
            try{
                dbOpen=new DbHelper(this);
                db=dbOpen.getWritableDatabase();
                db.execSQL(updateNombre,variables);
            }catch(Exception e){
                e.printStackTrace();
            }finally{
                if(null!=db){
                    db.close();
                }
                if(null!=dbOpen){
                    dbOpen.close();
                }

            }
        }
    }


    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.eliminarEquipo){
            eliminarEquipo();
            selector.setAdapter(cargarEquipos());
            equiposLiga.setAdapter(cargarEquipos());

        }else if(v.getId()==R.id.crearEquipo){
            crearEquipo();
            selector.setAdapter(cargarEquipos());
            equiposLiga.setAdapter(cargarEquipos());
        }else if(v.getId()==R.id.editarEquipo){
            editarEquipo();
            selector.setAdapter(cargarEquipos());
            equiposLiga.setAdapter(cargarEquipos());
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle datos) {
        datos.putString("equipo", String.valueOf(selector.getSelectedItem()));
        //datos.putString("equipoN",String.valueOf(creador.getText()));
        super.onSaveInstanceState(datos);

    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle datos) {
        super.onRestoreInstanceState(datos);
        String equipo=datos.getString("equipo");
        recuperarEquipo(selector,equipo);
        //creador.setText(datos.getString("equipoN"));
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
}