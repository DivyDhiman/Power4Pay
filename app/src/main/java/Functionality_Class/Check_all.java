package Functionality_Class;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Abhay dhiman
 */

public class Check_all {

    public boolean EmptyCheck_edittext(EditText editText) {
        if (editText.getText().toString().trim().length() > 0) {
            return false;
        } else {
            return true;
        }
    }

    public boolean Password_edittext(EditText editText,int start, int end) {
        if (editText.length() >= start  && editText.length() <= end) {
            return false;
        } else {
            return true;
        }
    }

    public boolean Email_edittext(EditText editText) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(editText.getText().toString());
        return matcher.matches();
    }

    public boolean ConfirmPassword_edittext(EditText editText,String password) {
        if(editText.getText().toString().equals(password)){
            return false;
        }else {
            return true;
        }
    }

    public boolean isNetWorkStatusAvialable(Context applicationContext) {
        ConnectivityManager connectivityManager = (ConnectivityManager)applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager != null){
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if(networkInfo != null) {
                if (networkInfo.isConnected())
                    return true;
            }
        }
        return false;
    }

    public void Snackbarshow(CoordinatorLayout coordinatelayout,String message){
        Snackbar snackbar = Snackbar
                .make(coordinatelayout, message, Snackbar.LENGTH_LONG);

        snackbar.show();

    }

}
