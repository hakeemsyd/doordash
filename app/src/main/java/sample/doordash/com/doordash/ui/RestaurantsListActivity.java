package sample.doordash.com.doordash.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import sample.doordash.com.doordash.Constants;
import sample.doordash.com.doordash.R;
import sample.doordash.com.doordash.domain.CartItem;
import sample.doordash.com.doordash.domain.Restaurant;
import sample.doordash.com.doordash.domain.User;
import sample.doordash.com.doordash.service.DoorDashClient;
import sample.doordash.com.doordash.storage.Preferences;
import sample.doordash.com.doordash.storage.Storage;

public class RestaurantsListActivity extends AppCompatActivity {

    private static final String KEY_LOC_LATITUDE = "loc_lat";
    private static final String KEY_LOC_LONGITUDE = "loc_long";
    private static final String KEY_MODE = "mode";

    private static final int PLACE_PICKER_REQUEST = 100;
    private RestaurantsAdapter mAdapter;
    private ListView mListView;
    private ProgressBar mProgress;
    private TextView mEmpty;
    private boolean mFavouritesMode = false;
    private LatLng mLoc = new LatLng(Constants.DEFAULT_LAT, Constants.DEFAULT_LNG);
    ;
    private List<Subscription> mSubscriptions;
    private Storage mStorage;
    private Preferences mPrefs;
    private MenuItem mCartMenuItem;

    public static Intent start(Context context, long lng, long lat) {
        Intent i = new Intent();
        i.setClass(context, RestaurantsListActivity.class);
        Bundle b = new Bundle();

        b.putDouble(RestaurantsListActivity.KEY_LOC_LONGITUDE, lng);
        b.putDouble(RestaurantsListActivity.KEY_LOC_LATITUDE, lat);

        i.putExtras(b);
        return i;
    }

    public static Intent start(Context context) {
        Intent i = new Intent();
        i.setClass(context, RestaurantsListActivity.class);
        Bundle b = new Bundle();

        b.putDouble(RestaurantsListActivity.KEY_LOC_LONGITUDE, Constants.DEFAULT_LNG);
        b.putDouble(RestaurantsListActivity.KEY_LOC_LATITUDE, Constants.DEFAULT_LAT);

        i.putExtras(b);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resturants_list);
        mStorage = new Storage(this);
        mPrefs = new Preferences(this);
        mSubscriptions = new ArrayList<>();

        if (getIntent().getExtras() != null) {
            Bundle b = getIntent().getExtras();
            mLoc = new LatLng(b.getDouble(KEY_LOC_LATITUDE), b.getDouble(KEY_LOC_LONGITUDE));
        }

        if (savedInstanceState != null) {
            mLoc = new LatLng(savedInstanceState.getDouble(KEY_LOC_LATITUDE), savedInstanceState.getDouble(KEY_LOC_LONGITUDE));
            mFavouritesMode = savedInstanceState.getBoolean(KEY_MODE);
        }

        mListView = (ListView) findViewById(R.id.list);
        mProgress = (ProgressBar) findViewById(R.id.progress);
        mEmpty = (TextView) findViewById(R.id.empty_text);
        mListView.setEmptyView(mProgress);

        mAdapter = new RestaurantsAdapter(this, new ArrayList<Restaurant>());
        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showDialog(mAdapter.getItem(position).mId);
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putDouble(KEY_LOC_LATITUDE, mLoc.latitude);
        outState.putDouble(KEY_LOC_LONGITUDE, mLoc.longitude);
        outState.putBoolean(KEY_MODE, mFavouritesMode);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUserInfo();
        if (mFavouritesMode) {
            showBookmarks();
        } else {
            showNearbyRestaurants();
        }

        if (mCartMenuItem != null) {
            updateCartOption();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        mCartMenuItem = menu.findItem(R.id.shopping_cart);
        mCartMenuItem.setVisible(false);
        updateCartOption();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.view_favourites) {
            if (mFavouritesMode) {
                mFavouritesMode = false;
                showNearbyRestaurants();
            } else {
                mFavouritesMode = true;
                showBookmarks();
            }
        } else if (item.getItemId() == R.id.pick_location) {
            launchPlacePicker();
        } else if (item.getItemId() == R.id.shopping_cart) {

        } else {
            return super.onOptionsItemSelected(item);
        }

        return true;
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

    private void updateListView(final List<Restaurant> restaurants) {
        mAdapter.update(restaurants);
        if (restaurants.size() == 0) {
            mProgress.setVisibility(View.GONE);
            mListView.setVisibility(View.GONE);
            mEmpty.setVisibility(View.VISIBLE);
        } else {
            mEmpty.setVisibility(View.GONE);
            mProgress.setVisibility(View.GONE);
            mListView.setVisibility(View.VISIBLE);
        }

    }

    private void setInProgress() {
        mListView.setVisibility(View.GONE);
        mEmpty.setVisibility(View.GONE);
        mProgress.setVisibility(View.VISIBLE);
    }

    private void showDialog(long id) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        Fragment prev = getSupportFragmentManager().findFragmentByTag("restaurant_detail_dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        DialogFragment newFragment = RestaurantDialogFragment.newInstance(id);
        newFragment.show(ft, "restaurant_detail_dialog");
    }

    private void launchPlacePicker() {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
            Toast.makeText(this, "Could not launch maps", Toast.LENGTH_SHORT).show();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
            Toast.makeText(this, "Could not launch maps", Toast.LENGTH_SHORT).show();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                String toastMsg = String.format("Place: %s", place.getName());
                Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
                mLoc = place.getLatLng();
                showNearbyRestaurants();
            } else {
                mLoc = new LatLng(Constants.DEFAULT_LAT, Constants.DEFAULT_LNG);
            }
        }
    }

    private void updateUserInfo() {
        String token = mPrefs.getToken();
        if (token != null && !token.isEmpty()) {
            Subscription sub = DoorDashClient.getInstance()
                    .getUserInfo(token)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<User>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            setTitle(R.string.guest_user_name);
                        }

                        @Override
                        public void onNext(User user) {
                            setTitle(user.mFirstName + " " + user.mLastName);
                        }
                    });
            mSubscriptions.add(sub);
        } else {
            setTitle(R.string.guest_user_name);
        }
    }

    void showBookmarks() {
        setInProgress();
        Subscription sub = mStorage.getBookmarks()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Restaurant>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("", "In onError()");
                    }

                    @Override
                    public void onNext(List<Restaurant> restaurants) {
                        updateListView(restaurants);
                    }
                });
        mSubscriptions.add(sub);
    }

    void showNearbyRestaurants() {
        setInProgress();
        Subscription sub = DoorDashClient.getInstance()
                .getRestaurants(mLoc.latitude, mLoc.longitude)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Restaurant>>() {
                    @Override
                    public void onCompleted() {
                        Log.d("", "In onCompleted()");
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Log.d("", "In onError()");
                    }

                    @Override
                    public void onNext(List<Restaurant> items) {
                        Log.d("", "In onNext()");
                        updateListView(items);
                    }
                });
        mSubscriptions.add(sub);
    }

    private void updateCartOption() {
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
