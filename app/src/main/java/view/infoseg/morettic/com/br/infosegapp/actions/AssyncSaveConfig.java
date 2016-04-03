package view.infoseg.morettic.com.br.infosegapp.actions;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.View;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;

import view.infoseg.morettic.com.br.infosegapp.util.HttpFileUpload;
import view.infoseg.morettic.com.br.infosegapp.util.HttpUtil;
import view.infoseg.morettic.com.br.infosegapp.util.ValueObject;
import view.infoseg.morettic.com.br.infosegapp.view.InfosegMain;

import static java.net.URLEncoder.*;
import static view.infoseg.morettic.com.br.infosegapp.util.ValueObject.MY_PREFERENCES;

/**
 * Created by LuisAugusto on 24/02/2016.
 */
public class AssyncSaveConfig extends AsyncTask<JSONObject, Void, String> {
    public static final String LOGIN_URL = "http://gaeloginendpoint.appspot.com/upload.exec";
    private ProgressDialog dialog;
    private View a1;
    private String idProfile, phone;



    @Override
    protected void onPreExecute() {
        try {
            dialog.setMessage("Salvando as configurações...");
            dialog.show();
        } catch (Exception e) {

        }
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        dialog.dismiss();
    }

    public AssyncSaveConfig(Context ctx, String idProfile, String phone) {
        this.dialog = new ProgressDialog(ctx);
        this.idProfile = idProfile;
        this.phone = phone;

    }

    protected String doInBackground(JSONObject... urls) {
        JSONObject js = null;
        // Creating new JSON Parser   public static final String getSaveUpdateProfile(String idProfile,String avatar,String nome,String cpfCnpj,String cep,String passwd, String complemento, boolean pjf,String nasc,String id){

        try {
            //Validacao @TODO
            StringBuilder sb = new StringBuilder();
            //URL PARA SALVAR O PERFIL.
            sb.append("ehMeu");
            sb.append(":");
            sb.append(MY_PREFERENCES.getBoolean("ehMeu",false));
            sb.append("-");
            sb.append("eMeuEstado");
            sb.append(":");
            sb.append(MY_PREFERENCES.getBoolean("eMeuEstado",false));
            sb.append("-");
            sb.append("ehMinhaCidade");
            sb.append(":");
            sb.append(MY_PREFERENCES.getBoolean("ehMinhaCidade",false));
            sb.append("-");
            sb.append("ehMeuPais");
            sb.append(":");
            sb.append(MY_PREFERENCES.getBoolean("ehMeuPais",false));
            sb.append("-");
            sb.append("saude");
            sb.append(":");
            sb.append(MY_PREFERENCES.getBoolean("saude",false));
            sb.append("-");
            sb.append("transporte");
            sb.append(":");
            sb.append(MY_PREFERENCES.getBoolean("transporte",false));
            sb.append("-");
            sb.append("meioAmbiente");
            sb.append(":");
            sb.append(MY_PREFERENCES.getBoolean("meioAmbiente",false));
            sb.append("-");
            sb.append("educacao");
            sb.append(":");
            sb.append(MY_PREFERENCES.getBoolean("educacao",false));
            sb.append("-");
            sb.append("seguranca");
            sb.append(":");
            sb.append(MY_PREFERENCES.getBoolean("seguranca",false));
            sb.append("-");
            sb.append("politica");
            sb.append(":");
            sb.append(MY_PREFERENCES.getBoolean("politica",false));
            sb.append("-");
            sb.append("upa");
            sb.append(":");
            sb.append(MY_PREFERENCES.getBoolean("upa",false));

            String url = HttpUtil.saveConfigInfo(idProfile, phone,sb.toString());
            //url =
            js = HttpUtil.getJSONFromUrl(url);
        } catch (Exception e) {
            js = new JSONObject();
        } finally {
            return js.toString();
        }
    }


}