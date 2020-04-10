package org.avmrus.fopds.inet;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

public class InetQueue {
    private static volatile InetQueue instance;
    private Context context;
    private RequestQueue requestQueue;
    private ImageLoader imageLoader;


    private InetQueue() {
        super();
    }


    public void init(Context context) {
        getInstance().context = context;
    }


    public static InetQueue getInstance() {
        if (instance == null) {
            synchronized (InetQueue.class) {
                if (instance == null) {
                    instance = new InetQueue();
                }
            }
        }
        return instance;
    }


    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context);
        }
        return requestQueue;
    }


    public Context getContext() {
        return context;
    }


    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (imageLoader == null) {
            imageLoader = new ImageLoader(requestQueue, new ImageLoader.ImageCache() {
                private final LruCache<String, Bitmap> cache = new LruCache<>(20);

                @Override
                public Bitmap getBitmap(String url) {
                    return cache.get(url);
                }

                @Override
                public void putBitmap(String url, Bitmap bitmap) {
                    cache.put(url, bitmap);
                }
            });
        }
        return imageLoader;
    }
}
