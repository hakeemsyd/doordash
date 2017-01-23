package sample.doordash.com.doordash.domain;

import android.content.ContentValues;
import android.database.Cursor;

import sample.doordash.com.doordash.storage.StorageDefinitions;

/**
 * Created by Hakeem on 1/22/17.
 */

public class CartItem {
    public int mId;
    public final MenuItem mMenuItem;
    public final long mRestaurantId;
    public final long mItemId;
    public final int mQuantity;

    public static CartItem fromCursor(Cursor cursor){
        try {
            int id = cursor.getInt(cursor.getColumnIndex(StorageDefinitions.Cart._ID));
            long rId = cursor.getLong(cursor.getColumnIndex(StorageDefinitions.Cart.COLUMN_RESTAURANT_ID));
            long iId = cursor.getLong(cursor.getColumnIndex(StorageDefinitions.Cart.COLUMN_ITEM_ID));
            int quantity = cursor.getInt(cursor.getColumnIndex(StorageDefinitions.Cart.COLUMN_QUANTITY));
            return new CartItem(id, rId, iId, quantity, null);
        }catch(Exception e){
            return null;
        }
    }

    public ContentValues toContentValues(){
        ContentValues values = new ContentValues();
        values.put(StorageDefinitions.Cart.COLUMN_RESTAURANT_ID, mRestaurantId);
        values.put(StorageDefinitions.Cart.COLUMN_ITEM_ID, mItemId);
        values.put(StorageDefinitions.Cart.COLUMN_QUANTITY, mQuantity);
        return values;
    }

    public CartItem(int id, long restaurantId, long itemId, int quantity, MenuItem menuItem){
        mId = id;
        mMenuItem = menuItem;
        mItemId = itemId;
        mRestaurantId = restaurantId;
        mQuantity = quantity;
    }
}
