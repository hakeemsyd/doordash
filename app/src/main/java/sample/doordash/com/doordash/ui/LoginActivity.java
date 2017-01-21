package sample.doordash.com.doordash.ui;

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

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import sample.doordash.com.doordash.storage.Preferences;
import sample.doordash.com.doordash.R;
import sample.doordash.com.doordash.domain.AuthToken;
import sample.doordash.com.doordash.domain.Credential;
import sample.doordash.com.doordash.domain.User;
import sample.doordash.com.doordash.service.DoorDashClient;
import sample.doordash.com.doordash.storage.Storage;

/**
 * Created by Hakeem on 1/15/17.
 */

public class LoginActivity extends Activity {

    private Button mLogin;
    private Button mGuest;
    private EditText mUser;
    private EditText mPass;
    private Preferences mPrefs;
    private TextView mName;
    private TextView mPhone;
    private TextView mAddress;
    private Button mLogout;
    private Button mContinue;
    private LinearLayout mLoginLayout;
    private LinearLayout mUserInfoLayout;

    private List<Subscription> mSubscriptions = new ArrayList<>();

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
        mPrefs = new Preferences(this);

        mLogin = (Button) findViewById(R.id.login);
        mGuest = (Button) findViewById(R.id.guest_user);
        mContinue = (Button) findViewById(R.id.btn_continue);
        mLogout = (Button) findViewById(R.id.btn_logout);

        mUser = (EditText) findViewById(R.id.username);
        mPass = (EditText) findViewById(R.id.password);

        mName = (TextView) findViewById(R.id.userinfo_name);
        mPhone = (TextView) findViewById(R.id.userinfo_phone);
        mAddress = (TextView) findViewById(R.id.userinfo_address);

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
            onTokenRetrieved(mPrefs.getToken());
        } else {
            showLoginScreen();
        }
    }

    @Override
    protected void onDestroy() {
        if(mSubscriptions != null){
            for(Subscription s: mSubscriptions){
                s.unsubscribe();
            }
        }
        super.onDestroy();
    }

    private void setInProgress(boolean prog) {
        mLogin.setEnabled(!prog);
        mGuest.setEnabled(!prog);
        mUser.setEnabled(!prog);
        mPass.setEnabled(!prog);
    }

    private void showUserInfo(User user) {
        mName.setText(user.mFirstName + " " + user.mLastName);
        mPhone.setText(user.mPhone);
        mAddress.setText(user.mAddress.mPrintableAddress);

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

    private void doLogin(){
        setInProgress(true);
        Subscription sub = DoorDashClient.getInstance().getAuthToken(new Credential(mUser.getText().toString(), mPass.getText().toString()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<AuthToken>() {
                    @Override
                    public void onCompleted() {
                        setInProgress(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        setInProgress(false);
                        showLoginScreen();
                        Toast.makeText(getApplicationContext(), "Login failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(AuthToken authToken) {
                        Toast.makeText(getApplicationContext(), "Login Success: " + authToken.mToken, Toast.LENGTH_LONG).show();
                        mPrefs.addToken(authToken.mToken);
                        onTokenRetrieved(authToken.mToken);
                    }
                });

        mSubscriptions.add(sub);
    }

    private void onTokenRetrieved(final String token){

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
                        setInProgress(false);
                        mPrefs.removeToken();
                        showLoginScreen();
                        Toast.makeText(getApplicationContext(), "Failed to get user info: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(User user) {
                        setInProgress(false);
                        Toast.makeText(getApplicationContext(), "Login Success: " + user.mFirstName, Toast.LENGTH_SHORT).show();
                        showUserInfo(user);
                    }
                });
    }
}
