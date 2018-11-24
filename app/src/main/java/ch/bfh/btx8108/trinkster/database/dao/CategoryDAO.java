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

public class CategoryDAO {
    private static final String LOG_TAG = CategoryDAO.class.getSimpleName();

    private SQLiteDatabase database;

    private String[] columns = {
            CategoryTable.COLUMN_ID,
            CategoryTable.COLUMN_NAME,
            CategoryTable.COLUMN_DESCRIPTION
    };

    public CategoryDAO(DbHelper dbHelper) {
        database = dbHelper.getWritableDatabase();
    }

    public Category createCategory(String name, String description) {
        ContentValues values = new ContentValues();
        values.put(CategoryTable.COLUMN_NAME, name);
        values.put(CategoryTable.COLUMN_DESCRIPTION, description);

        Cursor cursor;
        if (!exists(name)) {
            long insertId = database.insert(CategoryTable.TABLE_CATEGORY, null, values);
            cursor = database.query(CategoryTable.TABLE_CATEGORY,
                    columns, CategoryTable.COLUMN_ID + "=" + insertId,
                    null, null, null, null);
        } else{
            cursor = database.query(CategoryTable.TABLE_CATEGORY,
                    columns, CategoryTable.COLUMN_NAME + " = '" + name + "'",
                    null, null, null, null);;
        }

        cursor.moveToFirst();
        Category category = cursorToCategory(cursor);
        cursor.close();

        return category;
    }

    private boolean exists(String name) {
        Cursor cursor = database.query(CategoryTable.TABLE_CATEGORY,
                columns, CategoryTable.COLUMN_NAME + " = '" + name + "'",
                null, null, null, null);

        if (cursor.getCount() == 0) {
            return false;
        }

        return true;
    }

    private Category cursorToCategory(Cursor cursor) {
        int idIndex = cursor.getColumnIndex(CategoryTable.COLUMN_ID);
        int idName = cursor.getColumnIndex(CategoryTable.COLUMN_NAME);
        int idDescription = cursor.getColumnIndex(CategoryTable.COLUMN_DESCRIPTION);

        String name = cursor.getString(idName);
        String description = cursor.getString(idDescription);
        long id = cursor.getLong(idIndex);

        return new Category(id, name, description);
    }

    public List<Category> getAllCategories() {
        List<Category> categoryList = new ArrayList<>();

        Cursor cursor = database.query(CategoryTable.TABLE_CATEGORY,
                columns, null, null, null, null, null);

        cursor.moveToFirst();
        Category catetory;

        while(!cursor.isAfterLast()) {
            catetory = cursorToCategory(cursor);
            categoryList.add(catetory);
            Log.d(LOG_TAG, "ID: " + catetory.getId() + ", Inhalt: " + catetory.toString());
            cursor.moveToNext();
        }

        cursor.close();

        return categoryList;
    }
}
