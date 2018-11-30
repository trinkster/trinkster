package ch.bfh.btx8108.trinkster.database.tables;

import android.database.sqlite.SQLiteDatabase;

import ch.bfh.btx8108.trinkster.Category;

/**
 * Klasse, welche die Datenbanktabelle repräsentiert.
 */
public class CategoryTable {
    public static final String TABLE_CATEGORY = "category";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DESCRIPTION = "description";

    // Tabelle Category wird angelegt
    public static final String SQL_CREATE =
            "CREATE TABLE " + TABLE_CATEGORY +
                    "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " TEXT NOT NULL, " +
                    COLUMN_DESCRIPTION + " TEXT NOT NULL);";

    // Testdaten werden in der Tabelle Category angelegt
    public static void generateTestData(SQLiteDatabase db) {
        db.execSQL("INSERT INTO " + TABLE_CATEGORY + " (" + COLUMN_ID + ", " + COLUMN_NAME + ", "
                + COLUMN_DESCRIPTION + ") VALUES(1, 'Ungesüsste Getränke', 'Getränke ohne Zucker oder Süssstoff');");
        db.execSQL("INSERT INTO " + TABLE_CATEGORY + " (" + COLUMN_ID + ", " + COLUMN_NAME + ", "
                + COLUMN_DESCRIPTION + ") VALUES(2, 'Sonstige Getränke', 'Fruchtsäfte, Milch etc.');");
        db.execSQL("INSERT INTO " + TABLE_CATEGORY + " (" + COLUMN_ID + ", " + COLUMN_NAME + ", "
                + COLUMN_DESCRIPTION + ") VALUES(3, 'Koffeinhaltige Getränke', 'Kaffee, Cola etc.');");
        db.execSQL("INSERT INTO " + TABLE_CATEGORY + " (" + COLUMN_ID + ", " + COLUMN_NAME + ", "
                + COLUMN_DESCRIPTION + ") VALUES(4, 'Alkoholhaltige Getränke', 'Bier, Wein, Spirituosen etc.');");
    }
}
