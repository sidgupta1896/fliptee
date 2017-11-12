package com.example.welcome.fliptee;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.rtugeek.android.colorseekbar.ColorSeekBar;
import com.ugurtekbas.fadingindicatorlibrary.FadingIndicator;

public class frag2_clipart extends android.support.v4.app.Fragment {
    private RecyclerView recyclerView,horizontalList;
    TextView tv1;
    EditText et;
    ColorSeekBar colorSeekBar;
    SeekBar skbar;
    TextView txtv;
    Bitmap textBitmap;
    FadingIndicator indicator;
    private float[] lastEvent = null;
    final int [] iv_int={R.drawable.cinzel,R.drawable.hindi,R.drawable.adine,R.drawable.scripalt,R.drawable.walt2,R.drawable.ymms};
    final String [] iv_text={"fonts/CinzelDecorative-Black.ttf", "fonts/HindMedium.ttf","fonts/AdineKirnberg.ttf","fonts/SCRIPALT.ttf","fonts/waltograph42.ttf","fonts/YouMakeMeSmile.ttf"};
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_frag2_clipart,container,false);
        Log.i("CClipart","lpad");
        recyclerView = (RecyclerView)v. findViewById(R.id.recyclerView);
        //recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,true));
        recyclerView.setLayoutManager((new GridLayoutManager(getContext(),2, LinearLayoutManager.HORIZONTAL,true)));
        recyclerView.setAdapter(new CustomAdapter());
        et=(EditText)v.findViewById(R.id.eT);
        //skbar=(SeekBar)getActivity().findViewById(R.id.seekbar_height);
        colorSeekBar=(ColorSeekBar) getActivity().findViewById(R.id.id_colorTextSlider);
        final Button try_but=(Button)v.findViewById(R.id.try_button);

        txtv=(TextView) getActivity().findViewById(R.id.clipartTry);
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                TextView tv=(TextView) getActivity().findViewById(R.id.clipartTry);
                tv.setText(new String(s.toString()));
                String s1=tv.getText().toString();
                if(s1.length()==0) {
                    //skbar.setVisibility(View.INVISIBLE);
                    colorSeekBar.setVisibility(View.INVISIBLE);
                    et.setError(null);
                }
                else {
                    //skbar.setVisibility(View.VISIBLE);
                    colorSeekBar.setVisibility(View.VISIBLE);
                    et.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        try_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtv.getText().length()!=0) {
                    Log.i("qwnmqwnm", txtv.getText().toString());
                    Bitmap textBitmap;
                    txtv.setDrawingCacheEnabled(true);
                    txtv.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
                    textBitmap = Bitmap.createBitmap(txtv.getDrawingCache());
                    txtv.setDrawingCacheEnabled(false);
                    txtv.setText("");
                    FrameLayout canvas = (FrameLayout) getActivity().findViewById(R.id.canvasView);
                    StickerImageView iv_sticker = new StickerImageView(getActivity());
                    iv_sticker.setImageBitmap(textBitmap);
                    canvas.addView(iv_sticker);
                    et.setText("");

                }
            }
        });
        colorSeekBar.setMaxValue(255);
        colorSeekBar.setColors(R.array.material_colors); // material_colors is defalut included in res/color,just use it.
        colorSeekBar.setColorBarValue(0); //0 - maxValue
        //colorSeekBar.setAlphaBarValue(0); //0-255
        //colorSeekBar.setShowAlphaBar(true);
        colorSeekBar.setBarHeight(5); //5dpi
        colorSeekBar.setThumbHeight(30); //30dpi
        //colorSeekBar.setBarMargin(10); //set the margin between colorBar and alphaBar 10dpi
        colorSeekBar.setOnColorChangeListener(new ColorSeekBar.OnColorChangeListener() {
            @Override
            public void onColorChangeListener(int colorBarValue, int alphaBarValue, int color) {
                TextView tv=(TextView)getActivity().findViewById(R.id.clipartTry);
                tv.setTextColor(color);
            }
        });
        return v;
    }
    class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView imageView;
        private TextView itemText;
        CustomViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            imageView = (ImageView) view.findViewById(R.id.item_img);
            //itemText=(TextView)view.findViewById(R.id.item_img_text);
        }

        public void bindData(int position) {
            imageView.setImageResource(iv_int[position]);
            //itemText.setText(list.get(position));
        }
        @Override
        public void onClick(View view) {
            //Toast.makeText(getActivity(), "ON click", Toast.LENGTH_SHORT).show();
            final Typeface typeface;
            Context cxt = view.getContext();
            int pos=recyclerView.getChildLayoutPosition(view);
            TextView tv1 = (TextView)getActivity().findViewById(R.id.clipartTry);
            EditText et=(EditText)getActivity().findViewById(R.id.eT);
            //skbar.setVisibility(View.VISIBLE);
            colorSeekBar.setVisibility(View.VISIBLE);
            if(tv1.getText().toString().length()==0)
            {
              //  skbar.setVisibility(View.INVISIBLE);
                colorSeekBar.setVisibility(View.INVISIBLE);
                et.setError("");
            }
            else {
                //skbar.setVisibility(View.VISIBLE);
                colorSeekBar.setVisibility(View.VISIBLE);
                et.setError(null);
            }
            /*skbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    TextView tv1 = (TextView)getActivity().findViewById(R.id.clipartTry);
                    tv1.setTextSize(progress);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });*/
            typeface = Typeface.createFromAsset(getActivity().getAssets(), iv_text[pos]);
            tv1.setTypeface(typeface);


        }

    }
    class CustomAdapter extends RecyclerView.Adapter<CustomViewHolder> {
        @Override
        public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_images, parent, false);
            return new CustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(CustomViewHolder holder, int position) {
            holder.bindData(position);
        }

        @Override
        public int getItemCount() {
            return iv_int.length;
        }
    }
}
