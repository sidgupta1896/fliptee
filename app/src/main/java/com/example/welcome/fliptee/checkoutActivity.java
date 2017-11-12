package com.example.welcome.fliptee;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomControls;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.json.JSONObject;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class checkoutActivity extends AppCompatActivity {
    ImageView imageView;
    ZoomControls zoomControls;
    Bitmap bitmap;
    SlidingUpPanelLayout slidingUpPanelLayout;
    Integer Base=500;

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
    Button placeOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        imageView = (ImageView) findViewById(R.id.checkout_image);
        final TextView editsText=(TextView)findViewById(R.id.edits_id);
        final EditText quantityText=(EditText)findViewById(R.id.quantity_id);
        final TextView totalText=(TextView)findViewById(R.id.total_id);
       // final Button sendEmail=(Button)findViewById(R.id.sendEmail);
        quantityText.setText("1");
        Intent intent = getIntent();
        String changes = intent.getStringExtra("Changes");
        final String tempString = intent.getStringExtra("ImageString");
        byte[] encodeByte = Base64.decode(tempString, Base64.DEFAULT);
        bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
        imageView.setImageBitmap(bitmap);
        editsText.setText(changes);
        zoomControls=(ZoomControls)findViewById(R.id.checkout_zoomControls);
        placeOrder = (Button) findViewById(R.id.placeOrder);
        zoomControls.setOnZoomInClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float x = imageView.getScaleX();
                float y = imageView.getScaleY();
                //Log.i("coutplpl","x"+x+"y"+y);
                if(x<2.111 && y<2.11) {
                    imageView.setScaleX((float) (x + 0.3));
                    imageView.setScaleY((float) (y + 0.3));
                }
            }
        });
        zoomControls.setOnZoomOutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float x = imageView.getScaleX();
                float y = imageView.getScaleY();
               // Log.i("coutcout","x"+x+"y"+y);
                if(x>0.70 && y>0.70) {
                    imageView.setScaleX((float) (x - 0.3));
                    imageView.setScaleY((float) (y - 0.3));
                }
            }
        });
        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                drag(v, event);
                return true;
            }
        });

        //Place Order
        placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("LOGIN_GOOGLE",0);
                String email = sharedPreferences.getString("email","");
                String base64ImageData = tempString;
                new PlaceOrder().execute(email,base64ImageData,quantityText.getText().toString(),totalText.getText().toString());
            }
        });



        int total = (Base + (Integer.parseInt(editsText.getText().toString()) * 100))*Integer.parseInt(quantityText.getText().toString());
        totalText.setText("" + total);
        quantityText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(quantityText.getText().length()!=0) {
                    //int total = (Base + (Integer.parseInt(quantityText.getText().toString()) * 100))*Integer.parseInt(quantityText.getText().toString());
                    int total = (Base + (Integer.parseInt(editsText.getText().toString()) * 100))*Integer.parseInt(quantityText.getText().toString());
                    totalText.setText("" + total);
                }
                else
                {
                    Toast.makeText(checkoutActivity.this,"Enter quantity",Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        Display display=getWindowManager().getDefaultDisplay();
        int height=display.getHeight();
        //Log.i("uiui",""+height);
        slidingUpPanelLayout = (SlidingUpPanelLayout) findViewById(R.id.checkout_sliding_layout);
        int image_height=imageView.getMeasuredHeight();
        //Log.i("qwasqwas",""+image_height);
        slidingUpPanelLayout.setPanelHeight(height-600);
        slidingUpPanelLayout.setPanelSlideListener(onSlideListener());
    }

    public void drag(View v,MotionEvent event)
    {
        ImageView view = (ImageView) v;
        //Log.i("sdsdasas","asdadsa");
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
        //Log.i("aszxaszx","aszxasa");
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
    }

    private SlidingUpPanelLayout.PanelSlideListener onSlideListener() {
        return new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View view, float v) {
            }

            @Override
            public void onPanelCollapsed(View view) {
            }

            @Override
            public void onPanelExpanded(View view) {
                Button b = (Button) findViewById(R.id.button_pull_push);
            }

            @Override
            public void onPanelAnchored(View view) {
            }

            @Override
            public void onPanelHidden(View view) {
            }
        };
    }
    class PlaceOrder extends AsyncTask<String,String,String>
    {
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s==null)
            {
                Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
            }
            Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
        }

        @Override
        protected String doInBackground(String... params) {
            OkHttpClient client = new OkHttpClient();
            RequestBody body = new FormBody.Builder()
                    .add("email",params[0])
                    .add("imagestring",params[1])
                    .add("quantity",params[2])
                    .add("total",params[3])
                    .build();
            Log.d("imageData",params[1]);
            Request request = new Request.Builder()
                    .url("http://agenta1.pythonanywhere.com/placeorder/")
                    .post(body)
                    .build();
            try
            {
                Response response;
                response = client.newCall(request).execute();
                String data= response.body().string();
                Log.d("checkingForImageUpload",data);
                JSONObject jsonObject = new JSONObject(data);
                String result = jsonObject.getString("result");
                return result;
            }
            catch( Exception e)
            {

                e.printStackTrace();
            }
            return null;
        }
    }
}
