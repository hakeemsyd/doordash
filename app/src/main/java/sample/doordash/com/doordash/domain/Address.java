package sample.doordash.com.doordash.domain;

import com.google.gson.annotations.SerializedName;

import sample.doordash.com.doordash.Constants;

/**
 * Created by Hakeem on 1/21/17.
 */

public class Address {
    @SerializedName(Constants.KEY_RESTAURANT_PRINTABLE_ADDRESS)
    public final String mPrintableAddress;
    @SerializedName(Constants.KEY_CITY)
    public final String mCity;

    public Address(String addr, String city){
        mPrintableAddress = addr;
        mCity = city;
    }
}
