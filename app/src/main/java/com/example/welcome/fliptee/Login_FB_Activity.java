package com.example.welcome.fliptee;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;


public class Login_FB_Activity extends FragmentActivity
{
    private FragmentManager mFragmentManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);//change
        /*mFragmentManager = getSupportFragmentManager();
        Button b=(Button )findViewById(R.id.fbloginbutton);
        b.setVisibility(View.INVISIBLE);
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.add(android.R.id.content,new MainFragment());
        transaction.commit();*/
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}


