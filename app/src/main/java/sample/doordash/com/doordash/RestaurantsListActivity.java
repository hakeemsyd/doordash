package sample.doordash.com.doordash;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resturants_list);
        mReqQueue = Volley.newRequestQueue(this);

        mListView = (ListView) findViewById(R.id.list);
        mListView.setEmptyView(findViewById(R.id.empty));

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
                                restaurants.add(new Restaurant(response.getJSONObject(i), false));
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
                mAdapter.update(restaurants);
            }
        });
    }
}
