package view.infoseg.morettic.com.br.infosegapp.util;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Created by LuisAugusto on 17/08/2016.
 */
public class ToastHelper {
    private Context ctx;
    private int duration = Toast.LENGTH_LONG;


    public static final void makeToast(Context context, String message){
        CharSequence text = message;
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.setGravity(Gravity.CENTER_HORIZONTAL| Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();
    }

}
