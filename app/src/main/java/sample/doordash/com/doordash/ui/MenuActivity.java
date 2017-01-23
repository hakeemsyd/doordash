package sample.doordash.com.doordash.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;

import com.squareup.otto.Subscribe;

import sample.doordash.com.doordash.R;
import sample.doordash.com.doordash.domain.CartItemAddedEvent;

/**
 * Created by Hakeem on 1/21/17.
 */

public class MenuActivity extends CartActivity {

    private static final String KEY_RESTAURANT_ID = "restaurant_id";
    private long mRestaurantId;

    public static Intent start(Context context, long restaurantId) {
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.cart, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void showMenuItemDetails(long menuItemId) {

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        Fragment prev = getSupportFragmentManager().findFragmentByTag("menu_item_dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        DialogFragment newFragment = MenuItemDialogFragment.newInstance(mRestaurantId, menuItemId);
        newFragment.show(ft, "menu_item_dialog");

    }

    @Subscribe
    public void onEvent(CartItemAddedEvent event){
        updateCartOption();
    }
}
