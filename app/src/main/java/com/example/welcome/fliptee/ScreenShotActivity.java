package com.example.welcome.fliptee;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.jraska.falcon.Falcon;

/**
 * Created by welcome on 15-11-2016.
 */
public class ScreenShotActivity extends AppCompatActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_shots);
        Bitmap bm= Falcon.takeScreenshotBitmap(getParent());
        ImageView iv=(ImageView)findViewById(R.id.image_screen_shot);
        iv.setImageBitmap(bm);
        //FrameLayout fm=(FrameLayout)findViewById(R.id.layout_frame_shot);

    }
}
