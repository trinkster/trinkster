package ch.bfh.btx8108.trinkster.database.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.bfh.btx8108.trinkster.Category;
import ch.bfh.btx8108.trinkster.Drink;
import ch.bfh.btx8108.trinkster.database.DbHelper;
import ch.bfh.btx8108.trinkster.database.tables.CategoryTable;
import ch.bfh.btx8108.trinkster.database.tables.DrinkTable;

/**
 * Data Access Object (DAO) für den Typ Drink.
 * DAOs sind dafür da, die Zugriffslogik auf DB-Objekte zu kapseln und von der
 * Repräsentation der Daten (hier Klasse Drink) zu trennen.
 */
public class DrinkDAO {
    private static final String LOG_TAG = DrinkDAO.class.getSimpleName();
    private final static String COLUMN_CATEGORY_NAME = "category_name";

    private SQLiteDatabase database;
    private LocalDateTime lastKnownDateTime = LocalDateTime.now();

    // Stringarray mit den Tabellenspalten für spätere DB-Befehle
    private String[] columns = {
            DrinkTable.COLUMN_ID,
            DrinkTable.COLUMN_CATEGORY_ID,
            DrinkTable.COLUMN_NAME,
            DrinkTable.COLUMN_QUANTITY,
            DrinkTable.COLUMN_DATE_TIME
    };

    private String query = "SELECT d." + DrinkTable.COLUMN_ID + ","
            + "d." + DrinkTable.COLUMN_NAME + ", "
            + "d." + DrinkTable.COLUMN_QUANTITY + ", "
            + "d." + DrinkTable.COLUMN_CATEGORY_ID + ", "
            + "d." + DrinkTable.COLUMN_DATE_TIME + ", "
            + "c." + CategoryTable.COLUMN_NAME + " " + COLUMN_CATEGORY_NAME +", "
            + "c." + CategoryTable.COLUMN_DESCRIPTION
            + " FROM " + DrinkTable.TABLE_DRINK + " d"
            + " INNER JOIN " + CategoryTable.TABLE_CATEGORY + " c ON d." + DrinkTable.COLUMN_CATEGORY_ID + " = c." + CategoryTable.COLUMN_ID
            + " ORDER BY " + DrinkTable.COLUMN_DATE_TIME + " DESC";

    /**
     * Konstruktor, der eine schreibbare Instanz vom dbHelper holt
     * @param dbHelper
     */
    public DrinkDAO(DbHelper dbHelper) {
        database = dbHelper.getWritableDatabase();
    }

    /**
     * createDrink() erzeugt einen Drink-Datensatz in der DB und liefert ein Drink-Domain-
     * Objekt mit dessen Daten zurück
     * @param category
     * @param name
     * @param quantity
     * @param dateTime
     * @return drink
     */
    public Drink createDrink(Category category, String name, double quantity, String dateTime) {
        ContentValues values = new ContentValues();
        values.put(DrinkTable.COLUMN_CATEGORY_ID, category.getId());
        values.put(DrinkTable.COLUMN_NAME, name);
        values.put(DrinkTable.COLUMN_QUANTITY, quantity);
        values.put(DrinkTable.COLUMN_DATE_TIME, dateTime);

        long insertId = database.insert(DrinkTable.TABLE_DRINK, null, values);

        // Beim Abfragen eines Datenbankeintrags wird ein Cursor erstellt, d.h. ein Objekt, das pro Column Index den jeweiligen Wert
        // der Spalte enthält.
        Cursor cursor = database.rawQuery("SELECT d." + DrinkTable.COLUMN_ID + ","
                                    + "d." + DrinkTable.COLUMN_NAME + ", "
                                    + "d." + DrinkTable.COLUMN_QUANTITY + ", "
                                    + "d." + DrinkTable.COLUMN_CATEGORY_ID + ", "
                                    + "d." + DrinkTable.COLUMN_DATE_TIME + ", "
                                    + "c." + CategoryTable.COLUMN_NAME + " " + COLUMN_CATEGORY_NAME + ", "
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

    /**
     * getAllDrinks() lädt alle vorhandenen Drinks aus der Datenbank und gibt sie
     * als Liste zurück.
     * @return drinkList
     */
    public List<Drink> getAllDrinks() {
        List<Drink> drinkList = new ArrayList<>();
        Cursor cursor = database.rawQuery(query, new String[]{});

        cursor.moveToFirst();
        Drink drink;

        while(!cursor.isAfterLast()) {
            drink = cursorToDrink(cursor);
            drinkList.add(drink);
            Log.d(LOG_TAG, "getAllDrinks(): ID: " + drink.getId() + ", Inhalt: " + drink.toString() + ", Erfassungsdatum: " + drink.getDateTime().toLocalDate());
            cursor.moveToNext();
        }

        cursor.close();

        return drinkList;
    }

    /**
     * getAllDrinksWithDates() lädt alle vorhandenen Drinks aus der Datenbank und gibt sie als Liste zurück.
     * Im Vergleich zu getAllDrinks werden aber zwischen den Drinks noch die Daten in der Liste abgelegt.
     *
     * @return drinkList bestehend aus einer ArrayList mit einerseits Drink-Objekten und LocalDate-Objekten
     */
    // TODO: eventuell so abändern, dass nicht eine DB-Abfrage gemacht wird, sondern die Liste von getAllDrinks() entsprechend erweitert wird?
    public List getAllDrinksWithDates(){
        List<Object> drinkList = new ArrayList<>();
        Cursor cursor = database.rawQuery(query, new String[]{});

        cursor.moveToFirst();
        Drink drink;

        // FIXME: ugly solution but works for now
        // FIXME: needed because otherwise the first entry doesn't have a header
        drinkList.add(lastKnownDateTime.toLocalDate());

        while(!cursor.isAfterLast()) {
            drink = cursorToDrink(cursor);

            if(Duration.between(drink.getDateTime(), lastKnownDateTime).toDays() >= 1){
                lastKnownDateTime = drink.getDateTime();
                drinkList.add(lastKnownDateTime.toLocalDate());
            }
            drinkList.add(drink);
            cursor.moveToNext();
        }

        cursor.close();

        return drinkList;
    }

    /**
     * Lädt die Daten eines Cursors in ein Domain-Objekt
     * @param cursor
     * @return Drink
     */
    private Drink cursorToDrink(Cursor cursor) {
        int idIndex = cursor.getColumnIndex(DrinkTable.COLUMN_ID);
        int idName = cursor.getColumnIndex(DrinkTable.COLUMN_NAME);
        int idQuantity = cursor.getColumnIndex(DrinkTable.COLUMN_QUANTITY);
        int idDateTime = cursor.getColumnIndex(DrinkTable.COLUMN_DATE_TIME);
        int idCategoryIndex = cursor.getColumnIndex(DrinkTable.COLUMN_CATEGORY_ID);
        int idCategoryName = cursor.getColumnIndex(COLUMN_CATEGORY_NAME);
        int idCategoryDescription = cursor.getColumnIndex(CategoryTable.COLUMN_DESCRIPTION);

        long categoryId = cursor.getLong(idCategoryIndex);
        String categoryName = cursor.getString(idCategoryName);
        String categoryDescription = cursor.getString(idCategoryDescription);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DbHelper.DATE_TIME_FORMAT);
        LocalDateTime dateTime = LocalDateTime.parse(cursor.getString(idDateTime), formatter);

        long id = cursor.getLong(idIndex);
        String name = cursor.getString(idName);
        double quantity = cursor.getDouble(idQuantity);

        return new Drink(id, new Category(categoryId, categoryName, categoryDescription), name, quantity, dateTime);
    }
}
