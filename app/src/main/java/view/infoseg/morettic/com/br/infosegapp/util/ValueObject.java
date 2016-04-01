package view.infoseg.morettic.com.br.infosegapp.util;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;

import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

import java.util.HashMap;

import view.infoseg.morettic.com.br.infosegapp.view.LoginFragment;

/**
 * Created by LuisAugusto on 24/02/2016.
 */
public class ValueObject {
    public static String UPLOAD_PIC_OCORRENCIA_TOKEN3 = null;
    public static String UPLOAD_PIC_OCORRENCIA3 = null;
    public static String UPLOAD_PIC_OCORRENCIA_TOKEN2 = null;
    public static String UPLOAD_PIC_OCORRENCIA2 = null;
    public static String UPLOAD_PIC_OCORRENCIA_TOKEN1 = null;
    public static String UPLOAD_PIC_OCORRENCIA1 = null;
    public static String ID_PROFILE = "-1" ;
    public static Activity MAIN;
    public static String ID_OCORRENCIA = "-1" ;
    public static String URL_SUBMIT_UPLOAD;
    public static String UPLOAD_AVATAR = null;
    public static String UPLOAD_AVATAR_TOKEN;
    public static String UPLOAD_PIC_OCORRENCIA = null;
    public static String UPLOAD_PIC_OCORRENCIA_TOKEN;
    public static SharedPreferences MY_PREFERENCES;
    public static JSONObject OCORRENCIAS_JSON;
    public static boolean AUTENTICADO = false;
    public static LoginFragment LOGIN;
    public static Bitmap AVATAR_BITMAP;
    public static HashMap<Long,JSONObject> MAPA_OCORRENCIAS = new HashMap<Long, JSONObject>();
    public static HashMap<String,Bitmap> MAPA_BITMAPS = new HashMap<String, Bitmap>();
    public static Bitmap IMG_OCORRENCIA = null;
    public static Bitmap IMG_AUTHOR = null;
    public static LocationManager locationManager;
    public static Location myLocation;
    public static String[] LIST_OCORRENCIAS;
    public static Bitmap[] LIST_BITMAPS_OCORRENCIAS;
    public static Bitmap BITMAP_DEFAULT;
}
