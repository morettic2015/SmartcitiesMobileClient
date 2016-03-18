package view.infoseg.morettic.com.br.infosegapp.view;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import view.infoseg.morettic.com.br.infosegapp.R;
import view.infoseg.morettic.com.br.infosegapp.actions.AssyncLoginRegister;
import view.infoseg.morettic.com.br.infosegapp.actions.AssyncSocialConnect;
import view.infoseg.morettic.com.br.infosegapp.util.Validate;

import static view.infoseg.morettic.com.br.infosegapp.R.style.MyDialog;
import static view.infoseg.morettic.com.br.infosegapp.util.ValueObject.MY_PREFERENCES;

/**
 * Created by LuisAugusto on 16/03/2016.
 */
public class SocialFragment extends DialogFragment {
    private EditText email,senha0,senha1;
    private Button btRegisterSocial;
    private TextView txt;
    private View v;

    static SocialFragment newInstance() {
        return new SocialFragment();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, MyDialog);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.v = inflater.inflate(R.layout.content_webview, container, false);
    try {
        this.email = (EditText) this.v.findViewById(R.id.txtEmailSocial);
        this.senha0 = (EditText) this.v.findViewById(R.id.txtPassRR);
        this.senha1 = (EditText) this.v.findViewById(R.id.txtPassRT);
        this.btRegisterSocial = (Button) v.findViewById(R.id.btRegisterSocial);
        this.txt = (TextView)v.findViewById(R.id.textViewMensagemCad);
        this.btRegisterSocial.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //t(View activity, String email, String senha, Activity aWin)
                //Se for valido email e as duas senhas iguais submete.....
                if (!email.getText().toString().equals("")&&Validate.validateEmail(email.getText().toString())&&senha0.getText().toString().equals(senha1.getText().toString())) {
                    AssyncSocialConnect assyncSocialConnect;
                    assyncSocialConnect = new AssyncSocialConnect(v, email.getText().toString(), senha0.getText().toString(), getActivity(),txt,email,senha0,senha1);
                    assyncSocialConnect.execute();
                }
            }
        });



        email.getText().toString();
    }catch(Exception e){
        e.printStackTrace();
    }
        return this.v;
    }
}
