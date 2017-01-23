package sample.doordash.com.doordash.storage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Hakeem on 1/21/17.
 */

public class DoorDashDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "doordash.db";

    public DoorDashDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(StorageDefinitions.Bookmarks.SQL_CREATE_BOOKMARKS_TABLE);
        db.execSQL(StorageDefinitions.Cart.SQL_CREATE_CART_TABLE);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(StorageDefinitions.Bookmarks.SQL_DELETE_BOOKMARKS_TABLE);
        db.execSQL(StorageDefinitions.Cart.SQL_DELETE_CART_TABLE);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
