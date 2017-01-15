package sample.doordash.com.doordash;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
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

public class RestaurantsListActivity extends Activity {

    private RestaurantsAdapter mAdapter;
    private RequestQueue mReqQueue;
    private ListView mListView;
    private Preferences mPrefs;
    private ProgressBar mProgress;
    private TextView mEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resturants_list);
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

    void refrestList() {
        String q = "?lat=" + 37.422740 + "&lng=" + -122.139956;
        JsonArrayRequest request = new JsonArrayRequest(Constants.API_RESTURANT + q,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i("", "Response: " + response.length());

                        List<Restaurant> restaurants = new ArrayList<>();
                        for(int i = 0; i < response.length(); i++){
                            try {
                                String name = response.getJSONObject(i).getString(Constants.KEY_JSON_RESTAURANT_NAME);
                                restaurants.add(new Restaurant(name));
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
