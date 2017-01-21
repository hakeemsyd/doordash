package sample.doordash.com.doordash;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Hakeem on 1/21/17.
 */

public class MenuItem {
    @SerializedName("id")
    public final long mId;

    @SerializedName("name")
    public final String mName;

    @SerializedName("description")
    public final String mDesc;

    public MenuItem(long id, String name, String desc){
        mId = id;
        mName = name;
        mDesc = desc;
    }
}
