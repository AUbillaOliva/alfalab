package cl.alfa.alfalab.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import cl.alfa.alfalab.R;
import cl.alfa.alfalab.adapters.IntroViewPagerAdapter;
import cl.alfa.alfalab.fragments.intro_fragments.FirstIntroFragment;
import cl.alfa.alfalab.fragments.intro_fragments.FourthIntroFragment;
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

        if(mSharedPreferences.loadNightModeState()) {
            setTheme(R.style.AppThemeDark);
        } else {
            setTheme(R.style.AppTheme);
        }
        setContentView(R.layout.intro_activity_layout);

        screenPager = findViewById(R.id.screen_viewpager);
        IntroViewPagerAdapter adapter = new IntroViewPagerAdapter(getSupportFragmentManager());
        adapter.addPage(new FirstIntroFragment());
        adapter.addPage(new SecondIntroFragment());
        adapter.addPage(new ThirdIntroFragment());
        adapter.addPage(new FourthIntroFragment());
        screenPager.setAdapter(adapter);

        if(!mSharedPreferences.isFirstTime()) {
            if(mSharedPreferences.getToken() == null) {
                screenPager.setCurrentItem(1);
            }
        }

    }

    public static void setPosition(int pos) {
        position = pos;
        screenPager.setCurrentItem(position);
    }

    public static void setLastItemPosition() {
        screenPager.setCurrentItem(3);
    }

    public static void setSignInItemPosition() {
        screenPager.setCurrentItem(1);
    }

    public static void setSignUpItemPosition() {
        screenPager.setCurrentItem(2);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            if(screenPager.getCurrentItem() == 3) {
                screenPager.setCurrentItem(1);
            } else {
                screenPager.setCurrentItem(screenPager.getCurrentItem()-1);
                position--;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if(screenPager.getCurrentItem() == 3) {
            screenPager.setCurrentItem(1);
        } else {
            screenPager.setCurrentItem(screenPager.getCurrentItem()-1);
            position--;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        screenPager = null;
    }
}
