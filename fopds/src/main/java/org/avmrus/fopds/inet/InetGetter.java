package org.avmrus.fopds.inet;

import android.graphics.Bitmap;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

public class InetGetter {
    public void getUrl(String url, final Reply.UrlListener urlListener, final Reply.ErrorListener errorListener) {
        InetUrlRequest request = new InetUrlRequest(url, new Response.Listener<byte[]>() {
            @Override
            public void onResponse(byte[] response) {
                urlListener.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                errorListener.onError(parseVolleyError(error));
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(3000, 3, (float) 1.5));
        InetQueue.getInstance().getRequestQueue().add(request);
    }

    public void getImage(int priority, String url, final Reply.ImageListener imageListener, final Reply.ErrorListener errorListener) {
        InetImageRequest request = new InetImageRequest(url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                imageListener.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                errorListener.onError(parseVolleyError(error));
            }
        });
        request.setPriority(priority);
        InetQueue.getInstance().getRequestQueue().add(request);
    }

    private String parseVolleyError(VolleyError error) {
        String result = "";
        if (error instanceof NetworkError) {
            result = "Network error";
        } else if (error instanceof ServerError) {
            result = "Server error";
        } else if (error instanceof AuthFailureError) {
            result = "Authorisation error";
        } else if (error instanceof ParseError) {
            result = "Parsing error";
        } else if (error instanceof NoConnectionError) {
            result = "No connection error";
        } else if (error instanceof TimeoutError) {
            result = "Timeout error";
        }
        return result;
    }
}
