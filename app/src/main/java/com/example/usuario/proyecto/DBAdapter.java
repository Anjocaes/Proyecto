package com.example.usuario.proyecto;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBAdapter {
    static final String KEY_ROWID = "_id";
    static final String KEY_ATRIBUTO01 = "atributo01";  // este var a ser string USER
    static final String KEY_ATRIBUTO02 = "atributo02"; // este va a ser string PASS
    static final String KEY_ATRIBUTO03 = "atributo03"; // este va a ser entero MOD
    static final String KEY_ATRIBUTO04 = "atributo04"; // este va a ser entero PRO
    static final String TAG = "DBAdapter";

    public  String getDatabaseName() {
        return DATABASE_NAME;
    }

    static final  String DATABASE_NAME = "MiBaseDeDatos01.db";
    static final String DATABASE_TABLE = "mitabla";
    static String mensaje ="Mensaje inicial";

    // la version nos permite efectuar futuros cambios a nuestra base usando el metodo onUpgrade
    static final int DATABASE_VERSION = 1;


    // Tenemos dos instrucciones para crear la BD, la "vieja" es la versión anterior de la BD, y "nueva"
// para la actual. Originalmente creamos la BD con la "vieja" y dejamos "nueva" para modificaciones.
    static final String DATABASE_CREATE_vieja =
            "create table mitabla ("+KEY_ROWID +" integer primary key autoincrement, "
                    +  KEY_ATRIBUTO01 +" text not null, "+KEY_ATRIBUTO02 +" text not null, "+KEY_ATRIBUTO03 +" integer, "+KEY_ATRIBUTO04 +" integer);";

    static final String DATABASE_CREATE_nueva =
            "create table mitabla ("+KEY_ROWID +" integer primary key autoincrement, "
                    +  KEY_ATRIBUTO01 +" text not null, "+KEY_ATRIBUTO02 +" text not null, "+KEY_ATRIBUTO03 +" integer, "+KEY_ATRIBUTO04 +" integer);";

    final Context context;
    DatabaseHelper DBHelper;
    SQLiteDatabase db;

    public DBAdapter(Context ctx)
    {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }
    // creamos subclase DatabaseHelper
    private static class DatabaseHelper extends SQLiteOpenHelper
    {
        DatabaseHelper(Context context)
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
        @Override
        public void onCreate(SQLiteDatabase db)
        {// en este metodo creamos la base de datos si no existe previamente.
            try {
                db.execSQL(DATABASE_CREATE_vieja);
                mensaje ="Se ha creado la BD";

            } catch (SQLException e) {
                e.printStackTrace();
                mensaje ="Error creando la BD";
            }
        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        { // Usando oldVersion y newVersion podemos decidir qué hacer con la base anterior.
            // en esete caso optamos por desecharla y crearla de nuevo.
            db.execSQL("DROP TABLE IF EXISTS "+DATABASE_TABLE);
            try {
                db.execSQL(DATABASE_CREATE_nueva);
                mensaje ="Se ha creado una nueva version de la BD";

            } catch (SQLException e) {
                e.printStackTrace();
                mensaje ="Error creando la BD nueva";
            }
        }
    }
    //---Abrimos la base datos---
    public DBAdapter open() throws SQLException
    {
        db = DBHelper.getWritableDatabase();
        mensaje =DATABASE_NAME+" abierta para escritura";
        return this;
    }
    //---Cerramos la base de datos ---
    public void close()
    {
        DBHelper.close();
        mensaje ="La BD se ha cerrado";
    }
    //---Insertamos un dato en la BD---
    public long insertDato(String  atributo01, String atributo02, int atributo03, int atributo04)
    {
        long valor =-1;
        if(db != null) {
            ContentValues initialValues = new ContentValues();
            initialValues.put(KEY_ATRIBUTO01,  atributo01);
            initialValues.put(KEY_ATRIBUTO02, atributo02);
            initialValues.put(KEY_ATRIBUTO03,  atributo03);
            initialValues.put(KEY_ATRIBUTO04, atributo04);
            valor =db.insert(DATABASE_TABLE, null, initialValues);

        }
        mensaje = "Insertando. valor ="+valor;
        return valor;
    }

    // Otra version de Insertar pero usando SQL
    void   insertDatoSQL(String  atributo01, int  atributo02)
    {
        String orden = "INSERT INTO " + DATABASE_TABLE + " ("+KEY_ATRIBUTO01 +","+KEY_ATRIBUTO02
                +") VALUES ('" +  atributo01 +"'," + atributo02 + ")";
        try {
            db.execSQL(orden);
            mensaje ="Inserción OK";
        } catch (SQLException e) {
            e.printStackTrace();
            mensaje ="Error insertando";

        }



    }

    //---Borramos un dato particular---
    public boolean BorrarRegistroConID(long rowId)
    {
        mensaje = "Registro con codigo "+rowId+" de la BD";

        return db.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }

    void   BorrarRegistroConIDSQL(long rowId)
    {

        try {
            db.execSQL("DELETE FROM "+DATABASE_TABLE+" WHERE "+KEY_ROWID +"=" +  rowId  + "");
            mensaje ="Borrado OK";
        } catch (SQLException e) {
            e.printStackTrace();
            mensaje ="Error borrando";

        }

    }

    //---Actualizamos un dato---
    public boolean ActualizarDato(long rowId, String  atributo01, int  atributo02)
    {
        ContentValues args = new ContentValues();
        args.put(KEY_ATRIBUTO01, atributo01);
        args.put(KEY_ATRIBUTO02, atributo02);
        mensaje = "Se actualizó el registro "+rowId;
        return db.update(
                DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
    }

    public void ActualizarDatoSQL(long rowId, String  atributo01, int atributo02)
    {
        String orden = "UPDATE "+DATABASE_TABLE+" SET "+KEY_ATRIBUTO01 +" ='" +  atributo01+"', "+
                KEY_ATRIBUTO02 +" = "+ atributo02 + " WHERE "+KEY_ROWID +"="+rowId;
        try {
            db.execSQL(orden);
            mensaje ="Actualización OK";
        } catch (SQLException e) {
            e.printStackTrace();
            mensaje ="Error actualizando";

        }
    }

    //---Recuperamos todo los datos---
    public String ObtenerTodosLosRegistros()
    {
        Cursor c = db.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_ATRIBUTO01,
                KEY_ATRIBUTO02}, null, null, null, null, null);
        String msg="";
        while(c.moveToNext())
        {
            Integer codigo= c.getInt(0);
            String at1 = c.getString(1);
            int at2 = c.getInt(2);
            msg =msg+"Cod: " + codigo + " A1: " + at1 + " A2: " + at2 + "\n";
        } ;
        return msg;

    }
    //---recuperamos un dato particular---
    public String ObtenerRegistro(String user)
    {
        String msj = " ";
        Cursor mCursor =
                db.query(true, DATABASE_TABLE, new String[] {KEY_ROWID,
                                KEY_ATRIBUTO01}, KEY_ATRIBUTO01 + "=" + "atributo01", null,
                        null, null, null, null);
        if (mCursor.moveToFirst() ) {
            Integer codigo= mCursor.getInt(0);
            String at1 = mCursor.getString(1);
            if(at1.equals(user)){
                msj = at1;
            }
        }
        return msj;
    }

    public String ObtenerRegistro2(String user)
    {
        String msj = " ";
        Cursor mCursor =
                db.query(true, DATABASE_TABLE, new String[] {KEY_ROWID, KEY_ATRIBUTO02}, KEY_ATRIBUTO02 + "=" + "atributo02", null,
                        null, null, null, null);
        if (mCursor.moveToFirst() ) {
            Integer codigo= mCursor.getInt(0);
            String at1 = mCursor.getString(1);
            if(at1.equals(user)){
                msj = at1;
            }
        }
        return msj;
    }

    public int ObtenerRegistroInt(int user)
    {
        int msj = 0;
        Cursor mCursor =
                db.query(true, DATABASE_TABLE, new String[] {KEY_ROWID,
                                KEY_ATRIBUTO03, KEY_ATRIBUTO04}, KEY_ROWID + "=" + user, null,
                        null, null, null, null);
        if (mCursor.moveToFirst() ) {
            Integer codigo= mCursor.getInt(0);
            int at1 = mCursor.getInt(1);
            int at2 = mCursor.getInt(2);
            if(at1 == user){
                msj = at1;
            }else
                msj = at2;
        }
        return msj;
    }

    public int NumeroRegistrosTabla() {
        String countQuery = "SELECT  * FROM " + DATABASE_TABLE;
        // SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }

    void BorrarTabla(){
        db.execSQL("DROP TABLE IF EXISTS "+DATABASE_TABLE);
        db.execSQL(DATABASE_CREATE_nueva);
    }

    String Seleccione(String[] args){
        String respuesta ="";
        Cursor c = db.rawQuery(" SELECT * FROM "+DATABASE_TABLE+" WHERE "+KEY_ATRIBUTO02 +"=? ", args);
        if (c.moveToFirst()) {
            //Recorremos el cursor hasta que no haya más registros
            do {
                Integer codigo= c.getInt(0);
                String at1 = c.getString(1);
                String at2 = c.getString(2);
                respuesta = respuesta+"\n"+
                        "Cod: " + codigo + " At1: " + at1 + " At2: " + at2 + "\n";
            } while(c.moveToNext());
        }
        return respuesta;
    }

    public void BorrarPrimeraFila()
    {

        Cursor cursor = db.query(DATABASE_TABLE, null, null, null, null, null, null);

        if(cursor.moveToFirst()) {
            String rowId = cursor.getString(cursor.getColumnIndex(KEY_ROWID));

            db.delete(DATABASE_TABLE, KEY_ROWID + "=?",  new String[]{rowId});
        }

    }

}
// FIN DE LA CLASE DatabaseHelper