package sample.doordash.com.doordash;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Hakeem on 1/21/17.
 */

public class AuthToken {
    @SerializedName(Constants.KEY_TOKEN)
    public final String mToken;

    public AuthToken(String token){
        mToken = token;
    }
}
