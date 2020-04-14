package org.avmrus.fopds.inet;

import android.os.AsyncTask;
import android.util.Log;

import org.avmrus.fopds.Constants;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class InetDownloader extends AsyncTask<String, Integer, byte[]> {
    private Reply.UrlListener urlListener;
    private Reply.ProgressListener progressListener;
    private Reply.ErrorListener errorListener;
    private String filename;
    private int filesize;


    public String getFilename() {
        return this.filename;
    }

    public void setListeners(Reply.UrlListener urlListener, Reply.ProgressListener progressListener, Reply.ErrorListener errorListener) {
        this.urlListener = urlListener;
        this.progressListener = progressListener;
        this.errorListener = errorListener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected byte[] doInBackground(String... urls) {
        byte[] data = {};
        try {
            URL url = new URL(urls[0]);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                this.filesize = connection.getHeaderFieldInt("Content-Length", 0);
                String filenameField = connection.getHeaderField("Content-Disposition");
                this.filename = filenameField.replaceFirst("(?i)^.*filename=\"?([^\"]+)\"?.*$", "$1");
                InputStream inputStream = connection.getInputStream();
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int totalRead = 0;
                int bytesRead = 0;
                Log.d(Constants.LOG_TAG, "file " + filename + " size: " + filesize);
                while ((bytesRead = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, bytesRead);
                    totalRead = totalRead + bytesRead;
                    publishProgress((100 * totalRead) / this.filesize);
//                    Log.d(Settings.LOG_TAG, "read " + bytesRead);
                }
                data = outputStream.toByteArray();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        if (progressListener != null) {
            progressListener.onProgress(values[0]);
        }
    }


    @Override
    protected void onPostExecute(byte[] data) {
        super.onPostExecute(data);
        if (data.length > 0) {
            if (urlListener != null) {
                urlListener.onSuccess(data);
            }
        } else {
            if (errorListener != null) {
                errorListener.onError("Error ocured while downloading file");
            }
        }

    }
}
