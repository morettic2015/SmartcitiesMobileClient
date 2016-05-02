package view.infoseg.morettic.com.br.infosegapp.view;

import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import view.infoseg.morettic.com.br.infosegapp.R;
import view.infoseg.morettic.com.br.infosegapp.actions.AssyncImageLoad;
import view.infoseg.morettic.com.br.infosegapp.actions.AssyncMapQuery;
import view.infoseg.morettic.com.br.infosegapp.util.ActivityUtil;

import static view.infoseg.morettic.com.br.infosegapp.util.ValueObject.*;
import static view.infoseg.morettic.com.br.infosegapp.util.ValueObject.MAPA_OCORRENCIAS;

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
    private AlertDialog.Builder builder;
    private StringBuilder stringBuilder = new StringBuilder();
    private double longitude = 0, latitude = 0;
    private TextView txtInfoForecast;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // inflate and return the layout
        InfosegMain.setTitleToolbar(getString(R.string.view_events), container);
        View v = inflater.inflate(R.layout.activity_map, container,
                false);

        txtInfoForecast = (TextView) v.findViewById(R.id.txtInfoForecast);
        mMapView = (MapView) v.findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume();// needed to get the map to display immediately
        Location location = ActivityUtil.getMyLocation(getActivity(), builder);
        try {
            this.longitude = location.getLongitude();
            this.latitude = location.getLatitude();
        } catch (Exception e) {//LATITUDE DE BRASILIA
            this.longitude = -15.7941d;
            this.latitude = -47.8825d;
        }
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        googleMap = mMapView.getMap();
        // latitude and longitude
        JSONObject jsFilter = new JSONObject();
        try {
            //4000 = ~= 200km 400 = ~= 20km 1000 = ~= 50km
            int distance = MY_PREFERENCES.getBoolean("ehMeuPais", false) ? 4000 : MY_PREFERENCES.getBoolean("eMeuEstado", false) ? 1000 : 400;

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

            jsFilter.put("type", sbTipos.toString());
            jsFilter.put("d", distance);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {


            AssyncMapQuery assyncMapQuery = new AssyncMapQuery(v, jsFilter, googleMap);
            assyncMapQuery.setTxtInfoForecast(txtInfoForecast);
            assyncMapQuery.execute();


            // Add a marker in Sydney and move the camera
            /*
            googleMap.addMarker(new MarkerOptions().position(sydney).title("Sua localização é lat:" + latitude + " lon:" + longitude));

            // create marker
            final MarkerOptions marker = new MarkerOptions().position(new LatLng(latitude, longitude)).title("Posição atual");

            // Changing marker icon
            marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));

            // adding marker
            googleMap.addMarker(marker);*/
            googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            googleMap.setTrafficEnabled(true);//Adiciona a camada de transito?
            googleMap.setBuildingsEnabled(true);//predios
            LatLng local = new LatLng(latitude, longitude);
            //Adiciona raio
           /* CircleOptions circleOptions = new CircleOptions().center(local).radius(20000); // In meters
            Circle circle = this.googleMap.addCircle(circleOptions);
            circle.setFillColor(Color.TRANSPARENT);
            circle.setZIndex(-10f);
            circle.setStrokeColor(Color.BLACK);
            circle.setStrokeWidth(1.5f);*/

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
                            stringBuilder.append("Visualize as ocorrencias em seu celular! http://smartcitiesframework.com.br");


                         /*   bShare.setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    Intent sendIntent = new Intent();


                                    sendIntent.setAction(Intent.ACTION_SEND);
                                    sendIntent.putExtra(Intent.EXTRA_TEXT, stringBuilder.toString());
                                    sendIntent.setType("text/plain");
                                    startActivity(sendIntent);
                                }
                            });*/


                            txtTit.setText(js.getString("tit"));
                            txtAutor.setText(js.getString("author"));
                            txtDesc.setText(js.getString("desc"));
                            txtDt.setText(js.getString("date"));
                            txtTp.setText(js.getString("tipo"));


                            //((ImageView) v.findViewById(R.id.)).setImageBitmap(ValueObject.MAPA_BITMAPS.get(js.getString("")));

                        } catch (JSONException e) {
                            e.printStackTrace();
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
        stringBuilder = null;
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
        builder = null;
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


