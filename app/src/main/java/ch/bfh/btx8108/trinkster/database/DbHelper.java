package ch.bfh.btx8108.trinkster.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import ch.bfh.btx8108.trinkster.database.tables.CategoryTable;
import ch.bfh.btx8108.trinkster.database.tables.DrinkTable;

public class DbHelper extends SQLiteOpenHelper {
    private static final String LOG_TAG = DbHelper.class.getSimpleName();

    public static final String DB_NAME = "trinkster.db";
    public static final int DB_VERSION = 3;

    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        Log.d(LOG_TAG, "DbHelper hat die Datenbank: " + getDatabaseName() + " erzeugt.");
    }

    // Die onCreate-Methode wird nur aufgerufen, falls die Datenbank noch nicht existiert
    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            Log.d(LOG_TAG, "Die Tabelle CategoryTable wird mit SQL-Befehl: " + CategoryTable.SQL_CREATE + " angelegt.");
            db.execSQL(CategoryTable.SQL_CREATE);

            Log.d(LOG_TAG, "Die Tabelle DrinkTable wird mit SQL-Befehl: " + DrinkTable.SQL_CREATE + " angelegt.");
            db.execSQL(DrinkTable.SQL_CREATE);
        }
        catch (Exception ex) {
            Log.e(LOG_TAG, "Fehler beim Anlegen der Tabelle: " + ex.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(LOG_TAG, "onUpgrade() enter - oldVersion " + oldVersion + " and newVersion " + newVersion);

        if(newVersion == 2) { // in V2 sind Kategorien noch neu
            db.execSQL(CategoryTable.SQL_CREATE);
        }

        if (newVersion == 3) { // Datum Spalte zur Drink-Tabelle hinzugefügt
            db.execSQL(DrinkTable.DROP_TABLE_V3);
            db.execSQL(DrinkTable.SQL_CREATE);
        }
    }
}
