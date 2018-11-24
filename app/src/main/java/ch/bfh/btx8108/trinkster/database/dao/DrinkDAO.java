package ch.bfh.btx8108.trinkster.database.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import ch.bfh.btx8108.trinkster.Category;
import ch.bfh.btx8108.trinkster.Drink;
import ch.bfh.btx8108.trinkster.database.DbHelper;
import ch.bfh.btx8108.trinkster.database.tables.CategoryTable;
import ch.bfh.btx8108.trinkster.database.tables.DrinkTable;

public class DrinkDAO {
    private static final String LOG_TAG = DrinkDAO.class.getSimpleName();

    private SQLiteDatabase database;

    private String[] columns = {
            DrinkTable.COLUMN_ID,
            DrinkTable.COLUMN_CATEGORY_ID,
            DrinkTable.COLUMN_NAME,
            DrinkTable.COLUMN_QUANTITY
    };

    private final static String COLUMN_CATEGORY_NAME = "category_name";

    public DrinkDAO(DbHelper dbHelper) {
        database = dbHelper.getWritableDatabase();
    }

    public Drink createDrink(Category category, String name, double quantity) {
        ContentValues values = new ContentValues();
        values.put(DrinkTable.COLUMN_CATEGORY_ID, category.getId());
        values.put(DrinkTable.COLUMN_NAME, name);
        values.put(DrinkTable.COLUMN_QUANTITY, quantity);

        long insertId = database.insert(DrinkTable.TABLE_DRINK, null, values);

        // Beim Abfragen eines Datenbankeintrags wird ein Cursor, d.h. ein Objekt, das pro Column Index den jeweiligen Wert
        // der Spalte enthält.
        Cursor cursor = database.rawQuery("SELECT d." + DrinkTable.COLUMN_ID + ","
                                    + "d." + DrinkTable.COLUMN_NAME + ", "
                                    + "d." + DrinkTable.COLUMN_QUANTITY + ", "
                                    + "d." + DrinkTable.COLUMN_CATEGORY_ID + ", "
                                    + "c." + CategoryTable.COLUMN_NAME + " " + COLUMN_CATEGORY_NAME +", "
                                    + "c." + CategoryTable.COLUMN_DESCRIPTION
                            + " FROM " + DrinkTable.TABLE_DRINK + " d"
                            + " INNER JOIN " + CategoryTable.TABLE_CATEGORY + " c ON d." + DrinkTable.COLUMN_CATEGORY_ID + " = c." + CategoryTable.COLUMN_ID
                            + " WHERE d." + DrinkTable.COLUMN_ID + " = ?", new String[]{Long.toString(insertId)});

        cursor.moveToFirst();

        // Die Methode cursorToDrink mapt den Inhalt des Cursors wieder in ein DrinkTable Objekt zurück.
        Drink drink = cursorToDrink(cursor);
        cursor.close();

        return drink;
    }

    private Drink cursorToDrink(Cursor cursor) {
        int idIndex = cursor.getColumnIndex(DrinkTable.COLUMN_ID);
        int idName = cursor.getColumnIndex(DrinkTable.COLUMN_NAME);
        int idQuantity = cursor.getColumnIndex(DrinkTable.COLUMN_QUANTITY);
        int idCategoryIndex = cursor.getColumnIndex(DrinkTable.COLUMN_CATEGORY_ID);
        int idCategoryName = cursor.getColumnIndex(COLUMN_CATEGORY_NAME);
        int idCategoryDescription = cursor.getColumnIndex(CategoryTable.COLUMN_DESCRIPTION);

        long categoryId = cursor.getLong(idCategoryIndex);
        String categoryName = cursor.getString(idCategoryName);
        String categoryDescription = cursor.getString(idCategoryDescription);

        long id = cursor.getLong(idIndex);
        String name = cursor.getString(idName);
        double quantity = cursor.getDouble(idQuantity);

        return new Drink(id, new Category(categoryId, categoryName, categoryDescription), name, quantity);
    }

    public List<Drink> getAllDrinks() {
        List<Drink> drinkList = new ArrayList<>();

        Cursor cursor = database.rawQuery("SELECT d." + DrinkTable.COLUMN_ID + ","
                + "d." + DrinkTable.COLUMN_NAME + ", "
                + "d." + DrinkTable.COLUMN_QUANTITY + ", "
                + "d." + DrinkTable.COLUMN_CATEGORY_ID + ", "
                + "c." + CategoryTable.COLUMN_NAME + " " + COLUMN_CATEGORY_NAME +", "
                + "c." + CategoryTable.COLUMN_DESCRIPTION
                + " FROM " + DrinkTable.TABLE_DRINK + " d"
                + " INNER JOIN " + CategoryTable.TABLE_CATEGORY + " c ON d." + DrinkTable.COLUMN_CATEGORY_ID + " = c." + CategoryTable.COLUMN_ID, new String[]{});

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
