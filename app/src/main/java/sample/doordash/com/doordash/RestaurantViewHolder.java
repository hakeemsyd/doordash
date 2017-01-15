package sample.doordash.com.doordash;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Created by Hakeem on 1/14/17.
 */

public class RestaurantViewHolder {

    private Restaurant mRestaurant;
    private View mView;
    private ImageButton mFav;
    private TextView mName;

    public RestaurantViewHolder(View view, Restaurant restaurant){
        mView = view;
        mRestaurant = restaurant;
        mName = (TextView) mView.findViewById(R.id.name);
        mFav = (ImageButton) mView.findViewById(R.id.favorite);

        mName.setText(restaurant.getName());

        mFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mFav.setImageDrawable(R.drawable.);
            }
        });
    }

    public void update(Restaurant restaurant){
        mRestaurant = restaurant;
        mName.setText(mRestaurant.getName());
    }
}
