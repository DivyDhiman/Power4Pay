package Functionality_Class;

import android.app.Activity;
import android.content.Context;

import freacharge.power4pay.com.power4pay.R;

public class Animationall {

    public void Animallforward(Context context) {
        ((Activity) context).overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_exit);;
    }

    public void Animallbackward(Context context) {
        ((Activity) context).overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_exit);
    }


}
