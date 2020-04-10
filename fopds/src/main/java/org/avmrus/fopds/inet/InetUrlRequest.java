package org.avmrus.fopds.inet;

import android.support.annotation.NonNull;

import com.android.volley.Request;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

public class InetUrlRequest extends Request<byte[]> {
    final Response.Listener<byte[]> responseListener;

    public InetUrlRequest(String url, @NonNull Response.Listener<byte[]> listener, Response.ErrorListener errorListener) {
        super(Method.GET, url, errorListener);
        this.responseListener = listener;
    }

    @Override
    protected Response<byte[]> parseNetworkResponse(NetworkResponse response) {
        return Response.success(response.data, HttpHeaderParser.parseCacheHeaders(response));
    }

    @Override
    protected void deliverResponse(byte[] response) {
        responseListener.onResponse(response);
    }
}
