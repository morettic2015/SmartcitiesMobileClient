package view.infoseg.morettic.com.br.infosegapp.view;

/**
 * Created by LuisAugusto on 10/05/2016.
 */


        import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.google.firebase.crash.FirebaseCrash;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import view.infoseg.morettic.com.br.infosegapp.R;
import view.infoseg.morettic.com.br.infosegapp.util.HttpUtil;

import static view.infoseg.morettic.com.br.infosegapp.actions.AssyncUploadURLlink.UPLOAD_URL;
import static view.infoseg.morettic.com.br.infosegapp.util.HttpFileUpload.uploadFile;
import static view.infoseg.morettic.com.br.infosegapp.util.HttpUtil.getBitmapFromURL;
import static view.infoseg.morettic.com.br.infosegapp.util.HttpUtil.getImageUri;
import static view.infoseg.morettic.com.br.infosegapp.util.HttpUtil.getJSONFromUrl;
import static view.infoseg.morettic.com.br.infosegapp.util.HttpUtil.getRealPathFromURI;
import static view.infoseg.morettic.com.br.infosegapp.util.HttpUtil.getSaveImagePath;
import static view.infoseg.morettic.com.br.infosegapp.util.HttpUtil.getSaveUpdateProfile;
import static view.infoseg.morettic.com.br.infosegapp.util.ValueObject.AUTENTICADO;
import static view.infoseg.morettic.com.br.infosegapp.util.ValueObject.AVATAR_BITMAP;
import static view.infoseg.morettic.com.br.infosegapp.util.ValueObject.ID_PROFILE;
import static view.infoseg.morettic.com.br.infosegapp.util.ValueObject.LOGIN;
import static view.infoseg.morettic.com.br.infosegapp.util.ValueObject.MY_PREFERENCES;
import static view.infoseg.morettic.com.br.infosegapp.util.ValueObject.UPLOAD_AVATAR;
import static view.infoseg.morettic.com.br.infosegapp.util.ValueObject.UPLOAD_AVATAR_TOKEN;
import static view.infoseg.morettic.com.br.infosegapp.util.ValueObject.URL_SUBMIT_UPLOAD;
import static view.infoseg.morettic.com.br.infosegapp.view.InfosegMain.logException;

public class ActivityGPlus extends Activity implements OnClickListener,
        ConnectionCallbacks, OnConnectionFailedListener {

    private static final int RC_SIGN_IN = 0;
    // Logcat tag
    private static final String TAG = "MainActivity";

    // Profile pic image size in pixels
    private static final int PROFILE_PIC_SIZE = 200;

    // Google client to interact with Google API
    private GoogleApiClient mGoogleApiClient;

    /**
     * A flag indicating that a PendingIntent is in progress and prevents us
     * from starting further intents.
     */
    private boolean mIntentInProgress;

    private boolean mSignInClicked;

    private ConnectionResult mConnectionResult;

    private SignInButton btnSignIn;
   // private Button btnSignOut, btnRevokeAccess;
    private ImageView imgProfilePic;
    private TextView txtName, txtEmail;
    private LinearLayout llProfileLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_plus);

        btnSignIn = (SignInButton) findViewById(R.id.bt_Login_google_plus);
        imgProfilePic = (ImageView) findViewById(R.id.imgProfilePic);
        txtName = (TextView) findViewById(R.id.txtName);
        txtEmail = (TextView) findViewById(R.id.txtEmail);
        llProfileLayout = (LinearLayout) findViewById(R.id.llProfile);

        // Button click listeners
        btnSignIn.setOnClickListener(this);
      //  btnSignOut.setOnClickListener(this);
      //  btnRevokeAccess.setOnClickListener(this);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN).build();
    }

    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    /**
     * Method to resolve any signin errors
     * */
    private void resolveSignInError() {
        if (mConnectionResult.hasResolution()) {
            try {
                mIntentInProgress = true;
                mConnectionResult.startResolutionForResult(this, RC_SIGN_IN);
            } catch (SendIntentException e) {
                FirebaseCrash.log(e.toString());
                mIntentInProgress = false;
                mGoogleApiClient.connect();
            }
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        if (!result.hasResolution()) {
            GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), this,
                    0).show();
            return;
        }

        if (!mIntentInProgress) {
            // Store the ConnectionResult for later usage
            mConnectionResult = result;

            if (mSignInClicked) {
                // The user has already clicked 'sign-in' so we attempt to
                // resolve all
                // errors until the user is signed in, or they cancel.
                resolveSignInError();
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int responseCode,
                                    Intent intent) {
        if (requestCode == RC_SIGN_IN) {
            if (responseCode != RESULT_OK) {
                mSignInClicked = false;
            }

            mIntentInProgress = false;

            if (!mGoogleApiClient.isConnecting()) {
                mGoogleApiClient.connect();
            }
        }
    }

    @Override
    public void onConnected(Bundle arg0) {
        mSignInClicked = false;
        Toast.makeText(this, R.string.google_connected, Toast.LENGTH_LONG).show();

        // Get user's information
        getProfileInformation();

        // Update the UI after signin
        updateUI(true);

    }

    /**
     * Updating the UI, showing/hiding buttons and profile layout
     * */
    private void updateUI(boolean isSignedIn) {
        if (isSignedIn) {
            btnSignIn.setVisibility(View.GONE);

            llProfileLayout.destroyDrawingCache();
        } else {
            btnSignIn.setVisibility(View.VISIBLE);

            llProfileLayout.setVisibility(View.GONE);
        }
    }

    /**
     * Fetching user's information name, email, profile pic
     * */
    private void getProfileInformation() {
        try {
            if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
                Person currentPerson = Plus.PeopleApi
                        .getCurrentPerson(mGoogleApiClient);
                String personName = currentPerson.getDisplayName();
                String personPhotoUrl = currentPerson.getImage().getUrl();
                //String personGooglePlusProfile = currentPerson.getUrl();
                String email = Plus.AccountApi.getAccountName(mGoogleApiClient);

                txtName.setText(personName);
                txtEmail.setText(email);

                // by default the profile url gives 50x50 px image only
                // we can replace the value with whatever dimension we want by
                // replacing sz=X
                personPhotoUrl = personPhotoUrl.substring(0,
                        personPhotoUrl.length() - 2)
                        + PROFILE_PIC_SIZE;

                new LoadProfileImage(imgProfilePic).execute(personPhotoUrl);

                AssyncSaveGPLUSProfile assyncSaveGPLUSProfile = new AssyncSaveGPLUSProfile(currentPerson,email,this);
                assyncSaveGPLUSProfile.execute();


            } else {
                Toast.makeText(getApplicationContext(), R.string.person_info_null, Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            FirebaseCrash.log(e.toString());
        }
    }

    @Override
    public void onConnectionSuspended(int arg0) {
        mGoogleApiClient.connect();
        updateUI(false);
    }


    /**
     * Button on click listener
     * */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_Login_google_plus:
                // Signin button clicked
                signInWithGplus();
                break;
        }
    }

    /**
     * Sign-in into google
     * */
    private void signInWithGplus() {
        if (!mGoogleApiClient.isConnecting()) {
            mSignInClicked = true;
            resolveSignInError();
        }
    }




    /**
     * Background Async task to load user profile picture from url
     * */
    private class LoadProfileImage extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public LoadProfileImage(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                FirebaseCrash.report(e);
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

}

class AssyncSaveGPLUSProfile extends AsyncTask<JSONObject, Void, String> {

    private Person user;
    private Activity act;
    private ProgressDialog dialog;
    private String email;


    public AssyncSaveGPLUSProfile(Person user,String email, Activity a) {
        this.user = user;
        this.act = a;
        this.dialog = new ProgressDialog(a);
        this.email = email;

    }

    @Override
    protected void onPreExecute() {
        this.dialog.setMessage(act.getString(R.string.autenticando));
        this.dialog.show();
    }

    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        dialog.dismiss();
        if (LOGIN != null)
            LOGIN.dismissAllowingStateLoss();

        this.act.finish();
    }

    protected String doInBackground(JSONObject... urls) {
        Bitmap btm = null;
        JSONObject js = null;
        try {
            btm = getBitmapFromURL(user.getImage().getUrl());

            js = getJSONFromUrl(UPLOAD_URL);

            URL_SUBMIT_UPLOAD = js.getString("uploadPath");

            Uri tempUri = getImageUri(act.getApplicationContext(), btm);

            String realPathInSO = getRealPathFromURI(tempUri, act);

            js = uploadFile(realPathInSO, URL_SUBMIT_UPLOAD, realPathInSO.substring(realPathInSO.length() - 3, realPathInSO.length()));

            js = getJSONFromUrl(getSaveImagePath(js.getString("fName"), js.getString("token")));

            UPLOAD_AVATAR = js.getString("key");
            UPLOAD_AVATAR_TOKEN = js.getString("token");

            String pass = java.util.UUID.randomUUID().toString().substring(0, 8);

            js = getJSONFromUrl(getSaveUpdateProfile(email, UPLOAD_AVATAR, user.getDisplayName(), "xxx.xxx.xxx-xx", "00000-000", pass, "", true, user.getBirthday()==null?"dd/MM/yyyy":user.getBirthday(), "-1"));

            ID_PROFILE = js.getString("key");

            SharedPreferences.Editor editor = MY_PREFERENCES.edit();

            editor.putString("nome", user.getDisplayName()).commit();
            editor.putString("cpfCnpj", "xxx.xxx.xxx-xx").commit();
            editor.putString("cep", "00000-000").commit();
            editor.putString("passwd", pass).commit();
            editor.putString("complemento", "").commit();
            editor.putBoolean("pjf", true).commit();
            editor.putString("nasc", user.getBirthday()==null?"dd/MM/yyyy":user.getBirthday()).commit();
            editor.putString("email", email).commit();
            editor.putString("id", ID_PROFILE).commit();
            editor.putString("avatar", UPLOAD_AVATAR).commit();


            AUTENTICADO = true;
            AVATAR_BITMAP = HttpUtil.getResizedBitmap(btm, 200, 200);


        } catch (UnsupportedEncodingException ex) {
            logException(ex);
        } catch (JSONException ex) {
            logException(ex);
        } catch (IOException ex) {
            logException(ex);
        } catch (Exception ex) {
            logException(ex);
        } finally {
            return js.toString();
        }
    }

}
