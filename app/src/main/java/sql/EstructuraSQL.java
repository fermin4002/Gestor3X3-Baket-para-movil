package sql;

import android.provider.BaseColumns;

public class EstructuraSQL {


    public static class Usuarios{
            public static final String TABLE_NAME ="usuario";
            public static final String NOMBRE ="nombre";
            public static final String CONTRASENA="contrasena";
    }

    public static class Equipo{
        public static final String TABLE_NAME ="equipo";
        public static final String ID_EQUIPO ="id_equipo";
        public static final String NOMBRE ="nombre";
        public static final String VICTORIAS="victorias";
        public static final String DERROTAS="derrotas";
        public static final String USUARIO_FK="usuario_fk";


    }

    public static class Jugador{
        public static final String TABLE_NAME ="jugador";
        public static final String ID_JUGADOR="id_jugador";
        public static final String NOMBRE ="nombre";
        public static final String DORSAL="dorsal";
        public static final String POSICION="posicion";
        public static final String EQUIPO_FK="equipo_fk";
    }
}
