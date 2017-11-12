package com.example.welcome.fliptee;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TabHost;

public class designer_customer_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_designer_customer_);
        // TabHost tabhost=getTabHost();
       // tabhost.addTab(tabhost.newTabSpec("first").setIndicator("DESIGNER").setContent(new Intent(this,designerActivity.class)));
       // tabhost.addTab(tabhost.newTabSpec("second").setIndicator("CUSTOMER").setContent(new Intent(this,customerActivity.class)));
        //tabhost.addTab(tabhost.newTabSpec("second").setIndicator("CUSTOMER").setContent(new Intent(this,Login_google_Activity.class)));
       // tabhost.setCurrentTab(1);
        Intent intent=new Intent(this,customerActivity.class);
        startActivity(intent);
    }
}
