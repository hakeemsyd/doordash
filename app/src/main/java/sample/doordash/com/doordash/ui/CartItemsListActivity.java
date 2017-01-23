package sample.doordash.com.doordash.ui;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import sample.doordash.com.doordash.R;
import sample.doordash.com.doordash.domain.CartItem;
import sample.doordash.com.doordash.domain.CartItemDeletedEvent;
import sample.doordash.com.doordash.storage.Storage;
import sample.doordash.com.doordash.util.BusProvider;

/**
 * Created by Hakeem on 1/23/17.
 */

public class CartItemsListActivity extends AppCompatActivity {
    private CartItemsAdapter mAdapter;
    private ListView mListView;
    private List<Subscription> mSubscription;
    private Storage mStorage;

    public static Intent start(Context context) {
        Intent i = new Intent();
        i.setClass(context, CartItemsListActivity.class);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_items);
        mListView = (ListView) findViewById(R.id.cart_item_list);
        mStorage = new Storage(this);
        mSubscription = new ArrayList<>();
        mAdapter = new CartItemsAdapter(this, new ArrayList<CartItem>());
        mListView.setAdapter(mAdapter);
        mListView.setEmptyView(findViewById(R.id.empty));
    }

    @Override
    protected void onResume() {
        super.onResume();
        BusProvider.getBus().register(this);
        updateList();
    }

    @Override
    protected void onPause() {
        BusProvider.getBus().unregister(this);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        for(Subscription s : mSubscription){
            if(!s.isUnsubscribed()){
                s.unsubscribe();
            }
        }
        super.onDestroy();
    }

    private void updateList() {
        Subscription sub = mStorage.getCartItems()
                .subscribeOn(Schedulers.computation())
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
                        mAdapter.update(cartItems);
                    }
                });
        mSubscription.add(sub);
    }

    @Subscribe
    public void onEvent(CartItemDeletedEvent event){
        updateList();
    }
}