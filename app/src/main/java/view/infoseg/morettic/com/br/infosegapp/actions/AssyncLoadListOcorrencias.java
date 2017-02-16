package view.infoseg.morettic.com.br.infosegapp.actions;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import view.infoseg.morettic.com.br.infosegapp.R;
import view.infoseg.morettic.com.br.infosegapp.util.HttpUtil;
import view.infoseg.morettic.com.br.infosegapp.util.ImageCache;
import view.infoseg.morettic.com.br.infosegapp.view.InfosegMain;

import static view.infoseg.morettic.com.br.infosegapp.util.ValueObject.LIST_OCORRENCIAS;
import static view.infoseg.morettic.com.br.infosegapp.view.InfosegMain.logException;

/**
 * Created by LuisAugusto on 24/02/2016.
 */
public class AssyncLoadListOcorrencias extends AsyncTask<JSONObject, Void, String> {

    public InfosegMain info;
    public String city;

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        info.initListOcorrencias();
    }

    protected String doInBackground(JSONObject... urls) {
        JSONObject js = null;
        try {



            String url = HttpUtil.getListTOp20(city);
            //url =
            js = HttpUtil.getJSONFromUrl(url);
            JSONArray ja = js.getJSONArray("rList");
            JSONArray jt = js.getJSONArray("tList");
            JSONArray jw = js.getJSONArray("wList");

           // int totalJw = jw.length()>30?30:jw.length();

            int size = ja.length()+jt.length()+jw.length();
            LIST_OCORRENCIAS = new String[size];
            int indice = 0;
            for (int i = 0; i < ja.length(); i++) {

                if (!ImageCache.hasBitmapFromMemCache(ja.getJSONObject(i).getString("token"))) {
                    Bitmap m = HttpUtil.getBitmapFromURL(ja.getJSONObject(i).getString("token"));
                    if (m != null) {
                        ImageCache.addBitmapToMemoryCache(ja.getJSONObject(i).getString("token"),
                                HttpUtil.getResizedBitmap(m, 96, 96)
                        );
                    } else {
                        Resources res = info.getApplicationContext().getResources();
                        int id = R.drawable.ic_smartcities_icon_logo;
                        Bitmap b = BitmapFactory.decodeResource(res, id);
                        ImageCache.addBitmapToMemoryCache(ja.getJSONObject(i).getString("token"), HttpUtil.getResizedBitmap(b, 96, 96));
                    }
                }
                JSONObject j1 = ja.getJSONObject(i);
                j1.put("mType","experience");

                LIST_OCORRENCIAS[indice++] = j1.toString();
                j1 = null;
            }
            for (int i = 0; i < jt.length(); i++) {

                if (!ImageCache.hasBitmapFromMemCache(jt.getJSONObject(i).getString("avatar_url"))) {
                    Bitmap m = HttpUtil.getBitmapFromURL(jt.getJSONObject(i).getString("avatar_url"));
                    if (m != null) {
                        ImageCache.addBitmapToMemoryCache(
                                jt.getJSONObject(i).getString("avatar_url"),
                                HttpUtil.getResizedBitmap(m, 96, 96)
                        );
                    } else {
                        Resources res = info.getApplicationContext().getResources();
                        int id = R.drawable.ic_smartcities_icon_logo;
                        Bitmap b = BitmapFactory.decodeResource(res, id);
                        ImageCache.addBitmapToMemoryCache(jt.getJSONObject(i).getString("avatar_url"), HttpUtil.getResizedBitmap(b, 96, 96));
                    }
                }
                JSONObject j1 = jt.getJSONObject(i);
                j1.put("mType","twitter");

                LIST_OCORRENCIAS[indice++] = j1.toString();
                j1 = null;
            }
            for (int i = 0; i < jw.length(); i++) {
                if (!ImageCache.hasBitmapFromMemCache(jw.getJSONObject(i).getString("token"))) {
                    Bitmap m = HttpUtil.getBitmapFromURL(jw.getJSONObject(i).getString("token"));
                    if (m != null) {
                        ImageCache.addBitmapToMemoryCache(
                                jw.getJSONObject(i).getString("token"),
                                HttpUtil.getResizedBitmap(m, 96, 96)
                        );
                    } else {
                        Resources res = info.getApplicationContext().getResources();
                        int id = R.drawable.ic_smartcities_icon_logo;
                        Bitmap b = BitmapFactory.decodeResource(res, id);
                        ImageCache.addBitmapToMemoryCache(jw.getJSONObject(i).getString("token"), HttpUtil.getResizedBitmap(b, 96, 96));
                    }
                }
                JSONObject j1 = jw.getJSONObject(i);
                j1.put("mType","webhose");

                LIST_OCORRENCIAS[indice++] = j1.toString();
                j1 = null;
            }
            url = null;
            ja = null;
            jt = null;
            jw = null;
        } catch (Exception ex) {
            logException(ex);
            js = new JSONObject();
        } finally {
            //info.initListOcorrencias();
            return js.toString();
        }
    }
}