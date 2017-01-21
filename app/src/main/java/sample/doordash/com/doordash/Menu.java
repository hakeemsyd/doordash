package sample.doordash.com.doordash;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Hakeem on 1/21/17.
 */

public class Menu {
    @SerializedName("id")
    public final long mId;

    @SerializedName("name")
    public final String mName;

    @SerializedName("is_catering")
    public final boolean mIsCatering;

    @SerializedName("status_type")
    public final String mStatusType;

    @SerializedName("menu_categories")
    public List<MenuCategory> mMenuCategories;

    public Menu(long id, String name, boolean isCatering, String status, List<MenuCategory> menuCategories){
        this.mId = id;
        this.mName = name;
        this.mIsCatering = isCatering;
        this.mStatusType=status;
        this.mMenuCategories = menuCategories;
    }
}
