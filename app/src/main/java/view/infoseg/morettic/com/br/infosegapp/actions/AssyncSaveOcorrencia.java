package view.infoseg.morettic.com.br.infosegapp.actions;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.view.View;

import org.json.JSONObject;

import view.infoseg.morettic.com.br.infosegapp.util.HttpUtil;
import view.infoseg.morettic.com.br.infosegapp.util.ValueObject;

/**
 * Created by LuisAugusto on 24/02/2016.
 */
public class AssyncSaveOcorrencia extends AsyncTask<JSONObject, Void, String> {
   // public static final String UPLOAD_URL = "http://gaeloginendpoint.appspot.com/upload.exec";
    private ProgressDialog dialog;
    private View a1;
    private JSONObject ocorrencia;


    @Override
    protected void onPreExecute() {
        dialog.setMessage("Salvando ocorrencia...");
        dialog.show();
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    public AssyncSaveOcorrencia(View activity, JSONObject ocorrencia) {
        this.dialog = new ProgressDialog(activity.getContext());
        this.a1 = activity;
        this.ocorrencia = ocorrencia;
        //builder = new AlertDialog.Builder(this.a1.getContext());
    }

    protected String doInBackground(JSONObject... urls) {
        JSONObject js = null;
        // Creating new JSON Parser   public static final String getSaveUpdateProfile(String email,String avatar,String nome,String cpfCnpj,String cep,String passwd, String complemento, boolean pjf,String nasc,String id){

        try {

            //Pega os campos
            String tit = this.ocorrencia.getString("tit");
            String ocorrenciaPic = ValueObject.UPLOAD_PIC_OCORRENCIA;
            String desc = this.ocorrencia.getString("desc");
            double lat = this.ocorrencia.getDouble("lat");
            double lon = this.ocorrencia.getDouble("lon");
            String tp = this.ocorrencia.getString("tipoOcorrencia");
            String iProfile = ""+ValueObject.ID_PROFILE;
            String idOcorrencia = null;

            //Validacao @TODO

            //URL PARA SALVAR O ocorrencia.
            String url = HttpUtil.getSaveOcorrenciaPath(tit,lat,lon,desc,ocorrenciaPic,tp,iProfile);
            //url =
            js = HttpUtil.getJSONFromUrl(url);

            ValueObject.ID_OCORRENCIA = js.getString("key");



        } catch (Exception e) {
            js = new JSONObject();
            // ValueObject.URL_SUBMIT_UPLOAD = null;

            //e.printStackTrace();
        } finally {
            return js.toString();
        }
    }


}