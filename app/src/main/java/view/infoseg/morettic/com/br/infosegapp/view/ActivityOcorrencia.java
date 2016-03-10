package view.infoseg.morettic.com.br.infosegapp.view;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.media.Image;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

import view.infoseg.morettic.com.br.infosegapp.R;
import view.infoseg.morettic.com.br.infosegapp.actions.AssyncSaveOcorrencia;
import view.infoseg.morettic.com.br.infosegapp.actions.AssyncSaveProfile;
import view.infoseg.morettic.com.br.infosegapp.actions.AssyncUploadURLlink;
import view.infoseg.morettic.com.br.infosegapp.util.ValueObject;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.app.Activity.RESULT_OK;

public class ActivityOcorrencia extends Fragment {
    static int REQUEST_IMAGE_CAPTURE = 1;
    private View v;
    private RadioGroup radioGroup;
    private String tipoOcorrencia = "SERVICOS";
    private Button btEnviar;
    private ImageButton btCapCam,btCapCam1,btCapCam2,btCapCam3;
    private RadioButton selectedOne;
    private EditText txtTitulo, txtDescricao;
    private LocationManager lm;
    private Location location;
    private double longitude, latitude;
    private AlertDialog.Builder builder;
    public static int OPCAO = -1;
    private int codigo = 0;
    private int MY_REQUEST_CODE;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        InfosegMain.setTitleToolbar("Registrar ocorrência", container);

        v = inflater.inflate(R.layout.activity_ocorrencia, container, false);

        //Inicializa os campos da tela
        txtTitulo = (EditText) v.findViewById(R.id.txtTitulo);
        txtDescricao = (EditText) v.findViewById(R.id.txtDescricao);
        btEnviar = (Button) v.findViewById(R.id.btEnviarOcorrencia);
        radioGroup = (RadioGroup) v.findViewById(R.id.idRadioOcorrencia);
        builder = new AlertDialog.Builder(inflater.getContext());
        btCapCam = (ImageButton)v.findViewById(R.id.btCaptureCam);
        btCapCam1 = (ImageButton)v.findViewById(R.id.btCaptureCam1);
        btCapCam2 = (ImageButton)v.findViewById(R.id.btCaptureCam2);
        btCapCam3 = (ImageButton)v.findViewById(R.id.btCaptureCam3);


        //Evento para salvar a ocorrência
        btEnviar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                JSONObject js = new JSONObject();
                StringBuilder erros = new StringBuilder();
                try {
                    js.put("lon", longitude);
                    js.put("lat", latitude);
                    js.put("tit", txtTitulo.getText().toString());
                    js.put("desc", txtDescricao.getText().toString());
                    js.put("idProfile", ValueObject.ID_PROFILE);
                    js.put("idImagem", ValueObject.UPLOAD_PIC_OCORRENCIA);
                    js.put("tipoOcorrencia", tipoOcorrencia);

                    if(txtTitulo.getText().toString().equals("")){
                        erros.append("título ");
                    }
                    if(txtDescricao.getText().toString().equals("")){
                        erros.append("descrição ");
                    }
                    if(ValueObject.UPLOAD_PIC_OCORRENCIA==null){
                        erros.append("Foto da ocorrência ");
                        dispatchTakePictureIntent(v);//Abre janela para tirar foto
                    }


                    if (erros.toString().equals("")) {

                        Geocoder geoCoder = new Geocoder(v.getContext(), Locale.getDefault());
                      //geoCoder.getFromLocation(latitude,longitude,1);
                        AssyncSaveOcorrencia assyncSaveOcorrencia = new AssyncSaveOcorrencia(v, js,geoCoder,txtTitulo,txtDescricao,selectedOne,btCapCam,btCapCam1,btCapCam2,btCapCam3);
                        assyncSaveOcorrencia.execute();
                    } else {
                        builder.setTitle("Por favor verifique os campos [" + erros.toString() + "] e tente novamente.");
                        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
                        builder.create();
                        builder.show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {


                }
            }
        });

        btCapCam.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                codigo = R.id.btCaptureCam;
                dispatchTakePictureIntent(v);
            }
        });
        btCapCam1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                codigo = R.id.btCaptureCam1;
                dispatchTakePictureIntent(v);
            }
        });
        btCapCam2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                codigo = R.id.btCaptureCam2;
                dispatchTakePictureIntent(v);
            }
        });
        btCapCam3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                codigo = R.id.btCaptureCam3;
                dispatchTakePictureIntent(v);
            }
        });

        //Evento para atribuir o tipo conforme a enum server side
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int opcao = radioGroup.getCheckedRadioButtonId();
                OPCAO = opcao;
                selectedOne = (RadioButton)v.findViewById(OPCAO);
                switch (opcao) {
                    case R.id.rMeioAmbiente:
                        tipoOcorrencia = "MEIO_AMBIENTE";
                        break;
                    case R.id.rSaude:
                        tipoOcorrencia = "SAUDE";
                        break;
                    case R.id.rEducacao:
                        tipoOcorrencia = "EDUCACAO";
                        break;
                    case R.id.rSegurança:
                        tipoOcorrencia = "SEGURANCA";
                        break;
                    case R.id.rPolitica:
                        tipoOcorrencia = "POLITICA";
                        break;
                    case R.id.rTransporte:
                        tipoOcorrencia = "TRANSPORTE";
                        break;
                    default:
                        tipoOcorrencia = "SERVICOS";
                        break;

                }
            }
        });
        //Recupera a localização do usuário
       try {
           lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);


          // String bestProvider = lm.getBestProvider(getActivity().getCriteria(), true);
           if (ActivityCompat.checkSelfPermission(getActivity(), ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
               String[] p = {ACCESS_FINE_LOCATION};
               ActivityCompat.requestPermissions(getActivity(),p,MY_REQUEST_CODE);
           }

           Location NetLocation = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
           Location GPSLocation = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

           location = GPSLocation==null?NetLocation:GPSLocation;

           longitude = location.getLongitude();
           latitude = location.getLatitude();
       }catch(Exception e){
           e.printStackTrace();
       }

        return v;
    }

    private void dispatchTakePictureIntent(View v1) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ImageButton ib = (ImageButton) getView().findViewById(codigo);
            ib.setImageBitmap(imageBitmap);

            InfosegMain ism = (InfosegMain) getActivity();
            AssyncUploadURLlink aurl = new AssyncUploadURLlink(ism, imageBitmap,codigo);
            aurl.setOrigemOcorrencia(true);
            aurl.execute();
        }
    }

}
