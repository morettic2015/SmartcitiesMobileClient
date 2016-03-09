package view.infoseg.morettic.com.br.infosegapp.actions;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
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

/**
 * Created by LuisAugusto on 24/02/2016.
 */
public class AssyncLoginRegister extends AsyncTask<JSONObject, Void, String> {
    public static final String LOGIN_URL = "http://gaeloginendpoint.appspot.com/upload.exec";
    private ProgressDialog dialog;
    private View a1;
    private String email, senha;
    private SharedPreferences.Editor editor = ValueObject.MY_PREFERENCES.edit();


    @Override
    protected void onPreExecute() {
        dialog.setMessage("Autenticando...");
        dialog.show();
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
        if (ValueObject.AUTENTICADO) {
            ValueObject.LOGIN.dismiss();
        }
    }

    public AssyncLoginRegister(View activity, String login, String senha) {
        this.dialog = new ProgressDialog(activity.getContext());
        this.a1 = activity;
        this.email = login;
        this.senha = senha;

        //builder = new AlertDialog.Builder(this.a1.getContext());
    }

    protected String doInBackground(JSONObject... urls) {
        JSONObject js = null;
        // Creating new JSON Parser   public static final String getSaveUpdateProfile(String email,String avatar,String nome,String cpfCnpj,String cep,String passwd, String complemento, boolean pjf,String nasc,String id){

        try {


            //Validacao @TODO

            //URL PARA SALVAR O PERFIL.
            String url = HttpUtil.getPathLoginRegister(email, senha);
            //url =
            js = HttpUtil.getJSONFromUrl(url);

            if (js.has("erro")) {
                ValueObject.AUTENTICADO = false;
            } else {

                /**
                 * {
                 nasc: "18/2/2016",
                 cpfCnpj: "028.903.629-14",
                 complemento: "Jsnsns",
                 cep: "88020100",
                 email: "MALACMA@HOTMAIL.COM",
                 nome: "LUIS FERNANDO MISOTO",
                 avatar: 5464390522372096,
                 key: 6272386882076672,
                 pass: "jsjsj" pjf
                 }
                 * */


                ValueObject.ID_PROFILE = js.getString("key");
                ValueObject.UPLOAD_AVATAR = js.getString("avatar");

                this.editor.putString("nome", js.getString("nome")).commit();
                this.editor.putString("cpfCnpj", js.getString("cpfCnpj")).commit();
                this.editor.putString("cep", js.getString("cep")).commit();
                this.editor.putString("passwd", js.getString("pass")).commit();
                this.editor.putString("complemento", js.getString("complemento")).commit();
                this.editor.putBoolean("pjf", Boolean.getBoolean(js.getString("pjf"))).commit();
                this.editor.putString("nasc", js.getString("nasc")).commit();
                this.editor.putString("email", js.getString("email")).commit();
                this.editor.putString("id", ValueObject.ID_PROFILE).commit();
                this.editor.putString("avatar", ValueObject.UPLOAD_AVATAR).commit();


                ValueObject.AUTENTICADO = true;
                try {

                   // url = HttpUtil.getTokenImagemById(ValueObject.UPLOAD_AVATAR);
                   // js = HttpUtil.getJSONFromUrl(url);
                    ValueObject.AVATAR_BITMAP = HttpUtil.getBitmapFromURLBlobKey(js.getString("avatar"));
                    ValueObject.AVATAR_BITMAP = HttpUtil.getResizedBitmap(ValueObject.AVATAR_BITMAP, 96, 96);


                } catch (Exception e) {
                    e.printStackTrace();
                }
                //ValueObject.LOGIN.dismiss();
            }


        } catch (Exception e) {
            js = new JSONObject();
            // ValueObject.URL_SUBMIT_UPLOAD = null;

            //e.printStackTrace();
        } finally {

            return js.toString();
        }
    }


}