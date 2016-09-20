package view.infoseg.morettic.com.br.infosegapp.view;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import view.infoseg.morettic.com.br.infosegapp.R;
import view.infoseg.morettic.com.br.infosegapp.actions.AssyncSaveConfig;
import view.infoseg.morettic.com.br.infosegapp.util.ValueObject;

import static view.infoseg.morettic.com.br.infosegapp.util.ValueObject.MY_PREFERENCES;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * create an instance of this fragment.
 */


public class ActivityConfig extends Fragment {
    private CheckBox  saude,transporte,meioAmbiente, educacao, seguranca, politica,upa,esportes,imoveis;

    private Button bt;
    private View v;
    private EditText txtPhone;
    private TextView textViewSeekBar;
    private SeekBar seekBarDistance;
    private SharedPreferences.Editor editor;
    private int distance = 0;
    private final int constante = 200;
    private Switch myOcurrences;
    @Override
    public void onStop() {
        super.onStop();

        v.destroyDrawingCache();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        InfosegMain.setTitleToolbar(getString(R.string.configura_es), container);
        editor = ValueObject.MY_PREFERENCES.edit();
        v = inflater.inflate(R.layout.activity_config, container, false);
        bt = (Button) v.findViewById(R.id.btSalvarPreferencias);

        textViewSeekBar = (TextView)v.findViewById(R.id.txtViewSeekBar);
        seekBarDistance = (SeekBar)v.findViewById(R.id.seekBarDistance);
        seekBarDistance.setMax(50);
        seekBarDistance.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {


            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                distance = progresValue;
                //Toast.makeText(getApplicationContext(), "Changing seekbar's progress", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //Toast.makeText(getApplicationContext(), "Started tracking seekbar", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                textViewSeekBar.setText(seekBar.getContext().getText(R.string.selecione_as_dist_ncias_para_filtrar)+"("+distance+"KM )");
                //Toast.makeText(getApplicationContext(), "Stopped tracking seekbar", Toast.LENGTH_SHORT).show();
            }
        });
        myOcurrences = (Switch)v.findViewById(R.id.switchMy);
        saude = (CheckBox) v.findViewById(R.id.chkSaude);
        upa = (CheckBox) v.findViewById(R.id.chkUpa);
        transporte = (CheckBox) v.findViewById(R.id.chkTransporte);
        meioAmbiente = (CheckBox) v.findViewById(R.id.chkMeioAmbiente);
        educacao = (CheckBox) v.findViewById(R.id.chkEducacao);
        seguranca = (CheckBox) v.findViewById(R.id.chkSeguranca);
        politica = (CheckBox) v.findViewById(R.id.chkPolitica);
        esportes = (CheckBox) v.findViewById(R.id.chkEsporte);
        imoveis = (CheckBox) v.findViewById(R.id.chkImoveis);

        txtPhone = (EditText)v.findViewById(R.id.txtTelefoneConfig);
        /**
        Mascara do telefone
         * */


        txtPhone.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        bt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

              /*  editor.putBoolean("ehMeu",ehMeu.isChecked()).commit();
                editor.putBoolean("eMeuEstado",eMeuEstado.isChecked()).commit();
                editor.putBoolean("ehMinhaCidade",ehMinhaCidade.isChecked()).commit();
                editor.putBoolean("ehMeuPais",ehMeuPais.isChecked()).commit();*/
                editor.putInt("distance",distance).commit();
                editor.putBoolean("saude",saude.isChecked()).commit();
                editor.putBoolean("imoveis",imoveis.isChecked()).commit();
                editor.putBoolean("transporte",transporte.isChecked()).commit();
                editor.putBoolean("meioAmbiente",meioAmbiente.isChecked()).commit();
                editor.putBoolean("educacao",educacao.isChecked()).commit();
                editor.putBoolean("seguranca",seguranca.isChecked()).commit();
                editor.putBoolean("upa",upa.isChecked()).commit();
                editor.putBoolean("politica",politica.isChecked()).commit() ;
                editor.putString("phoneNumber",txtPhone.getText().toString()).commit() ;
                editor.putBoolean("esporte",esportes.isChecked()).commit();
                editor.putBoolean("mine",myOcurrences.isChecked()).commit();


                AssyncSaveConfig assyncSaveConfig = new AssyncSaveConfig(getContext(),ValueObject.ID_PROFILE.toString(),txtPhone.getText().toString());
                assyncSaveConfig.execute();

               // txtMsgConfig02.setText("Preferências salvas com sucesso!");

                ValueObject.MY_PREFERENCES = v.getContext().getSharedPreferences("INFOSEGMAIN", 0);

            }
        });

        //Init values from pref commited

/*        ehMeu.setChecked(MY_PREFERENCES.getBoolean("ehMeu",false));
        eMeuEstado.setChecked(MY_PREFERENCES.getBoolean("eMeuEstado",false));
        ehMinhaCidade.setChecked(MY_PREFERENCES.getBoolean("ehMinhaCidade",false));*/
        imoveis.setChecked(MY_PREFERENCES.getBoolean("imoveis",false));
        saude.setChecked(MY_PREFERENCES.getBoolean("saude",false));
        transporte.setChecked(MY_PREFERENCES.getBoolean("transporte",false));
        meioAmbiente.setChecked(MY_PREFERENCES.getBoolean("meioAmbiente",false));
        esportes.setChecked(MY_PREFERENCES.getBoolean("esporte",false));
        educacao.setChecked(MY_PREFERENCES.getBoolean("educacao",false));
        seguranca.setChecked(MY_PREFERENCES.getBoolean("seguranca",false));
        upa.setChecked(MY_PREFERENCES.getBoolean("upa",false));
        politica.setChecked(MY_PREFERENCES.getBoolean("politica",false));
        txtPhone.setText(MY_PREFERENCES.getString("phoneNumber",""));
        seekBarDistance.setProgress(MY_PREFERENCES.getInt("distance",0));
        myOcurrences.setChecked(MY_PREFERENCES.getBoolean("mine",false));
        return v;
    }

}
