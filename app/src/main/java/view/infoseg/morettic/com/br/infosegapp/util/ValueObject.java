package view.infoseg.morettic.com.br.infosegapp.util;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.location.LocationManager;
import android.support.v4.util.ArrayMap;
import android.widget.EditText;

import org.json.JSONObject;

import java.util.logging.Logger;

import view.infoseg.morettic.com.br.infosegapp.view.InfosegMain;
import view.infoseg.morettic.com.br.infosegapp.view.LoginFragment;

/**
 * Created by LuisAugusto on 24/02/2016.
 */
public class ValueObject {
    public static EditText WORD;

    public static Bitmap UPLOAD_PIC_OCORRENCIA3 = null;

    public static Bitmap UPLOAD_PIC_OCORRENCIA2 = null;

    public static Bitmap UPLOAD_PIC_OCORRENCIA1 = null;
    public static String ID_PROFILE = "-1" ;
    public static Activity MAIN;
    public static String ID_OCORRENCIA = "-1" ;
    public static String URL_SUBMIT_UPLOAD;
    public static String UPLOAD_AVATAR = null;
    public static String UPLOAD_AVATAR_TOKEN;
    public static Bitmap UPLOAD_PIC_OCORRENCIA = null;
    public static SharedPreferences MY_PREFERENCES;
    public static String KEYWORD = null;
    public static boolean AUTENTICADO = false;
    public static LoginFragment LOGIN;
    public static Bitmap AVATAR_BITMAP;
    public static ArrayMap<Long,JSONObject> MAPA_OCORRENCIAS = new ArrayMap<Long, JSONObject>();

    public static Bitmap IMG_OCORRENCIA = null;
    public static Bitmap IMG_AUTHOR = null;
    public static LocationManager locationManager;

    public static String[] LIST_OCORRENCIAS;

    public static Bitmap BITMAP_DEFAULT;
    public static JSONObject FORECAST;
    public static String MY_DEVICE_TOKEN = null;
    public static final Logger LOGGER = Logger.getLogger( InfosegMain.class.getName() );
    public static int COUNTER_CLICK = 0;
    public static final int AVAILABLE = 2;

}
