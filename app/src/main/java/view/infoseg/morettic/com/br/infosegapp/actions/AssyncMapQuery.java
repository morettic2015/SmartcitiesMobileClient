package view.infoseg.morettic.com.br.infosegapp.actions;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
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

import static view.infoseg.morettic.com.br.infosegapp.util.ValueObject.FORECAST;

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
    private TextView txtInfoForecast;
    private StringBuilder sb = new StringBuilder();
    private List<MarkerOptions> lMarkers = new ArrayList<MarkerOptions>();

    public void setTxtInfoForecast(TextView txt){
        this.txtInfoForecast = txt;
    }

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

            txtInfoForecast.setText(sb.toString());
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
                        marker.title(ocorrencia.getString("tit") + " " + ocorrencia.getString("date"));
                        marker.snippet(ocorrencia.getString("id"));
                        // marker.infoWindowAnchor()

                        // Changing marker icon

                        String mTypo = ocorrencia.getString("tipo");
                        float icon = BitmapDescriptorFactory.HUE_VIOLET;//DEFAULT

                        if (mTypo.equals("SERVICOS")) {
                            marker.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_map_service));
                        } else if (mTypo.equals("SAUDE")) {
                            marker.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_map_healthcare));
                        } else if (mTypo.equals("POLITICA")) {
                            marker.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_map_politics));
                        } else if (mTypo.equals("MEIO_AMBIENTE")) {
                            marker.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_map_meioambiente));
                        } else if (mTypo.equals("EDUCACAO")) {
                            marker.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_map_education));
                        } else if (mTypo.equals("TRANSPORTE")) {
                            marker.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_map_bus));
                        } else if (mTypo.equals("OUTROS")) {
                            marker.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_map_info));
                        } else if (mTypo.equals("SEGURANCA")) {
                            marker.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_map_security));
                        } else if (mTypo.equals("UPA")) {
                            marker.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_upa_icon));
                        } else {
                            marker.icon(BitmapDescriptorFactory.defaultMarker());
                        }

                        //Adiciona objeto no mapa de ocorrencias
                        ValueObject.MAPA_OCORRENCIAS.put(ocorrencia.getLong("id"), ocorrencia);
                          ;
                        //marker.infoWindowAnchor(0,0);
                        this.lMarkers.add(marker);

                    }

                }
                //Carrega info do tempo
                //Create a forecast {"curren_weather":[{"humidity":"94","pressure":"1011","temp":"27","temp_unit":"c","weather_code":"1","weather_text":"Partly cloudy","wind":[{"dir":"WSW","speed":"3","wind_unit":"kph"}]}],"forecast":[{"date":"2016-04-04","day":[{"weather_code":"0","weather_text":"Sunny skies","wind":[{"dir":"SSW","dir_degree":"197","speed":"18","wind_unit":"kph"}]}],"day_max_temp":"34","night":[{"weather_code":"1","weather_text":"Partly cloudy skies","wind":[{"dir":"SW","dir_degree":"230","speed":"22","wind_unit":"kph"}]}],"night_min_temp":"25","temp_unit":"c"},{"date":"2016-04-05","day":[{"weather_code":"2","weather_text":"Cloudy skies","wind":[{"dir":"SW","dir_degree":"224","speed":"18","wind_unit":"kph"}]}],"day_max_temp":"34","night":[{"weather_code":"1","weather_text":"Partly cloudy skies","wind":[{"dir":"WSW","dir_degree":"240","speed":"22","wind_unit":"kph"}]}],"night_min_temp":"26","temp_unit":"c"}]}
                //http://www.myweather2.com/developer/forecast.ashx?uac=9H1IUHm/Ih&query=" + lat + "," + lon + "&temp_unit=c&output=json"
                if(FORECAST==null) {
                    FORECAST = HttpUtil.getJSONFromUrl(HttpUtil.getForecast(lat+"", lon+""));
                }
                try {
                    sb.append("Temperatura:");
                    sb.append(FORECAST.getJSONObject("weather").getJSONArray("curren_weather").getJSONObject(0).getDouble("temp"));
                    sb.append("C - Humidade:");
                    sb.append(FORECAST.getJSONObject("weather").getJSONArray("curren_weather").getJSONObject(0).getDouble("humidity"));
                    sb.append("%\nVento:");
                    sb.append(FORECAST.getJSONObject("weather").getJSONArray("curren_weather").getJSONObject(0).getJSONArray("wind").getJSONObject(0).getString("dir"));
                    sb.append(",");
                    sb.append(FORECAST.getJSONObject("weather").getJSONArray("curren_weather").getJSONObject(0).getJSONArray("wind").getJSONObject(0).getString("speed"));
                    sb.append(FORECAST.getJSONObject("weather").getJSONArray("curren_weather").getJSONObject(0).getJSONArray("wind").getJSONObject(0).getString("wind_unit"));
                }catch(Exception e){
                    sb = new StringBuilder("Carregando dados meteorológicos...");
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