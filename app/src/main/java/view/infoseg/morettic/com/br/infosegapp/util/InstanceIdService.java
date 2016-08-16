package view.infoseg.morettic.com.br.infosegapp.util;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Belal on 5/27/2016.
 */


//Class extending FirebaseInstanceIdService
public class InstanceIdService extends FirebaseInstanceIdService {
    private static InstanceIdService instance;
    private static final String TAG = "MyFirebaseIIDService";
    private static String token = null;

    public static InstanceIdService getInstance(){
        if(instance==null)
            instance = new InstanceIdService();

        return instance;
    }

    public static String getToken(){
        getInstance().onTokenRefresh();
        return token;
    }

    @Override
    public void onTokenRefresh() {

        //Getting registration token
        token = FirebaseInstanceId.getInstance().getToken();

        //Displaying token on logcat
        Log.d(TAG, "Refreshed token: " + token);

    }

    private void sendRegistrationToServer(String token) {
        //You can implement this method to store the token on your server
        //Not required for current project
    }
}