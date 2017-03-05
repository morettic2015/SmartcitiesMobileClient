package view.infoseg.morettic.com.br.infosegapp.util;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;

import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONObject;

import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import view.infoseg.morettic.com.br.infosegapp.R;

import static view.infoseg.morettic.com.br.infosegapp.util.HttpFileUpload.uploadFile;
import static view.infoseg.morettic.com.br.infosegapp.util.HttpUtil.getImageUri;
import static view.infoseg.morettic.com.br.infosegapp.util.HttpUtil.getJSONFromUrl;
import static view.infoseg.morettic.com.br.infosegapp.util.HttpUtil.getRealPathFromURI;
import static view.infoseg.morettic.com.br.infosegapp.util.HttpUtil.getSaveImagePath;
import static view.infoseg.morettic.com.br.infosegapp.util.HttpUtil.getSaveUpdateProfile;
import static view.infoseg.morettic.com.br.infosegapp.util.ValueObject.*;
import static view.infoseg.morettic.com.br.infosegapp.util.ValueObject.ID_PROFILE;
import static view.infoseg.morettic.com.br.infosegapp.util.ValueObject.LOGIN;
import static view.infoseg.morettic.com.br.infosegapp.util.ValueObject.MAIN;
import static view.infoseg.morettic.com.br.infosegapp.util.ValueObject.MY_PREFERENCES;
import static view.infoseg.morettic.com.br.infosegapp.util.ValueObject.UPLOAD_AVATAR;
import static view.infoseg.morettic.com.br.infosegapp.util.ValueObject.UPLOAD_AVATAR_TOKEN;
import static view.infoseg.morettic.com.br.infosegapp.util.ValueObject.URL_SUBMIT_UPLOAD;
import static view.infoseg.morettic.com.br.infosegapp.view.InfosegMain.logException;

/**
 * Created by LuisAugusto on 29/08/2016.
 */
public class FacebookUtil {
    /* *************************************
    *              FACEBOOK               *
    ***************************************/
    /* The login button for Facebook */
    private static LoginButton mFacebookLoginButton;
    /* The callback manager for Facebook */
    private static CallbackManager mFacebookCallbackManager;
    /* Used to track user logging in/out off Facebook */

    private static AccessTokenTracker mFacebookAccessTokenTracker;


    public static void initInstanceSdkFacebook(Activity a){
        FacebookSdk.sdkInitialize(a.getApplicationContext());
    }
    public static void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent ) {
        mFacebookCallbackManager.onActivityResult(requestCode, resultCode, imageReturnedIntent);
    }
    public static boolean clickFace(){
        return mFacebookLoginButton.callOnClick();
    }
    public static void initFaceBookProps(Activity a){
        mFacebookCallbackManager = CallbackManager.Factory.create();
        mFacebookLoginButton = (LoginButton)a.findViewById(R.id.facebook_login_button_main);
        mFacebookLoginButton.registerCallback(mFacebookCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override

                            /**
                             *
                             * {"id":"10207525212627414","picture":{"data":{"is_silhouette":false,"url":"https:\/\/fbcdn-profile-a.akamaihd.net\/hprofile-ak-xfp1\/v\/t1.0-1\/p50x50\/12495190_10206598323655769_2243149356129613697_n.jpg?oh=1b1d1a29154e1c8790a4dcaec5668ae3&oe=585884C4&__gda__=1482070959_cee456fbca524e29234397b3eda120ee"}},"email":"malacma@gmail.com","name":"Lam Mxrettx"}
                             * {Response:  responseCode: 200, graphObject: {"id":"10207525212627414","picture":{"data":{"is_silhouette":false,"url":"https:\/\/fbcdn-profile-a.akamaihd.net\/hprofile-ak-xfp1\/v\/t1.0-1\/p50x50\/12495190_10206598323655769_2243149356129613697_n.jpg?oh=1b1d1a29154e1c8790a4dcaec5668ae3&oe=585884C4&__gda__=1482070959_cee456fbca524e29234397b3eda120ee"}}}, error: null}
                             * */
                            public void onCompleted(
                                    JSONObject object,
                                    GraphResponse response) {

                                response.getError();

                                try {
                                    if (android.os.Build.VERSION.SDK_INT > 9) {
                                        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                                        StrictMode.setThreadPolicy(policy);
                                        String profilePicUrl = object.getJSONObject("picture").getJSONObject("data").getString("url");

                                        ToastHelper.makeToast(LOGIN.getActivity().getApplicationContext(),MAIN.getString(R.string.autenticando));

                                        URL fb_url = new URL(profilePicUrl);//small | noraml | large
                                        HttpsURLConnection conn1 = (HttpsURLConnection) fb_url.openConnection();
                                        HttpsURLConnection.setFollowRedirects(true);
                                        conn1.setInstanceFollowRedirects(true);
                                        Bitmap btm = BitmapFactory.decodeStream(conn1.getInputStream());
                                        conn1.disconnect();

                                        //GAE 2 PHASE UPLOAD
                                        JSONObject js = getJSONFromUrl(UPLOAD_URL);
                                        //get upload path to blobstore
                                        URL_SUBMIT_UPLOAD = js.getString("uploadPath");
                                        Uri tempUri = getImageUri(MAIN.getApplicationContext(), btm);
                                        String realPathInSO = getRealPathFromURI(tempUri, MAIN);
                                        //Upload it
                                        js = uploadFile(realPathInSO, URL_SUBMIT_UPLOAD, realPathInSO.substring(realPathInSO.length() - 3, realPathInSO.length()));
                                        js = getJSONFromUrl(getSaveImagePath(js.getString("fName"), js.getString("token")));

                                        UPLOAD_AVATAR = js.getString("key");
                                        UPLOAD_AVATAR_TOKEN = js.getString("token");

                                        ToastHelper.makeToast(LOGIN.getActivity().getApplicationContext(),MAIN.getString(R.string.save_config));

                                        String email = object.getString("email");
                                        String name = object.getString("name");
                                        String birthday = object.has("birthday")?object.getString("birthday"):"dd/MM/yyyy";

                                        String bL = birthday.substring(3,5)+"/"+birthday.substring(0,2)+"/"+birthday.substring(6,10);
                                        String pass = java.util.UUID.randomUUID().toString().substring(0, 8);

                                        js = getJSONFromUrl(getSaveUpdateProfile(email.toUpperCase(), UPLOAD_AVATAR, name.toUpperCase(), "xxx.xxx.xxx-xx", "00000-000", pass, "Facebook profile", true, bL, "-1"));

                                        ID_PROFILE = js.getString("key");


                                        //ToastHelper.makeToast(MAIN.getApplicationContext(),"Atualizando preferencias....");
                                        SharedPreferences.Editor editor = MY_PREFERENCES.edit();

                                        editor.putString("nome", name.toUpperCase()).commit();
                                        editor.putString("cpfCnpj", "xxx.xxx.xxx-xx").commit();
                                        editor.putString("cep", "00000-000").commit();
                                        editor.putString("passwd", pass).commit();
                                        editor.putString("complemento", "Facebook profile").commit();
                                        editor.putBoolean("pjf", true).commit();
                                        editor.putString("nasc", bL).commit();
                                        editor.putString("email", email.toUpperCase()).commit();
                                        editor.putString("id", ID_PROFILE).commit();
                                        editor.putString("avatar", UPLOAD_AVATAR).commit();

                                        AUTENTICADO = true;
                                        ImageCache.addBitmapToMemoryCache("avatar",btm);

                                        conn1 = null;
                                        realPathInSO = null;
                                        btm = null;
                                        profilePicUrl = null;
                                        fb_url = null;
                                        URL_SUBMIT_UPLOAD = null;
                                        realPathInSO = null;
                                        tempUri = null;
                                        email = null;
                                        name = null;
                                        pass = null;
                                        js = null;
                                        bL = null;

                                    }
                                }catch (Exception ex) {
                                    ToastHelper.makeToast(LOGIN.getActivity().getApplicationContext(),MAIN.getString(R.string.erro_cadastro_perfil));
                                    logException(ex);
                                }finally{

                                    if (LOGIN != null)
                                        LOGIN.dismiss();


                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,picture,email,name,birthday");
                request.setParameters(parameters);
                request.executeAsync();

            }

            @Override
            public void onCancel() {
                ToastHelper.makeToast(MAIN.getApplicationContext(),MAIN.getString(R.string.erro_cadastro_perfil));
            }

            @Override
            public void onError(FacebookException e) {
                ToastHelper.makeToast(MAIN.getApplicationContext(),MAIN.getString(R.string.erro_cadastro_perfil));
            }
        });
    }

}
