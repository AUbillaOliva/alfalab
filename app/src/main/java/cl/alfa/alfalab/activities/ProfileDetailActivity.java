package cl.alfa.alfalab.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import cl.alfa.alfalab.R;
import cl.alfa.alfalab.models.AuthUser;
import cl.alfa.alfalab.models.User;
import cl.alfa.alfalab.utils.SharedPreferences;

public class ProfileDetailActivity extends AppCompatActivity {

    private Context context = this;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final SharedPreferences mSharedPreferences = new SharedPreferences(context);
        if(mSharedPreferences.loadNightModeState()) {
            setTheme(R.style.AppThemeDark);
        } else {
            setTheme(R.style.AppTheme);
        }
        setContentView(R.layout.profile_detail_activity_layout);

        //TODO: DELETE ACCOUNT ACTION
        final Toolbar mToolbar = findViewById(R.id.toolbar);
        final TextView toolbarTitle = mToolbar.findViewById(R.id.toolbar_title);
        final TextInputLayout nameInputLayout = findViewById(R.id.email_input_layout),
                lastInputLayout = findViewById(R.id.password_input_layout);
        final TextInputEditText nameInputEditText = nameInputLayout.findViewById(R.id.email_edit_text),
                lastInputEditText = lastInputLayout.findViewById(R.id.password_edit_text);

        setSupportActionBar(mToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        if(mSharedPreferences.loadNightModeState()) {
            mToolbar.setTitleTextAppearance(context, R.style.ToolbarTypefaceDark);
        } else {
            mToolbar.setTitleTextAppearance(context, R.style.ToolbarTypefaceLight);
        }
        toolbarTitle.setText(R.string.edit_profile_text);

        nameInputLayout.setHint(Html.fromHtml(getString(R.string.order_client_firstname_hint)));
        lastInputLayout.setHint(Html.fromHtml(getString(R.string.order_client_lastname_hint)));
        final ExtendedFloatingActionButton button = findViewById(R.id.button);

        nameInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length() > 1) {
                    nameInputLayout.setErrorEnabled(false);
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() < 1) {
                    nameInputLayout.setError(getResources().getString(R.string.required));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.length() < 1) {
                    nameInputLayout.setError(getResources().getString(R.string.required));
                }
            }
        });
        lastInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length() > 1) {
                    lastInputLayout.setErrorEnabled(false);
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() < 1) {
                    lastInputLayout.setError(getResources().getString(R.string.required));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.length() < 1) {
                    lastInputLayout.setError(getResources().getString(R.string.required));
                }
            }
        });

        button.setOnClickListener(v -> {
            if(nameInputEditText.getEditableText().length() < 1 && lastInputEditText.getEditableText().length() < 1) {
                Toast.makeText(context, "Ingresa tu nombre y apellido", Toast.LENGTH_SHORT).show();
                nameInputLayout.setError(getResources().getString(R.string.required));
                lastInputLayout.setError(getResources().getString(R.string.required));
            } else if(lastInputEditText.getEditableText().length() < 1) {
                lastInputLayout.setError(getResources().getString(R.string.required));
                Toast.makeText(context, "Ingresa tu apellido", Toast.LENGTH_SHORT).show();
            } else if(nameInputEditText.getEditableText().length() < 1) {
                Toast.makeText(context, "Ingresa tu nombre", Toast.LENGTH_SHORT).show();
                nameInputLayout.setError(getResources().getString(R.string.required));
            } else {
                mSharedPreferences.setResponsible(new AuthUser(nameInputEditText.getEditableText().toString(), lastInputEditText.getEditableText().toString()));
                startActivity(new Intent(context, SettingsActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(context, SettingsActivity.class));
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            startActivity(new Intent(context, SettingsActivity.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
