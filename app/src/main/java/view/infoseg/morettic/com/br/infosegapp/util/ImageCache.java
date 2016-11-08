package view.infoseg.morettic.com.br.infosegapp.util;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

/**
 * Created by LuisAugusto on 26/08/2016.
 */
public class ImageCache {
    private static LruCache<String, Bitmap> mMemoryCache;


    private static final void getInstanceOf() {

        if(mMemoryCache==null) {
            final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

            // Use 1/8th of the available memory for this memory cache.
            final int cacheSize = maxMemory / 8;
            mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
                @Override
                protected int sizeOf(String key, Bitmap bitmap) {
                    // The cache size will be measured in kilobytes rather than
                    // number of items.
                    return bitmap.getByteCount() / 1024;
                }
            };
        }
    }

    public static void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        getInstanceOf();
        //if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        //}
    }

    public static boolean hasBitmapFromMemCache(String key) {
        getInstanceOf();
        return mMemoryCache.get(key)==null?false:true;
    }

    public static Bitmap getBitmapFromMemCache(String key) {
        getInstanceOf();
        return mMemoryCache.get(key);
    }
}
