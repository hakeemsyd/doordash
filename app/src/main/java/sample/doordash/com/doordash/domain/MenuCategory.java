package sample.doordash.com.doordash.domain;

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

    @SerializedName("title")
    public final String mTitle;

    @SerializedName("subtitle")
    public final String mSubtitle;

    public MenuCategory(long id, List<MenuItem> items, String title, String subtitle){
        this.mId = id;
        this.mItems = items;
        this.mTitle = title;
        this.mSubtitle = subtitle;
    }
}
