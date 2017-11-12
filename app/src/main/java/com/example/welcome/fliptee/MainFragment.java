package com.example.welcome.fliptee;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import static com.facebook.FacebookSdk.getApplicationContext;
public class MainFragment extends Fragment implements TaskDelegate {
    private TextView mtextdetails;
    private CallbackManager mCallbackmanager;
    private AccessTokenTracker mTokenTracker;
    private ProfileTracker mProfileTracker;
    String email,name;
    @Override
    public void sendData(String result) {
    }
    // mtextdetails = (TextView)v.findViewById(R.id.text_details);
    private String constructWelcomeMessage(Profile profile) {
        StringBuffer stringBuffer = new StringBuffer();
        if (profile != null) {
            stringBuffer.append("Welcome " + profile.getName());
        }
        return stringBuffer.toString();
    }
    private FacebookCallback<LoginResult> mCallback =new FacebookCallback<LoginResult>()
    {
        @Override
        public void onSuccess(LoginResult loginResult) {
            AccessToken accessToken = loginResult.getAccessToken();
            Profile profile = Profile.getCurrentProfile();
            displayWelcomeMessage(profile);
        }
        @Override
        public void onCancel() {
        }
        @Override
        public void onError(FacebookException error) {
        }
    };
    public MainFragment() {
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        mCallbackmanager= CallbackManager.Factory.create();
        AccessTokenTracker tracker=new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
            }
        };
        ProfileTracker profileTracker=new ProfileTracker(){
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
            }
        };
        tracker.startTracking();
        profileTracker.startTracking();
    }
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState)
    {
        View v=inflater.inflate(R.layout.fragment_main,container,false);
        mtextdetails = (TextView)v.findViewById(R.id.text_details);
        return v;
    }
    private void displayWelcomeMessage(Profile profile)
    {
        if(profile != null) {
            //  mtextdetails.setText("Welcome" + profile.getName());
            email=profile.getId();
            name=profile.getName();
            CreateUserFb createUserFb = new CreateUserFb(getApplicationContext());
            createUserFb.delegate=this;
            createUserFb.execute(email,name);
            //nametextView.setText(acc.getDisplayName());
            //emailTextView.setText(acc.getEmail());
            Toast.makeText(getActivity(),"passed",Toast.LENGTH_LONG).show();
            Intent i=new Intent(getActivity(),designer_customer_Activity.class);
            startActivity(i);
        }
        else
        {
            Toast.makeText(getActivity(),"failedddddddd",Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onViewCreated(View view,Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        LoginButton loginButton=(LoginButton) view.findViewById(R.id.login_button);
        loginButton.setReadPermissions("user_friends");
        loginButton.setFragment(this);
        loginButton.registerCallback(mCallbackmanager,mCallback);
    }
    @Override
    public void onResume()
    {
        super.onResume();
        Profile profile= Profile.getCurrentProfile();
        displayWelcomeMessage(profile);
    }
    /*@Override
    public  void onStop()
    {
        super.onStop();
        mTokenTracker.stopTracking();
        mProfileTracker.stopTracking();
    }*/
    @Override
    public void onActivityResult(int requestcode, int resultcode, Intent data)
    {
        super.onActivityResult(requestcode,resultcode,data);
        mCallbackmanager.onActivityResult(requestcode,resultcode,data);
    }
}
class CreateUserFb extends AsyncTask<String,String,JSONObject>
{
    private Context context;
    public TaskDelegate delegate;
    public CreateUserFb(Context context) {
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
            delegate.sendData(result);
        }
        //  SignUpActivity.progressDialog.dismiss();
    }
}