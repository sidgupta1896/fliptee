package com.example.welcome.fliptee;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.File;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class designerActivity extends AppCompatActivity {
    ImageView iv;
    Button but_gall, cam;
    private static int request = 1;
    String ImageDecode;
    Button but_val;
    String[] FILE;
    String path = "";
    Uri file;
    //String[] file;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_designer);
        iv = (ImageView) findViewById(R.id.image_gallery);
        but_gall = (Button) findViewById(R.id.button_gallery);
        but_gall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, request);
            }

        });
        cam = (Button) findViewById(R.id.camera);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            cam.setEnabled(false);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }
        cam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                file = Uri.fromFile(getOutputMediaFile());
                intent.putExtra(MediaStore.EXTRA_OUTPUT, file);
                startActivityForResult(intent, 100);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == request && resultCode == RESULT_OK && data != null) {
            Uri URI = data.getData();
            String[] FILE = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(URI, FILE, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(FILE[0]);
            ImageDecode = cursor.getString(columnIndex);
            cursor.close();

            try {
                path = getPath(designerActivity.this, URI);
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
            iv.setImageBitmap(BitmapFactory.decodeFile(ImageDecode));
            but_val = (Button) findViewById(R.id.button_validate);
            TextView tv1 = (TextView) findViewById(R.id.text_title);
            tv1.setVisibility(View.VISIBLE);
            but_val.setVisibility(View.VISIBLE);
            but_val.setOnClickListener(new View.OnClickListener() {       //here add to admin database for validation
                @Override
                public void onClick(View v) {
                    String email = currentUserEmail();
                    String filename = getFileName(email);
                    Toast.makeText(getApplicationContext(),email,Toast.LENGTH_LONG).show();
                    Log.i("ememem",email);
                    Log.i("okokasdas",filename);
                    new uploadOrder().execute(email,filename);
                }
            });
        }
        else if (requestCode == 100)
        {
            // if (resultCode == RESULT_OK) {
            /*if(data!=null)
            {
               Uri URI = data.getData();
                String[] FILE = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(URI, FILE, null, null, null);
                cursor.moveToFirst();curer
                int columnIndex = cursor.getColumnIndex(FILE[0]);
                String ImageDecodeCam = cursor.getString(columnIndex);
                cursor.close();
                //iv.setImageURI(file);
                iv.setImageBitmap(BitmapFactory.decodeFile(ImageDecodeCam));
            }
            else
                Toast.makeText(this,"SAVED IN GALLERY",Toast.LENGTH_LONG);*/
        } else {
            Toast.makeText(this, "Could Not Fetch Image", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                cam.setEnabled(true);
            }
        }
    }
    public static String getPath(Context context, Uri uri) throws URISyntaxException {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = { "_data" };
            Cursor cursor = null;

            try {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                // Eat it
            }
        }
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }
    private static File getOutputMediaFile() {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "CameraDemo");

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return new File(mediaStorageDir.getPath() + File.separator +
                "IMG_" + timeStamp + ".jpg");
    }
    class uploadOrder extends AsyncTask<String,String,String>
    {
        ProgressDialog progressDialog ;
        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(designerActivity.this);
            progressDialog.setTitle("Uploading Image");
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
            if(s==null)
            {
                Toast.makeText(getApplicationContext(),"Error in uplaoding image1",Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String result = jsonObject.getString("result");
                    if(result.equals("yes"))
                    {
                        Toast.makeText(getApplicationContext(),"Succesfully Uploaded image",Toast.LENGTH_LONG).show();

                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"Error in uplaoding image2"+result,Toast.LENGTH_LONG).show();
                    }
                }
                catch (Exception e)
                {
                    Toast.makeText(getApplicationContext(),"Error in uplaoding image3",Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(String... params) {
            final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/*");
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("email",params[0])
                    .addFormDataPart("image",params[1], RequestBody.create(MEDIA_TYPE_PNG, new File(path)))
                    .build();
            OkHttpClient okHttpClient = new OkHttpClient();
            Request request = new Request.Builder().url("http://agenta1.pythonanywhere.com/uploaddesign/").post(requestBody).build();
            try
            {
                okhttp3.Response response = okHttpClient.newCall(request).execute();
                return response.body().string();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            return null;
        }
    }
    String getFileName(String userEmail)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd'T'HHmmss");
        String timeStamp = dateFormat.format(new Date());
        String imageFileName = userEmail + timeStamp + ".jpg";
        return imageFileName;
    }
    String currentUserEmail()
    {
        SharedPreferences shap=getSharedPreferences("LOGIN_GOOGLE",MODE_PRIVATE);
        String email = shap.getString("email","");
        // Log.d("chutiyokadebug",email);
        return email;
    }
}


