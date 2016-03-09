package view.infoseg.morettic.com.br.infosegapp.actions;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;

import view.infoseg.morettic.com.br.infosegapp.util.HttpFileUpload;
import view.infoseg.morettic.com.br.infosegapp.util.HttpUtil;
import view.infoseg.morettic.com.br.infosegapp.util.ValueObject;
import view.infoseg.morettic.com.br.infosegapp.view.InfosegMain;

import static java.net.URLEncoder.*;

/**
 * Created by LuisAugusto on 24/02/2016.
 */
public class AssyncSaveProfile extends AsyncTask<JSONObject, Void, String> {
    public static final String UPLOAD_URL = "http://gaeloginendpoint.appspot.com/upload.exec";
    private ProgressDialog dialog;
    private View a1;
    private JSONObject perfil;


    @Override
    protected void onPreExecute() {
        dialog.setMessage("Salvando perfil...");
        dialog.show();
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    public AssyncSaveProfile(View activity, JSONObject perfil) {
        this.dialog = new ProgressDialog(activity.getContext());
        this.a1 = activity;
        this.perfil = perfil;
        //builder = new AlertDialog.Builder(this.a1.getContext());
    }

    protected String doInBackground(JSONObject... urls) {
        JSONObject js = null;
        // Creating new JSON Parser   public static final String getSaveUpdateProfile(String email,String avatar,String nome,String cpfCnpj,String cep,String passwd, String complemento, boolean pjf,String nasc,String id){

        try {


            //Pega os campos
            String email = this.perfil.getString("email");
            String avatar = ValueObject.UPLOAD_AVATAR;
            String nome = this.perfil.getString("nome");
            String cpfCnpj = this.perfil.getString("cpfCnpj");
            String cep = this.perfil.getString("cep");
            String passwd = this.perfil.getString("passwd");
            String complemento = this.perfil.getString("complemento");
            boolean pjf = this.perfil.getBoolean("pjf");
            String nasc = this.perfil.getString("nasc");
            String id = this.perfil.getString("id");

            //Validacao @TODO

            //URL PARA SALVAR O PERFIL.
            String url = HttpUtil.getSaveUpdateProfile(email, avatar, nome, cpfCnpj, cep, passwd, complemento, pjf, nasc, id);
            //url =
            js = HttpUtil.getJSONFromUrl(url);

            ValueObject.ID_PROFILE = js.getString("key");

        } catch (Exception e) {
            js = new JSONObject();
            // ValueObject.URL_SUBMIT_UPLOAD = null;

            //e.printStackTrace();
        } finally {
            return js.toString();
        }
    }


}