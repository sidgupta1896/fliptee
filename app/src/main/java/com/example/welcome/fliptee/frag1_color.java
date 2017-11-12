package com.example.welcome.fliptee;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rtugeek.android.colorseekbar.ColorSeekBar;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

public class frag1_color extends android.support.v4.app.Fragment {
    ColorSeekBar colorSeekBar;
    TextView textView;
    View v;
    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

        v=inflater.inflate(R.layout.fragment_frag1_color,container,false);
        //LinearLayout lin=(LinearLayout)getActivity().findViewById(R.id.dragView);
        SlidingUpPanelLayout sl=(SlidingUpPanelLayout)getActivity().findViewById(R.id.sliding_layout);
        sl.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
        //lin.setVisibility(View.INVISIBLE);
        colorSeekBar=(ColorSeekBar)v.findViewById(R.id.id_colorSlider);
        colorSeekBar.setMaxValue(255);
        colorSeekBar.setColors(R.array.material_colors); // material_colors is defalut included in res/color,just use it.
        colorSeekBar.setColorBarValue(0); //0 - maxValue
        colorSeekBar.setAlphaBarValue(0); //0-255
        colorSeekBar.setShowAlphaBar(true);
        colorSeekBar.setBarHeight(5); //5dpi
        colorSeekBar.setThumbHeight(20); //30dpi
        colorSeekBar.setBarMargin(10); //set the margin between colorBar and alphaBar 10dpi
        colorSeekBar.setOnColorChangeListener(new ColorSeekBar.OnColorChangeListener() {
            @Override
            public void onColorChangeListener(int colorBarValue, int alphaBarValue, int color) {
                ImageView iv1=(ImageView)getActivity().findViewById(R.id.image_tshirt);
                iv1.setColorFilter(color);
            }
        });
        return v;
    }
}