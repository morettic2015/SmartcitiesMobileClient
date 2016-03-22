package view.infoseg.morettic.com.br.infosegapp.util;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.location.Location;
import android.location.LocationManager;
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
import java.net.URLEncoder;

import static java.net.URLEncoder.encode;

/**
 * Created by LuisAugusto on 24/02/2016.
 *
 * https://gaeloginendpoint.appspot.com/upload.exec
 */
public class HttpUtil {

    public static String getText(String url) throws Exception {
        StringBuilder response = new StringBuilder();
        try {
            URL website = new URL(url);
            URLConnection connection = website.openConnection();
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));

            String inputLine;

            while ((inputLine = in.readLine()) != null)
                response.append(inputLine);

            in.close();
        }catch(Exception e){
            e.printStackTrace();
        }finally {
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
        return "https://gaeloginendpoint.appspot.com/infosegcontroller.exec?action=2&iName"+image+"&iToken="+token;
    }
    public static final String getSaveUpdateProfile(String email,String avatar,String nome,String cpfCnpj,String cep,String passwd, String complemento, boolean pjf,String nasc,String id) throws UnsupportedEncodingException {
        return "https://gaeloginendpoint.appspot.com/infosegcontroller.exec?action=3&" +
                "email="+email+
                "&avatar="+avatar+
                "&nome="+ encode(nome, "UTF-8")+
                "&cpfCnpj="+cpfCnpj+
                "&cep="+cep+
                "&passwd="+passwd+
                "&complemento="+ encode(complemento)+
                "&pjf="+Boolean.toString(pjf)+
                "&nasc="+nasc+
                "&id="+id;
    }

    public static final String getSaveOcorrenciaPath(String tit,double lat, double lon, String desc, String idPic, String tipo, String idProfile,String address,String idPic1,String idPic2,String idPic3){
        String ret =  "https://gaeloginendpoint.appspot.com/infosegcontroller.exec?action=1&" +
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

    public static final String getOcorrenciasPath(String pId,String pLat, String pMine, String distance, String types){
        String r = "https://gaeloginendpoint.appspot.com/infosegcontroller.exec?action=6" +
                "&id=" + pId +
                "&lat=" + pLat +
                "&d="+ distance +
                "&type="+types ;

        if(pMine!=null)
            r+= "&mine";

        return r;
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static Bitmap getBitmapFromURLBlobKey(String src) throws IOException {
        String urlImage = "https://gaeloginendpoint.appspot.com/infosegcontroller.exec?action=8&id="+encode(src);
        return getBitmapFromURL(urlImage);
    }

    public static final String getPathLoginRegister(String login,String senha){
        return "https://gaeloginendpoint.appspot.com/infosegcontroller.exec?action=7&email="+encode(login)+"&pass="+encode(senha);
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

    public static final String getTokenImagemById(String id){
        return "https://gaeloginendpoint.appspot.com/infosegcontroller.exec?action=8&id="+encode(id);
    }
    public static final String getTokenImagemById(){
        return "https://gaeloginendpoint.appspot.com/infosegcontroller.exec";
    }

    public static final String getGeocodingUrl(String lat,String lon){
        return "https://maps.googleapis.com/maps/api/geocode/json?key=AIzaSyCkJEjT73RmsOw1Ldy3S9RbWg_-PDRh8zE&latlng="+lat+","+lon+"&sensor=true";
    }

    public static final String getProfileByEmail(String email){
        return "https://api.fullcontact.com/v2/person.json?email="+email+"&apiKey=ba2fbd5adb0456e2";
    }

    public static final String getHasEmailDataStore(String email){
        return "https://gaeloginendpoint.appspot.com/infosegcontroller.exec?action=11&email="+encode(email);
    }

    public static final String sendEmailNovoCadastro(String email){
        return "https://gaeloginendpoint.appspot.com/infosegcontroller.exec?action=12&email="+encode(email)+"&tipo=NOVO_CADASTRO";
    }


}
