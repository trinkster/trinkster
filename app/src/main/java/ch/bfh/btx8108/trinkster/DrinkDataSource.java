package ch.bfh.btx8108.trinkster;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DrinkDataSource {
    private static final String LOG_TAG = DrinkDataSource.class.getSimpleName();

    private SQLiteDatabase database;
    private DrinkDbHelper dbHelper;


    public DrinkDataSource(Context context) {
        Log.d(LOG_TAG, "Unsere DataSource erzeugt jetzt den dbHelper.");
        dbHelper = new DrinkDbHelper(context);
    }

    public void open() {
        Log.d(LOG_TAG, "Eine Referenz auf die Datenbank wird jetzt angefragt.");
        database = dbHelper.getWritableDatabase();
        Log.d(LOG_TAG, "Datenbank-Referenz erhalten. Pfad zur Datenbank: " + database.getPath());
    }

    public void close() {
        dbHelper.close();
        Log.d(LOG_TAG, "Datenbank mit Hilfe des DbHelpers geschlossen.");
    }
}
