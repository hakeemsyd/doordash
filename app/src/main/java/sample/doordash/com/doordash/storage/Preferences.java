package sample.doordash.com.doordash.storage;

import android.content.Context;
import android.content.SharedPreferences;

import sample.doordash.com.doordash.R;

/**
 * Created by Hakeem on 1/15/17.
 */

public class Preferences {
    private static final String KEY_TOKEN = "token";
    private SharedPreferences mPrefs;

    public Preferences(Context context){
        mPrefs = context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
    }

    public void addFavourite(String id){
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putBoolean(id,true);
        editor.commit();
    }

    public boolean isFavourite(String id){
        return mPrefs.getBoolean(id, false);
    }

    public void removeFavourite(String id){
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.remove(id);
        editor.commit();
    }

    public void addToken(String token){
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putString(KEY_TOKEN, token);
        editor.commit();
    }

    public void removeToken(){
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.remove(KEY_TOKEN);
        editor.commit();
    }

    public String getToken(){
        String tok = mPrefs.getString(KEY_TOKEN, "");
        return tok ;
    }
}
