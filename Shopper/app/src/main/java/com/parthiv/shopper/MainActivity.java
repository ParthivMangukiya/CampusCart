package com.parthiv.shopper;

import android.Manifest;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView mBottomNavigation;
    private static final int RC_INTERNET = 111;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBottomNavigation = findViewById(R.id.bottom_navigation);
        mBottomNavigation.setOnNavigationItemSelectedListener(
                item -> {
                    switch (item.getItemId()) {
                        case R.id.action_items:
                            break;
                    }
                    return true;
                });
        loadActivity();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @AfterPermissionGranted(RC_INTERNET)
    private void loadActivity() {

        String[] perms = new String[]{Manifest.permission.ACCESS_NETWORK_STATE,Manifest.permission.INTERNET,Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this, perms)) {
            viewPager = findViewById(R.id.viewpager);
            MainActivityPagerAdapter mainActivityPagerAdapter = new MainActivityPagerAdapter(getSupportFragmentManager());
            viewPager.setAdapter(mainActivityPagerAdapter);

        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.internet_permission), RC_INTERNET, perms);
        }
    }


}
