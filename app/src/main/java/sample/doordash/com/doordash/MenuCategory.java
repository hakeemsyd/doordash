package sample.doordash.com.doordash;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Hakeem on 1/21/17.
 */

public class MenuCategory {
    @SerializedName("id")
    public final long mId;

    @SerializedName("items")
    public final List<MenuItem> mItems;

    public MenuCategory(long id, List<MenuItem> items){
        this.mId = id;
        this.mItems = items;
    }
}
