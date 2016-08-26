package view.infoseg.morettic.com.br.infosegapp.view;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import org.json.JSONObject;

import view.infoseg.morettic.com.br.infosegapp.R;
import view.infoseg.morettic.com.br.infosegapp.actions.AssyncSaveProfile;
import view.infoseg.morettic.com.br.infosegapp.util.ImageCache;
import view.infoseg.morettic.com.br.infosegapp.util.Mask;
import view.infoseg.morettic.com.br.infosegapp.util.ToastHelper;
import view.infoseg.morettic.com.br.infosegapp.util.Validate;
import view.infoseg.morettic.com.br.infosegapp.util.ValueObject;

import static view.infoseg.morettic.com.br.infosegapp.util.ValueObject.MAIN;
import static view.infoseg.morettic.com.br.infosegapp.util.ValueObject.MY_PREFERENCES;
import static view.infoseg.morettic.com.br.infosegapp.view.InfosegMain.logException;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * create an instance of this fragment.
 */
public class ActivityProfile extends Fragment {
    private View v;
    private TextWatcher cpfMask, cnpjMask, cepMask;
    private RadioButton rdCNPJ;
    private RadioGroup radioGroup;
    private EditText cpf, nasc, cep, complemento, passwd, nome, email;
    private ImageButton btAvatar;
    private RadioButton rd;
    private SharedPreferences.Editor editor = ValueObject.MY_PREFERENCES.edit();


    @Override
    public void onDestroy() {
        super.onDestroy();
        cpf.destroyDrawingCache();
        rdCNPJ.destroyDrawingCache();
        complemento.destroyDrawingCache();
        cep.destroyDrawingCache();
        passwd.destroyDrawingCache();
        nome.destroyDrawingCache();
        email.destroyDrawingCache();
        btAvatar.destroyDrawingCache();
        rd.destroyDrawingCache();
        cpfMask = null;
        cnpjMask = null;
        cepMask = null;
        radioGroup = null;
        rdCNPJ = null;
        editor = null;
        rd = null;
        v.destroyDrawingCache();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        InfosegMain.setTitleToolbar(getString(R.string.perfil), container);

        //recupera a view
        v = inflater.inflate(R.layout.activity_profile, container, false);

        //inicializa campos
        nasc = (EditText) v.findViewById(R.id.txtDtNasc);
        cpf = (EditText) v.findViewById(R.id.txtCpfCnpj);
        rdCNPJ = (RadioButton) v.findViewById(R.id.radioPessoaJuridica);
        cep = (EditText) v.findViewById(R.id.txtCepBR);
        complemento = (EditText) v.findViewById(R.id.txtComplemento);
        passwd = (EditText) v.findViewById(R.id.txtPasswd);
        nome = (EditText) v.findViewById(R.id.txtNomeUsuario);
        rd = (RadioButton) v.findViewById(R.id.radioPessoaFisica);
        email = (EditText) v.findViewById(R.id.txtEmailUsuario);
        btAvatar = (ImageButton)v.findViewById(R.id.btAvatar);
        if(ImageCache.hasBitmapFromMemCache("avatar")){
            btAvatar.setImageBitmap(ImageCache.getBitmapFromMemCache("avatar"));
        }

        //Configura campos
        //Apenas focus para nao aparecer os numeros e sim a tela de seleção da data
        nasc.setFocusable(false);

        //Define a mascara do CEP
        cepMask = Mask.insert("#####-###", cep);
        cep.addTextChangedListener(cepMask);

        //COnfigura o listener dos radiobutton para atribuir a mascara corretamente conforme o tipo de pessoa fisica juridica
        radioGroup = (RadioGroup) v.findViewById(R.id.radioGroupTipoPessoa);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int opcao = radioGroup.getCheckedRadioButtonId();
                if (opcao == rdCNPJ.getId()) {
                    cnpjMask = Mask.insert("##.###.###/####-#########", cpf);
                    cpf.addTextChangedListener(cnpjMask);
                    if (cnpjMask != null)
                        cpf.removeTextChangedListener(cpfMask);
                } else {
                    cpfMask = Mask.insert("###.###.###-#########", cpf);
                    cpf.addTextChangedListener(cpfMask);
                    if (cnpjMask != null)
                        cpf.removeTextChangedListener(cnpjMask);

                }
            }
        });

        Button mButton = (Button) v.findViewById(R.id.btSalvarPerfilUsuario);
        mButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                JSONObject js = new JSONObject();


                try {
                    //Pega os campos
                    js.put("email", email.getText().toString());
                    // ValueObject.UPLOAD_AVATAR;
                    js.put("nome", nome.getText().toString());
                    js.put("cpfCnpj", cpf.getText().toString());
                    js.put("cep", cep.getText().toString().replaceAll("-", ""));
                    js.put("passwd", passwd.getText().toString());
                    js.put("complemento", complemento.getText().toString());
                    js.put("pjf", rd.isChecked());
                    js.put("nasc", nasc.getText().toString());
                    js.put("id", "" + ValueObject.ID_PROFILE);
                    js.put("idAvatar",ValueObject.UPLOAD_AVATAR);

                    StringBuilder erros = new StringBuilder();
                    if (email.getText().toString().equals("") || !Validate.validateEmail(email.getText().toString())) {
                        erros.append(MAIN.getString(R.string.email));
                        erros.append(" ");
                    }
                    if (nome.getText().toString().equals("")) {
                        erros.append(MAIN.getString(R.string.nome));
                        erros.append(" ");
                    }
                    if (cpf.getText().toString().equals("")) {
                        erros.append(MAIN.getString(R.string.cpf_cnpj));
                        erros.append(" ");
                    }
                    if (cep.getText().toString().equals("")) {
                        erros.append(MAIN.getString(R.string.cep));
                        erros.append(" ");
                    }
                    if (passwd.getText().toString().equals("")) {
                        erros.append(MAIN.getString(R.string.senha));
                        erros.append(" ");
                    }
                    if (complemento.getText().toString().equals("")) {
                        erros.append(MAIN.getString(R.string.complemento));
                        erros.append(" ");
                    }
                    if (nasc.getText().toString().equals("")) {
                        erros.append(MAIN.getString(R.string.nascimento));
                        erros.append(" ");
                    }
                    if (ValueObject.UPLOAD_AVATAR == null) {
                        erros.append(MAIN.getString(R.string.foto_1));
                        erros.append(" ");
                    }

                    if (erros.toString().equals("")) {
                        setSharedPreferences();
                        AssyncSaveProfile assyncSaveProfile = new AssyncSaveProfile(v, js);
                        assyncSaveProfile.execute();
                    } else {
                        ToastHelper.makeToast(getContext(),MAIN.getString(R.string.verifique_erros1) + erros.toString() + MAIN.getString(R.string.verifique_erros2));
                    }


                } catch (Exception ex) {
                    logException(ex);
                } finally{

                }

            }
        });
        //seta as
        getSharedPreferences();
        return v;
    }

    /**
     * js.put("email", email.getText().toString());
     * // ValueObject.UPLOAD_AVATAR;
     * js.put("nome", );
     * js.put("cpfCnpj", cpf.getText().toString());
     * js.put("cep", cep.getText().toString().replaceAll("-", ""));
     * js.put("passwd", passwd.getText().toString());
     * js.put("complemento", complemento.getText().toString());
     * js.put("pjf", rd.isChecked());
     * js.put("nasc", nasc.getText().toString());
     * js.put("id", "" + ValueObject.ID_PROFILE);
     */
    private void setSharedPreferences() {
        this.editor.putString("nome", nome.getText().toString()).commit();
        this.editor.putString("cpfCnpj", cpf.getText().toString()).commit();
        this.editor.putString("cep", cep.getText().toString()).commit();
        this.editor.putString("passwd", passwd.getText().toString()).commit();
        this.editor.putString("complemento", complemento.getText().toString()).commit();
        this.editor.putBoolean("pjf", rd.isChecked()).commit();
        this.editor.putString("nasc", nasc.getText().toString()).commit();
        this.editor.putString("email", email.getText().toString()).commit();
        this.editor.putString("id", ValueObject.ID_PROFILE).commit();
        this.editor.putString("avatar", ValueObject.UPLOAD_AVATAR).commit();
    }


    private void getSharedPreferences() {
        try {
            nome.setText(MY_PREFERENCES.getString("nome", ""));
            cpf.setText(MY_PREFERENCES.getString("cpfCnpj", ""));
            cep.setText(MY_PREFERENCES.getString("cep", ""));
            passwd.setText(MY_PREFERENCES.getString("passwd", ""));
            email.setText(MY_PREFERENCES.getString("email", "@"));
            complemento.setText(MY_PREFERENCES.getString("complemento", ""));
            nasc.setText(MY_PREFERENCES.getString("nasc", ""));
            ValueObject.ID_PROFILE = MY_PREFERENCES.getString("id", "");
            ValueObject.UPLOAD_AVATAR = MY_PREFERENCES.getString("avatar", "");
            rdCNPJ.setChecked(MY_PREFERENCES.getBoolean("pjf", false));
            rd.setChecked(!MY_PREFERENCES.getBoolean("pjf", false));
        }catch (Exception ex){
            logException(ex);
        }
    }
}
