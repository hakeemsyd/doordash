package sample.doordash.com.doordash.domain;

import android.content.ContentValues;
import android.database.Cursor;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import sample.doordash.com.doordash.Constants;
import sample.doordash.com.doordash.domain.Address;
import sample.doordash.com.doordash.domain.Menu;
import sample.doordash.com.doordash.storage.StorageDefinitions;

/**
 * Created by Hakeem on 1/14/17.
 */

public class Restaurant {
    @SerializedName(Constants.KEY_JSON_RESTAURANT_ID)
    public final long mId;

    @SerializedName(Constants.KEY_JSON_RESTAURANT_NAME)
    public final String mName;

    @SerializedName(Constants.KEY_PHONE)
    public final String mPhone;

    @SerializedName(Constants.KEY_RESTAURANT_DISTANCE)
    public final String mDistance;

    @SerializedName(Constants.KEY_ADDRESS)
    public Address mAddress;

    @SerializedName(Constants.KEY_COVER_IMAGE_URL)
    public final String mCoverImageSrc;

    @SerializedName("menus")
    public final List<Menu> mMenus;

    public static Restaurant fromCursor(Cursor cursor){
        try {
            long id = cursor.getLong(cursor.getColumnIndex(StorageDefinitions.Bookmarks.COLUMN_REMOTE_ID));
            String name = cursor.getString(cursor.getColumnIndex(StorageDefinitions.Bookmarks.COLUMN_NAME));
            String phone = cursor.getString(cursor.getColumnIndex(StorageDefinitions.Bookmarks.COLUMN_PHONE));
            String distance = cursor.getString(cursor.getColumnIndex(StorageDefinitions.Bookmarks.COLUMN_DISTANCE));
            String address = cursor.getString(cursor.getColumnIndex(StorageDefinitions.Bookmarks.COLUMN_ADDRESS));
            String city = cursor.getString(cursor.getColumnIndex(StorageDefinitions.Bookmarks.COLUMN_CITY));
            String coverImage = cursor.getString(cursor.getColumnIndex(StorageDefinitions.Bookmarks.COLUMN_COVER_IMAGE_URL));
            return new Restaurant(id, name, phone, new Address(address,city), distance, coverImage, new ArrayList<Menu>());
        }catch(Exception e){
            return null;
        }
    }

    public Restaurant(long id, String name, String phone, Address address, String distance, String cover_img, List<Menu> menus){
        this.mId = id;
        this.mName = name;
        this.mPhone = phone;
        this.mAddress = address;
        this.mDistance = distance;
        this.mCoverImageSrc = cover_img;
        this.mMenus = menus;
    }

    public ContentValues toContentValues(){
        ContentValues values = new ContentValues();
        values.put(StorageDefinitions.Bookmarks.COLUMN_REMOTE_ID, mId);
        values.put(StorageDefinitions.Bookmarks.COLUMN_NAME, mName);
        values.put(StorageDefinitions.Bookmarks.COLUMN_ADDRESS, mAddress.mPrintableAddress);
        values.put(StorageDefinitions.Bookmarks.COLUMN_DISTANCE, mDistance);
        values.put(StorageDefinitions.Bookmarks.COLUMN_COVER_IMAGE_URL, mCoverImageSrc);
        values.put(StorageDefinitions.Bookmarks.COLUMN_PHONE, mPhone);
        values.put(StorageDefinitions.Bookmarks.COLUMN_CITY, mAddress.mCity);
        return values;
    }
}
