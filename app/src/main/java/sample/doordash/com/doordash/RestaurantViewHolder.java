package sample.doordash.com.doordash;

import android.content.Context;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ToggleButton;

/**
 * Created by Hakeem on 1/14/17.
 */

public class RestaurantViewHolder {

    private Restaurant mRestaurant;
    private View mView;
    private ToggleButton mFav;
    private TextView mName;
    private Preferences mPrefs;

    public RestaurantViewHolder(Context context, View view, Restaurant restaurant){
        mPrefs = new Preferences(context);
        mView = view;
        mRestaurant = restaurant;
        mName = (TextView) mView.findViewById(R.id.name);
        mFav = (ToggleButton) mView.findViewById(R.id.favorite);
        update();
        mFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mFav.isChecked()){
                    mPrefs.addFavourite(mRestaurant.getName());
                }else{
                    mPrefs.removeFavourite(mRestaurant.getName());
                }
            }
        });
    }

    public void update(Restaurant restaurant){
        mRestaurant = restaurant;
    }

    private void update(){
        mName.setText(mRestaurant.getName());
        mFav.setChecked(mPrefs.isFavourite(mRestaurant.getName()));
    }
}
