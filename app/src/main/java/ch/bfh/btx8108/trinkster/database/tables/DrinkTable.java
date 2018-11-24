package ch.bfh.btx8108.trinkster.database.tables;

public class DrinkTable {
    public static final String TABLE_DRINK = "drink";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_CATEGORY_ID = "category_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_QUANTITY = "quantity";

    public static final String SQL_CREATE =
            "CREATE TABLE " + TABLE_DRINK +
                    "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_CATEGORY_ID + " INTEGER NOT NULL, " +
                    COLUMN_NAME + " TEXT NOT NULL, " +
                    COLUMN_QUANTITY + " REAL NOT NULL);";
}
