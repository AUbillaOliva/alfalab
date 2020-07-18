package cl.alfa.alfalab.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import cl.alfa.alfalab.MainActivity;
import cl.alfa.alfalab.MainApplication;
import cl.alfa.alfalab.R;
import cl.alfa.alfalab.adapters.IntroViewPagerAdapter;
import cl.alfa.alfalab.adapters.TabsAdapter;
import cl.alfa.alfalab.fragments.Delivered;
import cl.alfa.alfalab.fragments.Orders;
import cl.alfa.alfalab.fragments.intro_fragments.FirstIntroFragment;
import cl.alfa.alfalab.fragments.intro_fragments.SecondIntroFragment;
import cl.alfa.alfalab.fragments.intro_fragments.ThirdIntroFragment;
import cl.alfa.alfalab.models.IntroScreenItem;
import cl.alfa.alfalab.utils.NonSwipeableViewPager;
import cl.alfa.alfalab.utils.SharedPreferences;

public class OnBoardingActivity extends AppCompatActivity {

    private Context context = MainApplication.getContext();

    private static NonSwipeableViewPager screenPager;
    private IntroViewPagerAdapter introViewPagerAdapter;
    private static TabLayout tabIndicator;
    private ExtendedFloatingActionButton btnNext;
    private static Button btnGetStarted;
    private static int position = 0 ;
    private static Animation btnAnim ;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        SharedPreferences mSharedPreferences = new SharedPreferences(context);
        if(mSharedPreferences.loadNightModeState())
            setTheme(R.style.AppThemeDark);
        else
            setTheme(R.style.AppTheme);
        setContentView(R.layout.intro_activity_layout);

        btnNext = findViewById(R.id.button);
        btnGetStarted = findViewById(R.id.btn_get_started);
        tabIndicator = findViewById(R.id.tab_indicator);
        btnAnim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.button_animation);

        screenPager = findViewById(R.id.screen_viewpager);
        IntroViewPagerAdapter adapter = new IntroViewPagerAdapter(getSupportFragmentManager());
        adapter.addPage(new FirstIntroFragment());
        adapter.addPage(new SecondIntroFragment());
        adapter.addPage(new ThirdIntroFragment());
        screenPager.setAdapter(adapter);

        tabIndicator.setupWithViewPager(screenPager);

        if(position == 3){
            startActivity(new Intent(context, MainActivity.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            mSharedPreferences.setFirstTime(false);
            finish();
        }

    }

    public static void setPosition(int pos){
        position = pos;
        screenPager.setCurrentItem(position);
    }

    public static void setLastItemPosition() {
        position = 3;
        screenPager.setCurrentItem(position);
    }

    @Override
    public void onBackPressed() {
        screenPager.setCurrentItem(screenPager.getCurrentItem()-1);
        position--;
    }
}
