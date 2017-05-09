package co.firetools.copperink.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.HashMap;

import co.firetools.copperink.models.Account;

public final class DBContract {
    private static final String TYPE_STRING = "TEXT";
    private DBContract() {}



    /**
     * Account Table Details and Database Columns
     */
    public static class AccountTable implements BaseColumns {
        public  static final String TABLE_NAME   = "accounts";
        private static final String COLUMN_ID    = "id";
        private static final String COLUMN_NAME  = "name";
        private static final String COLUMN_IMAGE = "image";
        private static final String COLUMN_TYPE  = "type";

        public static final HashMap<String, String> FIELDS =
            new HashMap<String, String>() {{
                put(COLUMN_ID,    TYPE_STRING);
                put(COLUMN_NAME,  TYPE_STRING);
                put(COLUMN_IMAGE, TYPE_STRING);
                put(COLUMN_TYPE,  TYPE_STRING);
            }};

        public static ContentValues contentValues(Account account) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_ID,    account.getID());
            values.put(COLUMN_NAME,  account.getName());
            values.put(COLUMN_IMAGE, account.getImageUrl());
            values.put(COLUMN_TYPE,  account.getType());
            return values;
        }

        public static ArrayList<Account> all(SQLiteDatabase db) {
            Cursor cursor = db.query(
                TABLE_NAME,                     // The table to query
                new String[] { COLUMN_ID, COLUMN_NAME, COLUMN_IMAGE, COLUMN_TYPE },                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
            );

            ArrayList<Account> accounts = new ArrayList<>();
            while(cursor.moveToNext()) {
                Account account = new Account(
                    cursor.getString(cursor.getColumnIndex(COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_NAME)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_TYPE)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_IMAGE))
                );

                accounts.add(account);
            }

            cursor.close();
            return accounts;
        }
    }
}
