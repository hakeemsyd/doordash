package sample.doordash.com.doordash;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class RestaurantsListActivity extends AppCompatActivity {

    private RestaurantsAdapter mAdapter;
    private RequestQueue mReqQueue;
    private ListView mListView;
    private Preferences mPrefs;
    private ProgressBar mProgress;
    private TextView mEmpty;
    private boolean mFavouritesMode;
    private MenuItem mMenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resturants_list);
        mFavouritesMode = false;
        mReqQueue = Volley.newRequestQueue(this);
        mPrefs = new Preferences(this);

        mListView = (ListView) findViewById(R.id.list);
        mProgress = (ProgressBar) findViewById(R.id.progress);
        mEmpty = (TextView) findViewById(R.id.empty_text);
        mListView.setEmptyView(mProgress);

        mAdapter = new RestaurantsAdapter(this, new ArrayList<Restaurant>());
        mListView.setAdapter(mAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refrestList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        mMenu = menu.getItem(0);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.view_favourites){
            if(mFavouritesMode){
                mFavouritesMode = false;
                item.setIcon(R.drawable.ic_favorite_border_white_36dp);
            }else{
                mFavouritesMode = true;
                item.setIcon(R.drawable.ic_favorite_white_36dp);
            }

            refrestList();
            return true;
        }else {
            return super.onOptionsItemSelected(item);
        }
    }

    synchronized void refrestList() {
        mAdapter.clear();
        String q = "?lat=" + 37.422740 + "&lng=" + -122.139956;
        JsonArrayRequest request = new JsonArrayRequest(Constants.API_RESTURANT + q,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i("", "Response: " + response.length());

                        List<Restaurant> restaurants = new ArrayList<>();
                        for(int i = 0; i < response.length(); i++){
                            try {
                                Restaurant r = Restaurant.CreateFromJSONObject(response.getJSONObject(i));
                                if(!mFavouritesMode) {
                                    restaurants.add(r);
                                }else if(mFavouritesMode && mPrefs.isFavourite(String.valueOf(r.getId()))){
                                    restaurants.add(r);
                                }
                            } catch (JSONException e) {
                                continue;
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
        mReqQueue.add(request);
    }

    private void updateListView(final List<Restaurant> restaurants){
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(restaurants.size() == 0){
                    mProgress.setVisibility(View.GONE);
                    mEmpty.setVisibility(View.VISIBLE);
                }else {
                    mEmpty.setVisibility(View.GONE);
                    mAdapter.update(restaurants);
                }
            }
        });
    }
}
