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



        if(null==comprobarExistencia(usuario)){
            try {
                dbOpen=new DbHelper(this);
                db=dbOpen.getWritableDatabase();
                db.execSQL(sqlQuery, valores);

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
                irPrincipal(usuario);
            }
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


}