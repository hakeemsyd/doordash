package sample.doordash.com.doordash;

import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Hakeem on 1/15/17.
 */

public class User {
    @SerializedName(Constants.KEY_FIRST_NAME)
    public final String mFirstName;
    @SerializedName(Constants.KEY_LAST_NAME)
    public final String mLastName;
    @SerializedName(Constants.KEY_PHONE)
    public final String mPhone;
    @SerializedName(Constants.KEY_CITY)
    public final String mCity;
    @SerializedName(Constants.KEY_DEFAULT_ADDRESS)
    public final Address mAddress;


    public User(String fName, String lName, String phone, String city, Address address) {
        this.mFirstName = fName;
        this.mLastName = lName;
        this.mPhone = phone;
        this.mCity = city;
        this.mAddress = address;
    }
}
