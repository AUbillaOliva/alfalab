package cl.alfa.alfalab.adapters;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import cl.alfa.alfalab.fragments.intro_fragments.FirstIntroFragment;
import cl.alfa.alfalab.fragments.intro_fragments.SecondIntroFragment;
import cl.alfa.alfalab.fragments.intro_fragments.ThirdIntroFragment;

@SuppressWarnings("deprecation")
public class IntroViewPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> fragmentsList = new ArrayList<>();

    public IntroViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    public void addPage(Fragment fragment){
        fragmentsList.add(fragment);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return FirstIntroFragment.newInstance();
            case 1:
                return SecondIntroFragment.newInstance();
            case 3:
                return ThirdIntroFragment.newInstance();
        }
        return ThirdIntroFragment.newInstance();
    }

    @Override
    public int getCount() {
        return fragmentsList.size();
    }
}