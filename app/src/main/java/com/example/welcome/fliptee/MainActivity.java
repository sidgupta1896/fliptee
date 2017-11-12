package com.example.welcome.fliptee;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
//import android.os.CountDownTimer;

public class MainActivity extends AppCompatActivity {
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }
    /** Called when the activity is first created. */
    Thread splashTread;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);
        StartAnimations();


    }

    private void StartAnimations() {
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.alpa);
        anim.reset();
        LinearLayout l=(LinearLayout) findViewById(R.id.lin_lay);
        l.clearAnimation();
        l.startAnimation(anim);

        anim = AnimationUtils.loadAnimation(this, R.anim.translate);
        anim.reset();
        ImageView iv = (ImageView) findViewById(R.id.splash);
        iv.clearAnimation();
        iv.startAnimation(anim);

        splashTread = new Thread() {
            @Override
            public void run() {
                try {
                    int waited = 0;
                    // Splash screen pause time
                    while (waited < 3500) {
                        sleep(100);
                        waited += 100;
                    }
                    SharedPreferences shap=getSharedPreferences("LOGIN_GOOGLE",MODE_PRIVATE);
                    SharedPreferences.Editor editor=shap.edit();
                    editor.putInt("login",50);
                    pref = getSharedPreferences("FLIP",0);
                    editor = pref.edit();
                    boolean is_logged_in = pref.getBoolean("IS_LOGGED_IN",false);
                    String email= pref.getString("USER_EMAIL",null);
                    String name= pref.getString("USER_NAME",null);

                    if(shap.getInt("login",89)==100)
                    {

                        Intent i = new Intent(getApplicationContext(),designer_customer_Activity.class);
                        startActivity(i);

                    }
                    else {

                        Intent startActivity = new Intent(MainActivity.this,Login_google_Activity.class);
                        startActivity(startActivity);

                    }


                    MainActivity.this.finish();
                } catch (InterruptedException e) {
                    // do nothing
                } finally {
                    MainActivity.this.finish();
                }

            }
        };
        splashTread.start();


    }




}


