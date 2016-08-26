package view.infoseg.morettic.com.br.infosegapp.actions;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.maps.android.heatmaps.Gradient;
import com.google.maps.android.heatmaps.HeatmapTileProvider;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import view.infoseg.morettic.com.br.infosegapp.R;
import view.infoseg.morettic.com.br.infosegapp.util.HttpUtil;
import view.infoseg.morettic.com.br.infosegapp.util.ValueObject;

import static view.infoseg.morettic.com.br.infosegapp.util.ValueObject.FORECAST;
import static view.infoseg.morettic.com.br.infosegapp.util.ValueObject.MAIN;
import static view.infoseg.morettic.com.br.infosegapp.view.InfosegMain.logException;

/**
 * Created by LuisAugusto on 24/02/2016.
 */
public class AssyncMapQuery extends AsyncTask<JSONObject, Void, List<MarkerOptions>> {
    // public static final String UPLOAD_URL = "http://gaeloginendpoint.appspot.com/upload.exec";
    private ProgressDialog dialog;

    private JSONObject filter;
    private GoogleMap googleMap;

    private TextView txtInfoForecast;
    private StringBuilder sb = new StringBuilder();
    private List<MarkerOptions> lMarkers = new ArrayList<MarkerOptions>();
    private List<LatLng> lSaude = new ArrayList<LatLng>();
    private String msg;

    public void setTxtInfoForecast(TextView txt) {
        this.txtInfoForecast = txt;
    }

    @Override
    protected void onPreExecute() {
        dialog.setMessage(msg);
        dialog.show();
    }

    @Override
    protected void onPostExecute(List<MarkerOptions> result) {
        super.onPostExecute(result);
        try {
            for (MarkerOptions mo : result) {
                this.googleMap.addMarker(mo);
            }
            //Previsão do tempo
            txtInfoForecast.setText(sb.toString());
            //Adiciona o mapa de calor da saude
            addHeatMap();
        } catch (Exception ex) {
            logException(ex);
        } finally {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            lMarkers.clear();
            lMarkers = null;
            txtInfoForecast = null;
            lSaude.clear();
            lSaude = null;
            sb = null;
            dialog = null;
        }
    }

    public AssyncMapQuery(View activity, JSONObject filter, GoogleMap googleMap) {
        this.dialog = new ProgressDialog(activity.getContext());
      
        this.filter = filter;
        this.googleMap = googleMap;
        this.msg = activity.getContext().getString(R.string.consultando_ocorrencias);
    }

    protected List<MarkerOptions> doInBackground(JSONObject... urls) {
        JSONObject js = null;
        // Creating new JSON Parser   public static final String getSaveUpdateProfile(String email,String avatar,String nome,String cpfCnpj,String cep,String passwd, String complemento, boolean pjf,String nasc,String id){
        try {
            JSONArray jOcorrencias;
            try {
                js = HttpUtil.getJSONFromUrl(HttpUtil.getOcorrenciasPath(ValueObject.ID_PROFILE,
                        this.filter.getString("lat"),
                        this.filter.getString("lon"),
                        (this.filter.getBoolean("mine") ? "0" : null),
                        this.filter.getString("d"),
                        this.filter.getString("type")));

                if (js.has("rList")) {

                    jOcorrencias = js.getJSONArray("rList");
                    for (int i = 0; i < jOcorrencias.length(); i++) {
                        JSONObject ocorrencia = jOcorrencias.getJSONObject(i);

                        LatLng latLng = new LatLng(ocorrencia.getDouble("lat"), ocorrencia.getDouble("lon"));
                        // create marker
                        MarkerOptions marker = new MarkerOptions().position(latLng);
                        marker.title(ocorrencia.getString("tit") + " " + ocorrencia.getString("date"));
                        marker.snippet(ocorrencia.getString("id"));
                        if (ocorrencia.getString("tipo").equals("SAUDE")) {
                            marker.icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_health01));
                            lSaude.add(latLng);
                        } else if (ocorrencia.getString("tipo").equals("POLITICA")) {
                            marker.icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_politics01));
                        } else if (ocorrencia.getString("tipo").equals("MEIO_AMBIENTE")) {
                            marker.icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_nature01));
                        } else if (ocorrencia.getString("tipo").equals("EDUCACAO")) {
                            marker.icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_education01));
                        } else if (ocorrencia.getString("tipo").equals("TRANSPORTE")) {
                            marker.icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_transport01));
                        } else if (ocorrencia.getString("tipo").equals("SEGURANCA")) {
                            marker.icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_security01));
                        } else if (ocorrencia.getString("tipo").equals("UPA")) {
                            marker.icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_upa01));
                        } else if (ocorrencia.getString("tipo").equals("ESPORTE")) {
                            marker.icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_sport01));
                        } else {
                            marker.icon(BitmapDescriptorFactory.defaultMarker());
                        }
                        //Adiciona objeto no mapa de ocorrencias
                        ValueObject.MAPA_OCORRENCIAS.put(ocorrencia.getLong("id"), ocorrencia);
                        this.lMarkers.add(marker);
                        latLng = null;
                        ocorrencia = null;
                    }
                    jOcorrencias = null;
                }
                //Carrega info do tempo
                //Create a forecast {"curren_weather":[{"humidity":"94","pressure":"1011","temp":"27","temp_unit":"c","weather_code":"1","weather_text":"Partly cloudy","wind":[{"dir":"WSW","speed":"3","wind_unit":"kph"}]}],"forecast":[{"date":"2016-04-04","day":[{"weather_code":"0","weather_text":"Sunny skies","wind":[{"dir":"SSW","dir_degree":"197","speed":"18","wind_unit":"kph"}]}],"day_max_temp":"34","night":[{"weather_code":"1","weather_text":"Partly cloudy skies","wind":[{"dir":"SW","dir_degree":"230","speed":"22","wind_unit":"kph"}]}],"night_min_temp":"25","temp_unit":"c"},{"date":"2016-04-05","day":[{"weather_code":"2","weather_text":"Cloudy skies","wind":[{"dir":"SW","dir_degree":"224","speed":"18","wind_unit":"kph"}]}],"day_max_temp":"34","night":[{"weather_code":"1","weather_text":"Partly cloudy skies","wind":[{"dir":"WSW","dir_degree":"240","speed":"22","wind_unit":"kph"}]}],"night_min_temp":"26","temp_unit":"c"}]}
                //http://www.myweather2.com/developer/forecast.ashx?uac=9H1IUHm/Ih&query=" + lat + "," + lon + "&temp_unit=c&output=json"
                if (FORECAST == null) {
                    FORECAST = HttpUtil.getJSONFromUrl(HttpUtil.getForecast( this.filter.getString("lat") + "",  this.filter.getString("lon") + ""));
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
                } catch (Exception ex) {
                    logException(ex);
                    sb = new StringBuilder(MAIN.getApplicationContext().getString(R.string.dados_tempo));
                }
            } catch (Exception ex) {
                logException(ex);
                //js = new JSONObject();
            }
        } catch (Exception ex) {
            logException(ex);
        } finally {
            return lMarkers;
        }
    }



    private void addHeatMap() {
        int[] colors = {
                Color.rgb(102, 225, 0), // green
                Color.rgb(255, 0, 0)    // red
        };

        float[] startPoints = {
                0.2f, 1f
        };

        Gradient gradient = new Gradient(colors, startPoints);
        // Create a heat map tile provider, passing it the latlngs of the police stations.
        HeatmapTileProvider.Builder mProvider = new HeatmapTileProvider.Builder()
                .gradient(gradient)
                .radius(50)
                .data(lSaude);
        // Add a tile overlay to the map, using the heat map tile provider.
        this.googleMap.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider.build()));
    }


}