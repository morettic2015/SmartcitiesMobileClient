package view.infoseg.morettic.com.br.infosegapp.actions;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONObject;

import view.infoseg.morettic.com.br.infosegapp.R;
import view.infoseg.morettic.com.br.infosegapp.util.HttpUtil;
import view.infoseg.morettic.com.br.infosegapp.util.ToastHelper;

import static view.infoseg.morettic.com.br.infosegapp.util.ValueObject.MAIN;
import static view.infoseg.morettic.com.br.infosegapp.util.ValueObject.MY_PREFERENCES;
import static view.infoseg.morettic.com.br.infosegapp.view.InfosegMain.logException;

/**
 * Created by LuisAugusto on 24/02/2016.
 */
public class AssyncSaveConfig extends AsyncTask<JSONObject, Void, String> {
    public static final String LOGIN_URL = "http://gaeloginendpoint.appspot.com/upload.exec";
    private ProgressDialog dialog;
    //private View a1;
    private String idProfile, phone;
    private StringBuilder sb;
    private String msg;


    @Override
    protected void onPreExecute() {
        try {
            dialog.setMessage(msg);
            dialog.show();
        } catch (Exception e) {

        }
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        dialog.dismiss();
        sb = null;
        phone = null;
        idProfile = null;
        String msg = MAIN.getApplicationContext().getString(R.string.save_sucess);
        ToastHelper.makeToast(MAIN.getApplicationContext(),msg);
    }

    public AssyncSaveConfig(Context ctx, String idProfile, String phone) {
        this.dialog = new ProgressDialog(ctx);
        this.idProfile = idProfile;
        this.phone = phone;
        this.msg = ctx.getString(R.string.save_config);

    }

    protected String doInBackground(JSONObject... urls) {
        JSONObject js = null;
        // Creating new JSON Parser   public static final String getSaveUpdateProfile(String idProfile,String avatar,String nome,String cpfCnpj,String cep,String passwd, String complemento, boolean pjf,String nasc,String id){

        try {
            //Validacao @TODO
            sb = new StringBuilder();
            //URL PARA SALVAR O PERFIL.
            sb.append("mine");
            sb.append(":");
            sb.append(MY_PREFERENCES.getBoolean("mine",false));
            sb.append("-");
            sb.append("distance");
            sb.append(":");
            sb.append(MY_PREFERENCES.getInt("distance",0));
            sb.append("-");
            sb.append("imoveis");
            sb.append(":");
            sb.append(MY_PREFERENCES.getBoolean("imoveis",false));
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
            sb.append("-");
            sb.append("esporte");
            sb.append(":");
            sb.append(MY_PREFERENCES.getBoolean("esporte",false));

            String url = HttpUtil.saveConfigInfo(idProfile, phone,sb.toString());
            //url =
            js = HttpUtil.getJSONFromUrl(url);
        } catch (Exception ex) {
            logException(ex);
            js = new JSONObject();
        } finally {
            return js.toString();
        }
    }


}