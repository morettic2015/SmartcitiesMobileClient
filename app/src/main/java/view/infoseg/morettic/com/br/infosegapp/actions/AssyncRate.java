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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;

import view.infoseg.morettic.com.br.infosegapp.util.HttpFileUpload;
import view.infoseg.morettic.com.br.infosegapp.util.HttpUtil;
import view.infoseg.morettic.com.br.infosegapp.util.ValueObject;
import view.infoseg.morettic.com.br.infosegapp.view.InfosegMain;

import static java.net.URLEncoder.*;
import static view.infoseg.morettic.com.br.infosegapp.util.ValueObject.*;
import static view.infoseg.morettic.com.br.infosegapp.util.ValueObject.MY_PREFERENCES;

/**
 * Created by LuisAugusto on 24/02/2016.
 */
public class AssyncRate extends AsyncTask<JSONObject, Void, String> {

    private String rate, idOcorrencia;

    public AssyncRate(String r,String id){
        this.rate = r;
        this.idOcorrencia = id;
    }

    protected String doInBackground(JSONObject... urls) {
        JSONObject js = null;
        try {
            String url = HttpUtil.getRatingUrl(idOcorrencia,rate);
            //url =
            js = HttpUtil.getJSONFromUrl(url);

        } catch (Exception e) {
            js = new JSONObject();
        } finally {
            return js.toString();
        }
    }
}