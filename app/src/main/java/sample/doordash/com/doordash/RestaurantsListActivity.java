package sample.doordash.com.doordash;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class RestaurantsListActivity extends AppCompatActivity {

    private static final int PLACE_PICKER_REQUEST = 100;
    private RestaurantsAdapter mAdapter;
    private ListView mListView;
    private Preferences mPrefs;
    private ProgressBar mProgress;
    private TextView mEmpty;
    private boolean mFavouritesMode;
    private LatLng mLoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resturants_list);
        mFavouritesMode = false;
        mPrefs = new Preferences(this);
        mLoc = new LatLng(Constants.DEFAULT_LAT, Constants.DEFAULT_LNG);

        mListView = (ListView) findViewById(R.id.list);
        mProgress = (ProgressBar) findViewById(R.id.progress);
        mEmpty = (TextView) findViewById(R.id.empty_text);
        mListView.setEmptyView(mProgress);

        mAdapter = new RestaurantsAdapter(this, new ArrayList<Restaurant>());
        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showDialog(mAdapter.getItem(position).getId());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.view_favourites) {
            if (mFavouritesMode) {
                mFavouritesMode = false;
                item.setIcon(R.drawable.ic_favorite_border_white_36dp);
            } else {
                mFavouritesMode = true;
                item.setIcon(R.drawable.ic_favorite_white_36dp);
            }
            refreshList();
        } else if (item.getItemId() == R.id.pick_location) {
            launchPlacePicker();
        } else {
            return super.onOptionsItemSelected(item);
        }

        return true;
    }

    private void updateListView(final List<Restaurant> restaurants) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (restaurants.size() == 0) {
                    mProgress.setVisibility(View.GONE);
                    mEmpty.setVisibility(View.VISIBLE);
                } else {
                    mEmpty.setVisibility(View.GONE);
                    mAdapter.update(restaurants);
                }
            }
        });
    }

    private void showDialog(long id) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();

        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        DialogFragment newFragment = RestaurantDialogFragment.newInstance(id);
        newFragment.show(ft, "dialog");
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
                refreshList();
            } else {
                mLoc = new LatLng(Constants.DEFAULT_LAT, Constants.DEFAULT_LNG);
            }
        }
    }

    synchronized void refreshList() {
        RequestQueue reqQueue = Volley.newRequestQueue(this);
        mAdapter.clear();
        String q = "?lat=" + mLoc.latitude + "&lng=" + mLoc.longitude;
        JsonArrayRequest request = new JsonArrayRequest(Constants.API_RESTURANT + q,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i("", "Response: " + response.length());

                        List<Restaurant> restaurants = new ArrayList<>();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                Restaurant r = Restaurant.CreateFromJSONObject(response.getJSONObject(i));
                                if (!mFavouritesMode) {
                                    restaurants.add(r);
                                } else if (mFavouritesMode && mPrefs.isFavourite(String.valueOf(r.getId()))) {
                                    restaurants.add(r);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        updateListView(restaurants);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("", "Error: " + error.getMessage());
            }
        });
        reqQueue.add(request);
    }
}
