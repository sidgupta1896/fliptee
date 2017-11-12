package com.example.welcome.fliptee;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class ordersActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        recyclerView=(RecyclerView)findViewById(R.id.recycler_orderList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false));
        //recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContex));
        recyclerView.setAdapter(new CustomAdapter());
    }
    class CustomViewHolder
            extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView imageView;
        private TextView textQuantity;
        private TextView textTotal;
        CustomAdapter customAdapter;

        CustomViewHolder(View view,CustomAdapter customAdapter) {
            super(view);
            view.setOnClickListener(this);
            this.customAdapter=customAdapter;
            imageView = (ImageView) view.findViewById(R.id.orders_card_text_image);
            textQuantity = (TextView) view.findViewById(R.id.orders_card_text_quantity);
            textTotal=(TextView)view.findViewById(R.id.orders_card_text_total);
        }

        public void bindData(int position) {

        }
        @Override
        public void onClick(View view) {

        }
    }
    class CustomAdapter extends RecyclerView.Adapter<CustomViewHolder>
    {
        @Override
        public CustomViewHolder onCreateViewHolder(ViewGroup parent1, int viewType)
        {
            View view= LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_cardview_orders,parent1,false);
            return  new CustomViewHolder(view,CustomAdapter.this);
        }

        @Override
        public void onBindViewHolder(CustomViewHolder holder, int position) {
            holder.bindData(position);
        }

        @Override
        public int getItemCount() {

            int num=10;
            return num;
        }
    }
}
