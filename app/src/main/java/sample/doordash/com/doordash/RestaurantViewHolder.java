package sample.doordash.com.doordash;

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

    public RestaurantViewHolder(View view, Restaurant restaurant){
        mView = view;
        mRestaurant = restaurant;
        mName = (TextView) mView.findViewById(R.id.name);
        mFav = (ToggleButton) mView.findViewById(R.id.favorite);

        mName.setText(restaurant.getName());

        mFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //add to favorites
            }
        });
    }

    public void update(Restaurant restaurant){
        mRestaurant = restaurant;
        mName.setText(mRestaurant.getName());
    }
}
