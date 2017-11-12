package com.example.welcome.fliptee;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by welcome on 15-10-2016.
 */
public class frag_grid_recycler extends android.support.v4.app.Fragment {
    RecyclerView rv;
    final int [] iv_int={R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.drawable.scripalt,R.drawable.walt2,R.drawable.ymms,R.drawable.adine,R.drawable.scripalt,R.drawable.walt2,R.drawable.ymms};
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       //View v=super.onCreateView(inflater, container, savedInstanceState);
        //Log.i("Tag","hello");
        View v=inflater.inflate(R.layout.fragment_grid_recycler,container,false);
        rv=(RecyclerView)v.findViewById(R.id.recyclerView_grid);
        rv.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        rv.setAdapter(new CustomAdapter());
        return v;
    }
    class CustomViewHolder
            extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView imageView;
        private TextView textView;


        CustomViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            imageView = (ImageView) view.findViewById(R.id.item_grid_image);
        }

        public void bindData(int position) {
            //imageView.setImageResource(R.mipmap.ic_launcher);
            imageView.setImageResource(iv_int[position]);//this also haas to be removed after we have database
        }
        @Override
        public void onClick(View view) {
            Toast.makeText(getActivity(), textView.getText().toString(), Toast.LENGTH_SHORT).show();
        }
    }

    class CustomAdapter extends RecyclerView.Adapter<CustomViewHolder> {
        @Override
        public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_recycler_images, parent, false);
            return new CustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(CustomViewHolder holder, int position) {
            holder.bindData(position);
        }

        @Override
        public int getItemCount() {
            return iv_int.length;//thi sarray has to be removed after database
        }
    }
}
