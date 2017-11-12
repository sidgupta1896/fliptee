package com.example.welcome.fliptee;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jaredrummler.materialspinner.MaterialSpinner;
import com.rtugeek.android.colorseekbar.ColorSeekBar;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.ugurtekbas.fadingindicatorlibrary.FadingIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class customerActivity extends AppCompatActivity implements TaskDelegate,NavigationView.OnNavigationItemSelectedListener {
    final int[] iv_int = {R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.drawable.scripalt, R.drawable.walt2};
    ArrayList<String> arrayList;
    private SlidingUpPanelLayout slidingLayout;//sl
    private Button btnShow;//sl
    private Button btnHide;//sl
    private TextView textView;//
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
   // private Toolbar mToolbar;
    String imgurl, type;
    TextView tt;
    ImageView img_from_grid;
    ImageView img2_from_grid;
    ArrayList<DrawerItem> mDrawerItemList;
    RecyclerView recyclerView;
    Toolbar mToolbar;
    ViewPager vp;
    ImageView iv_tee;
    LinearLayout lin;
    ColorSeekBar colorSeekBar;
    BitmapFactory.Options bmOptions;
    //CircleIndicator indicator;

    private Matrix matrix = new Matrix();
    private Matrix savedMatrix = new Matrix();
    // we can be in one of these 3 states
    private static final int NONE = 0;
    private static final int DRAG = 1;
    private static final int ZOOM = 2;
    private int mode = NONE;
    // remember some things for zooming
    private PointF start = new PointF();
    private PointF mid = new PointF();
    private float oldDist = 1f;
    private float d = 0f;
    private float newRot = 0f;
    private float[] lastEvent = null;
    MaterialSpinner spinner;
    @Override
    public void sendData(String result) {
      /*  editor.putString("IMGURL",imgurl);
        editor.putString("TYPES",type);

        editor.commit();*/
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        arrayList = new ArrayList<String>();
        setContentView(R.layout.activity_customer);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        ImageView iv=(ImageView)findViewById(R.id.image_check);
        //SQLiteDatabase db=openOrCreateDatabase("imageDB", Context.MODE_PRIVATE,null);
        //db.execSQL("DROP TABLE IF EXISTS imageTable");
        // mDrawerLayout.addDrawerListener(mToggle);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });
        NavigationView navigationView=(NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
       spinner = (MaterialSpinner) findViewById(R.id.catagoryImage);
        new FetchCatagory().execute();
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                new FetchUrl(getApplicationContext()).execute(item);
            }
        });
        //new FetchUrl(getApplicationContext()).execute();
        iv_tee = (ImageView) findViewById(R.id.image_tshirt);
        iv_tee.setImageResource(R.mipmap.whtee);
        bmOptions = new BitmapFactory.Options();
        vp = (ViewPager) findViewById(R.id.id_viewPager);




        FrameLayout canvas=(FrameLayout)findViewById(R.id.canvasView);
        StickerImageView iv_sticker = new StickerImageView(customerActivity.this);
        iv_sticker.setImageDrawable(getResources().getDrawable(R.drawable.c10));
        canvas.addView(iv_sticker);


        StickerTextView tv_sticker = new StickerTextView(customerActivity.this);
        tv_sticker.setText("PRABHAT");
        //tv_sticker.
        canvas.addView(tv_sticker);


        vp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                slidingLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
                Log.i("PAge"," "+position);
                if (position == 2) {
                    slidingLayout.setPanelHeight(60);
                    Log.i("first","Ssliding");
                    slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                } else {
                    //slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
                    slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
                    Log.i("huhu","jijiji");
                    //slidingLayout.setPanelHeight(0);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        mDrawerItemList = new ArrayList<DrawerItem>();
        DrawerItem item = new DrawerItem();
        DrawerItem item2 = new DrawerItem();
        DrawerItem item3 = new DrawerItem();
        DrawerItem item4 = new DrawerItem();
        DrawerItem item5 = new DrawerItem();

        item.setIcon(R.mipmap.ic_launcher);
        mDrawerItemList.add(item);
        item2.setIcon(R.mipmap.ic_launcher);
        mDrawerItemList.add(item2);
        item.setIcon(R.mipmap.ic_launcher);
        mDrawerItemList.add(item3);
        item2.setIcon(R.mipmap.ic_launcher);
        mDrawerItemList.add(item4);
        item.setIcon(R.mipmap.ic_launcher);
        mDrawerItemList.add(item5);

        //DrawerAdapter adapter = new DrawerAdapter(mDrawerItemList);
        //recyclerView = (RecyclerView) findViewById(R.id.drawerRecyclerView);
        //recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        //recyclerView.setAdapter(adapter);
        //DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        //drawerLayout=new ActionBarDrawerToggle()
        //img_from_grid.setOnTouchListener(this);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mToggle.onOptionsItemSelected(item)){
            return true;

        }

        return super.onOptionsItemSelected(item);

    }
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        int id =item.getItemId();
        if (id == R.id.nav_account) {

            //Intent intent = new Intent(this, com.example.welcome.fliptee.sett.class);
            //startActivity(intent);
            return true;
        }
        if(id ==R.id.id_checkOut)
        {
            Log.i("mnmnmn","mnmnmn");
           Intent intent1 =new Intent(this,SavedList.class);
           startActivity(intent1);
            return true;
        }
        if(id==R.id.design)
        {
            Intent i=new Intent(customerActivity.this,designerActivity.class);
            startActivity(i);
            return  true;
        }
        if(id==R.id.orders) {
            Intent i=new Intent(customerActivity.this,ordersActivity.class);
            startActivity(i);
            return  true;
        }

        else{
            return true;
        }
    }
    private SlidingUpPanelLayout.PanelSlideListener onSlideListener() {
        return new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View view, float v) {
                //textView.setText("panel is sliding");
            }

            @Override
            public void onPanelCollapsed(View view) {
                //textView.setText("panel Collapse");
                Button b = (Button) findViewById(R.id.button_pull_push);
                b.setText("SLIDE ME UPWARDS!!!!");
                Log.i("HERE", "coll");
            }

            @Override
            public void onPanelExpanded(View view) {
                Button b = (Button) findViewById(R.id.button_pull_push);
                b.setText("SLIDE ME DOWNWARDS!!!!");
                Log.i("HERE", "exp");
            }

            @Override
            public void onPanelAnchored(View view) {
            }

            @Override
            public void onPanelHidden(View view) {
            }
        };



    }

    private class CustomAdapter extends FragmentPagerAdapter {
        private String[] fragments = {"frag1", "frag2", "frag3"};

        public CustomAdapter(FragmentManager suppFragMan, Context appContext) {
            super(suppFragMan);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    //slidingLayout.setPanelHeight(0);
                    return new frag1_color();
                case 1:
                    //slidingLayout.setPanelHeight(0);
                    return new frag2_clipart();
                case 2:
                    //slidingLayout.setPanelHeight(20);
                    return new frag3_designs();
                default:
                    return null;
                //return new frag1_color();
            }
        }

        @Override
        public int getCount() {
            return fragments.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragments[position];
        }
    }

    class CustomViewHolder////////////////////////////////
            extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView imageView;
        private TextView textView;

        CustomViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            imageView = (ImageView) view.findViewById(R.id.item_grid_image);
        }

        public void bindData(int position) {
            try {
                new DownloadImageTask(imageView).execute(arrayList.get(position));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        /*public void bindData1(int position) {
            imageView.setImageResource(iv_int[position]);//this also haas to be removed after we have database
        }*/
        @Override
        public void onClick(View view) {
            slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            //img_from_grid=(ImageView)findViewById(R.id.image_from_grid);
            FrameLayout canvas=(FrameLayout)findViewById(R.id.canvasView);
            StickerImageView iv_sticker = new StickerImageView(customerActivity.this);
            //iv_sticker.setImageDrawable(getResources().getDrawable(R.drawable.c10));
            //canvas.addView(iv_sticker);
            //img2_from_grid=(ImageView)findViewById(R.id.image2_from_grid);
            int k=getAdapterPosition();

            //if(img_from_grid.getDrawable()==null) {
                try {
                    new DownloadImageTask(img_from_grid).execute(arrayList.get(k));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                /*img_from_grid.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        drag(v, event);
                        return true;
                    }
                });*/
                //img2_from_grid.bringToFront();
            //}
            }
           /* public void drag(View v,MotionEvent event)
            {
                ImageView view = (ImageView) v;
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        savedMatrix.set(matrix);
                        start.set(event.getX(), event.getY());
                        mode = DRAG;
                        lastEvent = null;
                        break;
                    case MotionEvent.ACTION_POINTER_DOWN:
                        oldDist = spacing(event);
                        if (oldDist > 10f) {
                            savedMatrix.set(matrix);
                            midPoint(mid, event);
                            mode = ZOOM;
                        }
                        lastEvent = new float[4];
                        lastEvent[0] = event.getX(0);
                        lastEvent[1] = event.getX(1);
                        lastEvent[2] = event.getY(0);
                        lastEvent[3] = event.getY(1);
                        d = rotation(event);
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_POINTER_UP:
                        mode = NONE;
                        lastEvent = null;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (mode == DRAG) {
                            matrix.set(savedMatrix);
                            float dx = event.getX() - start.x;
                            float dy = event.getY() - start.y;
                            matrix.postTranslate(dx, dy);
                        } else if (mode == ZOOM) {
                            float newDist = spacing(event);
                            if (newDist > 10f) {
                                matrix.set(savedMatrix);
                                float scale = (newDist / oldDist);
                                matrix.postScale(scale, scale, mid.x, mid.y);
                            }
                            if (lastEvent != null && event.getPointerCount() == 3) {
                                newRot = rotation(event);
                                float r = newRot - d;
                                float[] values = new float[9];
                                matrix.getValues(values);
                                float tx = values[2];
                                float ty = values[5];
                                float sx = values[0];
                                float xc = (view.getWidth() / 2) * sx;
                                float yc = (view.getHeight() / 2) * sx;
                                matrix.postRotate(r, tx + xc, ty + yc);
                            }
                        }
                        break;
                }
                view.setImageMatrix(matrix);
            }
            private float spacing(MotionEvent event) {
                float x = event.getX(0) - event.getX(1);
                float y = event.getY(0) - event.getY(1);
                return (float) (Math.sqrt((x * x + y * y)));
                //return 2;
            }
            private void midPoint(PointF point, MotionEvent event) {
                float x = event.getX(0) + event.getX(1);
                float y = event.getY(0) + event.getY(1);
                point.set(x / 2, y / 2);
            }
            private float rotation(MotionEvent event) {
                double delta_x = (event.getX(0) - event.getX(1));
                double delta_y = (event.getY(0) - event.getY(1));
                double radians = Math.atan2(delta_y, delta_x);
                return (float) Math.toDegrees(radians);
            }*/
    }

    class CustomAdapter_recycler extends RecyclerView.Adapter<CustomViewHolder> {////////////////

        @Override
        public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_recycler_images, parent, false);
            return new CustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(CustomViewHolder holder, int position) {
            holder.bindData(position);
        }

        @Override
        public int getItemCount() {
            return arrayList.size();//thi sarray has to be removed after database
        }
    }

    public class DrawerAdapter extends RecyclerView.Adapter<DrawerAdapter.DrawerViewHolder> {
        private ArrayList<DrawerItem> drawerMenuList;

        public DrawerAdapter(ArrayList<DrawerItem> drawerMenuList) {
            this.drawerMenuList = drawerMenuList;
        }

        @Override
        public DrawerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view;
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_kart, parent, false);
            return new DrawerViewHolder(view);
        }

        @Override
        public void onBindViewHolder(DrawerViewHolder holder, int position) {
            // holder.title.setText(drawerMenuList.get(position).getTitle());
            holder.icon.setImageResource(drawerMenuList.get(position).getIcon());
        }

        @Override
        public int getItemCount() {
            return drawerMenuList.size();
        }

        class DrawerViewHolder extends RecyclerView.ViewHolder {
            TextView title;
            ImageView icon;

            public DrawerViewHolder(View itemView) {
                super(itemView);
                //title = (TextView) itemView.findViewById(R.id.title);
                icon = (ImageView) itemView.findViewById(R.id.imageView_kart);
            }
        }
    }

    public class DrawerItem {
        private int icon;

        //private String title;
        public int getIcon() {
            return icon;
        }

        public void setIcon(int icon) {
            this.icon = icon;
        }
    }

    void reflect() {
        //indicator = (CircleIndicator) findViewById(R.id.indicator);
        FadingIndicator indicator = (FadingIndicator) findViewById(R.id.circleIndicator);
        vp.setAdapter(new CustomAdapter(getSupportFragmentManager(), getApplicationContext()));
        indicator.setViewPager(vp);
        //Set fill color
        indicator.setFillColor(Color.RED);
//Set stroke color
        indicator.setStrokeColor(Color.BLACK);
//Set radius of indicator
        indicator.setRadius(10f);
//Set shape of indicator
        indicator.setShape("circle");
        //indicator.setViewPager(vp);



        RecyclerView rv = (RecyclerView) findViewById(R.id.recyclerView_grid_slider);//////////////
        rv.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        slidingLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        slidingLayout.setPanelHeight(0);
        slidingLayout.setPanelSlideListener(onSlideListener());
        slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
        rv.setAdapter(new CustomAdapter_recycler());

    }

    class FetchUrl extends AsyncTask<String, String, JSONArray> {
        private Context context;
        public TaskDelegate delegate;

        public FetchUrl(Context context) {
            this.context = context;
        }

        @Override
        protected JSONArray doInBackground(String... params) {
            OkHttpClient client = new OkHttpClient();
            RequestBody body = new FormBody.Builder()
                    .add("catagory",params[0])
                    .build();
            Request request = new Request.Builder()
                    .url("http://agenta1.pythonanywhere.com/getalldesign/")
                    .post(body)
                    .build();
            Response response = null;
            try {
                response = client.newCall(request).execute();
                String jsonData = response.body().string();
                try {
                    Log.d("debugkaro", jsonData);
                    JSONArray jsonArray = new JSONArray(jsonData);
                    return jsonArray;
                } catch (JSONException e) {
                    Log.d("debugkaro", "plplplpl");

                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONArray res) {
            if(res == null)
            {
                Toast.makeText(getApplicationContext(),"Error Occured",Toast.LENGTH_LONG).show();
            return;
            }
            super.onPostExecute(res);
            String result = "";
            arrayList.clear();
            JSONObject jsonObject = new JSONObject();
            for (int i = 0; i < res.length(); i++) {
                try {
                    Log.d("debugkaro", i + "");
                    jsonObject = res.getJSONObject(i);
                    arrayList.add(jsonObject.getString("imageUrl"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            reflect();
            Toast.makeText(context, result, Toast.LENGTH_LONG).show();
            if (result.equals("success")) {
                delegate.sendData(result);
            }
            //  SignUpActivity.progressDialog.dismiss();
        }
    }
    class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;
        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            }
            catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            Bitmap scaledBitmap = scaleDown(result,256, true);
            bmImage.setImageBitmap(scaledBitmap);
            Log.i("qwertyuiop","IAM HERE");

        }
        public  Bitmap scaleDown(Bitmap realImage, float maxImageSize,
                                       boolean filter) {
            float ratio = Math.min(
                    (float) maxImageSize / realImage.getWidth(),
                    (float) maxImageSize / realImage.getHeight());
            int width = Math.round((float) ratio * realImage.getWidth());
            int height = Math.round((float) ratio * realImage.getHeight());

            Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width,
                    height, filter);
            return newBitmap;
        }
    }
    class FetchCatagory extends AsyncTask<String,String,String>
    {
        @Override
        protected String doInBackground(String... params) {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("http://agenta1.pythonanywhere.com/getcatagory/")
                    .get()
                    .build();
            try
            {
                Response response;
                response = client.newCall(request).execute();
                String data= response.body().string();
                Log.d("checkingForImageUpload",data);
                return data;
            }
            catch( Exception e)
            {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            try
            {
                JSONArray jsonArray = new JSONArray(s);
                List<String> list = new ArrayList<>();
                for(int i=0;i<jsonArray.length();i++)
                {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    list.add(jsonObject.getString("typeProduct"));
                }
                spinner.setItems(list);
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
            super.onPostExecute(s);
        }
    }
      }
/*

 */