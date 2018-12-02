package ch.bfh.btx8108.trinkster.database.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import ch.bfh.btx8108.trinkster.DrinkName;
import ch.bfh.btx8108.trinkster.StatisticEntry;
import ch.bfh.btx8108.trinkster.database.DbHelper;
import ch.bfh.btx8108.trinkster.database.tables.CategoryTable;
import ch.bfh.btx8108.trinkster.database.tables.DrinkTable;

public class StatisticDAO {

    private static final String LOG_TAG = StatisticDAO.class.getSimpleName();

    private SQLiteDatabase database;

    /**
     * Konstruktor, der eine schreibbare Instanz vom dbHelper holt
     * @param dbHelper
     */
    public StatisticDAO(DbHelper dbHelper) {
        database = dbHelper.getWritableDatabase();
    }

    public List<StatisticEntry> totalQuantitiesPerCategoryAndDay(LocalDateTime localDateTimeBegin, LocalDateTime localDateTimeEnd) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String begin = localDateTimeBegin.format(formatter) + " 00:00:00";
        String end = localDateTimeEnd.format(formatter) + " 23:59:59";

        Cursor cursor = database.rawQuery("SELECT c." + CategoryTable.COLUMN_NAME + ", sum(d." + DrinkTable.COLUMN_QUANTITY + ") quantity"
                + " FROM " + DrinkTable.TABLE_DRINK + " d"
                + " INNER JOIN " + CategoryTable.TABLE_CATEGORY + " c ON d." + DrinkTable.COLUMN_CATEGORY_ID + " = c." + CategoryTable.COLUMN_ID
                + " WHERE d." + DrinkTable.COLUMN_DATE_TIME + " BETWEEN ? AND ?"
                + " GROUP BY c.name;", new String[]{begin, end});

        cursor.moveToFirst();

        List<StatisticEntry> statistic = new ArrayList<>();

        while(!cursor.isAfterLast()) {
            int idName = cursor.getColumnIndex(DrinkTable.COLUMN_NAME);
            int idQuantity = cursor.getColumnIndex(DrinkTable.COLUMN_QUANTITY);
            statistic.add(new StatisticEntry(cursor.getString(idName), cursor.getDouble(idQuantity)));
            cursor.moveToNext();
        }

        cursor.close();

        return statistic;
    }

    public List<DrinkName> getDrinksOfCategoryAndDate(String category, LocalDateTime localDateTimeBegin, LocalDateTime localDateTimeEnd) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String begin = localDateTimeBegin.format(formatter) + " 00:00:00";
        String end = localDateTimeEnd.format(formatter) + " 23:59:59";

        Cursor cursor = database.rawQuery("SELECT d." + DrinkTable.COLUMN_NAME + ", sum(d." + DrinkTable.COLUMN_QUANTITY + ") quantity"
                + " FROM " + DrinkTable.TABLE_DRINK + " d"
                + " INNER JOIN " + CategoryTable.TABLE_CATEGORY + " c ON d." + DrinkTable.COLUMN_CATEGORY_ID + " = c." + CategoryTable.COLUMN_ID
                + " WHERE c." + CategoryTable.COLUMN_NAME + "= ? AND d." + DrinkTable.COLUMN_DATE_TIME + " BETWEEN ? AND ?"
                + " GROUP BY d.name;", new String[]{category, begin, end});

        cursor.moveToFirst();

        List<DrinkName> drinkList = new ArrayList<>();
        DrinkName drink;

        while(!cursor.isAfterLast()) {
            //drink = cursorToDrink(cursor);
            //drinkList.add(drink);
            //Log.d(LOG_TAG, "ID: " + drink.getId() + ", Inhalt: " + drink.toString());
            int idName = cursor.getColumnIndex(DrinkTable.COLUMN_NAME);
            int idQuantity = cursor.getColumnIndex(DrinkTable.COLUMN_QUANTITY);
            String name = cursor.getString(idName);
            Double quantity = cursor.getDouble(idQuantity);
            drinkList.add(new DrinkName(name, quantity));
            Log.d(LOG_TAG, "Name: " + name + ", Inhalt: " + quantity);
            cursor.moveToNext();
        }

        cursor.close();

        return drinkList;
    }

    public double totalQuantityForACategoryAndDay(String category, LocalDateTime localDateTimeBegin, LocalDateTime localDateTimeEnd) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String begin = localDateTimeBegin.format(formatter) + " 00:00:00";
        String end = localDateTimeEnd.format(formatter) + " 23:59:59";

        Cursor cursor = database.rawQuery("SELECT sum(d." + DrinkTable.COLUMN_QUANTITY + ") quantity"
                + " FROM " + DrinkTable.TABLE_DRINK + " d"
                + " INNER JOIN " + CategoryTable.TABLE_CATEGORY + " c ON d." + DrinkTable.COLUMN_CATEGORY_ID + " = c." + CategoryTable.COLUMN_ID
                + " WHERE c." + CategoryTable.COLUMN_NAME + "= ? AND d." + DrinkTable.COLUMN_DATE_TIME + " BETWEEN ? AND ?"
                , new String[]{category, begin, end});

        double total = 0;

        cursor.moveToFirst();

        while(!cursor.isAfterLast()) {
        int idQuantity = cursor.getColumnIndex(DrinkTable.COLUMN_QUANTITY);
        total = cursor.getDouble(idQuantity);
            cursor.moveToNext();
        }

        cursor.close();

        return total;
    }

}
