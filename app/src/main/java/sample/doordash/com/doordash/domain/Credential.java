package sample.doordash.com.doordash.domain;

import com.google.gson.annotations.SerializedName;

import sample.doordash.com.doordash.Constants;

/**
 * Created by Hakeem on 1/21/17.
 */

public class Credential {
    @SerializedName(Constants.KEY_EMAIL)
    public final String mUsername;
    @SerializedName(Constants.KEY_PASSWORD)
    public final String mPass;

    public Credential(String user, String pass){
        mUsername = user;
        mPass = pass;
    }
}
