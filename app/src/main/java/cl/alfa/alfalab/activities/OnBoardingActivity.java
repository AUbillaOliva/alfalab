package cl.alfa.alfalab.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AppCompatActivity;
import cl.alfa.alfalab.MainActivity;
import cl.alfa.alfalab.R;
import cl.alfa.alfalab.adapters.IntroViewPagerAdapter;
import cl.alfa.alfalab.fragments.intro_fragments.FirstIntroFragment;
import cl.alfa.alfalab.fragments.intro_fragments.SecondIntroFragment;
import cl.alfa.alfalab.fragments.intro_fragments.ThirdIntroFragment;
import cl.alfa.alfalab.utils.NonSwipeableViewPager;
import cl.alfa.alfalab.utils.SharedPreferences;

public class OnBoardingActivity extends AppCompatActivity {

    private Context context = this;

    private static NonSwipeableViewPager screenPager;
    private static int position = 0 ;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        final SharedPreferences mSharedPreferences = new SharedPreferences(context);
        if(mSharedPreferences.loadNightModeState())
            setTheme(R.style.AppThemeDark);
        else
            setTheme(R.style.AppTheme);
        setContentView(R.layout.intro_activity_layout);

        final TabLayout tabIndicator = findViewById(R.id.tab_indicator);

        screenPager = findViewById(R.id.screen_viewpager);
        IntroViewPagerAdapter adapter = new IntroViewPagerAdapter(getSupportFragmentManager());
        adapter.addPage(new FirstIntroFragment());
        adapter.addPage(new SecondIntroFragment());
        adapter.addPage(new ThirdIntroFragment());
        screenPager.setAdapter(adapter);

        tabIndicator.setupWithViewPager(screenPager);

        if(position == 3){
            startActivity(new Intent(context, MainActivity.class));
            mSharedPreferences.setFirstTime(false);
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
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
