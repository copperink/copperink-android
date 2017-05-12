package co.firetools.copperink.behaviors;

import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;

public interface Model {
    Contract getContract();

    interface Contract<T extends Model> extends BaseColumns {
        T toModel(Cursor cursor);
        String getTableName();
        ContentValues contentValues(T model);
    }
}
