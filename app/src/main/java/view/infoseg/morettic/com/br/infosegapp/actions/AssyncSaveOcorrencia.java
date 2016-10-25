package view.infoseg.morettic.com.br.infosegapp.actions;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.List;

import view.infoseg.morettic.com.br.infosegapp.R;
import view.infoseg.morettic.com.br.infosegapp.util.HttpFileUpload;
import view.infoseg.morettic.com.br.infosegapp.util.HttpUtil;
import view.infoseg.morettic.com.br.infosegapp.util.ToastHelper;

import static view.infoseg.morettic.com.br.infosegapp.actions.AssyncUploadURLlink.UPLOAD_URL;
import static view.infoseg.morettic.com.br.infosegapp.util.ValueObject.ID_OCORRENCIA;
import static view.infoseg.morettic.com.br.infosegapp.util.ValueObject.ID_PROFILE;
import static view.infoseg.morettic.com.br.infosegapp.util.ValueObject.MAIN;
import static view.infoseg.morettic.com.br.infosegapp.util.ValueObject.UPLOAD_PIC_OCORRENCIA;
import static view.infoseg.morettic.com.br.infosegapp.util.ValueObject.UPLOAD_PIC_OCORRENCIA1;
import static view.infoseg.morettic.com.br.infosegapp.util.ValueObject.UPLOAD_PIC_OCORRENCIA2;
import static view.infoseg.morettic.com.br.infosegapp.util.ValueObject.UPLOAD_PIC_OCORRENCIA3;
import static view.infoseg.morettic.com.br.infosegapp.util.ValueObject.URL_SUBMIT_UPLOAD;
import static view.infoseg.morettic.com.br.infosegapp.view.InfosegMain.logException;

/**
 * Created by LuisAugusto on 24/02/2016.
 * <p/>
 * <p/>
 *
 */
public class AssyncSaveOcorrencia extends AsyncTask<JSONObject, Void, String> {
    // public static final String UPLOAD_URL = "http://gaeloginendpoint.appspot.com/upload.exec";
    private ProgressDialog dialog;
    private View a1;
    private JSONObject ocorrencia;
    private Geocoder geocoder;
    private EditText edTit, editDesc;
    private RadioButton rdSelect;
    private ImageButton i1,i2,i3,i4;
    private boolean hasErros = false;
    private TextView txt;
    private String msg;



    @Override
    protected void onPreExecute() {
        dialog.setMessage(msg);
        dialog.show();
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        //Limpa tela
        edTit.setText("");
        editDesc.setText("");
        rdSelect.setChecked(false);

        i1.setImageDrawable(null);
        i2.setImageDrawable(null);
        i3.setImageDrawable(null);
        i4.setImageDrawable(null);
        //limpa vo
        UPLOAD_PIC_OCORRENCIA = null;
        UPLOAD_PIC_OCORRENCIA1 = null;
        UPLOAD_PIC_OCORRENCIA2 = null;
        UPLOAD_PIC_OCORRENCIA3 = null;
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
        String msg = hasErros?this.a1.getContext().getString(R.string.erro_cadastro_perfil):this.a1.getContext().getString(R.string.sucesso_ocorrencia_registrada);
        ToastHelper.makeToast(this.a1.getContext(),msg);

    }

    public AssyncSaveOcorrencia(View activity, JSONObject ocorrencia, Geocoder geocoder1, EditText tit, EditText desc, TextView txt, RadioButton sel, ImageButton... imageButtons) {
        this.dialog = new ProgressDialog(activity.getContext());
        this.a1 = activity;
        this.ocorrencia = ocorrencia;
        this.geocoder = geocoder1;
        this.editDesc = desc;
        this.edTit = tit;
        this.rdSelect = sel;
        this.i1 = imageButtons[0];
        this.i2 = imageButtons[1];
        this.i3 = imageButtons[2];
        this.i4 = imageButtons[3];
        this.txt = txt;
        this.msg = activity.getContext().getString(R.string.save_occurrences);

        //builder = new AlertDialog.Builder(this.a1.getContext());
    }
    private JSONObject saveImage(Bitmap i) throws Exception {
        JSONObject js = HttpUtil.getJSONFromUrl(UPLOAD_URL);
        URL_SUBMIT_UPLOAD = js.getString("uploadPath");

        // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
        Uri tempUri = HttpUtil.getImageUri(a1.getContext(), i);

        // CALL THIS METHOD TO GET THE ACTUAL PATH
        String realPathInSO = HttpUtil.getRealPathFromURI(tempUri, MAIN);

        String fTYpe = realPathInSO.substring(realPathInSO.length() - 3, realPathInSO.length());
        if (URL_SUBMIT_UPLOAD != null) {

            js = HttpFileUpload.uploadFile(realPathInSO,
                    URL_SUBMIT_UPLOAD,
                    fTYpe);
            js = HttpUtil.getJSONFromUrl(HttpUtil.getSaveImagePath(js.getString("fName"), js.getString("token")));
        }
        return js;
    }
    protected String doInBackground(JSONObject... urls) {
        JSONObject js = null;
        // Creating new JSON Parser   public static final String getSaveUpdateProfile(String email,String avatar,String nome,String cpfCnpj,String cep,String passwd, String complemento, boolean pjf,String nasc,String id){

        try {

            //Pega os campos
            String tit = this.ocorrencia.getString("tit");


            String ocorrenciaPic = UPLOAD_PIC_OCORRENCIA!=null?saveImage(UPLOAD_PIC_OCORRENCIA).getString("key"):null;
            String ocorrenciaPic1 = UPLOAD_PIC_OCORRENCIA1!=null?saveImage(UPLOAD_PIC_OCORRENCIA1).getString("key"):null;
            String ocorrenciaPic2 = UPLOAD_PIC_OCORRENCIA2!=null?saveImage(UPLOAD_PIC_OCORRENCIA2).getString("key"):null;
            String ocorrenciaPic3 = UPLOAD_PIC_OCORRENCIA3!=null?saveImage(UPLOAD_PIC_OCORRENCIA3).getString("key"):null;
            String desc = this.ocorrencia.getString("desc");
            double lat = this.ocorrencia.getDouble("lat");
            double lon = this.ocorrencia.getDouble("lon");
            String tp = this.ocorrencia.getString("tipoOcorrencia");
            String iProfile = "" + ID_PROFILE;
            String idOcorrencia = null;

            //tenta localizar o endereço da ocorrencia pelo lat lon
            final List<Address> fromLocation = geocoder.getFromLocation(lat, lon, 1);
            StringBuilder sb = new StringBuilder();
            for (Address a1 : fromLocation) {
                sb.append(a1.getThoroughfare());
                sb.append(",");
                sb.append(a1.getSubLocality());
                sb.append(",");
                sb.append(a1.getFeatureName());
                sb.append(",");
                sb.append(a1.getLocality());
                sb.append(",");
                sb.append(a1.getPostalCode());
                sb.append(",");
                sb.append(a1.getAdminArea());
                sb.append(",");
                sb.append(a1.getCountryName());
            }

            //URL PARA SALVAR O ocorrencia.
            String url = HttpUtil.getSaveOcorrenciaPath(tit, lat, lon, desc, ocorrenciaPic, tp, iProfile,sb.toString(), ocorrenciaPic1, ocorrenciaPic2, ocorrenciaPic3);
            //url =
            js = HttpUtil.getJSONFromUrl(url);

            ID_OCORRENCIA = js.getString("key");


        } catch (Exception ex) {
            logException(ex);
            js = new JSONObject();
            // ValueObject.URL_SUBMIT_UPLOAD = null;
            hasErros = true;
            //e.printStackTrace();
        } finally {
            return js.toString();
        }
    }


}