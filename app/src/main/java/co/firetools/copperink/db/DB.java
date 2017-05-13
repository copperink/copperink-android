package co.firetools.copperink.db;

import android.database.sqlite.SQLiteDatabase;

import co.firetools.copperink.clients.GlobalClient;

public class DB {
    private static DBHelper helper;


    /**
     * Readable / Writable database instances
     */
    public static SQLiteDatabase getReadable() { return get().getReadableDatabase(); }
    public static SQLiteDatabase getWritable() { return get().getWritableDatabase(); }


    /**
     * Returns Globally Accessible DB Object
     */
    private static DBHelper get() {
        if (helper == null)
            helper = new DBHelper(GlobalClient.getContext());

        return helper;
    }


    /**
     * Reset DB
     */
    public static void reset() {
        helper.resetDatabase(getWritable());

    }


    /**
     * Closes the DB connection
     */
    public static void close() {
        helper.close();
        helper = null;
    }
}


