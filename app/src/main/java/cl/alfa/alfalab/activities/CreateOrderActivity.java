package cl.alfa.alfalab.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.security.Timestamp;
import java.util.Date;
import java.util.Objects;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import cl.alfa.alfalab.MainActivity;
import cl.alfa.alfalab.MainApplication;
import cl.alfa.alfalab.R;
import cl.alfa.alfalab.api.ApiClient;
import cl.alfa.alfalab.api.ApiService;
import cl.alfa.alfalab.models.Client;
import cl.alfa.alfalab.models.Orders;
import cl.alfa.alfalab.utils.SharedPreferences;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateOrderActivity extends AppCompatActivity {

    private Context context = MainApplication.getContext();
    private SharedPreferences mSharedPreferences = new SharedPreferences(context);

    private TextInputLayout firstnameInputLayout, lastnameInputLayout, emailInputLayout, typeInputLayout, instagramLayout, commentariesLayout, priceLayout;
    private TextInputEditText firstnameInputEditText, lastnameInputEditText, emailInputEditText, instagramInputEditText, commentariesInputEditText, priceInputEditText;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(mSharedPreferences.loadNightModeState())
            setTheme(R.style.AppThemeDark);
        else
            setTheme(R.style.AppTheme);
        setContentView(R.layout.create_order_activity_layout);

        final Toolbar mToolbar = findViewById(R.id.toolbar);
        final TextView toolbarTitle = mToolbar.findViewById(R.id.toolbar_title);
        setSupportActionBar(mToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        if(mSharedPreferences.loadNightModeState())
            mToolbar.setTitleTextAppearance(context, R.style.ToolbarTypefaceDark);
        else
            mToolbar.setTitleTextAppearance(context, R.style.ToolbarTypefaceLight);
        toolbarTitle.setText(R.string.create_order);

        firstnameInputLayout = findViewById(R.id.client_firstname_input_layout);
        lastnameInputLayout = findViewById(R.id.client_lastname_input_layout);
        emailInputLayout = findViewById(R.id.client_order_email);
        instagramLayout = findViewById(R.id.client_instagram_input_layout);
        typeInputLayout = findViewById(R.id.client_order_type);
        commentariesLayout = findViewById(R.id.order_commentaries_input_layout);
        priceLayout = findViewById(R.id.order_price_input_layout);

        final ExtendedFloatingActionButton button = findViewById(R.id.button);

        firstnameInputEditText = firstnameInputLayout.findViewById(R.id.client_firstname_input_edit_text);
        firstnameInputEditText.setHint(Html.fromHtml(getString(R.string.client_firstname)));
        lastnameInputEditText = lastnameInputLayout.findViewById(R.id.client_lastname_input_edit_text);
        lastnameInputEditText.setHint(Html.fromHtml(getString(R.string.client_lastname)));
        emailInputEditText = emailInputLayout.findViewById(R.id.client_email_input_edit_text);
        emailInputEditText.setHint(Html.fromHtml(getString(R.string.client_email)));
        instagramInputEditText = instagramLayout.findViewById(R.id.client_instagram_input_edit_text);
        commentariesInputEditText = commentariesLayout.findViewById(R.id.order_commentaries_input_edit_text);
        priceInputEditText = priceLayout.findViewById(R.id.order_price_input_edit_text);
        priceInputEditText.setHint(Html.fromHtml(getString(R.string.order_price)));

        firstnameInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length() < 1) firstnameInputLayout.setError(getResources().getString(R.string.required));
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.length() < 1 ) firstnameInputLayout.setError(getResources().getString(R.string.required));
            }
        });
        lastnameInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length() < 1) lastnameInputLayout.setError(getResources().getString(R.string.required));
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.length() < 1) lastnameInputLayout.setError(getResources().getString(R.string.required));
            }
        });
        emailInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length() < 1) emailInputLayout.setError(getResources().getString(R.string.required));
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.length() < 1) emailInputLayout.setError(getResources().getString(R.string.required));
            }
        });
        priceInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length() < 1) priceLayout.setError(getResources().getString(R.string.required));
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.length() < 1) priceLayout.setError(getResources().getString(R.string.required));
            }
        });

        button.setOnClickListener(view -> {
            progressDialog = ProgressDialog.show(this, "Creando orden...",
                    "Creando. Porfavor espera...", true);
            createOrder();
        });

    }

    private void createOrder(){

        final Orders order = new Orders();
        final Client client = new Client();
        if(priceInputEditText.getText() != null)
            order.setPrice(Integer.parseInt(String.valueOf(priceInputEditText.getEditableText())));
        if(firstnameInputEditText.getEditableText().length() != 0 || firstnameInputEditText.getEditableText().toString().equals(""))
            client.setFirstname(firstnameInputEditText.getEditableText().toString());
        if(Objects.requireNonNull(lastnameInputEditText.getEditableText()).toString().length() != 0 || lastnameInputEditText.getEditableText().toString().equals(""))
            client.setLastname(lastnameInputEditText.getEditableText().toString());
        if(Objects.requireNonNull(emailInputEditText.getEditableText()).toString().length() != 0 || emailInputEditText.getEditableText().toString().equals(""))
            client.setEmail(emailInputEditText.getEditableText().toString());
        client.setInstagram(Objects.requireNonNull(instagramInputEditText.getEditableText()).toString());
        order.setClient(client);
        order.setCommentaries(Objects.requireNonNull(commentariesInputEditText.getEditableText()).toString());
        order.setStatus(false);
        order.setResponsible(mSharedPreferences.getResponsible());
        order.setType("Blanco y negro");
        final Date date = new Date();
        long time = date.getTime();
        java.sql.Timestamp timestamp = new java.sql.Timestamp(time);
        order.setCheckin(timestamp.toString());
        try {
            final ApiService.PostOrderService service = ApiClient.getClient().create(ApiService.PostOrderService.class);
            Call<Orders> deleteRequest = service.postOrder(order);
            deleteRequest.enqueue(new Callback<Orders>() {
                @Override
                public void onResponse(Call<Orders> call, Response<Orders> response) {
                    Toast.makeText(context, "onResponse", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    Log.d(MainActivity.TAG, response.toString());
                    if(response.code() == 200){
                        Toast.makeText(context, "Pedido creado", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(context, MainActivity.class));
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        finish();
                    }
                }

                @Override
                public void onFailure(Call<Orders> call, Throwable t) {
                    Toast.makeText(context, "onFailure", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            });
        } catch (Throwable err){
            Log.e(MainActivity.TAG, Objects.requireNonNull(err.getMessage()));
        }
    }

    @Override
    public void onBackPressed() {
        if(firstnameInputEditText.isFocused())
            firstnameInputEditText.clearFocus();
        else if(lastnameInputEditText.isFocused())
            lastnameInputEditText.clearFocus();
        else if(emailInputEditText.isFocused())
            emailInputEditText.clearFocus();
        else if(instagramInputEditText.isFocused())
            instagramInputEditText.clearFocus();
        else if(commentariesInputEditText.isFocused())
            commentariesInputEditText.clearFocus();
        else if(priceInputEditText.isFocused())
            priceInputEditText.clearFocus();
        else if(Objects.requireNonNull(firstnameInputEditText.getText()).length() != 0 || Objects.requireNonNull(lastnameInputEditText.getText()).length() != 0 || Objects.requireNonNull(emailInputEditText.getText()).length() != 0 || Objects.requireNonNull(instagramInputEditText.getText()).length() != 0 || Objects.requireNonNull(priceInputEditText.getText()).length() != 0 || Objects.requireNonNull(commentariesInputEditText.getText()).length() != 0){
            final AlertDialog dialog = new AlertDialog.Builder(this)
                    .setView(R.layout.confirmation_dialog_layout)
                    .show();
            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            final ExtendedFloatingActionButton positiveButton = dialog.findViewById(R.id.confirm_button);
            assert positiveButton != null;
            positiveButton.setText(R.string.positive_delete_dialog_button);
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
        else {
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        //replace with switch if has more items
        if (item.getItemId() == android.R.id.home){
            final AlertDialog dialog = new AlertDialog.Builder(this)
                    .setView(R.layout.confirmation_dialog_layout)
                    .show();
            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            final ExtendedFloatingActionButton positiveButton = dialog.findViewById(R.id.confirm_button);
            assert positiveButton != null;
            positiveButton.setText(R.string.positive_delete_dialog_button);
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
        return super.onOptionsItemSelected(item);
    }

}
