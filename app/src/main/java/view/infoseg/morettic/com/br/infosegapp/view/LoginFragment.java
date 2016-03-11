package view.infoseg.morettic.com.br.infosegapp.view;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import view.infoseg.morettic.com.br.infosegapp.R;
import view.infoseg.morettic.com.br.infosegapp.actions.AssyncLoginRegister;
import view.infoseg.morettic.com.br.infosegapp.util.ValueObject;

import static view.infoseg.morettic.com.br.infosegapp.util.ValueObject.MY_PREFERENCES;

/**
 * Created by LuisAugusto on 02/03/2016.
 */
public class LoginFragment extends DialogFragment {

    private Button btLogin;
    private EditText email, senha;


    static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.MyDialog);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_login, container, false);

        btLogin = (Button) v.findViewById(R.id.btLoginRegister1);
        email = (EditText) v.findViewById(R.id.txtEmailLG);
        senha = (EditText) v.findViewById(R.id.txtSenhaLG);
        email.setText(MY_PREFERENCES.getString("email", ""));
        senha.setText(MY_PREFERENCES.getString("passwd", ""));

        btLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (email.getText().toString() == null || email.getText().toString().equals("")) {
                    email.setFocusable(true);
                } else if (senha.getText().toString() == null || senha.getText().toString().equals("")) {
                    senha.setFocusable(true);
                } else {
                    AssyncLoginRegister assyncLoginRegister = new AssyncLoginRegister(v, email.getText().toString(), senha.getText().toString());
                    assyncLoginRegister.execute();
                }


            }
        });


        return v;
    }

    public void onDismiss(final DialogInterface dialog) {
        //final Activity activity = getActivity();
        try {
            if (!ValueObject.AUTENTICADO) {
                LoginFragment loginFragment = LoginFragment.newInstance();
                loginFragment.show(getFragmentManager(), "dialog");
            } else {
                this.dismiss();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void onStart() {
        super.onStart();

        // safety check
        if (getDialog() == null)
            return;

        int width = getResources().getDimensionPixelSize(R.dimen.popup_width);
        int height = getResources().getDimensionPixelSize(R.dimen.popup_height);
        getDialog().getWindow().setLayout(width, height);

        getDialog().getWindow().setLayout(width, height);

        // ... other stuff you want to do in your onStart() method
    }
}