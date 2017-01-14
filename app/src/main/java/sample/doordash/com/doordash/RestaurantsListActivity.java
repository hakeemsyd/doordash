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
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RestaurantsListActivity extends ListActivity {

    private ArrayAdapter<String> mAdapter;
    private RequestQueue mReqQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mReqQueue = Volley.newRequestQueue(this);
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
        List<String> l = new ArrayList<>();
        l.add("restaurant 1");
        l.add("restaurant 2");

        String q = "?lat=" + 37.386052 + "&lng=" + -122.083851;
        JsonArrayRequest request = new JsonArrayRequest(Constants.API_RESTURANT + q,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i("", "Response: " + response.length());

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("", "Error: " + error.getMessage());
            }
        });
        mReqQueue.add(request);
    }
}
