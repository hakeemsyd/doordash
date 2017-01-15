package sample.doordash.com.doordash;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Hakeem on 1/15/17.
 */

public class Preferences {
    private SharedPreferences mPrefs;

    public Preferences(Context context){
        mPrefs = context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
    }

    public void addFavourite(String name){
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putBoolean(name,true);
        editor.commit();
    }

    public boolean isFavourite(String name){
        boolean f = mPrefs.getBoolean(name, false);
       return f;
    }

    public void removeFavourite(String name){
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.remove(name);
        editor.commit();
    }
}
