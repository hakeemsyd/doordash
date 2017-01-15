package sample.doordash.com.doordash;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Hakeem on 1/14/17.
 */

public class Restaurant {
    private long mId;
    private String mName;

    public static Restaurant CreateFromJSONObject(JSONObject obj) throws JSONException{
        long id = obj.getLong(Constants.KEY_JSON_RESTAURANT_ID);
        String name = obj.getString(Constants.KEY_JSON_RESTAURANT_NAME);
        return new Restaurant(id, name);
    }

    public Restaurant(long id, String name){
        this.mId = id;
        this.mName = name;
    }

    public String getName(){
        return mName;
    }

    public long getId(){
        return mId;
    }
}
