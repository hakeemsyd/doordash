package sample.doordash.com.doordash.ui;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import android.widget.ToggleButton;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import sample.doordash.com.doordash.storage.Preferences;
import sample.doordash.com.doordash.R;
import sample.doordash.com.doordash.domain.Restaurant;
import sample.doordash.com.doordash.storage.Storage;

/**
 * Created by Hakeem on 1/14/17.
 */

public class RestaurantViewHolder {

    private Restaurant mRestaurant;
    private View mView;
    private ToggleButton mFav;
    private TextView mName;
    private Storage mStorage;

    public RestaurantViewHolder(Context context, View view, Restaurant restaurant){
        mStorage = new Storage(context);
        mView = view;
        mRestaurant = restaurant;
        mName = (TextView) mView.findViewById(R.id.name);
        mFav = (ToggleButton) mView.findViewById(R.id.favorite);
        update();
        mFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mFav.isChecked()){
                    mStorage.addBookmark(mRestaurant)
                    .subscribeOn(Schedulers.computation())
                    .subscribe();
                }else{
                    mStorage.removeBookmark(mRestaurant)
                            .subscribeOn(Schedulers.computation())
                            .subscribe();
                }
            }
        });
    }

    public void update(Restaurant restaurant){
        mRestaurant = restaurant;
    }

    private void update(){
        mName.setText(mRestaurant.mName);
        //mFav.setChecked(mPrefs.isFavourite(String.valueOf(mRestaurant.mId)));
    }
}
