package view.infoseg.morettic.com.br.infosegapp.actions;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.view.View;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import view.infoseg.morettic.com.br.infosegapp.R;
import view.infoseg.morettic.com.br.infosegapp.util.HttpUtil;
import view.infoseg.morettic.com.br.infosegapp.util.ValueObject;

/**
 * Created by LuisAugusto on 24/02/2016.
 */
public class AssyncMapQuery extends AsyncTask<JSONObject, Void, List<MarkerOptions>> {
    // public static final String UPLOAD_URL = "http://gaeloginendpoint.appspot.com/upload.exec";
    private ProgressDialog dialog;
    private View a1;
    private JSONObject filter;
    private GoogleMap googleMap;
    private double lat, lon;
    private List<MarkerOptions> lMarkers = new ArrayList<MarkerOptions>();


    @Override
    protected void onPreExecute() {
        dialog.setMessage("Consultando ocorrencias...");
        dialog.show();
    }

    @Override
    protected void onPostExecute(List<MarkerOptions> result) {
        super.onPostExecute(result);
        try {
            for (MarkerOptions mo : result) {
                this.googleMap.addMarker(mo);
            }
        } catch (Exception e) {

        } finally {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }

    public AssyncMapQuery(View activity, JSONObject filter, GoogleMap googleMap) {
        this.dialog = new ProgressDialog(activity.getContext());
        this.a1 = activity;
        this.filter = filter;
        this.googleMap = googleMap;
        this.lat = lat;
        this.lon = lon;
        //builder = new AlertDialog.Builder(this.a1.getContext());
    }

    protected List<MarkerOptions> doInBackground(JSONObject... urls) {
        JSONObject js = null;
        // Creating new JSON Parser   public static final String getSaveUpdateProfile(String email,String avatar,String nome,String cpfCnpj,String cep,String passwd, String complemento, boolean pjf,String nasc,String id){

        try {
            String mine = null;
            if (this.filter.has("mine")) {
                mine = "0";
            }
            String url = HttpUtil.getOcorrenciasPath(ValueObject.ID_PROFILE,
                    this.filter.getString("lat"),
                    this.filter.getString("lon"),
                    mine,
                    this.filter.getString("d"),
                    this.filter.getString("type"));
            JSONArray jOcorrencias;
            try {
                js = HttpUtil.getJSONFromUrl(url);
                /**
                 *
                 * {
                 id: 5636026810761216,
                 author: "Luis Augusto Machado Moretto ",
                 lon: "-48.54502147",
                 desc: "Bsjsjs",
                 email: "moretto@efx.ufsc.br",
                 token: "["AMIfv94HhRKwQW6LqVYsuYWexhOGwcNABcTNnHygGpHVFmHGJdfrOBLwPz1YqZouIF0FKv6nN6baifTND8ScWqWYiPpTmNEhruu8Ok8V9knR5cZspS77vds0miH7g97JJc_jZ1VH_09jCfyedzMZD-wMlQrmoFzsgJh-Ix0iZ8mprrkqJY0A8oc"]",
                 tipo: "SAUDE",
                 tit: "T'es f",
                 avatar: "["AMIfv95Wmyr_t6kQ-UDOMz-N5oUS-PHfYyMa0kt9Uh-SWXffbCh6ui8gd6vBpkW6SV701ivWgGVF9_NWpNp3ChesV-bZwX8vUsqQHVAHmkCisRJA9M37gcItI34LVaP3j2C-bREQSd5R9dyZQgVlkdDfBA0VXPBEwFIoyrXFpBDI8Z2gOHOl5Zk"]",
                 lat: "-27.59639729",
                 ip: "187.65.204.56"
                 },

                 * */
                if (js.has("rList")) {
                    jOcorrencias = js.getJSONArray("rList");
                    for (int i = 0; i < jOcorrencias.length(); i++) {
                        JSONObject ocorrencia = jOcorrencias.getJSONObject(i);

                        // create marker
                        MarkerOptions marker = new MarkerOptions().position(new LatLng(ocorrencia.getDouble("lat"), ocorrencia.getDouble("lon")));
                        marker.title(ocorrencia.getString("tit")+" "+ocorrencia.getString("date"));
                        marker.snippet(ocorrencia.getString("id"));
                        // marker.infoWindowAnchor()

                        // Changing marker icon

                        String mTypo = ocorrencia.getString("tipo");
                        float icon = BitmapDescriptorFactory.HUE_VIOLET;//DEFAULT
                        /**
                         *  SERVICOS,
                         SAUDE,
                         POLITICA,
                         MEIO_AMBIENTE,
                         EDUCACAO,
                         TRANSPORTE,
                         OUTROS,
                         SEGURANCA;
                         *
                         * */
                        if(mTypo.equals("SERVICOS")){
                            marker.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_map_service));
                        }else if(mTypo.equals("SAUDE")){
                            marker.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_map_healthcare));
                        }else if(mTypo.equals("POLITICA")){
                            marker.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_map_politics));
                        }else if(mTypo.equals("MEIO_AMBIENTE")){
                            marker.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_map_meioambiente));
                        }else if(mTypo.equals("EDUCACAO")){
                            marker.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_map_education));
                        }else if(mTypo.equals("TRANSPORTE")){
                            marker.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_map_bus));
                        }else if(mTypo.equals("OUTROS")){
                            marker.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_map_info));
                        }else if(mTypo.equals("SEGURANCA")){
                            marker.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_map_security));
                        }else if(mTypo.equals("UPA")){
                            marker.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_upa_icon));
                        }else{
                            marker.icon(BitmapDescriptorFactory.defaultMarker());
                        }

                        //Adiciona objeto no mapa de ocorrencias
                        ValueObject.MAPA_OCORRENCIAS.put(ocorrencia.getLong("id"),ocorrencia);
                       // ValueObject.MAPA_BITMAPS.put(ocorrencia.getString("token"),HttpUtil.getBitmapFromURLBlobKey(ocorrencia.getString("token")));
                       // ValueObject.MAPA_BITMAPS.put(ocorrencia.getString("avatar"),HttpUtil.getBitmapFromURLBlobKey(ocorrencia.getString("avatar")));

                        ;
                        //marker.infoWindowAnchor(0,0);
                        this.lMarkers.add(marker);

                        // adding marker
                        //this.googleMap.addMarker(marker);
                    }
                }


            } catch (Exception e) {
                js = new JSONObject();
            }


        } catch (Exception e) {
            js = new JSONObject();
            // ValueObject.URL_SUBMIT_UPLOAD = null;

            //e.printStackTrace();
        } finally {
            return lMarkers;
        }
    }


}