package sample.doordash.com.doordash;

import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

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
}
