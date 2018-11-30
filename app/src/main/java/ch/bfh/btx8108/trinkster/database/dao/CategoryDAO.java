package ch.bfh.btx8108.trinkster.database.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import ch.bfh.btx8108.trinkster.Category;
import ch.bfh.btx8108.trinkster.database.DbHelper;
import ch.bfh.btx8108.trinkster.database.tables.CategoryTable;

/**
 * Data Access Object (DAO) für den Typ Category.
 * DAOs sind dafür da, die Zugriffslogik auf DB-Objekte zu kapseln und von der
 * Repräsentation der Daten (hier Klasse Category) zu trennen.
 * Holt und schreibt Daten in die Datenbank.
 */
public class CategoryDAO {
    private static final String LOG_TAG = CategoryDAO.class.getSimpleName();

    private SQLiteDatabase database;

    // Stringarray mit den Tabellenspalten für spätere DB-Befehle
    private String[] columns = {
            CategoryTable.COLUMN_ID,
            CategoryTable.COLUMN_NAME,
            CategoryTable.COLUMN_DESCRIPTION
    };

    /**
     * Konstruktor, der eine schreibbare Instanz vom dbHelper holt
     * @param dbHelper
     */
    public CategoryDAO(DbHelper dbHelper) {
        database = dbHelper.getWritableDatabase();
    }

    /**
     * createCategory() erzeugt einen Category-Datensatz in der DB und liefert ein Category-Domain-
     * Objekt mit dessen Daten zurück
     * @param name
     * @param description
     * @return category
     */
    public Category createCategory(String name, String description) {
        ContentValues values = new ContentValues();
        values.put(CategoryTable.COLUMN_NAME, name);
        values.put(CategoryTable.COLUMN_DESCRIPTION, description);

        Cursor cursor; // Objekt, das zurück kommt, wenn ein Query durchgeführt wird
        long insertId = database.insert(CategoryTable.TABLE_CATEGORY, null, values);
        cursor = database.query(CategoryTable.TABLE_CATEGORY,
                columns, CategoryTable.COLUMN_ID + "=" + insertId,
                null, null, null, null);

        cursor.moveToFirst();
        Category category = cursorToCategory(cursor);
        cursor.close();

        return category;
    }

    /**
     * Lädt die Daten eines Cursors in ein Domain-Objekt
     * @param cursor
     * @return Category
     */
    private Category cursorToCategory(Cursor cursor) {
        int idIndex = cursor.getColumnIndex(CategoryTable.COLUMN_ID);
        int idName = cursor.getColumnIndex(CategoryTable.COLUMN_NAME);
        int idDescription = cursor.getColumnIndex(CategoryTable.COLUMN_DESCRIPTION);

        String name = cursor.getString(idName);
        String description = cursor.getString(idDescription);
        long id = cursor.getLong(idIndex);

        return new Category(id, name, description);
    }

    /**
     * getAllCategories() lädt alle vorhandenen Categories aus der Datenbank und gibt sie
     * als Liste zurück.
     * @return categoryList
     */
    public List<Category> getAllCategories() {
        List<Category> categoryList = new ArrayList<>();

        Cursor cursor = database.query(CategoryTable.TABLE_CATEGORY,
                columns, null, null, null, null, null);

        cursor.moveToFirst();
        Category category;

        while(!cursor.isAfterLast()) {
            category = cursorToCategory(cursor);
            categoryList.add(category);
            Log.d(LOG_TAG, "ID: " + category.getId() + ", Inhalt: " + category.toString());
            cursor.moveToNext();
        }

        cursor.close();

        return categoryList;
    }
}
