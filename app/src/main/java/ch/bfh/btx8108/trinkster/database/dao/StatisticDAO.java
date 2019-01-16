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
     * @param dbHelper - helper for database
     */
    public StatisticDAO(DbHelper dbHelper) {
        database = dbHelper.getWritableDatabase();
    }

    /**
     * returns each category and their quantaty to create the pie chart
     * @param localDateTimeBegin - when the timeline starts, start of database query
     * @param localDateTimeEnd - when the timeline ends, end of database query
     * @return list of entries within the timeline
     */
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

    /**
     * returns the drinklist for one category and within a timeline, the same drinks, for example water,
     * are counted together
     * @param category - chosen drink category
     * @param localDateTimeBegin - when the timeline starts, start of database query
     * @param localDateTimeEnd - when the timeline ends, end of database query
     * @return the drinklist for one category
     */
    public List<Object> getDrinksOfCategoryAndDate(String category, LocalDateTime localDateTimeBegin, LocalDateTime localDateTimeEnd) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String begin = localDateTimeBegin.format(formatter) + " 00:00:00";
        String end = localDateTimeEnd.format(formatter) + " 23:59:59";

        Cursor cursor = database.rawQuery("SELECT d." + DrinkTable.COLUMN_NAME + ", sum(d." + DrinkTable.COLUMN_QUANTITY + ") quantity"
                + " FROM " + DrinkTable.TABLE_DRINK + " d"
                + " INNER JOIN " + CategoryTable.TABLE_CATEGORY + " c ON d." + DrinkTable.COLUMN_CATEGORY_ID + " = c." + CategoryTable.COLUMN_ID
                + " WHERE c." + CategoryTable.COLUMN_NAME + "= ? AND d." + DrinkTable.COLUMN_DATE_TIME + " BETWEEN ? AND ?"
                + " GROUP BY d.name;", new String[]{category, begin, end});

        cursor.moveToFirst();

        List<Object> drinkList = new ArrayList<>();
        DrinkName drink;

        while(!cursor.isAfterLast()) {
            drink = cursorToDrink(cursor);
            drinkList.add(drink);
            Log.d(LOG_TAG, "Name: " + drink.getName() + ", Quantity: " + drink.getQuantity());
            cursor.moveToNext();
        }

        cursor.close();

        Log.d(LOG_TAG, "DrinkList: " + drinkList);
        return drinkList;

    }

    /**
     * gets a drink name from the list and returns the name with the quantity
     * @param cursor - the curser getting all drink names and quantities
     * @return the drink name in a readable form
     */
    private DrinkName cursorToDrink(Cursor cursor) {
        int idName = cursor.getColumnIndex(DrinkTable.COLUMN_NAME);
        int idQuantity = cursor.getColumnIndex(DrinkTable.COLUMN_QUANTITY);
        String name = cursor.getString(idName);
        Double quantity = cursor.getDouble(idQuantity);

        return new DrinkName(name, quantity);
    }

    /**
     * gets the total quantity of one drink category
     * @param category - chosen drink category
     * @param localDateTimeBegin - when the timeline starts, start of database query
     * @param localDateTimeEnd - when the timeline ends, end of database query
     * @return total quantity for this drink category
     */
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
