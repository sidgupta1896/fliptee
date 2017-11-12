package com.example.welcome.fliptee;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class SavedList extends AppCompatActivity {
    final int [] iv_int={R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.drawable.scripalt,R.drawable.walt2};
    RecyclerView recyclerView;
    final CharSequence[] items = {"Delete"};
    Bitmap bitmap;
    String data;
    String encrypted;
    String tempString;
    SQLiteDatabase sqLiteDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_list);
        recyclerView=(RecyclerView)findViewById(R.id.recycler_savedList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false));
        //recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContex));
        recyclerView.setAdapter(new CustomAdapter());
    }
    class CustomViewHolder
            extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener{
        private ImageView imageView;
        private TextView textView;
        CustomAdapter customAdapter;

        CustomViewHolder(View view,CustomAdapter customAdapter) {
            super(view);
            view.setOnClickListener(this);
            view.setOnLongClickListener(this );
            this.customAdapter=customAdapter;
            imageView = (ImageView) view.findViewById(R.id.card_image);
            textView = (TextView) view.findViewById(R.id.card_text);
        }

        public void bindData(int position) {
            SQLiteDatabase db=openOrCreateDatabase("imageDB", Context.MODE_PRIVATE,null);
            Cursor cursor = db.rawQuery("select * from imageTable",null);;
            cursor.moveToFirst();
            Log.i("POSuyuy"," "+position);
            int p=0;
            do
            {
                Log.i("jijiqwqw","huhu");
                if(p==position)
                {
                    data=cursor.getString(cursor.getColumnIndex("changes"));
                    tempString=cursor.getString(cursor.getColumnIndex("image"));

                    /*AES obj=new AES();
                    try {
                        encrypted = obj.encrypt(tempString);
                    }
                    catch (Exception e)
                    {

                    }*/



                    byte [] encodeByte= Base64.decode(tempString,Base64.DEFAULT);
                    bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                    BitmapDrawable ob = new BitmapDrawable(getResources(), bitmap);
                    imageView.setImageDrawable(ob);
                    imageView.setImageBitmap(bitmap);
                    textView.setText(data);
                    break;
                }
                p=p+1;
            }while(cursor.moveToNext());
        }
        @Override
        public void onClick(View view) {
           // Toast.makeText(getApplicationContext(), textView.getText().toString(), Toast.LENGTH_SHORT).show();
            int position=getAdapterPosition();

            SQLiteDatabase db=openOrCreateDatabase("imageDB", Context.MODE_PRIVATE,null);
            Cursor cursor = db.rawQuery("select * from imageTable",null);;
            cursor.moveToFirst();
            Log.i("POSuyuy"," "+position);
            int p=0;
            do
            {
                Log.i("jijiqwqw","huhu");
                if(p==position)
                {
                    data=cursor.getString(cursor.getColumnIndex("changes"));
                    tempString=cursor.getString(cursor.getColumnIndex("image"));
                    break;
                }
                p=p+1;
            }while(cursor.moveToNext());

            Log.i("pospospos",""+position);
            Log.i("qwaw","qwas");
            Intent i=new Intent(SavedList.this,checkoutActivity.class);
            Log.i("qwaw1","qwas1");
            i.putExtra("Changes",data);

            i.putExtra("ImageString",tempString);
            startActivity(i);
            //startActivity(i);

        }

        @Override
        public boolean onLongClick(final View v) {
            int position=getAdapterPosition();
            AlertDialog.Builder alertDialog=new AlertDialog.Builder(getApplicationContext());
            alertDialog.setItems(items, new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    customAdapter.removeItem(getAdapterPosition());
                    dialog.dismiss();
                }

            });
            alertDialog.create().show();
            return true;
        }
    }
    class CustomAdapter extends RecyclerView.Adapter<CustomViewHolder>
    {
        @Override
        public CustomViewHolder onCreateViewHolder(ViewGroup parent1, int viewType)
        {
            View view= LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_cardview_saved,parent1,false);
            return  new CustomViewHolder(view,CustomAdapter.this);
        }

        @Override
        public void onBindViewHolder(CustomViewHolder holder, int position) {
            holder.bindData(position);
        }

        @Override
        public int getItemCount() {
            SQLiteDatabase db=openOrCreateDatabase("imageDB", Context.MODE_PRIVATE,null);
            Cursor c = db.rawQuery("select * from imageTable",null);
            Log.i("CCCQQQ"," "+c.getCount());
            return c.getCount();
        }
        public void removeItem(int position) {
            SQLiteDatabase db=openOrCreateDatabase("imageDB", Context.MODE_PRIVATE,null);
            Cursor c = db.rawQuery("select * from imageTable",null);
            int p=0;
            do
            {
                if(p==position)
                {

                    break;
                }
                p=p+1;
            }while(c.moveToNext());
            notifyItemRemoved(position);
            // Add whatever you want to do when removing an Item
        }
    }
}
