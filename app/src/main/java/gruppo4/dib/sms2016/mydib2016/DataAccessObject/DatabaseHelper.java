package gruppo4.dib.sms2016.mydib2016.DataAccessObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper{

    private static String DATABASE_NAME = "MyDib.db";
    private static String TABLE_LIBRETTO = "libretto";
    private static String TABLE_STUDENTE = "studente"; //creare la tabella

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_LIBRETTO + " (idEsame INTEGER PRIMARY KEY AUTOINCREMENT, materia TEXT UNIQUE, CFU TEXT, voto TEXT, data TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LIBRETTO);
        onCreate(db);
    }

    public boolean insertData(List<Coppia> param, String table) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        for(Coppia c : param) {
            contentValues.put(c.getKey(), c.getValue());
        }

        long result = db.insert(table, null, contentValues);

        if(result == -1) {
            return false;
        }
        return true;
    }

    public Cursor getAllData(String table) {
        String query = "SELECT * FROM " + table;
        SQLiteDatabase db = getWritableDatabase();
        Cursor result = db.rawQuery(query, null);
        return result;
    }

    public Cursor selectData(String table, String argomento) {
        String query = "SELECT * FROM " + table + " WHERE materia = '" + argomento + "'";
        SQLiteDatabase db = getWritableDatabase();
        Cursor result = db.rawQuery(query, null);
        return result;
    }

    public boolean updateData(List<Coppia> param, String table, String clausolaWhere, String argomento) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        for(Coppia c : param) {
            contentValues.put(c.getKey(), c.getValue());
        }

        int rows = db.update(table, contentValues, clausolaWhere + " = ?", new String[] {argomento});

        if(rows > 0) {
            return true;
        }
        else {
            return false;
        }
    }

    public boolean deleteData(String table, String clausolaWhere, String argomento) {
        SQLiteDatabase db = getWritableDatabase();
        int rows = db.delete(table, clausolaWhere + " = ?", new String[] {argomento});

        if(rows > 0) {
            return true;
        }
        else {
            return false;
        }
    }

    public boolean deleteAllData(String table) {
        SQLiteDatabase db = getWritableDatabase();
        int rows = db.delete(table, null, null);

        if(rows > 0) {
            Log.d("DATABASE ", String.valueOf(rows));
            return true;
        }
        else {
            return false;
        }
    }
}
