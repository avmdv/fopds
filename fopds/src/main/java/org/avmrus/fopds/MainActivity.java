package org.avmrus.fopds;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import org.avmrus.fopds.inet.InetQueue;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setPermissions();
        InetQueue.getInstance().init(getApplicationContext());
        Settings.getInstance().init(getApplicationContext());
        showMainFragment();
    }

    private void setPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }
    }

    public void showMainFragment() {
        MainFragment mainFragment = (MainFragment) getSupportFragmentManager().findFragmentByTag(Constants.FRAGMENT_MAIN);
        if (mainFragment == null) {
            mainFragment = new MainFragment();
        }
        getSupportFragmentManager().popBackStack();
        getSupportFragmentManager().executePendingTransactions();
        if ((!mainFragment.isAdded()) && (getSupportFragmentManager().getBackStackEntryCount() == 0)) {
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, mainFragment, Constants.FRAGMENT_MAIN).commit();
        }
    }

    private void updateMainFragment() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            FragmentTransaction ftrans = getSupportFragmentManager().beginTransaction();
            ftrans.remove(getSupportFragmentManager().findFragmentByTag(Constants.FRAGMENT_MAIN)).commit();
            showMainFragment();
        }
    }

    private void showSettingsFragment() {
        SettingsFragment settingsFragment = (SettingsFragment) getSupportFragmentManager().findFragmentByTag(Constants.FRAGMENT_SETTINGS);
        if (settingsFragment == null) {
            settingsFragment = new SettingsFragment();
            FragmentTransaction ftrans = getSupportFragmentManager().beginTransaction();
            ftrans.addToBackStack(null);
            ftrans.replace(R.id.fragment_container, settingsFragment, Constants.FRAGMENT_SETTINGS).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(Constants.FRAGMENT_MAIN);
        if ((fragment != null) && (fragment.isVisible())) {
            menu.findItem(R.id.menu_item_update).setVisible(true);
        } else {
            menu.findItem(R.id.menu_item_update).setVisible(false);
        }
        SettingsFragment settingsFragment = (SettingsFragment) getSupportFragmentManager().findFragmentByTag(Constants.FRAGMENT_SETTINGS);
        if ((settingsFragment != null) && (settingsFragment.isVisible())) {
            menu.findItem(R.id.menu_item_settings).setEnabled(false);
        } else {
            menu.findItem(R.id.menu_item_settings).setEnabled(true);
        }
        AboutFragment aboutFragment = (AboutFragment) getSupportFragmentManager().findFragmentByTag(Constants.FRAGMENT_ABOUT);
        if ((aboutFragment != null) && (aboutFragment.isVisible())) {
            menu.findItem(R.id.menu_item_about).setEnabled(false);
        } else {
            menu.findItem(R.id.menu_item_about).setEnabled(true);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_settings:
                showSettingsFragment();
                break;
            case R.id.menu_item_update:
                updateMainFragment();
                break;
            case R.id.menu_item_about:
                showAboutFragment();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showAboutFragment() {
        AboutFragment aboutFragment = (AboutFragment) getSupportFragmentManager().findFragmentByTag(Constants.FRAGMENT_ABOUT);
        if (aboutFragment == null) {
            aboutFragment = new AboutFragment();
            FragmentTransaction ftrans = getSupportFragmentManager().beginTransaction();
            ftrans.addToBackStack(null);
            ftrans.replace(R.id.fragment_container, aboutFragment, Constants.FRAGMENT_ABOUT).commit();
        }
    }
}
