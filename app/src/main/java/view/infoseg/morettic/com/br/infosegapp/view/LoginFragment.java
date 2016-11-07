package view.infoseg.morettic.com.br.infosegapp.view;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import view.infoseg.morettic.com.br.infosegapp.R;
import view.infoseg.morettic.com.br.infosegapp.util.FacebookUtil;
import view.infoseg.morettic.com.br.infosegapp.util.TwitterUtil;

import static view.infoseg.morettic.com.br.infosegapp.util.ValueObject.AUTENTICADO;
import static view.infoseg.morettic.com.br.infosegapp.util.ValueObject.LOGIN;
import static view.infoseg.morettic.com.br.infosegapp.util.ValueObject.MAIN;
import static view.infoseg.morettic.com.br.infosegapp.view.InfosegMain.logException;

/**
 * Created by LuisAugusto on 02/03/2016.
 */
public class LoginFragment extends DialogFragment implements View.OnClickListener {
    private Button google, facebook, twitter;
    private static LoginFragment loginFragment;

    public void onStop() {
        super.onStop();
    }

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

        this.google = (Button) v.findViewById(R.id.imageButtonGoogle);
        this.google.setOnClickListener(this);
        this.facebook = (Button) v.findViewById(R.id.imageButtonFacebook);
        this.facebook.setOnClickListener(this);
        this.twitter = (Button) v.findViewById(R.id.imageButtonTwitter);
        this.twitter.setOnClickListener(this);


        return v;
    }


    public void onDismiss(final DialogInterface dialog) {
        //final Activity activity = getActivity();
        try {
            if (!AUTENTICADO) {
                LOGIN = LoginFragment.newInstance();
                LOGIN.show(getFragmentManager(), "dialog");
            } else {
                LOGIN.dismiss();
                this.dismiss();
            }
        } catch (Exception ex) {
            logException(ex);
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

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.imageButtonGoogle:
                Intent googlePlus = new Intent(MAIN, ActivityGPlus.class);
                MAIN.startActivity(googlePlus);
                break;

            case R.id.imageButtonFacebook:
                // SocialFragment.newInstance().show(getFragmentManager(), "dialog");
                FacebookUtil.clickFace();
                break;
            case R.id.imageButtonTwitter:
                TwitterUtil.click();
                break;

        }
    }
}