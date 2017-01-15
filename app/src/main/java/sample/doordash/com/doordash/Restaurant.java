package sample.doordash.com.doordash;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Hakeem on 1/14/17.
 */

public class Restaurant {
    private String mName;
    private boolean mFav;

    public Restaurant(JSONObject obj, boolean fav){
        try {
            mName = obj.getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
            mName = "";
        }

        mFav = fav;
    }

    public String getName(){
        return mName;
    }

    public boolean isFavourite(){
        return mFav;
    }
}
