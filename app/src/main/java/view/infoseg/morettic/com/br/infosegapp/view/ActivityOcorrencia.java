package view.infoseg.morettic.com.br.infosegapp.view;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

import view.infoseg.morettic.com.br.infosegapp.R;
import view.infoseg.morettic.com.br.infosegapp.actions.AssyncSaveOcorrencia;
import view.infoseg.morettic.com.br.infosegapp.actions.AssyncUploadURLlink;
import view.infoseg.morettic.com.br.infosegapp.util.ActivityUtil;
import view.infoseg.morettic.com.br.infosegapp.util.ValueObject;

import static android.app.Activity.RESULT_OK;
import static view.infoseg.morettic.com.br.infosegapp.util.ValueObject.*;

public class ActivityOcorrencia extends Fragment implements View.OnClickListener {
    static int REQUEST_IMAGE_CAPTURE = 1;
    private View v;
    private RadioGroup radioGroup;
    private String tipoOcorrencia = "SERVICOS";
    private Button btEnviar, btMic1, btMic2;
    private ImageButton btCapCam, btCapCam1, btCapCam2, btCapCam3;
    private RadioButton selectedOne;
    private EditText txtTitulo, txtDescricao;
    private LocationManager lm;
    private Location location;
    private double longitude, latitude;
    private AlertDialog.Builder builder;
    public static int OPCAO = -1;
    private static int codigo = 0;
    private TextView txtMsg;
    private ImageButton ib;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        InfosegMain.setTitleToolbar(getString(R.string.register_event), container);

        v = inflater.inflate(R.layout.activity_ocorrencia, container, false);

        //Inicializa os campos da tela
        txtTitulo = (EditText) v.findViewById(R.id.txtTitulo);
        txtDescricao = (EditText) v.findViewById(R.id.txtDescricao);
        btEnviar = (Button) v.findViewById(R.id.btEnviarOcorrencia);
        btMic2 = (Button) v.findViewById(R.id.btMic2);
        btMic1 = (Button) v.findViewById(R.id.btMic1);
        radioGroup = (RadioGroup) v.findViewById(R.id.idRadioOcorrencia);
        builder = new android.support.v7.app.AlertDialog.Builder(inflater.getContext());
        btCapCam = (ImageButton) v.findViewById(R.id.btCaptureCam);
        btCapCam1 = (ImageButton) v.findViewById(R.id.btCaptureCam1);
        btCapCam2 = (ImageButton) v.findViewById(R.id.btCaptureCam2);
        btCapCam3 = (ImageButton) v.findViewById(R.id.btCaptureCam3);
        txtMsg = (TextView) v.findViewById(R.id.txtMsgOcorrencia1);

        //Evento para salvar a ocorrência
        btEnviar.setOnClickListener(this);
        btMic1.setOnClickListener(this);
        btMic2.setOnClickListener(this);
        btCapCam.setOnClickListener(this);
        btCapCam1.setOnClickListener(this);
        btCapCam2.setOnClickListener(this);
        btCapCam3.setOnClickListener(this);

        //Evento para atribuir o tipo conforme a enum server side
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int opcao = radioGroup.getCheckedRadioButtonId();
                OPCAO = opcao;
                selectedOne = (RadioButton) v.findViewById(OPCAO);
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
                    case R.id.rEsporte:
                        tipoOcorrencia = "ESPORTE";
                        break;
                    default:
                        tipoOcorrencia = "SERVICOS";
                        break;

                }
            }
        });
        //Recupera a localização do usuário
        try {
            location = ActivityUtil.getMyLocation(getActivity(), builder);

            longitude = location.getLongitude();
            latitude = location.getLatitude();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return v;
        }
    }

    private void dispatchTakePictureIntent(View v1) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                ib.setImageBitmap(imageBitmap);

                InfosegMain ism = (InfosegMain) getActivity();
                AssyncUploadURLlink aurl = new AssyncUploadURLlink(ism, imageBitmap, codigo);
                aurl.setOrigemOcorrencia(true);
                aurl.execute();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btCaptureCam3:
                ib = (ImageButton) v.findViewById(R.id.btCaptureCam3);
                codigo = R.id.btCaptureCam3;
                dispatchTakePictureIntent(v);
                break;
            case R.id.btCaptureCam2:
                ib = (ImageButton) v.findViewById(R.id.btCaptureCam2);
                codigo = R.id.btCaptureCam2;
                dispatchTakePictureIntent(v);
                break;
            case R.id.btCaptureCam1:
                ib = (ImageButton) v.findViewById(R.id.btCaptureCam1);
                codigo = R.id.btCaptureCam1;
                dispatchTakePictureIntent(v);
                break;
            case R.id.btCaptureCam:
                ib = (ImageButton) v.findViewById(R.id.btCaptureCam);
                codigo = R.id.btCaptureCam;
                dispatchTakePictureIntent(v);
                break;
            case R.id.btMic2:
                WORD = txtDescricao;
                ((InfosegMain) MAIN).startVoiceRecognitionActivity();
                break;
            case R.id.btMic1:
                WORD = txtTitulo;
                ((InfosegMain) MAIN).startVoiceRecognitionActivity();
                break;
            case R.id.btEnviarOcorrencia:
                JSONObject js = new JSONObject();
                StringBuilder erros = new StringBuilder();
                AssyncSaveOcorrencia assyncSaveOcorrencia;
                try {
                    js.put("lon", longitude);
                    js.put("lat", latitude);
                    js.put("tit", txtTitulo.getText().toString());
                    js.put("desc", txtDescricao.getText().toString());
                    js.put("idProfile", ID_PROFILE);
                    js.put("idImagem", UPLOAD_PIC_OCORRENCIA);
                    js.put("tipoOcorrencia", tipoOcorrencia);

                    if (txtTitulo.getText().toString().equals("")) {
                        erros.append(MAIN.getString(R.string.t_tulo_da_ocorr_ncia));
                    }
                    if (txtDescricao.getText().toString().equals("")) {
                        erros.append(MAIN.getString(R.string.descri_o));
                    }
                    if (UPLOAD_PIC_OCORRENCIA == null) {
                        erros.append(MAIN.getString(R.string.foto_1));
                        codigo = R.id.btCaptureCam;//Foto principal da ocorrência
                        dispatchTakePictureIntent(v);//Abre janela para tirar foto
                    }
                    if (selectedOne == null || !selectedOne.isChecked()) {
                        erros.append(MAIN.getString(R.string.tipo_do_servi_o));
                    }

                    if (erros.toString().equals("")) {

                        Geocoder geoCoder = new Geocoder(v.getContext(), Locale.getDefault());
                        //geoCoder.getFromLocation(latitude,longitude,1);
                        assyncSaveOcorrencia = new AssyncSaveOcorrencia(v, js, geoCoder, txtTitulo, txtDescricao, txtMsg, selectedOne, btCapCam, btCapCam1, btCapCam2, btCapCam3);

                        assyncSaveOcorrencia.execute();
                    } else {
                        builder.setTitle(MAIN.getString(R.string.verifique_erros1) + erros.toString() + MAIN.getString(R.string.verifique_erros2));
                        builder.setNegativeButton(MAIN.getString(R.string.ok), new DialogInterface.OnClickListener() {
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
                    js = null;
                    erros = null;
                }
                break;
        }

    }
}
