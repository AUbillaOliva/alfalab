package cl.alfa.alfalab.fragments.intro_fragments;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import cl.alfa.alfalab.MainApplication;
import cl.alfa.alfalab.R;
import cl.alfa.alfalab.activities.OnBoardingActivity;
import cl.alfa.alfalab.utils.SharedPreferences;

public class SecondIntroFragment extends Fragment {

    private Context context = MainApplication.getContext();
    private static TextInputEditText nameInputEditText, lastInputEditText;
    private TextInputLayout firstnameInputLayout, lastnameInputLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.second_intro_fragment_layout, container, false);

        final SharedPreferences mSharedPreferences = new SharedPreferences(context);

        final Toolbar mToolbar = view.findViewById(R.id.toolbar);
        final ExtendedFloatingActionButton button = view.findViewById(R.id.button);
        firstnameInputLayout = view.findViewById(R.id.name_intro_input_layout);
        lastnameInputLayout = view.findViewById(R.id.lastname_intro_input_layout);
        nameInputEditText = view.findViewById(R.id.name_intro_edit_text);
        firstnameInputLayout.setHint(Html.fromHtml(getString(R.string.order_client_firstname_hint)));
        lastInputEditText = view.findViewById(R.id.lastname_intro_edit_text);
        lastnameInputLayout.setHint(Html.fromHtml(getString(R.string.order_client_lastname_hint)));

        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_24dp);
        mToolbar.setNavigationOnClickListener(v -> OnBoardingActivity.setPosition(0));

        nameInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length() > 1) {
                    firstnameInputLayout.setErrorEnabled(false);
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() < 1) {
                    firstnameInputLayout.setError(getResources().getString(R.string.required));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.length() < 1) {
                    firstnameInputLayout.setError(getResources().getString(R.string.required));
                }
            }
        });
        lastInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length() > 1) {
                    lastnameInputLayout.setErrorEnabled(false);
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() < 1) {
                    lastnameInputLayout.setError(getResources().getString(R.string.required));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.length() < 1) {
                    lastnameInputLayout.setError(getResources().getString(R.string.required));
                }
            }
        });

        button.setOnClickListener(v -> {
            if(nameInputEditText.getEditableText().length() < 1 && lastInputEditText.getEditableText().length() < 1) {
                firstnameInputLayout.setError(getResources().getString(R.string.required));
                lastnameInputLayout.setError(getResources().getString(R.string.required));
            } else if(lastInputEditText.getEditableText().length() < 1) {
                lastnameInputLayout.setError(getResources().getString(R.string.required));
            } else if(nameInputEditText.getEditableText().length() < 1) {
                firstnameInputLayout.setError(getResources().getString(R.string.required));
            } else {
                mSharedPreferences.setResponsible(nameInputEditText.getEditableText().toString() + " " + lastInputEditText.getEditableText().toString());
                OnBoardingActivity.setLastItemPosition();
            }
        });

        return view;
    }

    public static String getName() {
        return nameInputEditText.getEditableText().toString();
    }

    public static Fragment newInstance() {
        return new SecondIntroFragment();
    }

}
