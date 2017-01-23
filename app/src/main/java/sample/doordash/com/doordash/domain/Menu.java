package sample.doordash.com.doordash.domain;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Hakeem on 1/21/17.
 */

public class Menu {
    @SerializedName("id")
    public final long mId;

    @SerializedName("menu_categories")
    public List<MenuCategory> mMenuCategories;

    public Menu(long id, List<MenuCategory> menuCategories){
        this.mId = id;
        this.mMenuCategories = menuCategories;
    }
}
