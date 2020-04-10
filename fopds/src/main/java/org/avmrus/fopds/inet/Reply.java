package org.avmrus.fopds.inet;

import android.graphics.Bitmap;

public class Reply {

    public interface UrlListener {
        public void onSuccess(byte[] reply);
    }

    public interface ImageListener {
        public void onSuccess(Bitmap bitmap);
    }

    public interface ProgressListener {
        public void onProgress(int progress);
    }

    public interface ErrorListener {
        public void onError(String error);
    }
}
