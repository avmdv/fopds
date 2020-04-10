package org.avmrus.fopds;

import android.content.Context;
import android.content.SharedPreferences;

public class Settings {
    private static volatile Settings instance;
    public final static String LOG_TAG = "fopds: ";
    private Context context;
    private String siteUrl;
    private String libraryPath;
    private String preferredFormat;
    private Boolean blacklist;

    private Settings() {
        super();
    }

    public static Settings getInstance() {
        if (instance == null) {
            synchronized (Settings.class) {
                if (instance == null) {
                    instance = new Settings();
                }
            }
        }
        return instance;
    }

    public void init(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public String getSiteUrl() {
        return siteUrl;
    }

    public String getPreferredFormat() {
        return preferredFormat;
    }

    public String getLibraryPath() {
        return libraryPath;
    }

    public Boolean getBlacklist() {
        return blacklist;
    }

    public void readPreferences(SharedPreferences preferences) {
        this.siteUrl = preferences.getString("site_url", "http://flibusta.is");
        this.libraryPath = preferences.getString("library_path", "Books");
        this.preferredFormat = preferences.getString("download_format", "FB2");
        this.blacklist = preferences.getBoolean("blacklist", false);
    }
}
