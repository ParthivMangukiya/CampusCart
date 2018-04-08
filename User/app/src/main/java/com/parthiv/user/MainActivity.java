package com.parthiv.user;

import android.Manifest;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.parthiv.user.Utils.Resource;
import com.parthiv.user.Utils.SharedPreferenceUtils;
import com.parthiv.user.model.User;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView mBottomNavigation;
    private FirebaseAuth mAuth;
    private static final int RC_INTERNET = 111;
    private ViewPager viewPager;
    private MainActivityPagerAdapter mMainActivityPagerAdapter;
    private MainActivityViewModel mMainActivityViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager = findViewById(R.id.viewpager);
        mAuth = FirebaseAuth.getInstance();
        mMainActivityPagerAdapter = new MainActivityPagerAdapter(getSupportFragmentManager());
        mMainActivityViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
        viewPager.setAdapter(mMainActivityPagerAdapter);
        mBottomNavigation = findViewById(R.id.bottom_navigation);
        User user = new User();
        FirebaseUser fuser = mAuth.getCurrentUser();
        user.setEmail(fuser.getEmail());
        user.setName(fuser.getDisplayName());
        mMainActivityViewModel.postUser(user).observe(this,userResource -> {
            if(userResource.status == Resource.Status.SUCCESS){
                SharedPreferenceUtils.setLongPreference(this,"uid",userResource.data.getId());
            }else{
                Toast.makeText(this,"User Not Created",Toast.LENGTH_LONG).show();
            }
        });
        mBottomNavigation.setOnNavigationItemSelectedListener(
                item -> {
                    switch (item.getItemId()) {
                        case R.id.action_items:
                            viewPager.setCurrentItem(0);
                            break;
                        case R.id.action_cart:
                            viewPager.setCurrentItem(1);
                            break;
                        case R.id.action_orders:
                            viewPager.setCurrentItem(2);
                            break;
                    }
                    return true;
                });
        loadActivity();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.sign_out:
                signOut();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void signOut() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        startActivity(new Intent(MainActivity.this, SignInActivity.class));
                        finish();
                    }
                });
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
