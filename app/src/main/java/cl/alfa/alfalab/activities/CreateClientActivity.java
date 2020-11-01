package cl.alfa.alfalab.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import cl.alfa.alfalab.MainActivity;
import cl.alfa.alfalab.R;
import cl.alfa.alfalab.models.Client;
import cl.alfa.alfalab.models.Orders;
import cl.alfa.alfalab.utils.SharedPreferences;

public class CreateClientActivity extends AppCompatActivity {

    private Context context = this;
    private TextInputLayout firstnameInputLayout,
            lastnameInputLayout,
            zoneInputLayout;
    private TextInputEditText firstnameInputEditText,
            lastnameInputEditText,
            emailInputEditText,
            instagramInputEditText;
    private Client client;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Intent dataIntent = getIntent();
        final Bundle data = dataIntent.getExtras();
        final Orders order = data != null ? (Orders) data.getSerializable("data") : null;

        if(order != null)
            client = order.getClient();

        final SharedPreferences mSharedPreferences = new SharedPreferences(context);
        if(mSharedPreferences.loadNightModeState())
            setTheme(R.style.AppThemeDark);
        else
            setTheme(R.style.AppTheme);
        setContentView(R.layout.create_client_activity_layout);

        final Toolbar mToolbar = findViewById(R.id.toolbar);
        final TextView toolbarTitle = mToolbar.findViewById(R.id.toolbar_title);
        setSupportActionBar(mToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        if(mSharedPreferences.loadNightModeState())
            mToolbar.setTitleTextAppearance(context, R.style.ToolbarTypefaceDark);
        else
            mToolbar.setTitleTextAppearance(context, R.style.ToolbarTypefaceLight);
        if(order != null)
            toolbarTitle.setText(context.getResources().getString(R.string.update_order));
        else
            toolbarTitle.setText(R.string.create_order);

        final ExtendedFloatingActionButton btnNext = findViewById(R.id.button);
        final AutoCompleteTextView zoneAutoCompleteTextView = findViewById(R.id.type_input_autocomplete);
        zoneAutoCompleteTextView.setOnItemClickListener((adapterView, view, i, l) -> {
            InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            in.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
        });
        zoneAutoCompleteTextView.setShowSoftInputOnFocus(false);
        final TextInputLayout emailInputLayout = findViewById(R.id.email_input_layout);
        final TextInputLayout instagramInputLayout = findViewById(R.id.instagram_input_layout);
        zoneInputLayout = findViewById(R.id.zone_input_layout);
        firstnameInputLayout = findViewById(R.id.firstname_input_layout);
        firstnameInputLayout.setHint(Html.fromHtml(getString(R.string.order_client_firstname_hint)));
        lastnameInputLayout = findViewById(R.id.lastname_input_layout);
        lastnameInputLayout.setHint(Html.fromHtml(getString(R.string.order_client_lastname_hint)));


        firstnameInputEditText = firstnameInputLayout.findViewById(R.id.firstname_edit_text);
        lastnameInputEditText = lastnameInputLayout.findViewById(R.id.lastname_edit_text);
        emailInputEditText = emailInputLayout.findViewById(R.id.email_edit_text);
        instagramInputEditText = instagramInputLayout.findViewById(R.id.instagram_edit_text);

        final String[] ZONES = new String[] {"Sur", "Norte"};

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.dropdown_menu_popup_item, ZONES);
        zoneAutoCompleteTextView.setAdapter(adapter);

        firstnameInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(Objects.requireNonNull(firstnameInputEditText.getText()).length() < 1) {
                    firstnameInputLayout.setError(getResources().getString(R.string.required));
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(Objects.requireNonNull(firstnameInputEditText.getText()).length() < 1) {
                    firstnameInputLayout.setError(getResources().getString(R.string.required));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.length() < 1) {
                    firstnameInputLayout.setError(getResources().getString(R.string.required));
                } else if(editable.length() >= 1) {
                    firstnameInputLayout.setError(null);
                }
            }
        });
        lastnameInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(Objects.requireNonNull(lastnameInputEditText.getText()).length() < 1) {
                    lastnameInputLayout.setError(getResources().getString(R.string.required));
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(Objects.requireNonNull(lastnameInputEditText.getText()).length() < 1) {
                    lastnameInputLayout.setError(getResources().getString(R.string.required));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.length() < 1) {
                    lastnameInputLayout.setError(getResources().getString(R.string.required));
                } else if(editable.length() >= 1) {
                    lastnameInputLayout.setError(null);
                }
            }
        });

        if(order != null) {
            firstnameInputEditText.setText(client.getFirstname());
            lastnameInputEditText.setText(client.getLastname());
            zoneAutoCompleteTextView.setText(order.getZone());
            instagramInputEditText.setText(client.getInstagram());
            emailInputEditText.setText(client.getEmail());
        }

        btnNext.setOnClickListener(view1 -> {
            if (Objects.requireNonNull(firstnameInputEditText.getText()).length() < 1 && Objects.requireNonNull(lastnameInputEditText.getText()).length() < 1) {
                firstnameInputLayout.setError(getResources().getString(R.string.required));
                lastnameInputLayout.setError(getResources().getString(R.string.required));
            } else if (firstnameInputEditText.getText().length() < 1) {
                firstnameInputLayout.setError(getResources().getString(R.string.required));
            } else if (Objects.requireNonNull(lastnameInputEditText.getText()).length() < 1) {
                lastnameInputLayout.setError(getResources().getString(R.string.required));
            } else if(zoneAutoCompleteTextView.getText().length() < 1) {
                zoneInputLayout.setError(getResources().getString(R.string.required));
            } else {
                final Intent intent = new Intent(context, CreateOrdersActivity.class);
                intent.putExtra("client", new Client(firstnameInputEditText.getText().toString(), lastnameInputEditText.getText().toString(), Objects.requireNonNull(emailInputEditText.getText()).toString(), Objects.requireNonNull(instagramInputEditText.getText()).toString()));
                intent.putExtra("zone", zoneAutoCompleteTextView.getText().toString());
                if(order != null) {
                    intent.putExtra("data", new Orders(order));
                }
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        });

    }

    public void showDialog() {
        final AlertDialog dialog = new AlertDialog.Builder(context)
                .setView(R.layout.confirmation_dialog_layout)
                .show();
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        final ExtendedFloatingActionButton positiveButton = dialog.findViewById(R.id.confirm_button);
        assert positiveButton != null;
        positiveButton.setText(R.string.positive_discard_dialog_button);
        positiveButton.setOnClickListener(v -> {
            dialog.dismiss();
            startActivity(new Intent(context, MainActivity.class));
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });
        final TextView dialogTitle = dialog.findViewById(R.id.dialog_title);
        assert dialogTitle != null;
        dialogTitle.setText(R.string.delete_order_dialog_title);
        final TextView dialogSubtitle = dialog.findViewById(R.id.dialog_subtitle);
        assert dialogSubtitle != null;
        dialogSubtitle.setText(R.string.delete_order_dialog_subtitle);
        final TextView negative = dialog.findViewById(R.id.negative_button);
        assert negative != null;
        negative.setText(R.string.cancel_dialog_buttton);
        negative.setOnClickListener(v -> dialog.dismiss());
    }

    @Override
    public void onBackPressed() {
        showDialog();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            showDialog();
        }
        return super.onOptionsItemSelected(item);
    }
}
