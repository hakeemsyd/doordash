package sample.doordash.com.doordash.storage;

import android.provider.BaseColumns;

/**
 * Created by Hakeem on 1/21/17.
 */

public class StorageDefinitions {

    private StorageDefinitions() {}

    /* Inner class that defines the table contents */
    public static class Bookmarks implements BaseColumns {
        public static final String TABLE_NAME = "bookmarks";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_PHONE = "phone";
        public static final String COLUMN_DISTANCE = "distance";
        public static final String COLUMN_ADDRESS = "address";
        public static final String COLUMN_CITY = "city";
        public static final String COLUMN_COVER_IMAGE_URL = "cover_image_url";
        public static final String COLUMN_REMOTE_ID = "remote_id";

        public static final String SQL_CREATE_BOOKMARKS_TABLE =
                "CREATE TABLE " + Bookmarks.TABLE_NAME + " (" +
                        Bookmarks._ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                        Bookmarks.COLUMN_REMOTE_ID + " TEXT NOT NULL," +
                        Bookmarks.COLUMN_PHONE+ " TEXT," +
                        Bookmarks.COLUMN_DISTANCE + " TEXT," +
                        Bookmarks.COLUMN_ADDRESS+ " TEXT," +
                        Bookmarks.COLUMN_CITY+ " TEXT," +
                        Bookmarks.COLUMN_COVER_IMAGE_URL + " TEXT," +
                        Bookmarks.COLUMN_NAME + " TEXT)";



        public static final String SQL_DELETE_BOOKMARKS_TABLE =
                "DROP TABLE IF EXISTS " + Bookmarks.TABLE_NAME;
    }

    public static class Cart implements  BaseColumns{
        public static final String TABLE_NAME = "Cart";
        public static final String COLUMN_RESTAURANT_ID = "restaurant_id";
        public static final String COLUMN_ITEM_ID = "item_id";
        /*public static final String COLUMN_PRICE = "price";
        public static final String COLUMN_NAME = "name";*/
        public static final String COLUMN_QUANTITY = "quantity";

        public static final String SQL_DELETE_CART_TABLE =
                "DROP TABLE IF EXISTS " + Cart.TABLE_NAME;

        public static final String SQL_CREATE_CART_TABLE =
                "CREATE TABLE " + Cart.TABLE_NAME + " (" +
                        Cart._ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                        Cart.COLUMN_RESTAURANT_ID + " TEXT NOT NULL," +
                        Cart.COLUMN_ITEM_ID+ " TEXT NOT NULL," +
                        Cart.COLUMN_QUANTITY + " INTEGER)";
                        /*Cart.COLUMN_NAME + " TEXT," +
                        Cart.COLUMN_PRICE + " TEXT," +
                        Cart.COLUMN_QUANTITY + " TEXT)";*/
    }

}
