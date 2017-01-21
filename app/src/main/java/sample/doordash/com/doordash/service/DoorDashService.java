package sample.doordash.com.doordash.service;

import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;
import sample.doordash.com.doordash.domain.MenuItem;
import sample.doordash.com.doordash.domain.Restaurant;
import sample.doordash.com.doordash.domain.AuthToken;
import sample.doordash.com.doordash.domain.Credential;
import sample.doordash.com.doordash.domain.Menu;
import sample.doordash.com.doordash.domain.User;

/**
 * Created by Hakeem on 1/20/17.
 */

public interface DoorDashService {
    @GET("/v2/restaurant/")
    Observable<List<Restaurant>> getRestaurants(@Query("lat") double lat, @Query("lng") double lng);

    @GET("/v2/restaurant/{restaurant_id}/")
    Observable<Restaurant> getRestaurant(@Path("restaurant_id") long id);

    @POST("/v2/auth/token/")
    Observable<AuthToken> getAuthToken(@Body Credential cred);

    @GET("/v2/consumer/me/")
    Observable<User> getUser(@Header("Authorization") String authHeader);

    @GET("/v2/restaurant/{restaurant_id}/menu/{menu_id}/")
    Observable<Menu> getMenu(@Path("restaurant_id") long rId, @Path("menu_id") long mId);

    @GET("/v2/restaurant/{restaurant_id}/item/{item_id}/")
    Observable<MenuItem> getMenuItem(@Path("restaurant_id") long rId, @Path("item_id") long iId);
}
