package cl.alfa.alfalab;

import android.animation.ObjectAnimator;
import android.animation.StateListAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;
import cl.alfa.alfalab.activities.OnBoardingActivity;
import cl.alfa.alfalab.activities.SettingsActivity;
import cl.alfa.alfalab.adapters.TabsAdapter;
import cl.alfa.alfalab.fragments.Delivered;
import cl.alfa.alfalab.fragments.Orders;
import cl.alfa.alfalab.fragments.Pending;
import cl.alfa.alfalab.utils.SharedPreferences;
import cl.alfa.alfalab.utils.databases.OrdersDatabaseHelper;

import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "API_RESPONSE";
    public static final String UI = "INTERFACE";
    private final Context context = this;

    public static final int cacheSize = 10 * 1024 * 1024; // 10 MiB

    public Toolbar mToolbar;
    public AppBarLayout appBarLayout;
    public View appBarLayoutShadow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences mSharedPreferences = new SharedPreferences(context);

        if(mSharedPreferences.isFirstTime()){
            startActivity(new Intent(context, OnBoardingActivity.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        }

        if (mSharedPreferences.loadNightModeState())
            setTheme(R.style.AppThemeDark);
        else
            setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_main);

        final ViewPager2 mViewPager = findViewById(R.id.vp_tabs);
        final StateListAnimator stateListAnimator = new StateListAnimator();
        final TabLayout mSlidingTabLayout = findViewById(R.id.stl_tabs);
        mToolbar = findViewById(R.id.toolbar);
        appBarLayout = findViewById(R.id.main_appbar_layout);
        appBarLayoutShadow = findViewById(R.id.divider);
        TextView toolbarTitle = mToolbar.findViewById(R.id.toolbar_title);

        stateListAnimator.addState(new int[0], ObjectAnimator.ofFloat(appBarLayout, "elevation", 2));
        appBarLayout.setStateListAnimator(stateListAnimator);

        if(mSharedPreferences.loadNightModeState())
            mToolbar.setTitleTextAppearance(context, R.style.ToolbarTypefaceDark);
        else
            mToolbar.setTitleTextAppearance(context, R.style.ToolbarTypefaceLight);
        setSupportActionBar(mToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        toolbarTitle.setText(R.string.app_name);
        Toolbar.LayoutParams llp = new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
        llp.gravity = Gravity.CENTER;
        toolbarTitle.setLayoutParams(llp);

        final TabsAdapter adapter = new TabsAdapter(this, context);
        adapter.addFragment(new Orders(), context.getResources().getString(R.string.orders));
        //adapter.addFragment(new Pending(), context.getResources().getString(R.string.pending));
        adapter.addFragment(new Delivered(), context.getResources().getString(R.string.delivered));

        mViewPager.setAdapter(adapter);
        mSlidingTabLayout.setElevation(0);
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(mSlidingTabLayout, mViewPager, true, (tab, position) -> {
            tab.setText(adapter.getPageTitle(position));
            tab.setCustomView(adapter.getTabView(position));
        });
        tabLayoutMediator.attach();
        if (mSharedPreferences.loadNightModeState())
            mSlidingTabLayout.setBackgroundColor(getResources().getColor(R.color.colorSecondaryDark)); //NIGHT MODE
        else
            mSlidingTabLayout.setBackgroundColor(getResources().getColor(R.color.colorSecondaryLight)); //DAY MODE

        mSlidingTabLayout.setUnboundedRipple(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_settings:
                startActivity(new Intent(context, SettingsActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                return true;
            case R.id.search:
                //startActivity(new Intent(context, SearchActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public AppBarLayout getAppBarLayout(){
        return appBarLayout;
    }

    public View getAppBarLayoutShadow(){
        return appBarLayoutShadow;
    }

}
