package view.infoseg.morettic.com.br.infosegapp.actions;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;

import org.json.JSONObject;

import view.infoseg.morettic.com.br.infosegapp.R;
import view.infoseg.morettic.com.br.infosegapp.util.HttpFileUpload;
import view.infoseg.morettic.com.br.infosegapp.util.HttpUtil;
import view.infoseg.morettic.com.br.infosegapp.util.ToastHelper;
import view.infoseg.morettic.com.br.infosegapp.util.ValueObject;
import view.infoseg.morettic.com.br.infosegapp.view.InfosegMain;

import static view.infoseg.morettic.com.br.infosegapp.util.ValueObject.MAIN;
import static view.infoseg.morettic.com.br.infosegapp.view.InfosegMain.logException;

/**
 * Created by LuisAugusto on 24/02/2016. @todo
 */
public class AssyncUploadURLlink extends AsyncTask<JSONObject, Void, String> {
    public static final String UPLOAD_URL = "http://gaeloginendpoint.appspot.com/upload.exec";
    private ProgressDialog dialog;
    private Activity a1;
    private Bitmap bitmapM;
    private boolean origemIsOcorrencia = false;
    private int code;

    public void setOrigemOcorrencia(boolean isIt) {
        this.origemIsOcorrencia = isIt;
    }

    @Override
    protected void onPreExecute() {
        dialog.setMessage(a1.getString(R.string.upload_image));
        dialog.show();
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
        a1 = null;
        bitmapM = null;
        dialog = null;
        String msg = MAIN.getApplicationContext().getString(R.string.save_sucess);
        ToastHelper.makeToast(MAIN.getApplicationContext(), msg);
    }

    public AssyncUploadURLlink(InfosegMain activity, Bitmap b1, int origem) {
        this.dialog = new ProgressDialog(activity);
        this.a1 = activity;
        this.bitmapM = b1;
        this.code = origem;
    }

    protected String doInBackground(JSONObject... urls) {
        JSONObject js = null;
        // Creating new JSON Parser
        try {
            js = HttpUtil.getJSONFromUrl(UPLOAD_URL);
            ValueObject.URL_SUBMIT_UPLOAD = js.getString("uploadPath");

            // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
            Uri tempUri = HttpUtil.getImageUri(a1.getApplicationContext(), this.bitmapM);

            // CALL THIS METHOD TO GET THE ACTUAL PATH
            String realPathInSO = HttpUtil.getRealPathFromURI(tempUri, this.a1);

            String fTYpe = realPathInSO.substring(realPathInSO.length() - 3, realPathInSO.length());
            if (ValueObject.URL_SUBMIT_UPLOAD != null) {

                js = HttpFileUpload.uploadFile(realPathInSO,
                        ValueObject.URL_SUBMIT_UPLOAD,
                        fTYpe);
                js = HttpUtil.getJSONFromUrl(HttpUtil.getSaveImagePath(js.getString("fName"), js.getString("token")));


                ValueObject.UPLOAD_AVATAR = js.getString("key");
                ValueObject.UPLOAD_AVATAR_TOKEN = js.getString("token");

            }
            realPathInSO = null;
            tempUri = null;
            fTYpe = null;
        } catch (Exception ex) {
            logException(ex);
            js = new JSONObject();
            ValueObject.URL_SUBMIT_UPLOAD = null;

            //e.printStackTrace();
        } finally {
            return js.toString();
        }
    }


}