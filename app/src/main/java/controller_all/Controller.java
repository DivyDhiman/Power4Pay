package controller_all;

import android.app.Application;
import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.widget.EditText;

import Functionality_Class.Animationall;
import Functionality_Class.Check_all;

/**
 * Created by Abhay dhiman
 */

public class Controller extends Application {

    private Check_all checkAll = new Check_all();
    private Animationall animationall = new Animationall();

    public boolean Check_all_empty(EditText editText){
        return checkAll.EmptyCheck_edittext(editText);
    }

    public boolean Check_all_password(EditText editText,int start, int end){
        return checkAll.Password_edittext(editText,start,end);
    }
    public boolean Check_all_email(EditText editText){
        return checkAll.Email_edittext(editText);
    }
    public boolean Check_all_confirmpassword(EditText editText,String password){
        return checkAll.ConfirmPassword_edittext(editText,password);
    }
    public boolean InternetCheck(Context context){
        return checkAll.isNetWorkStatusAvialable(context);
    }

    public void Shackbarall(CoordinatorLayout coordinatelayout, String message){
        checkAll.Snackbarshow(coordinatelayout,message);
    }

    public void Animationforward(Context context){
        animationall.Animallforward(context);
    }

    public void Animationbackward(Context context){
        animationall.Animallbackward(context);
    }

}

