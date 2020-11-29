package cl.alfa.alfalab;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.animation.StateListAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import cl.alfa.alfalab.activities.OnBoardingActivity;
import cl.alfa.alfalab.activities.SettingsActivity;
import cl.alfa.alfalab.adapters.TabsAdapter;
import cl.alfa.alfalab.fragments.Delivered;
import cl.alfa.alfalab.fragments.Orders;
import cl.alfa.alfalab.interfaces.FabInterface;
import cl.alfa.alfalab.utils.SharedPreferences;

import android.view.Gravity;
import android.view.View;
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

    public AppBarLayout appBarLayout;
    public View appBarLayoutShadow;
    private FabInterface fabInterface;
    private FloatingActionButton fab;

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

        final ViewPager2 mViewPager = findViewById(R.id.vp_tabs);
        final StateListAnimator stateListAnimator = new StateListAnimator();
        final TabLayout mSlidingTabLayout = findViewById(R.id.stl_tabs);
        final Toolbar mToolbar = findViewById(R.id.toolbar);
        final TextView toolbarTitle = mToolbar.findViewById(R.id.toolbar_title);
        fab = findViewById(R.id.fab);
        appBarLayout = findViewById(R.id.main_appbar_layout);
        appBarLayoutShadow = findViewById(R.id.divider);

        fab.setOnClickListener(v -> {
            if (mViewPager.getCurrentItem() == 0) {
                fabInterface.onFabClicked();
            }
        });

        stateListAnimator.addState(new int[0], ObjectAnimator.ofFloat(appBarLayout, "elevation", 2));
        appBarLayout.setStateListAnimator(stateListAnimator);

        if(mSharedPreferences.loadNightModeState()) {
            mToolbar.setTitleTextAppearance(context, R.style.ToolbarTypefaceDark);
        } else {
            mToolbar.setTitleTextAppearance(context, R.style.ToolbarTypefaceLight);
        }
        setSupportActionBar(mToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        toolbarTitle.setText(R.string.app_name);
        Toolbar.LayoutParams llp = new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
        llp.gravity = Gravity.CENTER;
        toolbarTitle.setLayoutParams(llp);

        final TabsAdapter adapter = new TabsAdapter(this, context);
        adapter.addFragment(new Orders(), context.getResources().getString(R.string.orders));
        adapter.addFragment(new Delivered(), context.getResources().getString(R.string.delivered));

        mViewPager.setAdapter(adapter);
        mSlidingTabLayout.setElevation(0);
        final TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(mSlidingTabLayout, mViewPager, true, (tab, position) -> {
            tab.setText(adapter.getPageTitle(position));
            tab.setCustomView(adapter.getTabView(position));
        });
        tabLayoutMediator.attach();
        if (mSharedPreferences.loadNightModeState()) {
            mSlidingTabLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark)); //NIGHT MODE
        } else {
            mSlidingTabLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight)); //DAY MODE
        }
        mSlidingTabLayout.setUnboundedRipple(false);
        mSlidingTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 1) {
                    fab.hide();
                } else {
                    fab.show();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
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
                finish();
                return true;
            /*TODO: WORK IN PROGRESS*/
            /*case R.id.action_search:
                startActivity(new Intent(context, SearchActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
                return true;*/
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

    public AppBarLayout getAppBarLayout(){
        return appBarLayout;
    }

    public View getAppBarLayoutShadow(){
        return appBarLayoutShadow;
    }

    public void setListener(FabInterface fabInterface) {
        this.fabInterface = fabInterface;
    }
}
