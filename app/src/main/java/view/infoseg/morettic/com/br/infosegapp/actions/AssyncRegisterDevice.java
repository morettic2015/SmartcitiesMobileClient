package view.infoseg.morettic.com.br.infosegapp.actions;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.crash.FirebaseCrash;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;

import view.infoseg.morettic.com.br.infosegapp.util.HttpFileUpload;
import view.infoseg.morettic.com.br.infosegapp.util.HttpUtil;
import view.infoseg.morettic.com.br.infosegapp.util.InstanceIdService;
import view.infoseg.morettic.com.br.infosegapp.util.ValueObject;
import view.infoseg.morettic.com.br.infosegapp.view.InfosegMain;

import static java.net.URLEncoder.*;
import static view.infoseg.morettic.com.br.infosegapp.util.ValueObject.*;
import static view.infoseg.morettic.com.br.infosegapp.util.ValueObject.MY_PREFERENCES;
import static view.infoseg.morettic.com.br.infosegapp.view.InfosegMain.logException;

/**
 * Created by LuisAugusto on 24/02/2016.
 */
public class AssyncRegisterDevice extends AsyncTask<String, Void, Void> {

    private SharedPreferences.Editor editor = ValueObject.MY_PREFERENCES.edit();
    private boolean submit = false;

    protected void onPostExecute(String result) {
        try {
            editor.commit();
        } catch (Exception ex) {
            logException(ex);
        }
    }

    protected Void doInBackground(String... urls) {
        MY_DEVICE_TOKEN = MY_PREFERENCES.getString("DEVICE_TYPE", "");

        if (MY_DEVICE_TOKEN.equals("")) {//When is new
            MY_DEVICE_TOKEN = InstanceIdService.getToken();
            editor.putString("DEVICE_TYPE", MY_DEVICE_TOKEN);
            submit = true;
        } else if (!MY_DEVICE_TOKEN.equals(InstanceIdService.getToken())) {//When token chances;
            submit = true;
        } else { //When token exists and is teh same
            submit = false;
        }

        try {
            if (submit) {
                HttpUtil.getJSONFromUrl(HttpUtil.getDeviceRegister(MY_DEVICE_TOKEN, "ANDROID", ID_PROFILE));
            }
        } catch (Exception ex) {
            logException(ex);
        } finally {
            return null;
        }
    }
}