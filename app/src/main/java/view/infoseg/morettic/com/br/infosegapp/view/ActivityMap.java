package view.infoseg.morettic.com.br.infosegapp.view;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import view.infoseg.morettic.com.br.infosegapp.R;
import view.infoseg.morettic.com.br.infosegapp.actions.AssyncImageLoad;
import view.infoseg.morettic.com.br.infosegapp.actions.AssyncMapQuery;
import view.infoseg.morettic.com.br.infosegapp.util.HttpUtil;
import view.infoseg.morettic.com.br.infosegapp.util.ValueObject;

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


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // inflate and return the layout
        InfosegMain.setTitleToolbar("Visualizar ocorrências", container);
        View v = inflater.inflate(R.layout.activity_map, container,
                false);
        mMapView = (MapView) v.findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume();// needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        builder = new AlertDialog.Builder(getActivity());
        googleMap = mMapView.getMap();
        // latitude and longitude
        LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

        }
        Location location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        double longitude = location.getLongitude();
        double latitude = location.getLatitude();


        JSONObject jsFilter = new JSONObject();
        try {
            jsFilter.put("lat", latitude);

            jsFilter.put("mine", "1");
            jsFilter.put("type", "aaaa,bbbb,assss");
            jsFilter.put("d", "100");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        AssyncMapQuery assyncMapQuery = new AssyncMapQuery(v, jsFilter, googleMap);
        assyncMapQuery.execute();


        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(latitude, longitude);
        googleMap.addMarker(new MarkerOptions().position(sydney).title("Sua localização é lat:" + latitude + " lon:" + longitude));

        // create marker
        final MarkerOptions marker = new MarkerOptions().position(new LatLng(latitude, longitude)).title("Posição atual");

        // Changing marker icon
        marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));

        // adding marker
        googleMap.addMarker(marker);
        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        //ZOOM no mapa com efeito emo flamenguista
        CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(18).build();
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

                        AssyncImageLoad ew = new AssyncImageLoad("0",js.getString("token"));
                        ew.execute();

                        AssyncImageLoad ew1 = new AssyncImageLoad("1",js.getString("avatar"));
                        ew1.execute();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }

            @Override
            public View  getInfoContents(Marker arg0) {
                String idOcorrencia = arg0.getSnippet();
                JSONObject js = null;

                View v = inflater.inflate(R.layout.content_info_map, null);
                if (idOcorrencia!=null&&MAPA_OCORRENCIAS.containsKey(new Long(idOcorrencia))) {

                    js = MAPA_OCORRENCIAS.get(new Long(idOcorrencia));

                    TextView txtTit = (TextView) v.findViewById(R.id.txtTitMapOcorrencia);
                    TextView txtAutor = (TextView) v.findViewById(R.id.txtAutorMapOcorrencia);
                    TextView txtDesc = (TextView) v.findViewById(R.id.txtDescricaoMapOcorrencia);
                    TextView txtDt = (TextView) v.findViewById(R.id.txtDataMapOcorrencia);
                    TextView txtTp = (TextView) v.findViewById(R.id.txtTipoMapOcorrencia);
                    ImageView imgVOcorrencia = (ImageView) v.findViewById(R.id.imageViewOcorrencia);
                    ImageView imgVAvatar = (ImageView) v.findViewById(R.id.imageViewAvatarMap);
                    imgVOcorrencia.setImageBitmap(ValueObject.IMG_OCORRENCIA);
                    imgVAvatar.setImageBitmap(ValueObject.IMG_AUTHOR);

                    //txtTit.setV(js.getString("tit").);

                    try {
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

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent){
        System.out.print(requestCode);
    }
 /*   public void onInfoWindowClick(Marker marker) {
        builder.setMessage(marker.getTitle())
                .setTitle("teste");

// 3. Get the AlertDialog from create()
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.setOnInfoWindowClickListener(getActivity());
    }*/
}


