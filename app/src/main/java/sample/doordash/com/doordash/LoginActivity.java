package sample.doordash.com.doordash;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Hakeem on 1/15/17.
 */

public class LoginActivity extends Activity {

    private RequestQueue mReqQueue;
    private Button mLogin;
    private Button mGuest;
    private EditText mUser;
    private EditText mPass;
    private Preferences mPrefs;
    private User mLoggedInUser;
    private TextView mName;
    private TextView mPhone;
    private TextView mAddress;
    private TextView mCity;
    private Button mLogout;
    private Button mContinue;
    private LinearLayout mLoginLayout;
    private LinearLayout mUserInfoLayout;

    private final View.OnClickListener mLoginButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            doLogin();
        }
    };

    private final View.OnClickListener mLogoutButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mPrefs.removeToken();
            showLoginScreen();
        }
    };

    private final View.OnClickListener mGuestButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i = new Intent(getApplicationContext(), RestaurantsListActivity.class);
            startActivity(i);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mReqQueue = Volley.newRequestQueue(this);
        mPrefs = new Preferences(this);
        mLoggedInUser = new User();

        mLogin = (Button) findViewById(R.id.login);
        mGuest = (Button) findViewById(R.id.guest_user);
        mContinue = (Button) findViewById(R.id.btn_continue);
        mLogout = (Button) findViewById(R.id.btn_logout);

        mUser = (EditText) findViewById(R.id.username);
        mPass = (EditText) findViewById(R.id.password);

        mName = (TextView) findViewById(R.id.userinfo_name);
        mPhone = (TextView) findViewById(R.id.userinfo_phone);
        mAddress = (TextView) findViewById(R.id.userinfo_address);
        mCity = (TextView) findViewById(R.id.userinfo_city);

        mLoginLayout = (LinearLayout) findViewById(R.id.login_prompt_layout);
        mUserInfoLayout = (LinearLayout) findViewById(R.id.user_info_layout);

        mLogin.setOnClickListener(mLoginButtonClickListener);
        mGuest.setOnClickListener(mGuestButtonListener);
        mLogout.setOnClickListener(mLogoutButtonClickListener);
        mContinue.setOnClickListener(mGuestButtonListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!mPrefs.getToken().isEmpty()) {
            showUserInfo();
            onTokenRetrieved(mPrefs.getToken());
        } else {
            showLoginScreen();
        }
    }

    private void setInProgress(boolean prog) {
        mLogin.setEnabled(!prog);
        mGuest.setEnabled(!prog);
        mUser.setEnabled(!prog);
        mPass.setEnabled(!prog);
    }

    private void showUserInfo() {
        mName.setText(mLoggedInUser.getmName());
        mPhone.setText(mLoggedInUser.getmPhone());
        mCity.setText(mLoggedInUser.getmCity());
        mAddress.setText(mLoggedInUser.getmAddress());

        mUserInfoLayout.setVisibility(View.VISIBLE);
        mLoginLayout.setVisibility(View.GONE);
    }

    private void showLoginScreen() {
        mUser.setText("");
        mPass.setText("");
        mPrefs.removeToken();
        mUserInfoLayout.setVisibility(View.GONE);
        mLoginLayout.setVisibility(View.VISIBLE);
    }

    private synchronized void doLogin() {
        setInProgress(true);

        JSONObject creds = new JSONObject();
        try {
            creds.put(Constants.KEY_EMAIL, mUser.getText());
            creds.put(Constants.KEY_PASSWORD, mPass.getText());
        } catch (JSONException e) {
            e.printStackTrace();
            showLoginScreen();
            setInProgress(false);
            return;
        }

        JsonObjectRequest jReq = new JsonObjectRequest(Constants.API_AUTH_TOKEN, creds,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        showLoginScreen();
                        setInProgress(false);
                        try {
                            String token = response.getString(Constants.KEY_TOKEN);
                            onTokenRetrieved(token);
                            Toast.makeText(getApplicationContext(), "Login Success: " + token, Toast.LENGTH_LONG).show();
                            mPrefs.addToken(token);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Login failure: " + error.getMessage(), Toast.LENGTH_LONG).show();
                        setInProgress(false);
                        showLoginScreen();
                    }
                });

        mReqQueue.add(jReq);
    }

    private synchronized void onTokenRetrieved(final String token) {
        setInProgress(true);

        JsonObjectRequest jReq = new JsonObjectRequest(Constants.API_ABOUT_ME, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        setInProgress(false);
                        try {
                            Toast.makeText(getApplicationContext(), "Login Success: " + response.getString("first_name"), Toast.LENGTH_SHORT).show();
                            mLoggedInUser = User.CreateFromJSONObject(response);
                            showUserInfo();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Me failure: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        setInProgress(false);
                        showLoginScreen();
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(Constants.KEY_AUTH_HEADER, "JWT " + token);

                return params;
            }
        };

        mReqQueue.add(jReq);
    }
}
