package sample.doordash.com.doordash.ui;

import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import sample.doordash.com.doordash.R;
import sample.doordash.com.doordash.domain.CartItem;
import sample.doordash.com.doordash.storage.Storage;
import sample.doordash.com.doordash.util.BusProvider;

/**
 * Created by Hakeem on 1/23/17.
 */

public abstract class CartActivity extends AppCompatActivity {
    protected MenuItem mCartMenuItem;
    protected List<Subscription> mSubscriptions = new ArrayList<>();
    protected Storage mStorage;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mCartMenuItem = menu.findItem(R.id.shopping_cart);
        mCartMenuItem.setVisible(false);
        updateCartOption();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.shopping_cart) {
            startActivity(CartItemsListActivity.start(this));
        } else {
            return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    protected void onResume() {
        BusProvider.getBus().register(this);
        mStorage = new Storage(this);
        super.onResume();
        if (mCartMenuItem != null) {
            updateCartOption();
        }
    }

    @Override
    protected void onPause() {
        BusProvider.getBus().unregister(this);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        for (Subscription sub : mSubscriptions) {
            if (!sub.isUnsubscribed()) {
                sub.unsubscribe();
            }
        }
        super.onDestroy();
    }

    protected void updateCartOption() {
        Subscription sub = mStorage.getCartItems()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<CartItem>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<CartItem> cartItems) {
                        mCartMenuItem.setVisible(cartItems != null && cartItems.size() > 0);
                    }
                });

        mSubscriptions.add(sub);
    }
}
