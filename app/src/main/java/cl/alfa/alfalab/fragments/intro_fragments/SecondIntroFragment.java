package cl.alfa.alfalab.fragments.intro_fragments;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import cl.alfa.alfalab.MainApplication;
import cl.alfa.alfalab.R;
import cl.alfa.alfalab.activities.OnBoardingActivity;
import cl.alfa.alfalab.utils.SharedPreferences;

public class SecondIntroFragment extends Fragment {

    private static TextInputEditText nameInputEditText, lastInputEditText;
    private Context context = MainApplication.getContext();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.second_intro_fragment_layout, container, false);

        final SharedPreferences mSharedPreferences = new SharedPreferences(context);

        final TextInputLayout nameInputLayout = view.findViewById(R.id.name_intro_input_layout), lastInputLayout = view.findViewById(R.id.lastname_intro_input_layout);
        final Toolbar mToolbar = view.findViewById(R.id.toolbar);
        nameInputEditText = view.findViewById(R.id.name_intro_edit_text);
        nameInputEditText.setHint(Html.fromHtml(getString(R.string.client_firstname)));
        lastInputEditText = view.findViewById(R.id.lastname_intro_edit_text);
        lastInputEditText.setHint(Html.fromHtml(getString(R.string.client_lastname)));
        ExtendedFloatingActionButton button = view.findViewById(R.id.button);

        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_24dp);
        mToolbar.setNavigationOnClickListener(v -> OnBoardingActivity.setPosition(0));

        nameInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length() > 1) nameInputLayout.setErrorEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() < 1) nameInputLayout.setError(getResources().getString(R.string.required));
                nameInputLayout.setEndIconDrawable(R.drawable.ic_check_mark_24dp);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.length() < 1) nameInputLayout.setError(getResources().getString(R.string.required));
            }
        });

        lastInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length() > 1) lastInputLayout.setErrorEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() < 1) lastInputLayout.setError(getResources().getString(R.string.required));
                lastInputLayout.setEndIconDrawable(R.drawable.ic_check_mark_24dp);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.length() < 1) lastInputLayout.setError(getResources().getString(R.string.required));
            }
        });

        button.setOnClickListener(v -> {
            if(nameInputEditText.getEditableText().length() < 1 && lastInputEditText.getEditableText().length() < 1) {
                Toast.makeText(context, "Ingresa tu nombre y apellido", Toast.LENGTH_SHORT).show();
                nameInputLayout.setError(getResources().getString(R.string.required));
                lastInputLayout.setError(getResources().getString(R.string.required));
            } else if(lastInputEditText.getEditableText().length() < 1){
                lastInputLayout.setError(getResources().getString(R.string.required));
                Toast.makeText(context, "Ingresa tu apellido", Toast.LENGTH_SHORT).show();
            } else if(nameInputEditText.getEditableText().length() < 1){
                Toast.makeText(context, "Ingresa tu nombre", Toast.LENGTH_SHORT).show();
                nameInputLayout.setError(getResources().getString(R.string.required));
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

    public static Fragment newInstance(){
        return new SecondIntroFragment();
    }
}
