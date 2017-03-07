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
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import view.infoseg.morettic.com.br.infosegapp.R;
import view.infoseg.morettic.com.br.infosegapp.actions.AssyncMapQuery;
import view.infoseg.morettic.com.br.infosegapp.actions.AssyncMapSearch;
import view.infoseg.morettic.com.br.infosegapp.util.HttpUtil;
import view.infoseg.morettic.com.br.infosegapp.util.LocationManagerUtil;
import view.infoseg.morettic.com.br.infosegapp.util.TipoOcorrencia;

import static view.infoseg.morettic.com.br.infosegapp.util.ValueObject.KEYWORD;
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
    public GoogleMap googleMap;

    private StringBuilder stringBuilder = new StringBuilder();
    private double longitude = 0, latitude = 0;
    private TextView txtInfoForecast;
    private View v;
    public static ImageView imgVOcorrencia, imgVAvatar;


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
        JSONObject myAddres = null;
        try {
            this.longitude = location.getLongitude();
            this.latitude = location.getLatitude();
            myAddres = LocationManagerUtil.getMyAddress(this.getContext(), this.latitude, this.longitude);
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
            int distance = MY_PREFERENCES.getInt("distance", 0) * 20;

            jsFilter.put("lat", latitude);
            jsFilter.put("lon", longitude);
            jsFilter.put("mine", false);

            StringBuilder sbTipos = new StringBuilder();

            for (TipoOcorrencia tp : TipoOcorrencia.values()) {
                if (MY_PREFERENCES.getBoolean(tp.toString(), false)) {
                    sbTipos.append(tp.toString());
                    sbTipos.append(",");
                }
            }

            if (myAddres != null) {
                jsFilter.put("opengraph", myAddres);
            }
            jsFilter.put("type", sbTipos.toString());
            jsFilter.put("distance", distance);

        } catch (Exception ex) {
            FirebaseCrash.report(ex);
        } finally {

            if (KEYWORD == null || KEYWORD.equalsIgnoreCase("keyword") || KEYWORD.equalsIgnoreCase("")) {

                AssyncMapQuery assyncMapQuery = new AssyncMapQuery(v, jsFilter, googleMap);
                assyncMapQuery.setTxtInfoForecast(txtInfoForecast);
                assyncMapQuery.execute();

            } else {
                //@todo implement keyword search
                //
                AssyncMapSearch assyncMapSearch = new AssyncMapSearch(v, jsFilter, googleMap);
                assyncMapSearch.setTxtInfoForecast(txtInfoForecast);
                assyncMapSearch.execute();
            }
            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            googleMap.setTrafficEnabled(true);//Adiciona a camada de transito?
            googleMap.setIndoorEnabled(true);
            googleMap.setBuildingsEnabled(true);//predios
            LatLng local = new LatLng(latitude, longitude);

            //ZOOM no mapa com efeito emo flamenguista
            CameraPosition cameraPosition = new CameraPosition.Builder().target(local).zoom(18).build();
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            LocationManagerUtil.setGMap(googleMap);
            /**
             *
             * Action para visualizar detalhes da ocorrencia
             *
             * */
            googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                @Override
                public View getInfoWindow(Marker arg0) {
                   /* try {
                        imgVOcorrencia = (ImageView) this.getInfoContents(arg0).findViewById(R.id.imageViewOcorrencia);
                        imgVAvatar = (ImageView) this.getInfoContents(arg0).findViewById(R.id.imageViewAvatarMap);
                        JSONObject js = MAPA_OCORRENCIAS.get(new Long(arg0.getSnippet()));
                        if (js.getString("type").equalsIgnoreCase(TipoOcorrencia.IMOVEIS_GIMO.toString())) {
                            String url = js.getString("nmPicture").replace("HTTP://www.", "https://");
                            Picasso.with(v.getContext()).load(url).error(R.drawable.logo).into(imgVOcorrencia);
                            url = js.getString("dsCompanyLogo").replace("HTTP://www.", "https://");
                            Picasso.with(v.getContext()).load(url).error(R.drawable.logo).into(imgVAvatar);
                        } else if (js.getString("type").equalsIgnoreCase(TipoOcorrencia.OPENSTREEMAP.toString())) {
                            Picasso.with(v.getContext()).load(js.getString("token")).error(R.drawable.logo).into(imgVOcorrencia);
                            Picasso.with(v.getContext()).load(js.getString("mPicA")).error(R.drawable.logo).into(imgVAvatar);
                        } else if (js.getString("type").equalsIgnoreCase(TipoOcorrencia.AIRBNB.toString())) {
                            Picasso.with(v.getContext()).load(js.getString("token")).error(R.drawable.logo).into(imgVOcorrencia);
                            Picasso.with(v.getContext()).load(js.getString("host_avatar")).error(R.drawable.logo).into(imgVAvatar);
                        } else {
                            Picasso.with(v.getContext()).load("http://gaeloginendpoint.appspot.com/infosegcontroller.exec?action=8&id=" + js.getString("token")).error(R.drawable.logo).into(imgVOcorrencia);
                            Picasso.with(v.getContext()).load("http://gaeloginendpoint.appspot.com/infosegcontroller.exec?action=8&id=" + js.getString("avatar")).error(R.drawable.logo).into(imgVAvatar);
                        }
                    } catch (Exception e) {
e.printStackTrace();
                    }*/
                    return this.getInfoContents(arg0);
                }

                @Override
                public View getInfoContents(Marker arg0) {
                    String idOcorrencia = arg0.getSnippet();
                    JSONObject js = null;

                    View v = inflater.inflate(R.layout.content_info_map, null);
                    TextView txtTit = (TextView) v.findViewById(R.id.txtTitMapOcorrencia);
                    TextView txtAutor = (TextView) v.findViewById(R.id.txtAutorMapOcorrencia);
                    TextView txtDesc = (TextView) v.findViewById(R.id.txtDescricaoMapOcorrencia);
                    TextView txtDt = (TextView) v.findViewById(R.id.txtDataMapOcorrencia);
                    TextView txtTp = (TextView) v.findViewById(R.id.txtTipoMapOcorrencia);
                    imgVOcorrencia = (ImageView) v.findViewById(R.id.imageViewOcorrencia);
                    imgVAvatar = (ImageView) v.findViewById(R.id.imageViewAvatarMap);


                    if (idOcorrencia != null && MAPA_OCORRENCIAS.containsKey(new Long(idOcorrencia))) {
                        try {
                            js = MAPA_OCORRENCIAS.get(new Long(idOcorrencia));
                            if (js.has("type") && js.getString("type").equalsIgnoreCase(TipoOcorrencia.IMOVEIS_GIMO.toString())) {
                                txtTit.setText(js.getString("nmCategory"));
                                txtAutor.setText(js.getString("nmCompany"));

                                String vlSale = js.getDouble("vlSale") <= 0 ? "ALUGUEL" : "VENDA";

                                txtDt.setText(vlSale);

                                String vl = js.getDouble("vlSale") <= 0 ? js.getString("vlRental") : js.getString("vlSale");

                                txtDesc.setText(js.getString("dsAddress") + " R$:" + vl);

                                txtTp.setText(TipoOcorrencia.IMOVEIS_GIMO.toString());

                                String url = js.getString("nmPicture").replace("HTTP://www.", "https://");
                                Picasso.with(v.getContext()).load(url).
                                        error(R.drawable.logo).
                                        into(imgVOcorrencia);


                                url = js.getString("dsCompanyLogo").replace("HTTP://www.", "https://");
                                Picasso.with(v.getContext()).load(url).
                                        error(R.drawable.logo).
                                        into(imgVAvatar);


                            } else if (js.has("type") && js.getString("type").equalsIgnoreCase(TipoOcorrencia.OPENSTREEMAP.toString())) {
                                txtTit.setText(js.getString("tit"));


                                // Usually this can be a field rather than a method variable


                                txtAutor.setText(js.getString("author"));

                                txtDt.setText(TipoOcorrencia.OPENSTREEMAP.toString());

                                txtDesc.setText(js.getString("desc") + ". " + js.getString("address"));

                                txtTp.setText(js.getString("tipo"));

                                Picasso.with(v.getContext()).load(js.getString("token")).
                                        error(R.drawable.logo).
                                        into(imgVOcorrencia);

                                Picasso.with(v.getContext()).load(js.getString("mPicA")).
                                        error(R.drawable.logo).
                                        into(imgVAvatar);

                            } else if (js.has("type") && js.getString("type").equalsIgnoreCase(TipoOcorrencia.AIRBNB.toString())) {
                                txtTit.setText(js.getString("tit"));


                                // Usually this can be a field rather than a method variable


                                txtAutor.setText(js.getString("host"));

                                txtDt.setText(TipoOcorrencia.AIRBNB.toString());

                                txtDesc.setText(js.getString("desc") + ". " + js.getString("address"));

                                txtTp.setText(js.getString("tipo"));

                                Picasso.with(v.getContext()).load(js.getString("token")).
                                        error(R.drawable.logo).
                                        into(imgVOcorrencia);

                                Picasso.with(v.getContext()).load(js.getString("host_avatar")).
                                        error(R.drawable.logo).
                                        into(imgVAvatar);

                            } else {

                                txtTit.setText(js.getString("tit"));
                                txtAutor.setText(js.getString("author"));
                                txtDesc.setText(js.getString("desc"));
                                txtDt.setText(js.getString("date"));
                                txtTp.setText(js.getString("tipo"));

                                Picasso.with(v.getContext()).
                                        load(HttpUtil.IMAGE_PATH + js.getString("token")).
                                        error(R.drawable.logo).
                                        into(imgVOcorrencia);

                                Picasso.with(v.getContext()).
                                        load(HttpUtil.IMAGE_PATH + js.getString("avatar")).
                                        error(R.drawable.logo).
                                        into(imgVAvatar);

                            }
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

    public void onStop() {
        super.onStop();
        //txtInfoForecast.destroyDrawingCache();
        //this.mMapView.destroyDrawingCache();
        this.v.destroyDrawingCache();
        LocationManagerUtil.setGMap(null);
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
        LocationManagerUtil.setGMap(null);
        stringBuilder = null;
        txtInfoForecast = null;
        googleMap = null;
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        LocationManagerUtil.setGMap(null);
        mMapView.onLowMemory();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        // System.out.print(requestCode);
    }
   /* static class MarkerCallback implements Callback {
        Marker marker=null;

        MarkerCallback(Marker marker) {
            this.marker=marker;
        }

        @Override
        public void onError() {
            Log.e(getClass().getSimpleName(), "Error loading thumbnail!");
        }

        @Override
        public void onSuccess() {
            if (marker != null && marker.isInfoWindowShown()) {
                marker.showInfoWindow();
            }
        }
    }*/
}


