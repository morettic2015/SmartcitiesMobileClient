package view.infoseg.morettic.com.br.infosegapp.actions;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONObject;

import view.infoseg.morettic.com.br.infosegapp.R;
import view.infoseg.morettic.com.br.infosegapp.util.HttpUtil;
import view.infoseg.morettic.com.br.infosegapp.util.TipoOcorrencia;
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

    private StringBuilder sb;
    private String msg, escolar,about,site,email,idProfile, phone;
    private char sexo;


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

    public AssyncSaveConfig(Context ctx, String idProfile, String phone,char sexo, String escolar, String about,String site, String email) {
        this.dialog = new ProgressDialog(ctx);
        this.idProfile = idProfile;
        this.phone = phone;
        this.sexo = sexo;
        this.escolar = escolar;
        this.about = about;
        this.site = site;
        this.email = email;
        this.msg = ctx.getString(R.string.save_config);

    }

    protected String doInBackground(JSONObject... urls) {
        JSONObject js = null;
        // Creating new JSON Parser   public static final String getSaveUpdateProfile(String idProfile,String avatar,String nome,String cpfCnpj,String cep,String passwd, String complemento, boolean pjf,String nasc,String id){

        try {
            //Validacao @TODO
            sb = new StringBuilder();
            //URL PARA SALVAR O PERFIL.

            for(TipoOcorrencia tp: TipoOcorrencia.values()) {
                sb.append(tp.name());
                sb.append(":");
                sb.append(MY_PREFERENCES.getBoolean(tp.name(), false));
                sb.append("-");
            }
            sb.append("EMAIL");
            sb.append(":");
            sb.append(email);
            sb.append("-");
            sb.append("ABOUT");
            sb.append(":");
            sb.append(about);
            sb.append("-");
            sb.append("SEXO");
            sb.append(":");
            sb.append(sexo);
            sb.append("-");
            sb.append("ESCOLARIDADE");
            sb.append(":");
            sb.append(escolar);
            sb.append("-");
            sb.append("SITE");
            sb.append(":");
            sb.append(site);
            sb.append("-");
            sb.append("PHONE");
            sb.append(":");
            sb.append(phone);
            sb.append("-");

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