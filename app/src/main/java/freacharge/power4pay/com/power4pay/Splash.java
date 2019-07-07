package freacharge.power4pay.com.power4pay;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import Functionality_Class.SharedPrefSave;
import controller_all.Controller;

public class Splash extends Activity implements Animation.AnimationListener {

    private Context context;
    private Intent intent;
    private ImageView splashlogo;
    private TextView splash_title;
    private Animation mAnim = null;
    private boolean checksplash, status;
    private SharedPrefSave sharedPrefSave;

    /**
     * Created by Abhay dhiman
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        context = Splash.this;
        checksplash = false;
        sharedPrefSave = new SharedPrefSave(context);
        status = sharedPrefSave.getBoolean("status");

        splashlogo = (ImageView) findViewById(R.id.splash_logo);
        splash_title = (TextView) findViewById(R.id.splash_title);
        splash_title.setVisibility(View.GONE);
        mAnim = AnimationUtils.loadAnimation(this, R.anim.translate);
        mAnim.setAnimationListener(this);
        splashlogo.clearAnimation();
        splashlogo.setAnimation(mAnim);
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {

        if (!checksplash) {
            splash_title.setVisibility(View.VISIBLE);

            mAnim = AnimationUtils.loadAnimation(this, R.anim.fade_in);
            mAnim.setAnimationListener(this);

            splash_title.startAnimation(mAnim);
            checksplash = true;

        } else {
            if (status) {
                intent = new Intent(context, MainActivity.class);
                startActivity(intent);
                finish();

            } else {
                intent = new Intent(context, AfterSplash.class);
                startActivity(intent);
                finish();

            }
        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
