package sample.doordash.com.doordash.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import sample.doordash.com.doordash.R;

/**
 * Created by Hakeem on 1/21/17.
 */

public class MenuActivity extends AppCompatActivity {

    private static final String KEY_RESTAURANT_ID = "restaurant_id";
    private long mRestaurantId;

    public static Intent start(Context context, long restaurantId){
        Intent i = new Intent();
        i.setClass(context, MenuActivity.class);
        i.putExtra(KEY_RESTAURANT_ID, restaurantId);
        return i;
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menus);
        mRestaurantId = getIntent().getLongExtra(KEY_RESTAURANT_ID, 0);

        if (savedInstanceState == null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, MenuItemListFragment.newInstance(mRestaurantId));
            ft.commit();
        }
    }

    public void showMenuItemDetails(long menuItemId){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, MenuItemFragment.newInstance(mRestaurantId, menuItemId));
        ft.commit();
    }
}
