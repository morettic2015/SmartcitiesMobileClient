package view.infoseg.morettic.com.br.infosegapp.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.models.User;
import com.twitter.sdk.android.core.services.StatusesService;
import com.twitter.sdk.android.tweetui.TimelineResult;
import com.twitter.sdk.android.tweetui.UserTimeline;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.fabric.sdk.android.Fabric;
import view.infoseg.morettic.com.br.infosegapp.R;

import static view.infoseg.morettic.com.br.infosegapp.actions.AssyncUploadURLlink.UPLOAD_URL;
import static view.infoseg.morettic.com.br.infosegapp.util.HttpFileUpload.uploadFile;
import static view.infoseg.morettic.com.br.infosegapp.util.HttpUtil.getBitmapFromURL;
import static view.infoseg.morettic.com.br.infosegapp.util.HttpUtil.getImageUri;
import static view.infoseg.morettic.com.br.infosegapp.util.HttpUtil.getJSONFromUrl;
import static view.infoseg.morettic.com.br.infosegapp.util.HttpUtil.getRealPathFromURI;
import static view.infoseg.morettic.com.br.infosegapp.util.HttpUtil.getSaveImagePath;
import static view.infoseg.morettic.com.br.infosegapp.util.HttpUtil.getSaveUpdateProfile;
import static view.infoseg.morettic.com.br.infosegapp.util.ValueObject.*;
import static view.infoseg.morettic.com.br.infosegapp.util.ValueObject.UPLOAD_AVATAR;
import static view.infoseg.morettic.com.br.infosegapp.util.ValueObject.UPLOAD_AVATAR_TOKEN;
import static view.infoseg.morettic.com.br.infosegapp.util.ValueObject.URL_SUBMIT_UPLOAD;

/**
 * Created by LuisAugusto on 06/05/2016.
 */
public class TwitterUtil {

    public static TwitterLoginButton loginButton;

    /**
     *
     *
     * */
    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    public static final String TWITTER_KEY = "NTFckkl2FqWiIzBiirbNMEQSm";
    public static final String TWITTER_SECRET = "WpqFTSWI4fLIGXJvtpREKRaV0UR0NJy2O1qNwMAnJy6qTAKAN1";
    private static Activity act;
    // private static TwitterSession ts;

    public static void initTwitterConfig(final Activity activity) {
        act = activity;
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(activity, new Twitter(authConfig));
        loginButton = (TwitterLoginButton) activity.findViewById(R.id.twitter_login_button);
        loginButton.setCallback(
                new Callback<TwitterSession>() {
                    @Override
                    public void success(Result<TwitterSession> result) {
                        getTwitterData();
                    }

                    @Override
                    public void failure(TwitterException exception) {
                        exception.printStackTrace();
                    }
                }

        );
    }

    public static void onActResult(int requestCode, int resultCode, Intent data) {
        loginButton.onActivityResult(requestCode, resultCode, data);
    }

    public static final boolean click() {
        return loginButton.callOnClick();
    }

    public static void getTwitterData() {
        TwitterSession session = Twitter.getSessionManager().getActiveSession();
        Twitter.getApiClient(session).getAccountService().verifyCredentials(true, false, new Callback<User>() {
            @Override
            public void success(Result<User> userResult) {
                User user = userResult.data;
                AssyncSaveTwitterProfile assyncSaveTwitterProfile = new AssyncSaveTwitterProfile(user, act);
                assyncSaveTwitterProfile.execute();
            }

            @Override
            public void failure(TwitterException e) {
                e.printStackTrace();
            }
        });

    }
}

class AssyncSaveTwitterProfile extends AsyncTask<JSONObject, Void, String> {

    private User user;
    private Activity act;
    private ProgressDialog dialog;

    public AssyncSaveTwitterProfile(User user, Activity a) {
        this.user = user;
        this.act = a;
        this.dialog = new ProgressDialog(a);

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
            LOGIN.dismiss();
    }

    protected String doInBackground(JSONObject... urls) {
        Bitmap btm = null;
        JSONObject js = null;
        try {
            btm = getBitmapFromURL(user.profileImageUrl);

            js = getJSONFromUrl(UPLOAD_URL);

            URL_SUBMIT_UPLOAD = js.getString("uploadPath");

            Uri tempUri = getImageUri(act.getApplicationContext(), btm);

            String realPathInSO = getRealPathFromURI(tempUri, act);

            js = uploadFile(realPathInSO, URL_SUBMIT_UPLOAD, realPathInSO.substring(realPathInSO.length() - 3, realPathInSO.length()));

            js = getJSONFromUrl(getSaveImagePath(js.getString("fName"), js.getString("token")));

            UPLOAD_AVATAR = js.getString("key");
            UPLOAD_AVATAR_TOKEN = js.getString("token");

            String email = user.email == null ? user.screenName + "@twitter.com" : user.email;
            String pass = java.util.UUID.randomUUID().toString().substring(0, 8);

            js = getJSONFromUrl(getSaveUpdateProfile(email, UPLOAD_AVATAR, user.name, "xxx.xxx.xxx-xx", "00000-000", pass, user.description, true, "dd/MM/yyyy", "-1"));

            ID_PROFILE = js.getString("key");

            SharedPreferences.Editor editor = MY_PREFERENCES.edit();

            editor.putString("nome", user.name).commit();
            editor.putString("cpfCnpj", "xxx.xxx.xxx-xx").commit();
            editor.putString("cep", "00000-000").commit();
            editor.putString("passwd", pass).commit();
            editor.putString("complemento", user.description).commit();
            editor.putBoolean("pjf", true).commit();
            editor.putString("nasc", "dd/MM/yyyy").commit();
            editor.putString("email", email).commit();
            editor.putString("id", ID_PROFILE).commit();
            editor.putString("avatar", UPLOAD_AVATAR).commit();


            AUTENTICADO = true;
            AVATAR_BITMAP = HttpUtil.getResizedBitmap(btm, 200, 200);

            tempUri = null;
            realPathInSO = null;
            tempUri = null;
            /////CARRRREEEGGGGGA TWEEETSSS de 5 dias atras...
          /*  Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, -5);

            UserTimeline userTimeline = new UserTimeline.Builder().screenName(user.screenName).build();
            userTimeline.next(cal.getTimeInMillis(), new Callback<TimelineResult<Tweet>>() {
                @Override
                public void success(Result<TimelineResult<Tweet>> result1) {
                    List<Tweet> lTweet = result1.data.items;
                    for (Tweet t1 : lTweet) {
                        if (t1.coordinates != null) {
                            t1.coordinates.getLatitude();
                            t1.coordinates.getLongitude();
                        }
                    }
                }

                @Override
                public void failure(TwitterException exception) {

                }
            });*/


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return js.toString();
        }
    }

}