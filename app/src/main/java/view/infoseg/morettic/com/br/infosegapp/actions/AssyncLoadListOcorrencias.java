package view.infoseg.morettic.com.br.infosegapp.actions;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import view.infoseg.morettic.com.br.infosegapp.R;
import view.infoseg.morettic.com.br.infosegapp.util.HttpUtil;
import view.infoseg.morettic.com.br.infosegapp.util.ImageCache;

import static view.infoseg.morettic.com.br.infosegapp.util.ValueObject.LIST_OCORRENCIAS;
import static view.infoseg.morettic.com.br.infosegapp.view.InfosegMain.logException;

/**
 * Created by LuisAugusto on 24/02/2016.
 */
public class AssyncLoadListOcorrencias extends AsyncTask<JSONObject, Void, String> {
    private Context ctx;

    public void setCtx(Context c) {
        this.ctx = c;
    }

    protected String doInBackground(JSONObject... urls) {
        JSONObject js = null;
        try {
            String url = HttpUtil.getListTOp20();
            //url =
            js = HttpUtil.getJSONFromUrl(url);
            JSONArray ja = js.getJSONArray("rList");
            LIST_OCORRENCIAS = new String[ja.length()];
            //LIST_BITMAPS_OCORRENCIAS = new Bitmap[ja.length()];
            for (int i = 0; i < ja.length(); i++) {

                if (!ImageCache.hasBitmapFromMemCache(ja.getJSONObject(i).getString("token"))) {
                    Bitmap m = HttpUtil.getBitmapFromURLBlobKey(ja.getJSONObject(i).getString("token"));
                    if (m != null) {
                        ImageCache.addBitmapToMemoryCache(
                                ja.getJSONObject(i).getString("token"),
                                HttpUtil.getResizedBitmap(m, 200, 200)
                        );
                    } else {
                        Resources res = ctx.getResources();
                        int id = R.drawable.ic_smartcities_icon_logo;
                        Bitmap b = BitmapFactory.decodeResource(res, id);
                        ImageCache.addBitmapToMemoryCache(ja.getJSONObject(i).getString("token"), HttpUtil.getResizedBitmap(b, 200, 200));
                    }
                }
                LIST_OCORRENCIAS[i] = ja.getJSONObject(i).toString();
            }
        } catch (Exception ex) {
            logException(ex);
            js = new JSONObject();
        } finally {
            return js.toString();
        }
    }
}