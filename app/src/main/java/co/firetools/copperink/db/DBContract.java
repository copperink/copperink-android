package co.firetools.copperink.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;

import java.util.HashMap;

import co.firetools.copperink.behaviors.Model;
import co.firetools.copperink.models.Account;
import co.firetools.copperink.models.Post;

public final class DBContract {
    public  static final String TYPE_PRIMARY = "INTEGER PRIMARY KEY AUTOINCREMENT";
    private static final String TYPE_STRING  = "TEXT";
    private static final String TYPE_NUMBER  = "INTEGER";
    private static final String TYPE_BOOLEAN = "INTEGER DEFAULT 0";

    private DBContract() {}


    /**
     * Account Table Details and Columns
     */
    public static class AccountTable implements BaseColumns, Model.Contract<Account> {
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

        @Override
        public ContentValues contentValues(Account account) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_ID,    account.getID());
            values.put(COLUMN_NAME,  account.getName());
            values.put(COLUMN_IMAGE, account.getImageUrl());
            values.put(COLUMN_TYPE,  account.getType());
            return values;
        }

        @Override
        public Account toModel(Cursor cursor) {
            return new Account(
                cursor.getString(cursor.getColumnIndex(COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(COLUMN_NAME)),
                cursor.getString(cursor.getColumnIndex(COLUMN_TYPE)),
                cursor.getString(cursor.getColumnIndex(COLUMN_IMAGE))
            );
        }

        @Override
        public String getTableName() {
            return TABLE_NAME;
        }
    }



    /**
     * Post Table Details and Columns
     */
    public static class PostTable implements BaseColumns {
        public  static final String TABLE_NAME        = "posts";
        private static final String COLUMN_ID         = "id";
        private static final String COLUMN_STATUS     = "status";
        private static final String COLUMN_CONTENT    = "content";
        private static final String COLUMN_IMAGE      = "image_url";
        private static final String COLUMN_POST_AT    = "post_at";
        private static final String COLUMN_SYNCED     = "synced";
        private static final String COLUMN_ACCOUNT_ID = "account_id";
        private static final String COLUMN_AUTHOR_ID  = "author_id";


        public static final HashMap<String, String> FIELDS =
            new HashMap<String, String>() {{
                put(COLUMN_ID,         TYPE_STRING);
                put(COLUMN_STATUS,     TYPE_STRING);
                put(COLUMN_CONTENT,    TYPE_STRING);
                put(COLUMN_IMAGE,      TYPE_STRING);
                put(COLUMN_ACCOUNT_ID, TYPE_STRING);
                put(COLUMN_AUTHOR_ID,  TYPE_STRING);
                put(COLUMN_POST_AT,    TYPE_NUMBER);
                put(COLUMN_SYNCED,     TYPE_BOOLEAN);
            }};


        public static ContentValues contentValues(Post post) {
            ContentValues values = new ContentValues();

            values.put(COLUMN_ID,          post.getID());
            values.put(COLUMN_STATUS,      post.getStatus());
            values.put(COLUMN_CONTENT,     post.getContent());
            values.put(COLUMN_IMAGE,       post.getImageUrl());
            values.put(COLUMN_ACCOUNT_ID,  post.getAccountID());
            values.put(COLUMN_AUTHOR_ID,   post.getAuthorID());
            values.put(COLUMN_POST_AT,     post.getPostAt());
            values.put(COLUMN_SYNCED,      post.isSynced());

            return values;
        }
    }

}
