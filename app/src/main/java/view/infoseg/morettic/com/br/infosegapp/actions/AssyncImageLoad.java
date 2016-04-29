package view.infoseg.morettic.com.br.infosegapp.actions;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.InputStream;

import view.infoseg.morettic.com.br.infosegapp.util.HttpUtil;
import view.infoseg.morettic.com.br.infosegapp.util.ValueObject;

/**
 * Created by LuisAugusto on 05/03/2016.
 */
public class AssyncImageLoad extends AsyncTask<String, Void, Bitmap> {
    private String key;
    private String img;

    public AssyncImageLoad(String im, String key) {
        this.key = key;
        this.img = im;
    }

    public Bitmap doInBackground(String... urls) {

        Bitmap mIcon11 = null;
        try {

            mIcon11 = HttpUtil.getBitmapFromURLBlobKey(this.key);
            mIcon11 = HttpUtil.getResizedBitmap(mIcon11,250,160);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }

        if(img.equals("0")){
            ValueObject.IMG_OCORRENCIA = mIcon11;
        }else{
            ValueObject.IMG_AUTHOR = mIcon11;
        }

        return mIcon11;
    }

}