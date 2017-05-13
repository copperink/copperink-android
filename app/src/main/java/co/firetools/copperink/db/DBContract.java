package co.firetools.copperink.db;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.HashMap;

import co.firetools.copperink.behaviors.Model;
import co.firetools.copperink.models.Account;
import co.firetools.copperink.models.Post;

public final class DBContract {
    private DBContract() {}

    // Table Names
    public static final String TABLE_ACCOUNTS    = "accounts";
    public static final String TABLE_POSTS       = "posts";

    // Column Types
    public static final String TYPE_PRIMARY      = "INTEGER PRIMARY KEY AUTOINCREMENT";
    public static final String TYPE_STRING       = "TEXT";
    public static final String TYPE_NUMBER       = "INTEGER";
    public static final String TYPE_BOOLEAN      = "INTEGER DEFAULT 0";

    // Common Column Names
    public static final String COLUMN_ID         = "id";
    public static final String COLUMN_OID        = "_id";
    public static final String COLUMN_IMAGE      = "image";

    // Account-specific columns
    public static final String COLUMN_NAME       = "name";
    public static final String COLUMN_TYPE       = "type";

    // Post-specific columns
    public static final String COLUMN_STATUS     = "status";
    public static final String COLUMN_CONTENT    = "content";
    public static final String COLUMN_POST_AT    = "post_at";
    public static final String COLUMN_SYNCED     = "synced";
    public static final String COLUMN_AUTHOR_ID  = "author_id";
    public static final String COLUMN_ACCOUNT_ID = "account_id";




    /**
     * Account Table Details and Columns
     */
    public static class AccountTable implements Model.Contract<Account> {
        public static final HashMap<String, String> FIELDS =
            new HashMap<String, String>() {{
                put(COLUMN_ID,    TYPE_STRING);
                put(COLUMN_NAME,  TYPE_STRING);
                put(COLUMN_IMAGE, TYPE_STRING);
                put(COLUMN_TYPE,  TYPE_STRING);
            }};

        @Override
        public String getTableName() {
            return TABLE_ACCOUNTS;
        }

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
    }



    /**
     * Post Table Details and Columns
     */
    public static class PostTable implements Model.Contract<Post> {
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

        @Override
        public String getTableName() { return TABLE_POSTS; }


        @Override
        public ContentValues contentValues(Post post) {
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

        @Override
        public Post toModel(Cursor cursor) {
            return new Post(
                cursor.getLong(cursor.getColumnIndex(COLUMN_OID)),
                cursor.getString(cursor.getColumnIndex(COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(COLUMN_STATUS)),
                cursor.getString(cursor.getColumnIndex(COLUMN_CONTENT)),
                cursor.getString(cursor.getColumnIndex(COLUMN_AUTHOR_ID)),
                cursor.getString(cursor.getColumnIndex(COLUMN_ACCOUNT_ID)),
                cursor.getString(cursor.getColumnIndex(COLUMN_IMAGE)),
                cursor.getLong(cursor.getColumnIndex(COLUMN_POST_AT)),
                cursor.getInt(cursor.getColumnIndex(COLUMN_SYNCED)) > 0
            );
        }
    }

}
