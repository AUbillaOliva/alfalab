package cl.alfa.alfalab;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import cl.alfa.alfalab.activities.OnBoardingActivity;
import cl.alfa.alfalab.activities.SearchActivity;
import cl.alfa.alfalab.activities.SettingsActivity;
import cl.alfa.alfalab.fragments.bottom_menu_fragments.HomeFragment;
import cl.alfa.alfalab.fragments.bottom_menu_fragments.NotificationsFragment;
import cl.alfa.alfalab.fragments.bottom_menu_fragments.OrdersFragment;
import cl.alfa.alfalab.utils.SharedPreferences;

import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    public static final String API = "API_RESPONSE";
    public static final String UI = "INTERFACE";
    public static final String SYS = "SYSTEM";
    public static final String REP = "REPORT";

    public static final int REQUEST_CODE = 1;

    public static final int cacheSize = 10 * 1024 * 1024; // 10 MiB

    private final Context context = this;

    private static BottomNavigationView mBottomNavigationView;
    @SuppressLint("StaticFieldLeak")
    private static Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final SharedPreferences mSharedPreferences = new SharedPreferences(context);

        if(mSharedPreferences.isFirstTime()) {
            startActivity(new Intent(context, OnBoardingActivity.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        }

        if(mSharedPreferences.getToken() == null) {
            startActivity(new Intent(context, OnBoardingActivity.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        }

        if (mSharedPreferences.loadNightModeState()) {
            setTheme(R.style.AppThemeDark);
        } else {
            setTheme(R.style.AppTheme);
        }
        setContentView(R.layout.activity_main);

        mToolbar = findViewById(R.id.toolbar);
        final TextView toolbarTitle = mToolbar.findViewById(R.id.toolbar_title);
        mBottomNavigationView = findViewById(R.id.bottom_navigation_view);

        if(mSharedPreferences.loadNightModeState()) {
            mToolbar.setTitleTextAppearance(context, R.style.ToolbarTypefaceDark);
        } else {
            mToolbar.setTitleTextAppearance(context, R.style.ToolbarTypefaceLight);
        }
        setSupportActionBar(mToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        toolbarTitle.setText(R.string.app_name);
        final Toolbar.LayoutParams llp = new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
        llp.gravity = Gravity.CENTER;
        toolbarTitle.setLayoutParams(llp);

        //TODO: ADD BADGES
        mBottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Fragment fragment = null;
            if(mBottomNavigationView.getSelectedItemId() != item.getItemId()) {
                switch (item.getItemId()) {
                    case R.id.orders:
                        if (mBottomNavigationView.getBadge(item.getItemId()) != null) {
                            mBottomNavigationView.removeBadge(item.getItemId());
                        }
                        fragment = new OrdersFragment();
                        break;
                    case R.id.home:
                        if (mBottomNavigationView.getBadge(item.getItemId()) != null) {
                            mBottomNavigationView.removeBadge(item.getItemId());
                        }
                        fragment = new HomeFragment();
                        break;
                    case R.id.notifications:
                        if (mBottomNavigationView.getBadge(item.getItemId()) != null) {
                            mBottomNavigationView.removeBadge(item.getItemId());
                        }
                        fragment = new NotificationsFragment();
                        break;
                }
                replaceFragment(fragment);
            }
            return true;
        });

        setInitialFragment();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivity(new Intent(context, SettingsActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                return true;
            case R.id.action_search:
                startActivity(new Intent(context, SearchActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull final String[] permissions, @NonNull final int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if(ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_CODE);
            }
        }
    }

    public static void setBadge(int count, int itemId) {
        final Menu menu = mBottomNavigationView.getMenu();
        final MenuItem bottomItem = menu.findItem(itemId);
        final BadgeDrawable badge = mBottomNavigationView.getOrCreateBadge(bottomItem.getItemId());
        Objects.requireNonNull(mBottomNavigationView.getBadge(bottomItem.getItemId())).setNumber(count);
        badge.setBackgroundColor(Color.parseColor("#B00020"));
    }

    private void setInitialFragment() {
        final FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.parent_fragment_container, new OrdersFragment());
        fragmentTransaction.commit();
    }

    private void replaceFragment(Fragment fragment) {
        final FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.parent_fragment_container, fragment);
        fragmentTransaction.commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public static Toolbar getToolbar() { return mToolbar; }

    public static BottomNavigationView getmBottomNavigationView() {
        return mBottomNavigationView;
    }

}
