package sample.doordash.com.doordash.domain;

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

    @SerializedName("price")
    public final long mPrice;

    @SerializedName("image_url")
    public final String mImageSrc;

    public MenuItem(long id, String name, String desc, long price, String imageSrc){
        mId = id;
        mName = name;
        mDesc = desc;
        mPrice = price;
        mImageSrc = imageSrc;
    }
}
