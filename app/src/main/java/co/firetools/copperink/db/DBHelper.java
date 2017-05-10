package co.firetools.copperink.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.HashMap;
import java.util.Map;

public class DBHelper extends SQLiteOpenHelper {
    private static final int    DATABASE_VERSION = 1;
    private static final String DATABASE_NAME    = "CopperInk.db";


    /**
     * DB Helper Constructor
     */
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }



    /**
     * onCreate Callback
     */
    public void onCreate(SQLiteDatabase db) {
        createDatabase(db);
    }


    /**
     * Upgrade DB on version change
     *
     * TODO:
     * Don't destroy existing data
     */
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        resetDatabase(db);
    }


    /**
     * Downgrade DB
     */
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }



    // -------


    /**
     * Helper to Create DB
     */
    public static void createDatabase(SQLiteDatabase db) {
        createTable(db, DBContract.AccountTable.TABLE_NAME, DBContract.AccountTable.FIELDS);
    }

    /**
     * Helper to Reset DB
     */
    public static void resetDatabase(SQLiteDatabase db) {
        destroyTable(db, DBContract.AccountTable.TABLE_NAME);
        createDatabase(db);
    }



    /**
     * Helper to create a table
     */
    private static void createTable(SQLiteDatabase db, String tableName, HashMap<String, String> fields) {
        String command = "CREATE TABLE " + tableName + " ( _id INTEGER PRIMARY KEY AUTOINCREMENT";

        for (Map.Entry<String, String> entry : fields.entrySet())
            command = command + ", " + entry.getKey() + " " + entry.getValue();

        command = command + " )";

        db.execSQL(command);
    }


    /**
     * Helper to destroy table
     */
    private static void destroyTable(SQLiteDatabase db, String tableName){
        db.execSQL("DROP TABLE IF EXISTS " + tableName);
    }

}
