package view.infoseg.morettic.com.br.infosegapp.view;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

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
    private CheckBox saude, transporte, meioAmbiente, educacao, seguranca, politica, upa, esportes, imoveis;

    private Button bt;
    private View v;
    private EditText txtPhone,txtEmail,txtAbout,txtSite;
    private SharedPreferences.Editor editor;
    private Spinner spinner, spinner1;
    final char[] actualValuesSexo={'M','F'};
    private  char sexo;
    private String formacao;
    final String[] actualValuesFormacao={"FUNDAMENTAL","ENSINO_MEDIO","GRADUACAO","POS_GRADUACAO"};

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
        spinner = (Spinner) v.findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(container.getContext(),R.array.sexo, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
                sexo = actualValuesSexo[arg2];
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {// TODO Auto-generated method stub
            }
        });

        spinner.post(new Runnable() {
            @Override
            public void run() {
                int position = MY_PREFERENCES.getString("SEXO", "").equals("M")?0:1;
                spinner.setSelection(position);
            }
        });

        spinner1 = (Spinner) v.findViewById(R.id.spinner2);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(container.getContext(), R.array.nEscolar, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                formacao = actualValuesFormacao[arg2];
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });
        spinner1.post(new Runnable() {
            @Override
            public void run() {
                for(int i=0;i<actualValuesFormacao.length;i++){
                    if(MY_PREFERENCES.getString("FORMACAO", "").equals(actualValuesFormacao[i])){
                        spinner1.setSelection(i);
                        break;
                    }
                }
            }
        });


        txtPhone = (EditText) v.findViewById(R.id.txtTelefoneConfig);
        txtAbout = (EditText) v.findViewById(R.id.about);
        txtEmail = (EditText) v.findViewById(R.id.email2);
        txtSite = (EditText) v.findViewById(R.id.site2);
        /**
         Mascara do telefone
         * */


        txtPhone.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        bt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                editor.putString("SEXO",sexo+"").commit();
                editor.putString("PHONE",txtPhone.getText().toString()).commit();
                editor.putString("ABOUT",txtAbout.getText().toString()).commit();
                editor.putString("SITE",txtSite.getText().toString()).commit();
                editor.putString("EMAIL",txtEmail.getText().toString()).commit();
                editor.putString("FORMACAO",formacao).commit();

                AssyncSaveConfig assyncSaveConfig = new AssyncSaveConfig(getContext(), ValueObject.ID_PROFILE.toString(), txtPhone.getText().toString(),sexo,formacao,txtAbout.getText().toString(),txtSite.getText().toString(),txtEmail.getText().toString());
                assyncSaveConfig.execute();

                // txtMsgConfig02.setText("Preferências salvas com sucesso!");

                ValueObject.MY_PREFERENCES = v.getContext().getSharedPreferences("INFOSEGMAIN", 0);

            }
        });

        //Init values from pref commited


        txtPhone.setText(MY_PREFERENCES.getString("PHONE", ""));
        txtAbout.setText(MY_PREFERENCES.getString("ABOUT", ""));
        txtSite.setText(MY_PREFERENCES.getString("SITE", ""));
        txtEmail.setText(MY_PREFERENCES.getString("EMAIL", ""));

        return v;
    }

}
