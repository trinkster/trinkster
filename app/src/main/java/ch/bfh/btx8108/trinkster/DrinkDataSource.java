package ch.bfh.btx8108.trinkster;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DrinkDataSource {
    private static final String LOG_TAG = DrinkDataSource.class.getSimpleName();

    private SQLiteDatabase database;
    private DrinkDbHelper dbHelper;

    private String[] columns = {
            DrinkDbHelper.COLUMN_ID,
            DrinkDbHelper.COLUMN_NAME,
            DrinkDbHelper.COLUMN_QUANTITY
    };

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

    public Drink createDrink(String name, double quantity) {
        ContentValues values = new ContentValues();
        values.put(DrinkDbHelper.COLUMN_NAME, name);
        values.put(DrinkDbHelper.COLUMN_QUANTITY, quantity);

        long insertId = database.insert(DrinkDbHelper.TABLE_DRINK, null, values);

        Cursor cursor = database.query(DrinkDbHelper.TABLE_DRINK,
                columns, DrinkDbHelper.COLUMN_ID + "=" + insertId,
                null, null, null, null);

        cursor.moveToFirst();
        Drink drink = cursorToDrink(cursor);
        cursor.close();

        return drink;
    }

    private Drink cursorToDrink(Cursor cursor) {
        int idIndex = cursor.getColumnIndex(DrinkDbHelper.COLUMN_ID);
        int idName = cursor.getColumnIndex(DrinkDbHelper.COLUMN_NAME);
        int idQuantity = cursor.getColumnIndex(DrinkDbHelper.COLUMN_QUANTITY);

        String name = cursor.getString(idName);
        double quantity = cursor.getDouble(idQuantity);
        long id = cursor.getLong(idIndex);

        Drink drink = new Drink(id, name, quantity);

        return drink;
    }

    public List<Drink> getAllDrinks() {
        List<Drink> drinkList = new ArrayList<>();

        Cursor cursor = database.query(DrinkDbHelper.TABLE_DRINK,
                columns, null, null, null, null, null);

        cursor.moveToFirst();
        Drink drink;

        while(!cursor.isAfterLast()) {
            drink = cursorToDrink(cursor);
            drinkList.add(drink);
            Log.d(LOG_TAG, "ID: " + drink.getId() + ", Inhalt: " + drink.toString());
            cursor.moveToNext();
        }

        cursor.close();

        return drinkList;
    }

}
