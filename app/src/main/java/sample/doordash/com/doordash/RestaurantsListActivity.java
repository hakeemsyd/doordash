package sample.doordash.com.doordash;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import java.util.ArrayList;
import java.util.List;

public class RestaurantsListActivity extends ListActivity {

    private ArrayAdapter<String> mAdapter;
    private RequestQueue mReqQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mReqQueue = Volley.newRequestQueue(this);
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        getListView().setAdapter(mAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAdapter.clear();
        refrestList();
    }

    void refrestList() {
        String q = "?lat=" + 37.422740 + "&lng=" + -122.139956;
        JsonArrayRequest request = new JsonArrayRequest(Constants.API_RESTURANT + q,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i("", "Response: " + response.length());

                        List<String> restaurants = new ArrayList<String>();
                        for(int i = 0; i < response.length(); i++){
                            try {
                                mAdapter.add(response.getJSONObject(i).getString("name"));
                            } catch (JSONException e) {
                                continue;
                            }
                        }

                        updateListView();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("", "Error: " + error.getMessage());
            }
        });
        mReqQueue.add(request);
    }

    private void updateListView(){
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAdapter.notifyDataSetChanged();
            }
        });
    }
}
