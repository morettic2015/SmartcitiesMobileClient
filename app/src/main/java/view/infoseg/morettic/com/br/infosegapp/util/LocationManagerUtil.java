package view.infoseg.morettic.com.br.infosegapp.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import java.util.List;

import view.infoseg.morettic.com.br.infosegapp.R;
import view.infoseg.morettic.com.br.infosegapp.actions.AssyncSinalizePush;

import static view.infoseg.morettic.com.br.infosegapp.util.ValueObject.AVAILABLE;
import static view.infoseg.morettic.com.br.infosegapp.util.ValueObject.MAIN;
import static view.infoseg.morettic.com.br.infosegapp.util.ValueObject.locationManager;

/**
 * Created by LuisAugusto on 24/03/2016.
 */
public class LocationManagerUtil {
    private static Location myLocal;
    private static boolean hasListener = false;
    private static FragmentActivity fa1;


    /**
     * @PQP O GOOGLE TEM QUE CAGAR NE AGORA PRECISO VERIFICAR CADA PERMISSAO NA MAO.... A  VAI SE FUDE SENAO A PORRA DA NULL POINTE E O CARALHO
     * @ BRIAN e LARRY VCS CHUPAM MUITA ROLA PQP entendo o contexto da mudanca mas nao seria algo que a ENgine Android fosse responsavel em habilitar em runtime/;
     * @ Porra pra que dificultar caralho bugado esse android M ou 6 bando de chupa rola do caralho
     * <p>
     * E SE VOCE ESTIVER LENDO ISSO PORRA E PORQUE E UM VIADO FILHO DA PUTA QUE NAO MERECER SER UM DEUS VIVO
     * <p>
     * HUAUHAUAUHAU NO ARCO IRIS DA MANHA UM SILENCIO INVADE O PANTANO E FAZ OS CARNEIROS FLUTUAREM NAS MARGES DO NILO
     */

    public static Location isLocationValid(final Activity fa) throws  SecurityException{

        locationManager = (LocationManager) fa.getSystemService(Context.LOCATION_SERVICE);
        // String bestProvider = lm.getBestProvider(getActivity().getCriteria(), true);


        List<String> providers = locationManager.getAllProviders();
        Location l;
        for (String provider : providers) {
            myLocal = locationManager.getLastKnownLocation(provider);
            if (myLocal != null) {
                break;
            }
        }
        if(!hasListener) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new InnerLocationManager());
            hasListener = true;
        }
        if (myLocal == null) {
            Intent i = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            fa.startActivity(i);

            ToastHelper.makeToast(fa.getApplicationContext(),
                    fa.getString(R.string.para_que_voc_consiga_visualizar_e_relatar_ocorr_ncias_necess_rio_ativar_o_seu_gps_ative_o_gps_no_seu_dispositivo_e_tente_novamente));

            return null;
        } else {
            //Atualiza a posição do GPS a cada 15 minutos e a cada 5 mill metros
            return myLocal;
        }
    }


    public static Location getMyLocation(FragmentActivity fa) throws SecurityException{

        fa1 = fa;
        locationManager = (LocationManager) fa.getSystemService(Context.LOCATION_SERVICE);
        // String bestProvider = lm.getBestProvider(getActivity().getCriteria(), true);

        List<String> providers = locationManager.getAllProviders();

        for (String provider : providers) {
            myLocal = locationManager.getLastKnownLocation(provider);
            if (myLocal != null) {
                break;
            }
        }
        if(!hasListener) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new InnerLocationManager());
            hasListener = true;
        }

        if (myLocal == null) {
            Intent i = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            fa.startActivity(i);
            //define o titulo
            ToastHelper.makeToast(fa.getApplicationContext(),
                    fa.getString(R.string.para_que_voc_consiga_visualizar_e_relatar_ocorr_ncias_necess_rio_ativar_o_seu_gps_ative_o_gps_no_seu_dispositivo_e_tente_novamente));
            //Infelizmente nao tem localização retorna null
            return null;
        } else {

            return myLocal;
        }
    }

    private static class InnerLocationManager implements LocationListener {


        @Override
        public void onLocationChanged(Location locFromGps) {
            new AssyncSinalizePush(locFromGps.getLatitude(), locFromGps.getLongitude()).execute();
            myLocal = locFromGps;
        }

        @Override
        public void onProviderDisabled(String provider) {
            // called when the GPS provider is turned off (user turning off the GPS on the phone)
            ToastHelper.makeToast(MAIN.getApplicationContext(),
                    MAIN
                            .getApplicationContext()
                            .getString(R.string.para_que_voc_consiga_visualizar_e_relatar_ocorr_ncias_necess_rio_ativar_o_seu_gps_ative_o_gps_no_seu_dispositivo_e_tente_novamente));

        }

        @Override
        public void onProviderEnabled(String provider) {
            // called when the GPS provider is turned on (user turning on the GPS on the phone)
            ToastHelper.makeToast(MAIN.getApplicationContext(),
                    MAIN
                            .getApplicationContext()
                            .getString(R.string.gps_habilitado));
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            // called when the status of the GPS provider changes
            if (AVAILABLE == status) {
                ToastHelper.makeToast(MAIN.getApplicationContext(),
                        MAIN
                                .getApplicationContext()
                                .getString(R.string.gps_habilitado));
            } else {
                ToastHelper.makeToast(MAIN.getApplicationContext(),
                        MAIN
                                .getApplicationContext()
                                .getString(R.string.para_que_voc_consiga_visualizar_e_relatar_ocorr_ncias_necess_rio_ativar_o_seu_gps_ative_o_gps_no_seu_dispositivo_e_tente_novamente));
            }
        }
    }

}
