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

import view.infoseg.morettic.com.br.infosegapp.R;
import view.infoseg.morettic.com.br.infosegapp.util.HttpFileUpload;
import view.infoseg.morettic.com.br.infosegapp.util.HttpUtil;
import view.infoseg.morettic.com.br.infosegapp.util.ToastHelper;
import view.infoseg.morettic.com.br.infosegapp.util.ValueObject;
import view.infoseg.morettic.com.br.infosegapp.view.InfosegMain;

import static java.net.URLEncoder.*;
import static view.infoseg.morettic.com.br.infosegapp.util.ValueObject.*;
import static view.infoseg.morettic.com.br.infosegapp.util.ValueObject.MY_PREFERENCES;
import static view.infoseg.morettic.com.br.infosegapp.view.InfosegMain.logException;

/**
 * Created by LuisAugusto on 24/02/2016.
 */
public class AssyncRate extends AsyncTask<JSONObject, Void, String> {

    private String rate, idOcorrencia;

    public AssyncRate(String r,String id){
        this.rate = r;
        this.idOcorrencia = id;
    }
    protected void onPostExecute(String result) {
        this.rate = null;
        this.idOcorrencia = null;
        String msg = MAIN.getApplicationContext().getString(R.string.save_sucess);
        ToastHelper.makeToast(MAIN.getApplicationContext(),msg);
    }

    protected String doInBackground(JSONObject... urls) {
        JSONObject js = null;
        try {
           js = HttpUtil.getJSONFromUrl(HttpUtil.getRatingUrl(idOcorrencia,rate));

        } catch (Exception ex) {
            js = new JSONObject();
            logException(ex);
        } finally {
            //rate = null;
            //idOcorrencia = null;
            return js.toString();
        }
    }
}