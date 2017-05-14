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
        deleteWhere(contract, null, null);
    }


    /**
     * Delete by given column
     */
    public static void deleteBy(Model.Contract contract, String column, String value) {
        deleteWhere(contract, column + " = ?", new String[] { value });
    }


    /**
     * Delete with custom query
     */
    private static void deleteWhere(Model.Contract contract, String whereSelector, String[] whereValues) {
        DB.getWritable().delete(contract.getTableName(), whereSelector, whereValues);
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
        return first(contract, getCursor(contract));
    }

    public static Model first(Model.Contract contract, Cursor cursor) {
        Model model = null;

        if (cursor != null && cursor.moveToFirst()) {
            model = contract.toModel(cursor);
            cursor.close();
        }

        return model;
    }



    /**
     * Find by ID
     */
    public static Model findBy(Model.Contract contract, String column, String value) {
        Cursor cursor = getCursor(contract, column + " = ?", new String[] { value });
        return first(contract, cursor);
    }




    /**
     * Get All rows for a contract and cursor
     */
    public static ArrayList<? extends Model> getAll(Model.Contract contract) {
        return getAll(contract, getCursor(contract));
    }

    public static ArrayList<? extends Model> getAll(Model.Contract contract, Cursor cursor) {
        ArrayList<Model> modelList = new ArrayList<>();

        while(cursor.moveToNext())
            modelList.add(contract.toModel(cursor));

        cursor.close();
        return modelList;
    }



    /**
     * Get a cursor with all columns for a DB and contract
     */
    public static Cursor getCursor(Model.Contract contract) {
        return getCursor(contract, null, null);
    }

    public static Cursor getCursor(Model.Contract contract, String whereSelector, String[] whereValues) {
        return getCursor(contract, whereSelector, whereValues, null);
    }

    public static Cursor getCursor(Model.Contract contract, String whereSelector, String[] whereValues, String orderBy) {
        return DB.getReadable().query(
                contract.getTableName(), // Table Name
                new String[] { "*" },    // Get all Columns
                whereSelector,           // The columns for the WHERE clause
                whereValues,             // The values for the WHERE clause
                null,                    // don't group the rows
                null,                    // don't filter by row groups
                orderBy                  // The sort order
        );
    }

}
