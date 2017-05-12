package co.firetools.copperink.behaviors;

import android.content.ContentValues;
import android.database.Cursor;

public interface Model {
    Contract getContract();

    interface Contract<T extends Model> {
        String getTableName();
        ContentValues contentValues(T model);
        T toModel(Cursor cursor);
    }
}
