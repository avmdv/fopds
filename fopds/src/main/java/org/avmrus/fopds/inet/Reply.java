package org.avmrus.fopds.inet;

import android.graphics.Bitmap;

public class Reply {

    public interface UrlListener {
        void onSuccess(byte[] reply);
    }

    public interface ImageListener {
        void onSuccess(Bitmap bitmap);
    }

    public interface ProgressListener {
        void onProgress(int progress);
    }

    public interface ErrorListener {
        void onError(String error);
    }
}
