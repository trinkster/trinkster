package ch.bfh.btx8108.trinkster.database.tables;

public class CategoryTable {
    public static final String TABLE_CATEGORY = "category";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DESCRIPTION = "description";

    public static final String SQL_CREATE =
            "CREATE TABLE " + TABLE_CATEGORY +
                    "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " TEXT NOT NULL, " +
                    COLUMN_DESCRIPTION + " TEXT NOT NULL);";
}
