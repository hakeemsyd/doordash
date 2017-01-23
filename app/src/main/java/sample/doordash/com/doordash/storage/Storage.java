package sample.doordash.com.doordash.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import rx.Observable;
import rx.Subscriber;
import sample.doordash.com.doordash.domain.CartItem;
import sample.doordash.com.doordash.domain.Restaurant;

/**
 * Created by Hakeem on 1/21/17.
 */

public class Storage {

    private DoorDashDBHelper mHelper;

    public Storage(Context context) {
        mHelper = new DoorDashDBHelper(context);
    }

    public Observable<List<Restaurant>> getBookmarks() {
        return makeObservable(new Callable<List<Restaurant>>() {
            @Override
            public List<Restaurant> call() throws Exception {
                return getBookmarksInt();
            }
        });
    }

    public Observable<Long> addBookmark(final Restaurant restaurant) {
        return makeObservable(new Callable<Long>() {
            @Override
            public Long call() throws Exception {
                return addBookmarkInt(restaurant);
            }
        });
    }

    public Observable<Boolean> removeBookmark(final Restaurant restaurant){
        return makeObservable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return removeBookmarkInt(restaurant);
            }
        });
    }

    public Observable<Boolean> isFavourite(final Restaurant restaurant){
        return makeObservable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return isFavouriteInt(restaurant);
            }
        });
    }

    public Observable<List<CartItem>> getCartItems(){
        return makeObservable(new Callable<List<CartItem>>() {
            @Override
            public List<CartItem> call() throws Exception {
                return getCartItemsInt();
            }
        });
    }

    public Observable<Void> addCartItem(final CartItem item){
        return makeObservable(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                addItemToCartInt(item);
                return null;
            }
        });
    }

    public Observable<Void> removeCartItem(final CartItem item){
        return makeObservable(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                removeItemFromCartInt(item);
                return null;
            }
        });
    }

    public Observable<Void> clearCart(){
        return makeObservable(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                clearCartInt();
                return null;
            }
        });
    }

    private long addBookmarkInt(final Restaurant restaurant) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        long newRowId = db.insert(StorageDefinitions.Bookmarks.TABLE_NAME, null, restaurant.toContentValues());
        return newRowId;
    }

    private boolean removeBookmarkInt(final Restaurant restaurant) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        String selection = StorageDefinitions.Bookmarks.COLUMN_REMOTE_ID + "=" + restaurant.mId;
        return db.delete(StorageDefinitions.Bookmarks.TABLE_NAME, selection, null) > 0;
    }

    private List<Restaurant> getBookmarksInt() {
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from "
                + StorageDefinitions.Bookmarks.TABLE_NAME, null);

        try {
            List<Restaurant> values = new ArrayList<>(cursor.getCount());

            while (cursor.moveToNext()) {
                Restaurant r = Restaurant.fromCursor(cursor);
                if (r != null) {
                    values.add(r);
                }
            }
            return values;
        } finally {
            cursor.close();
        }
    }

    private boolean isFavouriteInt(Restaurant restaurant){
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + StorageDefinitions.Bookmarks.TABLE_NAME
                + " WHERE "
                + StorageDefinitions.Bookmarks.COLUMN_REMOTE_ID + "=" + restaurant.mId, null);
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    private static <T> Observable<T> makeObservable(final Callable<T> func) {
        return Observable.create(
                new Observable.OnSubscribe<T>() {
                    @Override
                    public void call(Subscriber<? super T> subscriber) {
                        try {
                            subscriber.onNext(func.call());
                        } catch (Exception ex) {
                            subscriber.onError(ex);
                            Log.e("", "Error reading from the database", ex);
                        }
                    }
                });
    }

    private void addItemToCartInt(CartItem item){
        SQLiteDatabase db = mHelper.getWritableDatabase();
        CartItem prev = findCartItem(item.mRestaurantId, item.mItemId);
        if(prev == null){
            addNewItemToCartInt(item);
        }else{
            CartItem newItem = new CartItem(prev.mId, item.mRestaurantId, item.mItemId, prev.mQuantity + 1, null);
            db.update(StorageDefinitions.Cart.TABLE_NAME, newItem.toContentValues(),
                    StorageDefinitions.Cart._ID + "=" + prev.mId, null);
        }
    }

    private long addNewItemToCartInt(CartItem item){
        SQLiteDatabase db = mHelper.getWritableDatabase();
        ContentValues contentVal = item.toContentValues();
        long rowId = db.insert(StorageDefinitions.Cart.TABLE_NAME, null, contentVal);
        return rowId;
    }

    private CartItem findCartItem(long restaurantId, long itemId){
        SQLiteDatabase db = mHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + StorageDefinitions.Cart.TABLE_NAME
                + " WHERE "
                + StorageDefinitions.Cart.COLUMN_RESTAURANT_ID + "=" + restaurantId + " AND " +
                StorageDefinitions.Cart.COLUMN_ITEM_ID + "=" + itemId, null);
         if(cursor.moveToFirst()){
             return CartItem.fromCursor(cursor);
         }

        return null;
    }

    private void removeItemFromCartInt(CartItem item){
        SQLiteDatabase db = mHelper.getWritableDatabase();
        if(item.mId != -1){
            db.delete(StorageDefinitions.Cart.TABLE_NAME, StorageDefinitions.Cart._ID +"="+ item.mId, null);
        }else{
            db.delete(StorageDefinitions.Cart.TABLE_NAME, StorageDefinitions.Cart.COLUMN_ITEM_ID +"="+ item.mItemId, null);
        }
    }

    private void clearCartInt(){
        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.delete(StorageDefinitions.Cart.TABLE_NAME,"1",null);
    }

    private List<CartItem> getCartItemsInt(){
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from "
                + StorageDefinitions.Cart.TABLE_NAME, null);

        try {
            List<CartItem> values = new ArrayList<>(cursor.getCount());

            while (cursor.moveToNext()) {
                CartItem i = CartItem.fromCursor(cursor);
                if (i != null) {
                    values.add(i);
                }
            }
            return values;
        } finally {
            cursor.close();
        }
    }
}
