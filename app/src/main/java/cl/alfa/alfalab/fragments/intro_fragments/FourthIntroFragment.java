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
import cl.alfa.alfalab.utils.SharedPreferences;

public class FourthIntroFragment extends Fragment {

    private final Context context = MainApplication.getContext();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fourth_intro_fragment_layout, container, false);
        final ExtendedFloatingActionButton btnNext = view.findViewById(R.id.button);
        final Toolbar mToolbar = view.findViewById(R.id.toolbar);

        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_24dp);
        mToolbar.setNavigationOnClickListener(v -> OnBoardingActivity.setPosition(1));

        final SharedPreferences mSharedPreferences = new SharedPreferences(context);
        btnNext.setOnClickListener(v -> {
            mSharedPreferences.setFirstTime(false);
            startActivity(new Intent(requireActivity(), MainActivity.class));
            requireActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            requireActivity().finish();
        });

        return view;
    }

    public static Fragment newInstance() {
        return new FourthIntroFragment();
    }
}
