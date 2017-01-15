package sample.doordash.com.doordash;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Hakeem on 1/14/17.
 */

public class Restaurant {
    private String mName;

    public Restaurant(String name){
        mName = name;
    }

    public String getName(){
        return mName;
    }
}
