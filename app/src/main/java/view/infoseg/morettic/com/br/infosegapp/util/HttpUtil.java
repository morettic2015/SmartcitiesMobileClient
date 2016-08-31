package view.infoseg.morettic.com.br.infosegapp.util;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.provider.MediaStore;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import view.infoseg.morettic.com.br.infosegapp.R;

import static java.net.URLEncoder.encode;
import static view.infoseg.morettic.com.br.infosegapp.util.ValueObject.ID_PROFILE;
import static view.infoseg.morettic.com.br.infosegapp.util.ValueObject.MAIN;
import static view.infoseg.morettic.com.br.infosegapp.view.InfosegMain.logException;

/**
 * Created by LuisAugusto on 24/02/2016.
 *
 * http://gaeloginendpoint.appspot.com/upload.exec
 */
public class HttpUtil {
    //private BitmapFactory.Options options;
   // private Bitmap reusedBitmap;
    public static String getText(String url) throws Exception {
        StringBuilder response = new StringBuilder();
        URL website = null;
        URLConnection connection = null;
        BufferedReader in = null;
        String inputLine = null;
        try {
            website = new URL(url);
            connection = website.openConnection();
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            //Le o arquivo remoto linha a linha
            while ((inputLine = in.readLine()) != null)
                response.append(inputLine);

            inputLine = null;
        }catch(Exception ex){
            logException(ex);
        }finally {
            //in.close();
            website = null;
            in = null;
            connection = null;
            return response.toString();
        }

    }

    public static JSONObject getJSONFromUrl(String url) throws Exception {

        String json = getText(url);

        return new JSONObject(json);

    }

    public static Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public static String getRealPathFromURI(Uri uri,Activity a) {
        Cursor cursor = a.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    public static final String getSaveImagePath(String image, String token){
        return "http://gaeloginendpoint.appspot.com/infosegcontroller.exec?action=2&iName="+image+"&iToken="+token;
    }
    public static final String getSaveUpdateProfile(String email,String avatar,String nome,String cpfCnpj,String cep,String passwd, String complemento, boolean pjf,String nasc,String id) throws UnsupportedEncodingException {
        return "http://gaeloginendpoint.appspot.com/infosegcontroller.exec?action=3&" +
                "email="+email+
                "&avatar="+avatar+
                "&nome="+ encode(nome, "UTF-8")+
                "&cpfCnpj="+encode(cpfCnpj)+
                "&cep="+encode(cep)+
                "&passwd="+passwd+
                "&complemento="+ encode(complemento)+
                "&pjf="+Boolean.toString(pjf)+
                "&nasc="+encode(nasc)+
                "&id="+id;
    }

    public static final String getSaveOcorrenciaPath(String tit,double lat, double lon, String desc, String idPic, String tipo, String idProfile,String address,String idPic1,String idPic2,String idPic3){
        String ret =  "http://gaeloginendpoint.appspot.com/infosegcontroller.exec?action=1&" +
                "titulo=" + encode(tit) +
                "&lat=" + lat +
                "&lon=" + lon +
                "&desc=" + encode(desc) +
                "&idPic=" + idPic +
                "&tipo=" + tipo +
                "&address=" + encode(address) +
                "&idProfile="+ idProfile;

        if(idPic1!=null){
            ret+="&idPic1=" + idPic1;
        }
        if(idPic2!=null){
            ret+="&idPic2=" + idPic2;
        }
        if(idPic3!=null){
            ret+="&idPic3=" + idPic3;
        }

        return ret;
    }

    public static final String getOcorrenciasPath(String pId,String pLat,String pLon, String pMine, int distance, String types){
        String r = "http://gaeloginendpoint.appspot.com/infosegcontroller.exec?action=6" +
                "&id=" + pId +
                "&lat=" + pLat +
                "&lon=" + pLon +
                "&d="+ distance +
                "&type="+types ;

        if(pMine!=null)
            r+= "&mine";

        return r;
    }

    public static Bitmap getBitmapFromURL(String src) throws IOException {
        URL url = null;
        HttpURLConnection connection = null;
        Bitmap myBitmap = null;
        InputStream input = null;
        try {
            url = new URL(src);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            input = connection.getInputStream();

            myBitmap = BitmapFactory.decodeStream(input);
        } catch (IOException ex) {
            //logException(ex);
            myBitmap = BitmapFactory.decodeResource(MAIN.getResources(), R.mipmap.ic_city_watch);
        } finally {
            url = null;
            //input.close();
            //connection.disconnect();
            return myBitmap;
        }
    }
    public static Bitmap getBitmapFromURLBlobKey(String src) throws IOException {
        String urlImage = "http://gaeloginendpoint.appspot.com/infosegcontroller.exec?action=8&id="+encode(src);
        return getBitmapFromURL(urlImage);
    }

    public static final String getPathLoginRegister(String login,String senha){
        return "http://gaeloginendpoint.appspot.com/infosegcontroller.exec?action=7&email="+encode(login)+"&pass="+encode(senha);
    }
    public static final Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        // create a matrix for the manipulation
        Matrix matrix = new Matrix();

        // resize the bit map
        matrix.postScale(scaleWidth, scaleHeight);

        // recreate the new Bitmap
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);

        return resizedBitmap;
    }

    /*public static final String getTokenImagemById(String id){
        return "http://gaeloginendpoint.appspot.com/infosegcontroller.exec?action=8&id="+encode(id);
    }

    public static final String getTokenImagemById(){
        return "http://gaeloginendpoint.appspot.com/infosegcontroller.exec";
    }

    public static final String getGeocodingUrl(String lat,String lon){
        return "http://maps.googleapis.com/maps/api/geocode/json?key=AIzaSyCkJEjT73RmsOw1Ldy3S9RbWg_-PDRh8zE&latlng="+lat+","+lon+"&sensor=true";
    }*/

    public static final String sinalizePushServerLocationChanged(double lat,double lon,String token, String id){
        return "http://gaeloginendpoint.appspot.com/infosegcontroller.exec?action=33&lat="+ lat + "&lon=" + lon +"&token="+token+"&id="+id;
    }
    public static final String getProfileByEmail(String email){
        return "http://api.fullcontact.com/v2/person.json?email="+email+"&apiKey=ba2fbd5adb0456e2";
    }
    public static final String getHasEmailDataStore(String email){
        return "http://gaeloginendpoint.appspot.com/infosegcontroller.exec?action=11&email="+encode(email);
    }
    public static final String sendEmailNovoCadastro(String email){
        return "http://gaeloginendpoint.appspot.com/infosegcontroller.exec?action=12&email="+encode(email)+"&tipo=NOVO_CADASTRO";
    }
    public static final String saveConfigInfo(String id,String phone,String properties){
        return "http://gaeloginendpoint.appspot.com/infosegcontroller.exec?action=15&idProfile="+id+"&phone="+encode(phone)+"&props="+encode(properties);
    }
    public static final String getListTOp20(){
        return "http://gaeloginendpoint.appspot.com/infosegcontroller.exec?action=16";
    }
    public static final String getRatingUrl(String idOcorrencia,String rate){
        return "http://gaeloginendpoint.appspot.com/infosegcontroller.exec?action=13&idPerfil="+ID_PROFILE+"&idOcorrencia="+idOcorrencia+"&rating="+rate;
    }
    public static final String getForecast(String lat,String lon){
        return "http://www.myweather2.com/developer/forecast.ashx?uac=9H1IUHm/Ih&query=" + encode(lat) + "," + encode(lon) + "&temp_unit=c&output=json";
    }
    public static final String getDeviceRegister(String token,String so,String idUser){
        return "http://gaeloginendpoint.appspot.com/infosegcontroller.exec?action=19&so="+encode(so)+"&token="+encode(token)+"&idUser="+idUser;
    }
}
