package view.infoseg.morettic.com.br.infosegapp.actions;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.view.View;

import org.json.JSONObject;

import view.infoseg.morettic.com.br.infosegapp.R;
import view.infoseg.morettic.com.br.infosegapp.util.HttpUtil;
import view.infoseg.morettic.com.br.infosegapp.util.InstanceIdService;
import view.infoseg.morettic.com.br.infosegapp.util.ValueObject;

import static view.infoseg.morettic.com.br.infosegapp.util.ValueObject.ID_PROFILE;
import static view.infoseg.morettic.com.br.infosegapp.util.ValueObject.MY_DEVICE_TOKEN;
import static view.infoseg.morettic.com.br.infosegapp.view.InfosegMain.logException;

/**
 * Created by LuisAugusto on 24/02/2016.
 */
public class AssyncLoginRegister extends AsyncTask<JSONObject, Void, String> {
    //public static final String LOGIN_URL = "http://gaeloginendpoint.appspot.com/upload.exec";
    private ProgressDialog dialog;
    private View a1;
    private String email, senha, desc, imagePath, adrress;
    private SharedPreferences.Editor editor = ValueObject.MY_PREFERENCES.edit();
    String msg;

    @Override
    protected void onPreExecute() {
        try {
            dialog.setMessage(msg);
            dialog.show();
        } catch (Exception ex) {
            //logException(ex);
        }
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        try {
            if (dialog.isShowing() || ValueObject.AUTENTICADO) {
                dialog.dismiss();
                if (ValueObject.LOGIN != null)
                    ValueObject.LOGIN.dismiss();
            }
        } catch (Exception ex) {
            logException(ex);
        } finally {
            email = null;
            senha = null;
            dialog = null;
            editor = null;
        }
    }

    public AssyncLoginRegister(Context ctx, String login, String senha,String desc, String imagePath, String adrress) {
        this.dialog = new ProgressDialog(ctx);
        msg = ctx.getString(R.string.autenticando);
        this.email = login;
        this.senha = senha;
        this.adrress = adrress;
        this.imagePath = imagePath;
        this.desc = desc;
        this.adrress = adrress;
        //builder = new AlertDialog.Builder(this.a1.getContext());
    }
    public AssyncLoginRegister(Context ctx, String login, String senha) {
        this.dialog = new ProgressDialog(ctx);
        msg = ctx.getString(R.string.autenticando);
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

                //Update device ID for push notifications
                MY_DEVICE_TOKEN = InstanceIdService.getToken();
                this.editor.putString("DEVICE_TYPE", MY_DEVICE_TOKEN);
                HttpUtil.getJSONFromUrl(HttpUtil.getDeviceRegister(MY_DEVICE_TOKEN, "ANDROID", ID_PROFILE));

                ValueObject.AUTENTICADO = true;
                try {
                    ValueObject.AVATAR_BITMAP = HttpUtil.getBitmapFromURLBlobKey(js.getString("avatar"));
                    ValueObject.AVATAR_BITMAP = HttpUtil.getResizedBitmap(ValueObject.AVATAR_BITMAP, 200, 200);
                } catch (Exception ex) {
                    logException(ex);
                }
                //ValueObject.LOGIN.dismiss();
            }


        } catch (Exception ex) {
            logException(ex);
            js = new JSONObject();
            // ValueObject.URL_SUBMIT_UPLOAD = null;

            //e.printStackTrace();
        } finally {

            return js.toString();
        }
    }


}