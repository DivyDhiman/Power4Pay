package Functionality_Class;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefSave {


    private SharedPreferences Power4Pay;
    private static String file = "Power4Pay";

    public SharedPrefSave(Context context) {
        Power4Pay = ((Activity) context).getSharedPreferences(file, 0);
    }

    public void setBoolean(String key, boolean value) {
        SharedPreferences.Editor editors = Power4Pay.edit();
        editors.putBoolean(key, value);
        editors.commit();
    }


    public void setString(String key, String value) {
        SharedPreferences.Editor editors = Power4Pay.edit();
        editors.putString(key, value);
        editors.commit();
    }


    public boolean getBoolean(String key) {
        boolean result = Power4Pay.getBoolean(key, false);
        return result;
    }


    public String getString(String key) {
        String res = Power4Pay.getString(key, "AS");
        return res;
    }

    public void removesharedpreferences(String key) {
        Power4Pay.edit().remove(key).commit();

    }


}
