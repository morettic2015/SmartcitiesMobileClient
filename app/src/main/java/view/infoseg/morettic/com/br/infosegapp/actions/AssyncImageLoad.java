package view.infoseg.morettic.com.br.infosegapp.actions;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import view.infoseg.morettic.com.br.infosegapp.util.HttpUtil;
import view.infoseg.morettic.com.br.infosegapp.util.ImageCache;
import view.infoseg.morettic.com.br.infosegapp.util.ValueObject;

import static view.infoseg.morettic.com.br.infosegapp.view.InfosegMain.logException;

/**
 * Created by LuisAugusto on 05/03/2016.
 */
public class AssyncImageLoad extends AsyncTask<String, Void, Bitmap> {
    private String key;
    private String img,path;

    public AssyncImageLoad(String im, String key, String path) {
        this.key = key;
        this.img = im;
        this.path = path;

    }

    public Bitmap doInBackground(String... urls) {

        Bitmap mIcon11 = null;
        try {
            if(ImageCache.hasBitmapFromMemCache(this.key)){
                mIcon11 =  ImageCache.getBitmapFromMemCache(this.key);
            }else if(path==null){//Big data of experience
                mIcon11 = HttpUtil.getBitmapFromURLBlobKey(this.key);
                mIcon11 = HttpUtil.getResizedBitmap(mIcon11, 250, 250);
                ImageCache.addBitmapToMemoryCache(this.key,mIcon11);
            }else{//Data from genimo openstreemap

                mIcon11 = HttpUtil.getBitmapFromURL(path);
                mIcon11 = HttpUtil.getResizedBitmap(mIcon11, 250, 250);
                ImageCache.addBitmapToMemoryCache(this.key,mIcon11);
            }
        } catch (Exception ex) {
            logException(ex);
        }

        if(img.equals("0")){
            ValueObject.IMG_OCORRENCIA = mIcon11;
        }else{
            ValueObject.IMG_AUTHOR = mIcon11;
        }

        return mIcon11;
    }

}