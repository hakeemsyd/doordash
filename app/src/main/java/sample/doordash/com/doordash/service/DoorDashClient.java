package sample.doordash.com.doordash.service;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import sample.doordash.com.doordash.Constants;
import sample.doordash.com.doordash.domain.MenuCategory;
import sample.doordash.com.doordash.domain.MenuItem;
import sample.doordash.com.doordash.domain.Restaurant;
import sample.doordash.com.doordash.domain.AuthToken;
import sample.doordash.com.doordash.domain.Credential;
import sample.doordash.com.doordash.domain.Menu;
import sample.doordash.com.doordash.domain.User;

/**
 * Created by Hakeem on 1/20/17.
 */

public class DoorDashClient {
    private static DoorDashClient mInstance;
    private DoorDashService mService;

    private DoorDashClient(){
        final Gson gson =
                new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
        final Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.API_ROOT)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        mService = retrofit.create(DoorDashService.class);
    }

    public static DoorDashClient getInstance(){
        if(mInstance == null){
            mInstance = new DoorDashClient();
        }
        return mInstance;
    }

    public Observable<List<Restaurant>> getRestaurants(double lat, double lng) {
        return mService.getRestaurants(lat, lng);
    }

    public Observable<AuthToken> getAuthToken(Credential cred){
        return mService.getAuthToken(cred);
    }

    public Observable<Restaurant> getRestaurant(long id){
        return mService.getRestaurant(id);
    }

    public Observable<User> getUserInfo(String token){
        return mService.getUser("JWT " + token);
    }

    public Observable<Menu> getMenu(long restaurantId, long menuId){
        return mService.getMenu(restaurantId, menuId);
    }

    public Observable<List<Menu>> getMenu(long restaurantId){
        return mService.getMenu(restaurantId);
    }

    public Observable<MenuItem> getMenuItem(long restaurantId, long itemId){
        return mService.getMenuItem(restaurantId, itemId);
    }
}
