package sample.doordash.com.doordash;

import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

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

    public Restaurant(long id, String name, String phone, Address address, String distance, String cover_img){
        this.mId = id;
        this.mName = name;
        this.mPhone = phone;
        this.mAddress = address;
        this.mDistance = distance;
        this.mCoverImageSrc = cover_img;
    }
}
