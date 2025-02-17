package com.moviles.gestornba;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.media.MediaPlayer;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import sql.DbHelper;

public class Login extends AppCompatActivity implements View.OnClickListener {

    EditText usuario,contrasena;
    Button btnInicio,btnRegistro;
    Switch visibilidad;
    TextView error;
    SQLiteDatabase db;
    DbHelper dbOpen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.vista_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //

        //
        usuario=findViewById(R.id.usuario);
        contrasena=findViewById(R.id.contrasena);
        error=findViewById(R.id.error);
        visibilidad=findViewById(R.id.visi);
        btnInicio=findViewById(R.id.btnIniciarSesion);
        btnInicio.setOnClickListener(this);
        btnRegistro=findViewById(R.id.btnRegistrarse);
        btnRegistro.setOnClickListener(this);

        visibilidad.setOnClickListener(this);


        Intent intent = new Intent(this, Audio.class);
        intent.setAction(Audio.ACTION_PLAY); // o ACTION_PAUSE, ACTION_STOP
        startService(intent);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btnIniciarSesion){
            iniciarSesion();
        } else if (v.getId()==R.id.btnRegistrarse) {
            registrarse();
        }else if(v.getId()==R.id.visi){
            boolean s=visibilidad.isChecked();
            if(s){
                contrasena.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            }else{
                contrasena.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }

        }
    }

    public void irPrincipal(String  usuario) {
        Intent i=new Intent(this, MainActivity.class);
        i.putExtra("usuario", usuario);
        startActivity(i);
    }

    public void iniciarSesion(){
        String usuario= String.valueOf(this.usuario.getText());
        String contrasena= String.valueOf(this.contrasena.getText());
        String contra=comprobarExistencia(usuario);

            if(contra!=null&&contra.equals(contrasena)){
                Log.d("BBDD", "tira");
                irPrincipal(usuario);
            }else{
                error.setText("Usuario o contraseña incorrecto");
            }
    }

    public void registrarse(){
        String usuario= String.valueOf(this.usuario.getText());
        String contrasena= String.valueOf(this.contrasena.getText());
        String sqlQuery="INSERT INTO usuario (nombre, contrasena) VALUES (?, ?)";
        String[] valores={usuario,contrasena};



        if(null==comprobarExistencia(usuario)||(usuario.equals("")||contrasena.equals(""))){
            try {
                dbOpen=new DbHelper(this);
                db=dbOpen.getWritableDatabase();
                db.execSQL(sqlQuery, valores);
                insertarRegistro();
                Log.d("BBDD", "tira");

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
            irPrincipal(usuario);
        }else{
            error.setText("El ususario introducido ya existe");
        }


    }

    public String comprobarExistencia(String usuario){

        String salida=null;
        Cursor c=null;
        String consulta="select contrasena from usuario where nombre=?";
        String[] variables={usuario};
        try{
            dbOpen=new DbHelper(this);
            db=dbOpen.getReadableDatabase();
            c=db.rawQuery(consulta,variables);
            if(c.moveToFirst()){
                salida=c.getString(c.getColumnIndex("contrasena"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            if(null!=db) {
                db.close();
            }
            if(null!=c){
                c.close();
            }
            if(null!=dbOpen){
                dbOpen.close();
            }

        }

        return salida;
    }

    public void insertarRegistro(){
        DbHelper dbOpne=null;
        SQLiteDatabase db=null;
        Cursor c=null;

        String query1= "insert into equipo(nombre,victorias,derrotas,usuario_fk) values" +
                        "(\"Lakers\",0,0,?)" ;
        String query2= "insert into equipo(nombre,victorias,derrotas,usuario_fk) values" +
                "(\"Boston Celtics\",0,0,?)" ;
        String query3= "insert into equipo(nombre,victorias,derrotas,usuario_fk) values" +
                "(\"Denver Nugets\",0,0,?)" ;

        String query11="insert into jugador(nombre,dorsal,posicion,equipo_fk)values " +
                "(\"Lebron James\",1,\"C\",?)";
        String query12="insert into jugador(nombre,dorsal,posicion,equipo_fk)values " +
                "(\"Antony Davies\",2,\"C\",?)";
        String query13="insert into jugador(nombre,dorsal,posicion,equipo_fk)values " +
                "(\"Lonzo Ball\",3,\"C\",?)";

        String query21="insert into jugador(nombre,dorsal,posicion,equipo_fk)values " +
                "(\"Jason Tatum\",1,\"C\",?)";
        String query22="insert into jugador(nombre,dorsal,posicion,equipo_fk)values " +
                "(\"Antony Smart\",2,\"C\",?)";
        String query23="insert into jugador(nombre,dorsal,posicion,equipo_fk)values " +
                "(\"Kevin Garnet\",3,\"C\",?)";

        String query31="insert into jugador(nombre,dorsal,posicion,equipo_fk)values " +
                "(\"Nikola Jokik\",1,\"C\",?)";
        String query32="insert into jugador(nombre,dorsal,posicion,equipo_fk)values " +
                "(\"Jamal Murray\",2,\"C\",?)";
        String query33="insert into jugador(nombre,dorsal,posicion,equipo_fk)values " +
                "(\"Michel Porter Jr.\",3,\"C\",?)";

        try{
            String[] user= {String.valueOf(this.usuario.getText())};
            String[] pk=new String[1];
            dbOpne=new DbHelper(this);
            db=dbOpen.getWritableDatabase();
            db.execSQL(query1,user);
            c=db.rawQuery("SELECT last_insert_rowid()", null);
            if (c.moveToFirst()) {
                long lastId = c.getLong(0);
                pk[0]=String.valueOf(lastId);
            }
            db.execSQL(query11,pk);
            db.execSQL(query12,pk);
            db.execSQL(query13,pk);
            //
            db.execSQL(query2,user);
            c=db.rawQuery("SELECT last_insert_rowid()", null);
            if (c.moveToFirst()) {
                long lastId = c.getLong(0);
                pk[0]=String.valueOf(lastId);
            }
            db.execSQL(query21,pk);
            db.execSQL(query22,pk);
            db.execSQL(query23,pk);
            //
            db.execSQL(query3,user);
            c=db.rawQuery("SELECT last_insert_rowid()", null);
            if (c.moveToFirst()) {
                long lastId = c.getLong(0);
                pk[0]=String.valueOf(lastId);
            }
            db.execSQL(query31,pk);
            db.execSQL(query32,pk);
            db.execSQL(query33,pk);

        }catch(Exception e){
            e.printStackTrace();
        }finally{
            if(db!=null){
                db.close();
            }
            if(dbOpne!=null){
                dbOpen.close();
            }
            if(null!=c){
                c.close();
            }
        }
    }


}