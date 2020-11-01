package cl.alfa.alfalab.fragments.intro_fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import androidx.fragment.app.Fragment;
import cl.alfa.alfalab.R;
import cl.alfa.alfalab.activities.OnBoardingActivity;

public class FirstIntroFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.first_intro_fragment_layout, container, false);
        final ExtendedFloatingActionButton btnNext = view.findViewById(R.id.button);
        btnNext.setOnClickListener(v -> OnBoardingActivity.setPosition(1));
        return view;
    }

    public static Fragment newInstance() {
        return new FirstIntroFragment();
    }
}
