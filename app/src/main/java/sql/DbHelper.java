package sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DbHelper extends SQLiteOpenHelper {

    private static final String SQL_CREATE_USUARIO=
            "create table "+ EstructuraSQL.Usuarios.TABLE_NAME+"( "+
                    EstructuraSQL.Usuarios.NOMBRE+" string primary key ,"+
                    EstructuraSQL.Usuarios.CONTRASENA+" string not null)";

    private static final String SQL_DELETE_USUARIO=
            "drop_table if exist"+ EstructuraSQL.Usuarios.TABLE_NAME;

    private static final String SQL_CREATE_EQUIPO=
            "create table "+ EstructuraSQL.Equipo.TABLE_NAME+"( "+
                    EstructuraSQL.Equipo.ID_EQUIPO+" integer primary key autoincrement, "+
                    EstructuraSQL.Equipo.NOMBRE+" string not null,"+
                    EstructuraSQL.Equipo.USUARIO_FK+" string not null," +
                    EstructuraSQL.Equipo.VICTORIAS+" integer not null default 0," +
                    EstructuraSQL.Equipo.DERROTAS+" integer not null default 0," +
                    "foreign key (usuario_fk) references usuario(nombre))";

    private static final String SQL_DELETE_EQUIPO=
            "drop table if exists "+ EstructuraSQL.Equipo.TABLE_NAME;

    private static final String SQL_CREATE_JUGADOR=
            "create table "+ EstructuraSQL.Jugador.TABLE_NAME+"( "+
                    EstructuraSQL.Jugador.ID_JUGADOR+" integer primary key autoincrement, "+
                    EstructuraSQL.Jugador.NOMBRE+" string not null, "+
                    EstructuraSQL.Jugador.DORSAL+" integer not null, "+
                    EstructuraSQL.Jugador.POSICION+" string not null,"+
                    EstructuraSQL.Jugador.EQUIPO_FK+" integer not null," +
                    " foreign key (equipo_fk) references equipo(id_equipo))";

    private static final String SQL_DELETE_JUGADOR=
            "drop table if exists "+ EstructuraSQL.Jugador.TABLE_NAME;




    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "NBA.db";

    public DbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_USUARIO);
        Log.d("BBDD", SQL_CREATE_USUARIO);
        db.execSQL(SQL_CREATE_EQUIPO);
        Log.d("BBDD", SQL_CREATE_EQUIPO);
        db.execSQL(SQL_CREATE_JUGADOR);
        Log.d("BBDD", SQL_CREATE_JUGADOR);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int veresionAntigua, int versionNueva) {
        db.execSQL(SQL_DELETE_JUGADOR);
        db.execSQL(SQL_DELETE_EQUIPO);
        db.execSQL(SQL_DELETE_JUGADOR);

        onCreate(db);
    }


    //Consultas




}
