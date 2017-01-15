package sample.doordash.com.doordash;

import android.app.DialogFragment;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Hakeem on 1/15/17.
 */

public class RestaurantDialogFragment extends DialogFragment {

    private long mId;
    private TextView mRestName;
    private TextView mRestPhone;
    private TextView mCity;
    private TextView mAddress;
    private TextView mDistance;
    private ImageView mIcon;

    static RestaurantDialogFragment newInstance(long id){
        RestaurantDialogFragment f = new RestaurantDialogFragment();

        Bundle args = new Bundle();
        args.putLong(Constants.KEY_JSON_RESTAURANT_ID, id);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mId = getArguments().getLong(Constants.KEY_JSON_RESTAURANT_ID);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Material_Light_Dialog_Alert);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_restaurant_detail, container, false);
        mRestName = (TextView) v.findViewById(R.id.resinfo_name);
        mCity = (TextView) v.findViewById(R.id.resinfo_city);
        mAddress = (TextView) v.findViewById(R.id.resinfo_address);
        mDistance = (TextView) v.findViewById(R.id.resinfo_distance);
        mRestPhone = (TextView) v.findViewById(R.id.resinfo_phone);
        mIcon = (ImageView) v.findViewById(R.id.restinfo_icon);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        update();
    }

    private void update(){
        RequestQueue reqQueue = Volley.newRequestQueue(getActivity());
        String url = Constants.API_RESTAURANT_INFO + "/" + mId + "/";

        JsonObjectRequest req = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.i("", "Restaurant info: " + response.getString("name"));
                    mRestName.setText("Name: " + response.getString(Constants.KEY_JSON_RESTAURANT_NAME));
                    mRestPhone.setText("Pone: " + response.getString(Constants.KEY_PHONE));
                    mCity.setText("City: " + response.getJSONObject(Constants.KEY_ADDRESS).getString(Constants.KEY_CITY));
                    mAddress.setText("Address: " + response.getJSONObject(Constants.KEY_ADDRESS).getString(Constants.KEY_RESTAURANT_PRINTABLE_ADDRESS));
                    mDistance.setText("Distance: " + response.getString(Constants.KEY_RESTAURANT_DISTANCE));
                    Picasso.with(getActivity()).load(response.getString(Constants.KEY_COVER_IMAGE_URL)).into(mIcon);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Restaurant info error:" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        reqQueue.add(req);
    }
}
