package com.onemena.app.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.arabsada.news.R;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.onemena.app.config.ConfigUrls;
import com.onemena.base.BaseActicity;
import com.onemena.listener.JsonObjectListener;
import com.onemena.service.PublicService;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.core.models.User;

import java.util.Arrays;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

import static com.onemena.data.UserManager.addUserInfo;

/**
 * Created by WHF on 2016-12-06.
 */

public class LoginActivity extends BaseActicity implements GoogleApiClient.OnConnectionFailedListener {

    @BindView(R.id.textView2)
    SignInButton textView2;


    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;
    @BindView(R.id.login_btn)
    TextView mStatusTextView;
    @BindView(R.id.txt_password)
    EditText txtPassword;
    @BindView(R.id.regist_btn)
    TextView registBtn;
    @BindView(R.id.facebok_login)
    Button facebokLogin;
    @BindView(R.id.downpush)
    ImageView downpush;

    private GoogleApiClient mGoogleApiClient;
    private ProgressDialog mProgressDialog;
    CallbackManager callbackManager;
    private TextView twitter_button;
    private  Twitter twitter;

    TwitterAuthClient twitterAuthClient;
    TwitterSession twitterSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_login);
        ButterKnife.bind(this);
        twitterAuthClient = new TwitterAuthClient();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestScopes(new Scope(Scopes.PLUS_LOGIN))
                .requestEmail()
                .requestId()//B6:28:78:E1:FD:98:E8:F5:81:89:46:AB:D1:0A:0A:96:10:D1:86:9D  com.mysada.news
                .build();
        // ATTENTION: This "addApi(AppIndex.API)"was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .addApi(AppIndex.API).build();
        callbackManager = CallbackManager.Factory.create();

        ((AnimationDrawable) downpush.getBackground()).start();

        twitter_button = (TextView) findViewById(R.id.twitter);
        twitter_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                twitterLogin();
            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();// ATTENTION: This was auto-generated to implement the App Indexing API.
// See https://g.co/AppIndexing/AndroidStudio for more information.
        mGoogleApiClient.connect();


        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            showProgressDialog();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    hideProgressDialog();
                    handleSignInResult(googleSignInResult);
                }
            });
        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.start(mGoogleApiClient, getIndexApiAction());
    }


    @Override
    protected void onStop() {
        super.onStop();// ATTENTION: This was auto-generated to implement the App Indexing API.
// See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(mGoogleApiClient, getIndexApiAction());
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        mGoogleApiClient.disconnect();
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();


            acct.getId();
            acct.getDisplayName();
            acct.getPhotoUrl();
            acct.getEmail();
            LoginOther(acct.getEmail(),acct.getDisplayName(),"google",acct.getPhotoUrl()+"",acct.getId());
            mStatusTextView.setText(acct.getDisplayName());
//            acct.get
//            updateUI(true);
        } else {
            // Signed out, show unauthenticated UI.
//            updateUI(false);
        }
    }


    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }

        twitterAuthClient.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @OnClick({R.id.textView2, R.id.regist_btn, R.id.facebok_login})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.textView2:
                signIn();
                break;
            case R.id.regist_btn:
//                FirebaseAuth.getInstance().signOut();
                break;
            case R.id.facebok_login:
                onfbClick();
                break;
        }
    }



    public void LoginOther(String email,String first_name,String lonin_from,String profile,String user_name){
        HashMap<String ,String> map=new HashMap<>();
        map.put("email",email+"");
        map.put("first_name",first_name+"");
        map.put("login_from",lonin_from+"");
        map.put("profile_photo",profile+"");
        map.put("user_name",user_name+"");
        PublicService.getInstance().postJsonObjectRequest(false, ConfigUrls.LOGIN_THIRD, map, new JsonObjectListener() {
            @Override
            public void onJsonObject(JSONObject obj, Boolean isCacheData) throws Exception {
                JSONObject content = obj.getJSONObject("content");
                addUserInfo("userinfo",content.toJSONString());//存储用户信息
                addUserInfo("User-Token",content.getString("user_token"));
                EventBus.getDefault().post(content);
            }

            @Override
            public void onObjError() {

            }
        });

    }



    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Login Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }



    //Facebook 第三方登录
    private void onfbClick() {

        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email","public_profile"));
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest request=GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback(){

                            @Override
                            public void onCompleted(org.json.JSONObject object, GraphResponse response) {
                                Log.i("loginResult", JSONObject.toJSON(object).toString());
                                String photourl="";
                                try {
                                    if (object.optJSONObject("picture") != null) {
                                        photourl=object.optJSONObject("picture").optJSONObject("data").optString("url");
                                    }
                                }catch (Exception e){}
                                mStatusTextView.setText(object.optString("first_name"));
                                LoginOther(object.optString("email"),object.optString("first_name"),"facebook",photourl,object.optString("id"));
                            }
                        });
                Bundle parameters=new Bundle();
                parameters.putString("fields","id,first_name,last_name,email,gender,birthday,picture");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
    }


    //推特第三方登录
    private void twitterLogin(){
        twitterAuthClient.authorize(this, new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                twitterSession = result.data;
                Twitter.getApiClient(twitterSession).getAccountService().verifyCredentials(true,false).enqueue(new Callback<User>() {
                    @Override
                    public void success(Result<User> result) {
                        User currentUser = result.data;
                        String name =  currentUser.name;
                        String userName = currentUser.screenName;
                        String profilePicture = currentUser.profileImageUrl;
                        String email = currentUser.email;

                        TwitterSession twiiterSession = Twitter.getInstance().core.getSessionManager().getActiveSession();
                        String userId = String.valueOf(twiiterSession.getUserId());
                        Log.d("name",name);
                        Log.d("userName",userName);
                        Log.d("profilePicture",profilePicture);
                        Log.d("userId",userId);
                        LoginOther(email,userName,"twitter",profilePicture,userId);
//                        login.setVisibility(View.GONE);
//                        logout.setVisibility(View.VISIBLE);

                        Toast.makeText(getApplicationContext(),name+"\n"+userName+"\n"+profilePicture+"\n"+userId,Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void failure(TwitterException exception) {

                    }
                });
            }

            @Override
            public void failure(TwitterException exception) {

            }
        });
    }

}
