package sample.doordash.com.doordash;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Hakeem on 1/14/17.
 */

public class Restaurant {
    private String mName;

    public Restaurant(JSONObject obj){
        try {
            mName = obj.getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
            mName = "";
        }
    }

    public String getName(){
        return mName;
    }
}
