package ch.bfh.btx8108.trinkster.database.tables;

import android.database.sqlite.SQLiteDatabase;

/**
 * Klasse, welche die Datenbanktabelle repräsentiert.
 */
public class DrinkTable {
    public static final String TABLE_DRINK = "drink";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_CATEGORY_ID = "category_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_QUANTITY = "quantity";
    public static final String COLUMN_DATE_TIME = "date_time";

    // Tabelle Drink wird angelegt
    public static final String SQL_CREATE =
            "CREATE TABLE " + TABLE_DRINK +
                    "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_CATEGORY_ID + " INTEGER NOT NULL, " +
                    COLUMN_NAME + " TEXT NOT NULL, " +
                    COLUMN_QUANTITY + " REAL NOT NULL, " +
                    COLUMN_DATE_TIME + " TEXT NOT NULL);";

    // Löscht die Drink Tabelle für DB-Version 3 um sie anschliessend neu zu erzeugen.
    public static final String DROP_TABLE_V3 = "DROP TABLE " + TABLE_DRINK + ";";

    // Testdaten werden in der Tabelle Drink angelegt
    public static void generateTestData(SQLiteDatabase db) {
        db.execSQL("INSERT INTO " + TABLE_DRINK + " (" + COLUMN_ID + ", " + COLUMN_CATEGORY_ID + ", " + COLUMN_NAME + ", "
                + COLUMN_QUANTITY + ", " + COLUMN_DATE_TIME + ") VALUES(1, 1, 'Grüntee', 2.5, '2018-12-01 08:12:00');");
        db.execSQL("INSERT INTO " + TABLE_DRINK + " (" + COLUMN_ID + ", " + COLUMN_CATEGORY_ID + ", " + COLUMN_NAME + ", "
                + COLUMN_QUANTITY + ", " + COLUMN_DATE_TIME + ") VALUES(2, 1, 'Wasser', 5.0, '2018-12-01 09:12:00');");
        db.execSQL("INSERT INTO " + TABLE_DRINK + " (" + COLUMN_ID + ", " + COLUMN_CATEGORY_ID + ", " + COLUMN_NAME + ", "
                + COLUMN_QUANTITY + ", " + COLUMN_DATE_TIME + ") VALUES(3, 1, 'Wasser', 3.0, '2018-12-01 13:02:00');");

        db.execSQL("INSERT INTO " + TABLE_DRINK + " (" + COLUMN_ID + ", " + COLUMN_CATEGORY_ID + ", " + COLUMN_NAME + ", "
                + COLUMN_QUANTITY + ", " + COLUMN_DATE_TIME + ") VALUES(4, 2, 'Fruchtsaft', 2.0, '2018-12-01 08:00:00');");
        db.execSQL("INSERT INTO " + TABLE_DRINK + " (" + COLUMN_ID + ", " + COLUMN_CATEGORY_ID + ", " + COLUMN_NAME + ", "
                + COLUMN_QUANTITY + ", " + COLUMN_DATE_TIME + ") VALUES(5, 2, 'Milch', 2.5, '2018-12-01 07:37:00');");

        db.execSQL("INSERT INTO " + TABLE_DRINK + " (" + COLUMN_ID + ", " + COLUMN_CATEGORY_ID + ", " + COLUMN_NAME + ", "
                + COLUMN_QUANTITY + ", " + COLUMN_DATE_TIME + ") VALUES(6, 3, 'Kaffee', 2.0, '2018-12-01 10:00:00');");
        db.execSQL("INSERT INTO " + TABLE_DRINK + " (" + COLUMN_ID + ", " + COLUMN_CATEGORY_ID + ", " + COLUMN_NAME + ", "
                + COLUMN_QUANTITY + ", " + COLUMN_DATE_TIME + ") VALUES(7, 3, 'Kaffee', 2.0, '2018-12-01 13:30:00');");
        db.execSQL("INSERT INTO " + TABLE_DRINK + " (" + COLUMN_ID + ", " + COLUMN_CATEGORY_ID + ", " + COLUMN_NAME + ", "
                + COLUMN_QUANTITY + ", " + COLUMN_DATE_TIME + ") VALUES(8, 3, 'Cola', 3.0, '2018-12-01 14:45:00');");

        db.execSQL("INSERT INTO " + TABLE_DRINK + " (" + COLUMN_ID + ", " + COLUMN_CATEGORY_ID + ", " + COLUMN_NAME + ", "
                + COLUMN_QUANTITY + ", " + COLUMN_DATE_TIME + ") VALUES(9, 4, 'Bier', 3.0, '2018-12-01 17:22:00');");
        db.execSQL("INSERT INTO " + TABLE_DRINK + " (" + COLUMN_ID + ", " + COLUMN_CATEGORY_ID + ", " + COLUMN_NAME + ", "
                + COLUMN_QUANTITY + ", " + COLUMN_DATE_TIME + ") VALUES(10, 4, 'Wein', 1.0, '2018-12-01 18:03:00');");
        db.execSQL("INSERT INTO " + TABLE_DRINK + " (" + COLUMN_ID + ", " + COLUMN_CATEGORY_ID + ", " + COLUMN_NAME + ", "
                + COLUMN_QUANTITY + ", " + COLUMN_DATE_TIME + ") VALUES(11, 4, 'Wein', 3.0, '2018-12-01 19:07:00');");
    }
}
