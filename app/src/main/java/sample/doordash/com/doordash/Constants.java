package sample.doordash.com.doordash;

/**
 * Created by Hakeem on 1/14/17.
 */

public final class Constants {
    private Constants(){

    }

    public static final String API_ROOT = "https://api.doordash.com";
    public static final String API_RESTURANT = API_ROOT + "/v2/restaurant/";
    public static final String API_AUTH_TOKEN = API_ROOT + "/v2/auth/token/";
    public static final String API_ABOUT_ME = API_ROOT + "/v2/consumer/me/";
    public static final String KEY_JSON_RESTAURANT_NAME = "name";
    public static final String KEY_JSON_RESTAURANT_ID = "id";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_FIRST_NAME = "first_name";
    public static final String KEY_LAST_NAME = "last_name";
    public static final String KEY_PHONE = "phone_number";
    public static final String KEY_ADDRESS= "default_address";
    public static final String KEY_CITY= "city";
    public static final String KEY_STREET_ADDRESS= "street";
    public static final String KEY_TOKEN = "token";

    public static final String KEY_AUTH_HEADER= "Authorization";

}
