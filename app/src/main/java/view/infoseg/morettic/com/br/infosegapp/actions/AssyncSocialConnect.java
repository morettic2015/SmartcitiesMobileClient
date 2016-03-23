package view.infoseg.morettic.com.br.infosegapp.actions;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;

import view.infoseg.morettic.com.br.infosegapp.R;
import view.infoseg.morettic.com.br.infosegapp.util.HttpFileUpload;
import view.infoseg.morettic.com.br.infosegapp.util.HttpUtil;
import view.infoseg.morettic.com.br.infosegapp.util.ValueObject;
import view.infoseg.morettic.com.br.infosegapp.view.InfosegMain;

import static java.net.URLEncoder.*;
import static view.infoseg.morettic.com.br.infosegapp.actions.AssyncUploadURLlink.*;

/**
 * Created by LuisAugusto on 24/02/2016.
 * <p/>
 * <p/>
 * <p/>
 */
public class AssyncSocialConnect extends AsyncTask<JSONObject, Void, String> {

    private ProgressDialog dialog;
    private View a1;
    private String email, senha, mensagem,nome,imageToken = null;
    private Bitmap avatar;
    private Resources r1;
    private Context ctx;
    private Activity aWin;
    private EditText edit1,edit2,edit3;
    private TextView txt;


    @Override
    protected void onPreExecute() {
        dialog.setMessage("Criando perfil...");
        dialog.show();
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
        txt.setText(mensagem);
        this.edit1.setText("");
        this.edit2.setText("");
        this.edit3.setText("");
    }

    public AssyncSocialConnect(View activity, String email, String senha, Activity aWin, TextView txt, EditText... editTexts) {
        this.dialog = new ProgressDialog(activity.getContext());
        this.a1 = activity;
        this.email = email;
        this.senha = senha;
        this.r1 = activity.getResources();
        this.ctx = activity.getContext();
        this.aWin = aWin;
        this.edit1 = editTexts[0];
        this.edit2 = editTexts[1];
        this.edit3 = editTexts[2];
        this.txt = txt;
    }

    protected String doInBackground(JSONObject... urls) {
        JSONObject js = null;
        // Creating new JSON Parser   public static final String getSaveUpdateProfile(String email,String avatar,String nome,String cpfCnpj,String cep,String passwd, String complemento, boolean pjf,String nasc,String id){

        try {
            //Validacao @TODO
            String url = HttpUtil.getHasEmailDataStore(email);
            js = HttpUtil.getJSONFromUrl(url);
            //Não existe na base
            if(js.getInt("total")==0) {

                //URL PARA SALVAR O PERFIL.
                url = HttpUtil.getProfileByEmail(email);
                //Load data from social network
                try {
                    js = HttpUtil.getJSONFromUrl(url);
                }catch(Exception e){
                    js = new JSONObject();
                    js.put("status",500);
                }

                //Status 200 conseguiu obter o perfil....
                this.nome = "Não Informado";
                String imagePath = null;
                if (js.getInt("status") == 200) {

                    if (js.has("contactInfo")) {
                        this.nome = js.getJSONObject("contactInfo").getString("fullName");
                    }
                    if (js.has("photos")) {
                        JSONArray ja = js.getJSONArray("photos");
                        int tamanho = ja.length();
                        for (int i = 0; i < tamanho; i++) {
                            JSONObject jLocal = ja.getJSONObject(i);
                            //if (jLocal.getBoolean("isPrimary")) {
                                imagePath = jLocal.getString("url");
                                break;
                            //}
                        }
                        //Recupera o avatar e cria um bitmap
                        this.avatar = HttpUtil.getBitmapFromURL(imagePath);
                    } else {//Cria um bitmap do APP
                        this.avatar = BitmapFactory.decodeResource(r1, R.drawable.ic_avatar_novo_01);
                    }
                    mensagem = "Perfil criado com sucesso.";
                } else {
                    this.nome = this.email;
                    //Não achou o usuario cria o padrao mesmo.....
                    this.avatar = BitmapFactory.decodeResource(r1, R.drawable.ic_avatar_novo_01);
                    mensagem = "Perfil criado com sucesso.";
                }
                //Cria o usuario
                Uri tempUri = HttpUtil.getImageUri(ctx, this.avatar);
                // CALL THIS METHOD TO GET THE ACTUAL PATH
                String realPathInSO = HttpUtil.getRealPathFromURI(tempUri, aWin);
                String fTYpe = realPathInSO.substring(realPathInSO.length() - 3, realPathInSO.length());
                //Get Upload URL
                js = HttpUtil.getJSONFromUrl(UPLOAD_URL);
                ValueObject.URL_SUBMIT_UPLOAD = js.getString("uploadPath");


                if (ValueObject.URL_SUBMIT_UPLOAD != null) {
                    js = HttpFileUpload.uploadFile(realPathInSO, ValueObject.URL_SUBMIT_UPLOAD, fTYpe);
                    js = HttpUtil.getJSONFromUrl(HttpUtil.getSaveImagePath(js.getString("fName"), js.getString("token")));
                    imageToken = js.getString("key");
                }
                url = HttpUtil.getSaveUpdateProfile(this.email, imageToken, this.nome, "xxx.xxx.xxx-xx", "88000-000", this.senha, "N/I", true, "dd/MM/yyyy", "-1");
                js = HttpUtil.getJSONFromUrl(url);
                ValueObject.ID_PROFILE = js.getString("key");

                //Envia email de cadastro
                url = HttpUtil.sendEmailNovoCadastro(email);
                js = HttpUtil.getJSONFromUrl(url);

            }else{
                //@Todo implementar o envio de email.....
                mensagem = "Email já registrado. Um email com sua senha foi enviado para sua conta.";
            }
        }catch (Exception e) {
            js = new JSONObject();
            // ValueObject.URL_SUBMIT_UPLOAD = null;

            //e.printStackTrace();
        } finally {
            return js.toString();
        }
    }


}