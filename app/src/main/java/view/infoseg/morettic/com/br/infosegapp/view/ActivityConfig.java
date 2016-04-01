package view.infoseg.morettic.com.br.infosegapp.view;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.telephony.TelephonyManager;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import static android.Manifest.permission.GET_ACCOUNTS;
import static view.infoseg.morettic.com.br.infosegapp.util.ValueObject.*;

import view.infoseg.morettic.com.br.infosegapp.R;
import view.infoseg.morettic.com.br.infosegapp.actions.AssyncSaveConfig;
import view.infoseg.morettic.com.br.infosegapp.util.Mask;
import view.infoseg.morettic.com.br.infosegapp.util.ValueObject;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * create an instance of this fragment.
 */


public class ActivityConfig extends Fragment {
    private CheckBox ehMeu, eMeuEstado,ehMinhaCidade, ehMeuPais, saude,transporte,meioAmbiente, educacao, seguranca, politica;
    private TextView txtMsgConfig02;
    private Button bt;
    private View v;
    private EditText txtPhone;
    private SharedPreferences.Editor editor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        InfosegMain.setTitleToolbar("Configurações", container);
        editor = ValueObject.MY_PREFERENCES.edit();
        v = inflater.inflate(R.layout.activity_config, container, false);
        bt = (Button) v.findViewById(R.id.btSalvarPreferencias);
        ehMeu = (CheckBox) v.findViewById(R.id.chkMine);
        eMeuEstado = (CheckBox) v.findViewById(R.id.chkEstado);
        ehMinhaCidade = (CheckBox) v.findViewById(R.id.chkCity);
        ehMeuPais = (CheckBox) v.findViewById(R.id.chkPais);
        saude = (CheckBox) v.findViewById(R.id.chkSaude);
        transporte = (CheckBox) v.findViewById(R.id.chkTransporte);
        meioAmbiente = (CheckBox) v.findViewById(R.id.chkMeioAmbiente);
        educacao = (CheckBox) v.findViewById(R.id.chkEducacao);
        seguranca = (CheckBox) v.findViewById(R.id.chkSeguranca);
        politica = (CheckBox) v.findViewById(R.id.chkPolitica);
        txtMsgConfig02 = (TextView) v.findViewById(R.id.txtMsgConfig02);
        txtPhone = (EditText)v.findViewById(R.id.txtTelefoneConfig);
        /**
        Mascara do telefone
         * */


        txtPhone.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        bt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                editor.putBoolean("ehMeu",ehMeu.isChecked()).commit();
                editor.putBoolean("eMeuEstado",eMeuEstado.isChecked()).commit();
                editor.putBoolean("ehMinhaCidade",ehMinhaCidade.isChecked()).commit();
                editor.putBoolean("ehMeuPais",ehMeuPais.isChecked()).commit();
                editor.putBoolean("saude",saude.isChecked()).commit();
                editor.putBoolean("transporte",transporte.isChecked()).commit();
                editor.putBoolean("meioAmbiente",meioAmbiente.isChecked()).commit();
                editor.putBoolean("educacao",educacao.isChecked()).commit();
                editor.putBoolean("seguranca",seguranca.isChecked()).commit();
                editor.putBoolean("politica",politica.isChecked()).commit() ;
                editor.putString("phoneNumber",txtPhone.getText().toString()).commit() ;


                AssyncSaveConfig assyncSaveConfig = new AssyncSaveConfig(getContext(),ValueObject.ID_PROFILE.toString(),txtPhone.getText().toString());
                assyncSaveConfig.execute();

                txtMsgConfig02.setText("Preferências salvas com sucesso!");

            }
        });

        //Init values from pref commited

        ehMeu.setChecked(MY_PREFERENCES.getBoolean("ehMeu",false));
        eMeuEstado.setChecked(MY_PREFERENCES.getBoolean("eMeuEstado",false));
        ehMinhaCidade.setChecked(MY_PREFERENCES.getBoolean("ehMinhaCidade",false));
        ehMeuPais.setChecked(MY_PREFERENCES.getBoolean("ehMeuPais",false));
        saude.setChecked(MY_PREFERENCES.getBoolean("saude",false));
        transporte.setChecked(MY_PREFERENCES.getBoolean("transporte",false));
        meioAmbiente.setChecked(MY_PREFERENCES.getBoolean("meioAmbiente",false));
        educacao.setChecked(MY_PREFERENCES.getBoolean("educacao",false));
        seguranca.setChecked(MY_PREFERENCES.getBoolean("seguranca",false));
        politica.setChecked(MY_PREFERENCES.getBoolean("politica",false));
        txtPhone.setText(MY_PREFERENCES.getString("phoneNumber",""));

        return v;
    }

}
