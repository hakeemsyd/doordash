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

    public void addFavourite(String id){
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putBoolean(id,true);
        editor.commit();
    }

    public boolean isFavourite(String id){
        boolean f = mPrefs.getBoolean(id, false);
       return f;
    }

    public void removeFavourite(String id){
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.remove(id);
        editor.commit();
    }
}
