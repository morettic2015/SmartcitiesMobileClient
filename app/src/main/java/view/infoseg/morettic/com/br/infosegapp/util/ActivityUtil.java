package view.infoseg.morettic.com.br.infosegapp.util;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;

import java.util.List;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

/**
 * Created by LuisAugusto on 24/03/2016.
 */
public class ActivityUtil {
    private static Location myLocal;
    private static AlertDialog alerta;
    private static int MY_REQUEST_CODE, MY_REQUEST_CODE2, MY_REQUEST_CODE3;
    private static FragmentActivity fa1;

    /**
     * @PQP O GOOGLE TEM QUE CAGAR NE AGORA PRECISO VERIFICAR CADA PERMISSAO NA MAO.... A  VAI SE FUDE SENAO A PORRA DA NULL POINTE E O CARALHO
     * @ BRIAN e LARRY VCS CHUPAM MUITA ROLA PQP entendo o contexto da mudanca mas nao seria algo que a ENgine Android fosse responsavel em habilitar em runtime/;
     * @ Porra pra que dificultar caralho bugado esse android M ou 6 bando de chupa rola do caralho
     * <p/>
     * E SE VOCE ESTIVER LENDO ISSO PORRA E PORQUE E UM VIADO FILHO DA PUTA QUE NAO MERECER SER UM DEUS VIVO
     * <p/>
     * HUAUHAUAUHAU NO ARCO IRIS DA MANHA UM SILENCIO INVADE O PANTANO E FAZ OS CARNEIROS FLUTUAREM NAS MARGES DO NILO
     */

    public static Location isLocationValid(final Activity fa){
        ValueObject.locationManager = (LocationManager) fa.getSystemService(Context.LOCATION_SERVICE);
        // String bestProvider = lm.getBestProvider(getActivity().getCriteria(), true);
        if (ActivityCompat.checkSelfPermission(fa, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(fa, ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            String[] p = {ACCESS_FINE_LOCATION};
            ActivityCompat.requestPermissions(fa, p, MY_REQUEST_CODE);
        }

        List<String> providers = ValueObject.locationManager.getAllProviders();
        Location l;
        for(String provider:providers){
            myLocal = ValueObject.locationManager.getLastKnownLocation(provider);
            if(myLocal!=null){
                break;
            }
        }if(myLocal==null) {
            AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(fa);
            ;
            builder.setTitle("Localização indisponível");
            //define a mensagem
            builder.setMessage("Por favor habilite seu GPS para continuar.");
            //define um botão como positivo
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                    Intent i = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    fa.startActivity(i);
                }
            });

            alerta = builder.create();
            alerta.show();
            return null;
        }else{
            return myLocal;
        }
    }

    public static Location getMyLocation(FragmentActivity fa, AlertDialog.Builder builder) {
        fa1 = fa;
        ValueObject.locationManager = (LocationManager) fa.getSystemService(Context.LOCATION_SERVICE);
        // String bestProvider = lm.getBestProvider(getActivity().getCriteria(), true);
        if (ActivityCompat.checkSelfPermission(fa, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(fa, ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            String[] p = {ACCESS_FINE_LOCATION};
            ActivityCompat.requestPermissions(fa, p, MY_REQUEST_CODE);
        }

        List<String> providers = ValueObject.locationManager.getAllProviders();
        Location l;
        for(String provider:providers){
            myLocal = ValueObject.locationManager.getLastKnownLocation(provider);
            if(myLocal!=null){
               break;
            }
        }
        if(myLocal==null) {
            //define o titulo
            builder.setTitle("Localização indisponível");
            //define a mensagem
            builder.setMessage("Por favor habilite seu GPS para continuar.");
            //define um botão como positivo
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                    Intent i = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    fa1.startActivity(i);
                }
            });

            alerta = builder.create();
            alerta.show();
            //Infelizmente nao tem localização retorna null
            return null;
        }else{
            return myLocal;
        }
    }

}
