package com.example.welcome.fliptee;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class Login_google_Activity extends AppCompatActivity implements View.OnClickListener,TaskDelegate, GoogleApiClient.OnConnectionFailedListener {

    private SignInButton signInButton;
    private GoogleSignInOptions gso;
    private GoogleApiClient googleApiClient;
    private TextView nametextView;
    private TextView emailTextView;

    private int RC_SIGN_IN=100;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String email,name;

    @Override
    public void sendData(String result) {
        editor.putString("USER_EMAIL",email);
        editor.putString("USER_NAME",name);
        editor.putBoolean("IS_LOGGED_IN",true);
        Log.i("dodo","prabhat");
        editor.commit();
        String checkemail = currentUserEmail();
        Toast.makeText(getApplicationContext(),checkemail,Toast.LENGTH_LONG).show();
    }
    String currentUserEmail()
    {
        SharedPreferences shap=getSharedPreferences("LOGIN_GOOGLE",MODE_PRIVATE);
        String email = shap.getString("USER_EMAIL","");
        Log.d("chutiyokadebug",email);
        return email;
    }
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        pref = getSharedPreferences("FLIP",0);
        editor=pref.edit();
        //nametextView=(TextView)findViewById(R.id.textName);
        //emailTextView=(TextView)findViewById(R.id.textEmail);

        gso=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();

        signInButton=(SignInButton)findViewById(R.id.button_Google_Signin);
        signInButton.setSize(SignInButton.SIZE_WIDE);
        signInButton.setScopes(gso.getScopeArray());
        googleApiClient=new GoogleApiClient.Builder(this).enableAutoManage(this,this).addApi(Auth.GOOGLE_SIGN_IN_API,gso).build();

        signInButton.setOnClickListener(this);
    }
    private void signIn()
    {
        Intent i=Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(i,RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RC_SIGN_IN)
        {
            GoogleSignInResult result=Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignResult(result);
        }

    }
    private void handleSignResult(GoogleSignInResult result)
    {
        //I WILL DO CHANGES HERE IN IF STATEMENT
        if (result.isSuccess())
        {
            GoogleSignInAccount acc=result.getSignInAccount();
            //nametextView.setText(acc.getDisplayName());
            //emailTextView.setText(acc.getEmail());
            //Toast.makeText(this,"passed",Toast.LENGTH_LONG).show();
            //Intent i=new Intent(Login_google_Activity.this,designer_customer_Activity.class);
            //startActivity(i);
            email = acc.getEmail();
            name = acc.getDisplayName();
            Toast.makeText(Login_google_Activity.this,email,Toast.LENGTH_LONG).show();
            /*CreateUser createUser = new CreateUser(getApplicationContext());
            createUser.delegate=this;
            createUser.execute(email,name);*/
            func();
            //nametextView.setText(acc.getDisplayName());
            //emailTextView.setText(acc.getEmail());
            Toast.makeText(this,"passed",Toast.LENGTH_LONG).show();
            SharedPreferences shap=getSharedPreferences("LOGIN_GOOGLE",MODE_PRIVATE);
            SharedPreferences.Editor editor=shap.edit();
            editor.putInt("login",100);
            editor.putString("email",email);
            editor.commit();
            Intent i=new Intent(Login_google_Activity.this,designer_customer_Activity.class);
            startActivity(i);

        }
        else
        {
            Toast.makeText(this,"failed",Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public void onClick(View view) {
        if(view==signInButton) {
            signIn();
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(this,"connection fail",Toast.LENGTH_LONG).show();
    }
    void func()
    {
        CreateUser createUser = new CreateUser(getApplicationContext());
        createUser.delegate=this;
        createUser.execute(email,name);
    }
}
class CreateUser extends AsyncTask<String,String,JSONObject>
{
    private Context context;
    public TaskDelegate delegate;
    public CreateUser(Context context) {
        this.context=context;
    }
    @Override
    protected JSONObject doInBackground(String... params) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("email",params[0])
                .add("name",params[1])
                .build();
        Request request = new Request.Builder()
                .url("http://agenta1.pythonanywhere.com/register/")
                .post(body)
                .build();
        Response response = null;
        try {
            response= client.newCall(request).execute();
            String jsonData= response.body().string();
            try {
                JSONObject json = new JSONObject(jsonData);
                return json;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    protected void onPostExecute(JSONObject res) {
        super.onPostExecute(res);
        String result = "";
        try {
            result = res.getString("login");
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        Toast.makeText(context,result,Toast.LENGTH_LONG).show();
        if(result.equals("success"))
        {
            Toast.makeText(context,"GOOGLE",Toast.LENGTH_LONG).show();
            delegate.sendData(result);
        }
        //  SignUpActivity.progressDialog.dismiss();
    }
}

