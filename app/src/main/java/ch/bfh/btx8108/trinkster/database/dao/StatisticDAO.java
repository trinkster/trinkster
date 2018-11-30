package ch.bfh.btx8108.trinkster.database.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

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

    public List<StatisticEntry> totalQuantitiesPerCategoryAndDay(LocalDateTime localDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String begin = localDateTime.format(formatter) + " 00:00:00";
        String end = localDateTime.format(formatter) + " 23:59:59";

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
}
