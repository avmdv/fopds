package org.avmrus.fopds.inet;

import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;

public class InetImageRequest extends ImageRequest {
    private Priority priority;

    public InetImageRequest(String url, Response.Listener<Bitmap> listener, int maxWidth, int maxHeight, ImageView.ScaleType scaleType, Bitmap.Config decodeConfig, @Nullable Response.ErrorListener errorListener) {
        super(url, listener, maxWidth, maxHeight, scaleType, decodeConfig, errorListener);
    }

    public InetImageRequest(String url, Response.Listener<Bitmap> listener, Response.ErrorListener errorListener) {
        super(url, listener, 0, 0, ImageView.ScaleType.CENTER_INSIDE, null, errorListener);
    }

    @Override
    public Priority getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        switch (priority) {
            case InetPriority.PRIORITY_IMMEDIATE:
                this.priority = Priority.IMMEDIATE;
                break;
            case InetPriority.PRIORITY_HIGH:
                this.priority = Priority.HIGH;
                break;
            case InetPriority.PRIORITY_NORMAL:
                this.priority = Priority.NORMAL;
                break;
            case InetPriority.PRIORITY_LOW:
                this.priority = Priority.LOW;
                break;
            default:
                this.priority = Priority.LOW;
                break;
        }
    }
}
