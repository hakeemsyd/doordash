package sample.doordash.com.doordash;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Hakeem on 1/15/17.
 */

public class User {
    private String mName;
    private String mPhone;
    private String mCity;
    private String mAddress;

    public static User CreateFromJSONObject(JSONObject obj) throws JSONException {
        String name = obj.getString(Constants.KEY_FIRST_NAME) + " " + obj.getString(Constants.KEY_LAST_NAME);
        String phone = obj.getString(Constants.KEY_PHONE);
        String city = obj.getJSONObject(Constants.KEY_ADDRESS).getString(Constants.KEY_CITY);
        String address = obj.getJSONObject(Constants.KEY_ADDRESS).getString(Constants.KEY_STREET_ADDRESS);
        return new User(name, phone, city, address);
    }

    public User() {
        mName = "";
        mPhone = "";
        mCity = "";
        mAddress = "";
    }

    public User(String name, String phone, String city, String address) {
        this.mName = name;
        this.mPhone = phone;
        this.mCity = city;
        this.mAddress = address;
    }

    public String getmName() {
        return mName;
    }

    public String getmPhone() {
        return mPhone;
    }

    public String getmCity() {
        return mCity;
    }

    public String getmAddress() {
        return mAddress;
    }
}
