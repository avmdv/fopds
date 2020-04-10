package org.avmrus.fopds;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;

import org.avmrus.fopds.inet.InetQueue;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setPermissions();
        initInetQueue(getApplicationContext());
        initSettings(getApplicationContext());
        showMainFragment();
    }

    private void setPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }
    }

    private void initInetQueue(Context context) {
        InetQueue.getInstance().init(context);
    }

    private void initSettings(Context context) {
        Settings.getInstance().init(context);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        Settings.getInstance().readPreferences(preferences);
    }

    public void showMainFragment() {
        getSupportFragmentManager().beginTransaction().add(R.id.fragmentContainer, new MainFragment(), "mainFragment").commit();
    }

    private void updateMainFragment() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            FragmentTransaction ftrans = getSupportFragmentManager().beginTransaction();
            ftrans.remove(getSupportFragmentManager().findFragmentByTag("mainFragment")).commit();
            showMainFragment();
        }
    }

    private void showSettingsFragment() {
        FragmentTransaction ftrans = getSupportFragmentManager().beginTransaction();
        ftrans.addToBackStack(null);
        ftrans.replace(R.id.fragmentContainer, new SettingsFragment()).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            menu.findItem(R.id.menuUpdate).setEnabled(true);
        } else {
            menu.findItem(R.id.menuUpdate).setEnabled(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuSettings:
                showSettingsFragment();
                break;
            case R.id.menuUpdate:
                updateMainFragment();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
