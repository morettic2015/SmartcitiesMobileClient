package view.infoseg.morettic.com.br.infosegapp.actions;

/**
 * Created by LuisAugusto on 23/08/2016.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import static view.infoseg.morettic.com.br.infosegapp.view.InfosegMain.logException;

public class TAPPXBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            if (intent.getAction().equals("com.android.vending.INSTALL_REFERRER")) {
                //Tappx Track Install
                try {
                    com.tappx.TrackInstall tappx = new com.tappx.TrackInstall();
                    tappx.onReceive(context, intent);
                } catch (Exception ex) {
                    logException(ex);
                }
                //Other tracking sdk/networks
            }
        }
    }
}