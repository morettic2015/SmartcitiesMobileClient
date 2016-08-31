
package view.infoseg.morettic.com.br.infosegapp.actions;

import android.os.AsyncTask;

import view.infoseg.morettic.com.br.infosegapp.util.HttpUtil;

import static view.infoseg.morettic.com.br.infosegapp.util.ValueObject.ID_PROFILE;
import static view.infoseg.morettic.com.br.infosegapp.view.InfosegMain.logException;

/**
 * Created by LuisAugusto on 24/02/2016.
 */
public class AssyncSinalizePush extends AsyncTask<Void, Void, Void> {

    private double lat,lon;
    private String token;

    public AssyncSinalizePush(double lat, double lon,String token) {
        this.lat = lat;
        this.lon = lon;
        this.token = token;
    }

    protected Void doInBackground(Void... urls) {
        try {
            HttpUtil.getText(HttpUtil.sinalizePushServerLocationChanged(this.lat, this.lon,this.token, ID_PROFILE));
        } catch (Exception ex) {
            logException(ex);
        } finally {
            return null;
        }
    }
}