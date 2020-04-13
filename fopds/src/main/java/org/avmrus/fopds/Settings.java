package org.avmrus.fopds;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Settings {
    private static volatile Settings instance;
    public final static String LOG_TAG = "fopds: ";
    private Context context;
    private String siteUrl;
    private String libraryPath;
    private String preferredFormat;
    private Boolean blacklist;
    private SharedPreferences preferences;

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
        this.preferences = PreferenceManager.getDefaultSharedPreferences(context);
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
        return preferences.getBoolean("blacklist", false);
    }

    public SharedPreferences getPreferences() {
        return preferences;
    }

    public void readPreferences() {
        this.siteUrl = preferences.getString("site_url", "http://flibusta.is");
        this.libraryPath = preferences.getString("library_path", "Books");
        this.preferredFormat = preferences.getString("download_format", "FB2");
        this.blacklist = preferences.getBoolean("blacklist", false);
    }

    public void storeArrayList(String key, ArrayList list) {
        SharedPreferences.Editor editor = preferences.edit();
        Set<String> set = new HashSet<String>();
        set.addAll(list);
        editor.putStringSet(key, set);
        editor.commit();
    }

    public ArrayList restoreArrayList(String key) {
        ArrayList list;
        Set<String> set = preferences.getStringSet(key, null);
        if (set != null) {
            list =new ArrayList(set);
        } else {
            list = new ArrayList();
        }
        return list;
    }

}
