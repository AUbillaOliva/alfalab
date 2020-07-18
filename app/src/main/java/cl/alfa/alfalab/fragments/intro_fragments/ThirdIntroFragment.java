package cl.alfa.alfalab.fragments.intro_fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import cl.alfa.alfalab.MainActivity;
import cl.alfa.alfalab.MainApplication;
import cl.alfa.alfalab.R;
import cl.alfa.alfalab.activities.OnBoardingActivity;

public class ThirdIntroFragment extends Fragment {

    private Context context = MainApplication.getContext();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.third_intro_fragment_layout, container, false);

        ExtendedFloatingActionButton btnNext = view.findViewById(R.id.button);
        Toolbar mToolbar = view.findViewById(R.id.toolbar);

        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_24dp);
        mToolbar.setNavigationOnClickListener(v -> OnBoardingActivity.setPosition(1));

        btnNext.setOnClickListener(v -> {
            startActivity(new Intent(context, MainActivity.class));
        });

        return view;
    }

    public static Fragment newInstance(){
        return new ThirdIntroFragment();
    }
}
