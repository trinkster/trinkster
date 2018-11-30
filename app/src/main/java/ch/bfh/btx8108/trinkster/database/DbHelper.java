package ch.bfh.btx8108.trinkster.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import ch.bfh.btx8108.trinkster.database.tables.CategoryTable;
import ch.bfh.btx8108.trinkster.database.tables.DrinkTable;

/**
 * Helferklasse um die Datenbank der App zu verwalten.
 */
public class DbHelper extends SQLiteOpenHelper {
    private static final String LOG_TAG = DbHelper.class.getSimpleName();

    public static final String DB_NAME = "trinkster.db";
    public static final int DB_VERSION = 4;

    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * Konstruktor, der den Superkonstruktor mit Name und Version aufruft
     * @param context
     */
    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        Log.d(LOG_TAG, "DbHelper hat die Datenbank: " + getDatabaseName() + " erzeugt.");
    }

    /**
     * Die onCreate-Methode wird nur aufgerufen, falls die Datenbank noch nicht existiert
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            Log.d(LOG_TAG, "Die Tabelle CategoryTable wird mit SQL-Befehl: " + CategoryTable.SQL_CREATE + " angelegt.");
            db.execSQL(CategoryTable.SQL_CREATE);

            Log.d(LOG_TAG, "Die Tabelle DrinkTable wird mit SQL-Befehl: " + DrinkTable.SQL_CREATE + " angelegt.");
            db.execSQL(DrinkTable.SQL_CREATE);

            CategoryTable.generateTestData(db);
            DrinkTable.generateTestData(db);
        }
        catch (Exception ex) {
            Log.e(LOG_TAG, "Fehler beim Anlegen der Tabelle: " + ex.getMessage());
        }
    }

    /**
     * Methode wird aufgerufen um zu prüfen, ob ein Datenbankupgrade durchgeführt werden muss
     * @param db
     * @param oldVersion
     * @param newVersion
     */
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

        if (newVersion == 4) { // Bisherige DB-Einträge löschen und Testdaten anlegen
            db.execSQL("DELETE FROM " + CategoryTable.TABLE_CATEGORY);
            db.execSQL("DELETE FROM " + DrinkTable.TABLE_DRINK);
            CategoryTable.generateTestData(db);
            DrinkTable.generateTestData(db);
        }
    }
}
