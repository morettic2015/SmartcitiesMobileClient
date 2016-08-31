package view.infoseg.morettic.com.br.infosegapp.view;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.firebase.crash.FirebaseCrash;

import org.json.JSONException;
import org.json.JSONObject;

import view.infoseg.morettic.com.br.infosegapp.R;
import view.infoseg.morettic.com.br.infosegapp.actions.AssyncImageLoad;
import view.infoseg.morettic.com.br.infosegapp.actions.AssyncMapQuery;
import view.infoseg.morettic.com.br.infosegapp.util.LocationManagerUtil;

import static view.infoseg.morettic.com.br.infosegapp.util.ValueObject.IMG_AUTHOR;
import static view.infoseg.morettic.com.br.infosegapp.util.ValueObject.IMG_OCORRENCIA;
import static view.infoseg.morettic.com.br.infosegapp.util.ValueObject.MAPA_OCORRENCIAS;
import static view.infoseg.morettic.com.br.infosegapp.util.ValueObject.MY_PREFERENCES;
import static view.infoseg.morettic.com.br.infosegapp.view.InfosegMain.logException;

/*public class ActivityMap extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_activity_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
*/

/**
 * A fragment that launches other parts of the demo application.
 */
public class ActivityMap extends Fragment /* implements OnMapReadyCallback */ {

    private MapView mMapView;
    private GoogleMap googleMap;

    private StringBuilder stringBuilder = new StringBuilder();
    private double longitude = 0, latitude = 0;
    private TextView txtInfoForecast;
    private View v;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // inflate and return the layout
        InfosegMain.setTitleToolbar(getString(R.string.view_events), container);
        v = inflater.inflate(R.layout.activity_map, container,
                false);

        txtInfoForecast = (TextView) v.findViewById(R.id.txtInfoForecast);
        mMapView = (MapView) v.findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume();// needed to get the map to display immediately
        Location location = LocationManagerUtil.getMyLocation(this.getActivity());
        try {
            this.longitude = location.getLongitude();
            this.latitude = location.getLatitude();
        } catch (Exception e) {//LATITUDE DE BRASILIA
            FirebaseCrash.report(new Exception("IMPOSSIVEL DETERMINAR LOCALIZAÇÃO DO EMO"));
            this.longitude = -15.7941d;
            this.latitude = -47.8825d;
        }
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception ex) {
            FirebaseCrash.report(ex);
        }
        googleMap = mMapView.getMap();
        // latitude and longitude
        JSONObject jsFilter = new JSONObject();
        try {
            //4000 = ~= 200km 400 = ~= 20km 1000 = ~= 50km
            int distance = MY_PREFERENCES.getInt("distance",0)*20;

            jsFilter.put("lat", latitude);
            jsFilter.put("lon", longitude);
            jsFilter.put("mine", MY_PREFERENCES.getBoolean("ehMeu",false));

            StringBuilder sbTipos = new StringBuilder();

            if (MY_PREFERENCES.getBoolean("saude", false)) {
                sbTipos.append("SAUDE,");
            }
            if (MY_PREFERENCES.getBoolean("politica", false)) {
                sbTipos.append("POLITICA,");
            }
            if (MY_PREFERENCES.getBoolean("meioAmbiente", false)) {
                sbTipos.append("MEIO_AMBIENTE,");
            }
            if (MY_PREFERENCES.getBoolean("transporte", false)) {
                sbTipos.append("TRANSPORTE,");
            }
            if (MY_PREFERENCES.getBoolean("seguranca", false)) {
                sbTipos.append("SEGURANCA,");
            }
            if (MY_PREFERENCES.getBoolean("educacao", false)) {
                sbTipos.append("EDUCACAO,");
            }
            if (MY_PREFERENCES.getBoolean("upa", false)) {
                sbTipos.append("UPA,");
            }
            if (MY_PREFERENCES.getBoolean("esporte", false)) {
                sbTipos.append("ESPORTE,");
            }
            if (MY_PREFERENCES.getBoolean("imoveis", false)) {
                sbTipos.append("IMOVEIS,");
            }

            jsFilter.put("type", sbTipos.toString());
            jsFilter.put("distance", distance);

        } catch (Exception ex) {
            FirebaseCrash.report(ex);
        } finally {


            AssyncMapQuery assyncMapQuery = new AssyncMapQuery(v, jsFilter, googleMap);
            assyncMapQuery.setTxtInfoForecast(txtInfoForecast);
            assyncMapQuery.execute();


            googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            googleMap.setTrafficEnabled(true);//Adiciona a camada de transito?
            googleMap.setBuildingsEnabled(true);//predios
            LatLng local = new LatLng(latitude, longitude);

            //ZOOM no mapa com efeito emo flamenguista
            CameraPosition cameraPosition = new CameraPosition.Builder().target(local).zoom(12).build();
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


            /**
             *
             * Action para visualizar detalhes da ocorrencia
             *
             * */
            googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                @Override
                public View getInfoWindow(Marker arg0) {
                    String idOcorrencia = arg0.getSnippet();
                    JSONObject js = null;
                    if (idOcorrencia != null && MAPA_OCORRENCIAS.containsKey(new Long(idOcorrencia))) {

                        js = MAPA_OCORRENCIAS.get(new Long(idOcorrencia));
                        try {

                            AssyncImageLoad ew = new AssyncImageLoad("0", js.getString("token"));
                            ew.execute();

                            AssyncImageLoad ew1 = new AssyncImageLoad("1", js.getString("avatar"));
                            ew1.execute();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    return null;
                }

                @Override
                public View getInfoContents(Marker arg0) {
                    String idOcorrencia = arg0.getSnippet();
                    JSONObject js = null;

                    View v = inflater.inflate(R.layout.content_info_map, null);
                    if (idOcorrencia != null && MAPA_OCORRENCIAS.containsKey(new Long(idOcorrencia))) {
                        try {
                            js = MAPA_OCORRENCIAS.get(new Long(idOcorrencia));

                            TextView txtTit = (TextView) v.findViewById(R.id.txtTitMapOcorrencia);
                            TextView txtAutor = (TextView) v.findViewById(R.id.txtAutorMapOcorrencia);
                            TextView txtDesc = (TextView) v.findViewById(R.id.txtDescricaoMapOcorrencia);
                            TextView txtDt = (TextView) v.findViewById(R.id.txtDataMapOcorrencia);
                            TextView txtTp = (TextView) v.findViewById(R.id.txtTipoMapOcorrencia);
                            ImageView imgVOcorrencia = (ImageView) v.findViewById(R.id.imageViewOcorrencia);
                            ImageView imgVAvatar = (ImageView) v.findViewById(R.id.imageViewAvatarMap);
                            imgVOcorrencia.setImageBitmap(IMG_OCORRENCIA);
                            imgVAvatar.setImageBitmap(IMG_AUTHOR);
                            //Button bShare = (Button) v.findViewById(R.id.btCompartilharOcorrencia);

                            /**
                             *
                             *      @TODO
                             *      CRIAR MENSAGEM PADRAO PARA DAR O SHARE! REFACTOR PARA UNIFICAR COM O OUTRO BOTAO DE SHARE PASSANDO APENAS UMA MENSAGEM COMO PARAMETRO DE ENTRADA
                             *
                             *      CRIAR EVENTO DAS ESTRELAS PONTUANDO A OCORRENCIA E O AUTHOR
                             * */
                            //Mensagem share
                            stringBuilder = new StringBuilder();
                            stringBuilder.append("Ocorrencia:");
                            stringBuilder.append(js.getString("tit"));
                            stringBuilder.append(" descricao:");
                            stringBuilder.append(js.getString("desc"));
                            stringBuilder.append(" date:");
                            stringBuilder.append(js.getString("date"));
                            stringBuilder.append(" tipo:");
                            stringBuilder.append(js.getString("tipo"));
                            stringBuilder.append(" author:");
                            stringBuilder.append(js.getString("author"));
                            stringBuilder.append("http://citywatch.com.br");

                            txtTit.setText(js.getString("tit"));
                            txtAutor.setText(js.getString("author"));
                            txtDesc.setText(js.getString("desc"));
                            txtDt.setText(js.getString("date"));
                            txtTp.setText(js.getString("tipo"));


                            //((ImageView) v.findViewById(R.id.)).setImageBitmap(ValueObject.MAPA_BITMAPS.get(js.getString("")));

                        } catch (JSONException ex) {
                            logException(ex);
                        }
                    }
                    return v;
                }
            });

            // Perform any camera updates here
            return v;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    public void onStop (){
        super.onStop();
        //txtInfoForecast.destroyDrawingCache();
        //this.mMapView.destroyDrawingCache();
        this.v.destroyDrawingCache();

        txtInfoForecast = null;
    }
    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        stringBuilder = null;
        txtInfoForecast = null;
        googleMap = null;
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        // System.out.print(requestCode);
    }

}


