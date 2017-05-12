package co.firetools.copperink.db;

import android.database.Cursor;
import android.database.DatabaseUtils;

import java.util.ArrayList;

import co.firetools.copperink.behaviors.Model;

public class DBQuery {

    /**
     * Get count of a table's rows
     */
    public static long count(Model.Contract contract) {
        return DatabaseUtils.queryNumEntries(DB.getReadable(), contract.getTableName());
    }


    /**
     * Delete all rows for a contract
     */
    public static void deleteAll(Model.Contract contract) {
        DB.getWritable().delete(contract.getTableName(), null, null);
    }


    /**
     * Insert a new row
     */
    public static void insert(Model.Contract contract, Model model) {
        DB.getWritable().insert(contract.getTableName(), null, contract.contentValues(model));
    }

    public static void insert(Model model) {
        insert(model.getContract(), model);
    }


    /**
     * Get First Row
     */
    public static Model first(Model.Contract contract) {
        Model model = null;
        Cursor cursor = getReadCursor(contract);

        if (cursor != null && cursor.moveToFirst()) {
            model = contract.toModel(cursor);
            cursor.close();
        }

        return model;
    }


    /**
     * Get All rows for a contract
     */
    public static ArrayList<? extends Model> getAll(Model.Contract contract) {
        Cursor cursor = getReadCursor(contract);
        ArrayList<Model> modelList = new ArrayList<>();

        while(cursor.moveToNext())
            modelList.add(contract.toModel(cursor));

        cursor.close();
        return modelList;
    }


    /**
     * Get a cursor with all columns for a DB and contract
     */
    private static Cursor getReadCursor(Model.Contract contract) {
        return DB.getReadable().query(
            contract.getTableName(), // Table Name
            new String[] { "*" },    // Get all Columns
            null,                    // The columns for the WHERE clause
            null,                    // The values for the WHERE clause
            null,                    // don't group the rows
            null,                    // don't filter by row groups
            null                     // The sort order
        );
    }

}
